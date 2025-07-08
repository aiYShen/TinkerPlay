package com.aiyuns.tinkerplay.Controller;

import com.aiyuns.tinkerplay.Config.Properties.NacosInfoProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: aiYunS @Date: 2023年3月25日, 0025 下午 5:18:56 @Description: Nacos配置中心Test
 */
@RestController
@Tag(name = "NacosController", description = "Nacos配置中心模块")
@RequestMapping("/nacos")
public class NacosController {

  @Autowired
  private NacosInfoProperty nacosInfoProperty;

  @Operation(summary = "Nacos配置中心Test")
  @GetMapping("/getConfigInfo")
  public String getConfigInfo() {
    return nacosInfoProperty.getInfo();
  }
}
