package com.aiyuns.tinkerplay.Controller.Service.ServiceImpl.Dao.Repository;

import com.aiyuns.tinkerplay.Entity.Account;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface AccountRepository extends JpaRepository<Account, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE) // jpa加悲观锁
  Optional<Account> findByAccountNumber(String accountNumber);
}
