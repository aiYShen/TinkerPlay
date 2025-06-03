package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl;

import com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.Repository.AccountRepository;
import com.aiyuns.tinkerplay.Controller.Service.TransferService;
import com.aiyuns.tinkerplay.Entity.Account;
import com.baomidou.dynamic.datasource.annotation.DS;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 转账Service实现 */
@Service
public class TransferServiceImpl implements TransferService {

  @Autowired private AccountRepository accountRepository;

  @DS("slave_1")
  @Transactional
  @Override
  public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
    Account fromAccount =
        accountRepository
            .findByAccountNumber(fromAccountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
    Account toAccount =
        accountRepository
            .findByAccountNumber(toAccountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

    if (fromAccount.getBalance().compareTo(amount) < 0) {
      throw new IllegalArgumentException("Insufficient balance");
    }

    fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
    toAccount.setBalance(toAccount.getBalance().add(amount));

    accountRepository.save(fromAccount);
    accountRepository.save(toAccount);
  }
}
