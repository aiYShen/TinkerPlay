package com.aiyuns.tinkerplay.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: aiYunS @Date: 2022-9-30 下午 04:43 @Description: 文件上传所需
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  /** 图片保存路径，自动从yml文件中获取数据 示例： E:/images/ */
  @Value("${file-save-path}")
  private String fileSavePath;

  //    @Value("${location:E:\\aiyuns\\Temp}")
  //    private String tempDir;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    /**
     * 配置资源映射 意思是：如果访问的资源路径是以“/images/”开头的， 就给我映射到本机的“E:/images/”这个文件夹内，去找你要的资源 注意：E:/images/ 后面的
     * “/”一定要带上
     */
    registry
        .addResourceHandler("/uploadFile/**", "/swagger-ui/**")
        .addResourceLocations(
            "file:" + fileSavePath, "classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
        .resourceChain(false);
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/swagger-ui/").setViewName("forward:/swagger-ui/index.html");
  }

  //    @Bean
  //    MultipartConfigElement multipartConfigElement() {
  //        MultipartConfigFactory factory = new MultipartConfigFactory();
  //        File tmpDirFile = new File(tempDir);
  //        // 判断文件夹是否存在
  //        if (!tmpDirFile.exists()) {
  //            //创建文件夹
  //            tmpDirFile.mkdirs();
  //        }
  //        factory.setLocation(tempDir);
  //        return factory.createMultipartConfig();
  //    }
}
