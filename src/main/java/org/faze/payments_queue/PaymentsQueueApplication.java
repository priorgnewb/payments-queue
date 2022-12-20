package org.faze.payments_queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PaymentsQueueApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentsQueueApplication.class, args);
  }

}
