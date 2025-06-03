package com.aiyuns.tinkerplay.Controller.Service;

import java.math.BigDecimal;

public interface TransferService {
  void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount);
}
