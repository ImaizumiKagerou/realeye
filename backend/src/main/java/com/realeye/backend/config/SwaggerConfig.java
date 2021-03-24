//package com.realeye.backend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * /swagger-ui/index.html
// */
////@Configuration
////@EnableSwagger2
//public class SwaggerConfig {
//    @Bean
//    public Docket api() {
//        // 添加请求参数，我们这里把token作为请求头部参数传入后端
//        ParameterBuilder parameterBuilder = new ParameterBuilder();
//        List<Parameter> parameters = new ArrayList<>();
//        parameterBuilder.name("jwtToken").description("令牌").modelRef(new ModelRef("string")).parameterType("header")
//                .required(false).build();
//        parameters.add(parameterBuilder.build());
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                // 自行修改为自己的包路径
//                .apis(RequestHandlerSelectors.basePackage("com.oax.app.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .globalOperationParameters(parameters);
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("realeye文档")
//                .description("restful 风格接口")
//                //服务条款网址
//                //.termsOfServiceUrl("http://blog.csdn.net/forezp")
//                .version("1.0")
//                .contact(new Contact("realeye", "url", "email"))
//                .build();
//    }
//}
