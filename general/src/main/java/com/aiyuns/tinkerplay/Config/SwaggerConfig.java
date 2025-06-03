package com.aiyuns.tinkerplay.Config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: aiYunS @Date: 2022-11-21 11:02 @Description: Swagger配置类
 */
@Configuration
public class SwaggerConfig {

  //    @Bean
  //    public Docket docket() {
  //        return new Docket(DocumentationType.SWAGGER_2)
  //                .apiInfo(apiInfo())
  //                .select()
  //                // 为当前包下controller生成API文档
  //                .apis(RequestHandlerSelectors.basePackage("com.aiyuns.tinkerplay.Controller"))
  //                // 为有@Api注解的Controller生成API文档
  ////                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
  //                // 为有@Operation注解的方法生成API文档
  ////                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
  //                .paths(PathSelectors.any())
  //                .build();
  //    }

  @Bean
  public OpenAPI springShopOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("TinkerPlay")
                .description("tinkerplay")
                .version("0.0.1-SNAPSHOT")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
        .externalDocs(
            new ExternalDocumentation()
                .description("TinkerPlayDoc")
                .url("https://github.com/aiYunS-love/TinkerPlay"));
  }

  //    private ApiInfo apiInfo() {
  //        // 作者信息
  //        Contact contact = new Contact("aiYunS","https://gitee.com/aiYunS", "784724280@qq.com");
  //        return new ApiInfoBuilder()
  //                .title("学习")
  //                .description("aiYunS-study")
  //                .contact(contact)
  //                .version("0.0.1-SNAPSHOT")
  //                .build();
  ////        return new ApiInfo(
  ////                "学习",
  ////                "aiYunS",
  ////                "1.0.0",
  ////                "https://gitee.com/aiYunS",
  ////                contact,
  ////                "Apache 2.0",
  ////                "http://www.apache.org/licenses/LICENSE-2.0",
  ////                new ArrayList());
  //    }

  //    @Bean
  //    public Docket docket(Environment environment) {
  //        // 获取项目的环境 需要创建多个application-xx环境配置文件
  //        String[] profiles = environment.getActiveProfiles();
  //        System.out.println("当前环境");
  //        for (String temp : profiles) {
  //            System.out.println(temp);
  //        }
  //        Boolean enableSwagger = false;
  //        // if (profiles.length > 0 && profiles[0].equals("windows")) {
  //        if (profiles.length > 0) {
  //            enableSwagger = true;
  //        }
  //        System.out.println("swagger是否开启：" + enableSwagger);
  //
  //        return new Docket(DocumentationType.OAS_30)
  //                .groupName("Test")
  //                .apiInfo(apiInfo())
  //                // 是否启用swagger, false: 不能在浏览器访问
  //                .enable(enableSwagger)
  //                .select()
  //                // RequestHandlerSelectors, 配置要扫描接口的方式
  //                // basePackage: 指定要扫描的包
  //                // any(): 扫描全部
  //                // none(): 不扫描
  //                // withClassAnnotation: 扫描类上的注解, 参数是一个注解的反射对象
  // withClassAnnotation(RestController.class)
  //                // withMethodAnnotation: 扫描方法上的注解
  //                .apis(RequestHandlerSelectors.basePackage("com.aiYunS.tinkerplay.Controller"))
  //                // paths 过滤什么路径 不显示到swagger文档中
  //                // .paths(PathSelectors.ant("/loong/**"))
  //                .build();
  //    }
  //
  //    @Autowired
  //    private TypeResolver typeResolver;
  //
  //    private ApiKey apiKey() {
  //        return new ApiKey("mykey", "api_key", "header");
  //    }
  //
  //    private SecurityContext securityContext() {
  //        return SecurityContext.builder()
  //                .securityReferences(defaultAuth())
  //                .forPaths(PathSelectors.regex("/anyPath.*"))
  //                .build();
  //    }
  //
  //    List<SecurityReference> defaultAuth() {
  //        AuthorizationScope authorizationScope
  //                = new AuthorizationScope("global", "accessEverything");
  //        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
  //        authorizationScopes[0] = authorizationScope;
  //        return singletonList(
  //                new SecurityReference("mykey", authorizationScopes));
  //    }
  //
  //    @Bean
  //    SecurityConfiguration security() {
  //        return SecurityConfigurationBuilder.builder()
  //                .clientId("test-app-client-id")
  //                .clientSecret("test-app-client-secret")
  //                .realm("test-app-realm")
  //                .appName("test-app")
  //                .scopeSeparator(",")
  //                .additionalQueryStringParams(null)
  //                .useBasicAuthenticationWithAccessCodeGrant(false)
  //                .enableCsrfSupport(false)
  //                .build();
  //    }
  //
  //    @Bean
  //    UiConfiguration uiConfig() {
  //        return UiConfigurationBuilder.builder()
  //                .deepLinking(true)
  //                .displayOperationId(false)
  //                .defaultModelsExpandDepth(1)
  //                .defaultModelExpandDepth(1)
  //                .defaultModelRendering(ModelRendering.EXAMPLE)
  //                .displayRequestDuration(false)
  //                .docExpansion(DocExpansion.NONE)
  //                .filter(false)
  //                .maxDisplayedTags(null)
  //                .operationsSorter(OperationsSorter.ALPHA)
  //                .showExtensions(false)
  //                .showCommonExtensions(false)
  //                .tagsSorter(TagsSorter.ALPHA)
  //                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
  //                .validatorUrl(null)
  //                .build();
  //    }

  //    @Bean
  //    public Docket docket(Environment environment) {
  //        // 获取项目的环境
  //        String[] profiles = environment.getActiveProfiles();
  //        System.out.println("当前环境");
  //        for (String temp : profiles) {
  //            System.out.println(temp);
  //        }
  //        Boolean enableSwagger = false;
  //        if (profiles.length > 0 && profiles[0].equals("dev")) {
  //            enableSwagger = true;
  //        }
  //        System.out.println("swagger是否开启：" + enableSwagger);
  //
  //        return new Docket(DocumentationType.SWAGGER_2)
  //                .apiInfo(apiInfo())
  //                .groupName("loong")
  //                .enable(enableSwagger)
  //                .select()
  //                .apis(RequestHandlerSelectors.basePackage("com.loong.controller"))
  //                .build();
  //    }

  // 配置多个分组
  //    @Bean
  //    public Docket docket1() {
  //        return new Docket(DocumentationType.SWAGGER_2)
  //                .groupName("loong-A");
  //    }

  // 配置多个分组
  //    @Bean
  //    public Docket docket2() {
  //        return new Docket(DocumentationType.SWAGGER_2)
  //                .groupName("loong-B");
  //    }

  // 配置多个分组
  //    @Bean
  //    public Docket docket3() {
  //        return new Docket(DocumentationType.SWAGGER_2)
  //                .groupName("loong-C");
  //    }

}
