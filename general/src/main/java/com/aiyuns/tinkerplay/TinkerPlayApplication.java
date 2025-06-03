package com.aiyuns.tinkerplay;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

// @EnableRabbit
// @EnableApolloConfig //Apollo配置项
// @EnableDiscoveryClient //注册中心
// @EnableScheduling
@ServletComponentScan
@EnableEncryptableProperties // 开启yml敏感数据加密
@EnableAsync // 开启多线程,异步
// @EnableSwagger2 //开启swagger2
@EnableCaching // 开启redis缓存
// @EntityScan(basePackages = "{com.aiyuns.tinkerplay.Entity,
// com.aiyuns.tinkerplay.Controller.Elasticsearch.EsEntity}")
// 排除SecurityAutoConfiguration类,否则会启用认证
@EnableElasticsearchRepositories(
    basePackages = "com.aiyuns.tinkerplay.Controller.Elasticsearch.Repository")
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class, SecurityAutoConfiguration.class})
public class TinkerPlayApplication {

  public static void main(String[] args) {

    /* 启动类的main方法中设置系统参数,为了解决yml配置文件配置nacos的日志加载log-enable: true的报错问题
      错误原因在于nacos引入的nacsos-client.jar内含有默认的nacos-logback.xml/nacos-log4j2.xml，其中nacos-logback.xml中contextName属性为nacos
      该属性与自定义的logback.xml不一致导致冲突
      该设置与yml配置文件注释log-enable: false 作用一致
    */
    // System.setProperty("nacos.logging.default.config.enabled","false");

    SpringApplication.run(TinkerPlayApplication.class, args);
    // 启动打开默认浏览器访问
    //        try {
    //            Runtime.getRuntime().exec("cmd /c start http://localhost:9092/api/upload.html");
    //            // Runtime.getRuntime().exec("cmd /c start
    // http://192.168.1.7:1001/api/upload.html");
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
  }

  /*解决文件名中含有":\\"等特殊字符时，接口400的问题
   * Tomcat的新版本中增加了一个新特性，就是严格按照 RFC 3986规范进行访问解析，而 RFC 3986规范定义了Url中只允许包含英文字母（a-zA-Z）、数字（0-9）、-_.~4个特殊字符
   * 以及所有保留字符(RFC3986中指定了以下字符为保留字符：! * ’ ( ) ; : @ & = + $ , / ? # [ ])。*/
  @Bean
  public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
    // 修改内置的 tomcat 容器配置
    TomcatServletWebServerFactory tomcatServlet = new TomcatServletWebServerFactory();
    tomcatServlet.addConnectorCustomizers(
        (TomcatConnectorCustomizer)
            connector -> connector.setProperty("relaxedQueryChars", "[]{}:\\"));
    return tomcatServlet;
  }
}
