package com.aiyuns.tinkerplay.Entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "账户信息实体类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "账户ID")
  private Long id;

  @Schema(description = "账户号码")
  private String accountNumber;

  @Schema(description = "转账金额")
  private BigDecimal balance;

  // @Version //乐观锁
  @Schema(description = "版本号")
  private Long version;
}
