package com.aiyuns.tinkerplay.Controller;

import com.aiyuns.tinkerplay.Controller.Service.TransferService;
import com.aiyuns.tinkerplay.CustomAnnotations.WebLog;
import com.aiyuns.tinkerplay.Entity.TransferRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/transferController")
@Tag(name = "TransferController", description = "模拟金融模块")
public class TransferController {

  @Resource private TransferService transferService;

  @PostMapping
  @Operation(summary = "模拟转账")
  @WebLog(description = "模拟转账")
  public ResponseEntity<String> transfer(@RequestBody TransferRequest transferRequest) {
    transferService.transfer(
        transferRequest.getFromAccountNumber(),
        transferRequest.getToAccountNumber(),
        transferRequest.getAmount());
    return ResponseEntity.ok("Transfer successful");
  }
}
