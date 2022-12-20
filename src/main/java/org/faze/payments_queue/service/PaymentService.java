package org.faze.payments_queue.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.faze.payments_queue.dao.PaymentDao;
import org.faze.payments_queue.dto.PaymentDto;
import org.faze.payments_queue.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class PaymentService {

  PaymentDao paymentDao;

  KafkaTemplate<String, PaymentDto> kafkaTemplate;

  private long lastPaymentId = 0;

  private final File file;

  @Value("${payment.kafka.topic}")
  private String topicName;

  @Autowired
  public PaymentService(PaymentDao paymentDao,
      KafkaTemplate<String, PaymentDto> kafkaTemplate,
      @Value("${payment.kafka.output.file}") String outputFile
      ) {
    this.paymentDao = paymentDao;
    this.kafkaTemplate = kafkaTemplate;
    this.file = new File(outputFile);
  }

  public Long add(PaymentDto paymentDto) {
    Payment savedPayment = paymentDao.save(
        new Payment(paymentDto.getAccountId(), paymentDto.getAmount()));
    sendMessage(paymentDto);
    return savedPayment.getId();
  }

  public void sendMessage(PaymentDto paymentDto) {
    ListenableFuture<SendResult<String, PaymentDto>> future = kafkaTemplate.send(topicName,
        paymentDto);

    future.addCallback(new ListenableFutureCallback<>() {
      @Override
      public void onSuccess(SendResult<String, PaymentDto> result) {
        System.out.println(
            "Отправлено сообщение: [" + paymentDto + "] с offset=[" +
                result.getRecordMetadata().offset() + "]");
      }

      @Override
      public void onFailure(Throwable ex) {
        System.out.println(
            "Невозможно отправить сообщение: [" + paymentDto + "], причина : " + ex.getMessage());
      }
    });
  }

  public List<PaymentDto> findAll() {
    List<PaymentDto> allPayments = paymentDao.findAll().stream()
        .map(payment -> new PaymentDto(payment.getAccountId(),
            payment.getAmount())).collect(Collectors.toList());
    return allPayments;
  }

  @Scheduled(fixedDelay = 60_000)
  public void readMessages() {
    System.out.println("Id последней записанной в файл транзакции: " + lastPaymentId);
    List<Payment> payments = paymentDao.findByIdGreaterThanOrderById(lastPaymentId);

    try (BufferedWriter bf = new BufferedWriter(new FileWriter(file, true))) {
      for (Payment p : payments) {
        bf.write(String.format("%d;%d;%s;%s%n",
            p.getId(),
            p.getAccountId(),
            p.getAmount().toString(),
            p.getDatetimeTransaction().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        bf.flush();
      }
      System.out.println("Все транзакции записаны в output-файл.");
      if(!payments.isEmpty()) lastPaymentId=payments.get(payments.size()-1).getId();
    } catch (IOException e) {
      System.out.println("Ошибка записи в output-файл!");
      e.printStackTrace();
    }
  }
}