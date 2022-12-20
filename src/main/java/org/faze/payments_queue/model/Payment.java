package org.faze.payments_queue.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="payments_queue")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name="account_id")
  private Integer accountId;

  @Column(name="amount")
  private BigDecimal amount;

  @Column(name="datetime_transaction")
  private LocalDateTime datetimeTransaction;

  public Payment(Integer accountId, BigDecimal amount) {
    this.accountId = accountId;
    this.amount = amount;
    this.datetimeTransaction=LocalDateTime.now();
  }

  public Payment() {
  }

  public LocalDateTime getDatetimeTransaction() {
    return datetimeTransaction;
  }

  public void setDatetimeTransaction(LocalDateTime datetimeTransaction) {
    this.datetimeTransaction = datetimeTransaction;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}
