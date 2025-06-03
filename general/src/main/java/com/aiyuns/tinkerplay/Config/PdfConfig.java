package com.aiyuns.tinkerplay.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: aiYunS @Date: 2023-1-14 14:16 @Description:
 * pdf配置类:定义一个配置类和它的静态属性,并用@Value注解注入yml配置文件的自定义的配置项,工具类PdfUtil.java,直接new对象获取.
 * 通过yml动态配置一些东西,不用在代码中写死一些配置
 */
@Component("PdfConfig")
public class PdfConfig {

  private static String templatePath;

  private static String output;

  private static String fontPath;

  public String getTemplatePath() {
    return templatePath;
  }

  @Value("${pdf.config.templatePath}")
  public void setTemplatePath(String templatePath) {
    PdfConfig.templatePath = templatePath;
  }

  public String getOutput() {
    return output;
  }

  @Value("${pdf.config.output}")
  public void setOutput(String output) {
    PdfConfig.output = output;
  }

  public String getFontPath() {
    return fontPath;
  }

  @Value("${pdf.config.fontPath}")
  public void setFontPath(String fontPath) {
    PdfConfig.fontPath = fontPath;
  }
}
