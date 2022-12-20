package org.faze.payments_queue.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.validation.constraints.Min;

public class PaymentDto {

  @Min(value = 1, message = "Неверный id консультанта!")
  private final Integer accountId;

  @Min(value = 0, message = "Указана сумма менее 0!")
  private final BigDecimal amount;

  public Integer getAccountId() {
    return accountId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public PaymentDto(@JsonProperty("accountId") Integer accountId, @JsonProperty("amount")BigDecimal amount) {
    this.accountId = accountId;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "PaymentDto{" +
        "accountId=" + accountId +
        ", amount=" + amount +
        '}';
  }
}
