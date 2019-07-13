package com.php25.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig extends WebFluxConfigurationSupport {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.php25.api"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("xxx系统前台api文档")
                .description(
                        "1.http状态码描述:<br>" +
                                "200:正常<br>" +
                                "500:服务器错误<br>" +
                                "400:参数验证不通过<br>" +
                                "404:无法找到相关服务连接<br>" +
                                "401:jwt不合法<br>" +
                                "2.当http状态码是200的时候,所有接口返回格式如下:<br>{\"errorCode\":0,\"returnObject\":{\"name\":\"xxx\",\"mobile\":\"18812345678\",\"email\":\"123456789@qq.com\"},\"message\":null}<br>" +
                                "json对象的各个属性项解析如下:<br>" +
                                "errorCode:0表示没有错误一切正常，1001服务器错误(这个保留暂不使用)， 1002业务逻辑错误,1003自定义错误<br>" +
                                "returnObject:返回需要用到的对象数据都会在这里面。上面这个例子是一个用户对象。注意只有errorCode为0的时候,此属性项才有效果<br>" +
                                "message:如果发生错误如业务逻辑错误,这里面就是错误的信息，格式为00000=验证码错误。注意只有当errorCode为1002或1003时候，这项才会有效果<br>")
                .termsOfServiceUrl("http://www.xxxxx.com")
                .version("1.0")
                .build();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html**")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
