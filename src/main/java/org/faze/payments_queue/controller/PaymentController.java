package org.faze.payments_queue.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.faze.payments_queue.dto.PaymentDto;
import org.faze.payments_queue.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

  PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PaymentDto>> showAll() {

    return ResponseEntity.ok(paymentService.findAll());
  }

  @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addPay(@RequestBody @Valid PaymentDto paymentDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.of(Optional.empty());
    }

    Long newPaymentId = paymentService.add(paymentDto);

    return ResponseEntity.ok("Транзакция с id = " + newPaymentId + " успешно обработана.");
  }

}