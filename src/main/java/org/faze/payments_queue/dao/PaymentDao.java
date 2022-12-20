package org.faze.payments_queue.dao;

import java.util.List;
import org.faze.payments_queue.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDao extends JpaRepository<Payment, Long> {

      List<Payment> findByIdGreaterThanOrderById(Long id);

}
