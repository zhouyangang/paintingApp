package com.lee.culture.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@SpringBootApplication(scanBasePackages = "com.lee.culture.demo")
@EnableSwagger2
@EntityScan(basePackages="com.lee.culture.demo.po")
@EnableJpaRepositories(basePackages= "com.lee.culture.demo.dao")
@EnableTransactionManagement
public class Application extends WebMvcConfigurerAdapter {


	public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.lee.culture.demo.api";
	public static final String VERSION = "1.0.0";

	// Swagger Config.
	@Bean
	public Docket customImplementation() {

		ParameterBuilder parameterBuilder = new ParameterBuilder();
		parameterBuilder.parameterType("header")
				.name("Authorization")
				.defaultValue("userId")
				.description("登录用户 ID")
				.modelRef(new ModelRef("string"))
				.required(false).build();
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(parameterBuilder.build());


		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(basePackage(SWAGGER_SCAN_BASE_PACKAGE))
				.build()
				.apiInfo(metaData()).globalOperationParameters(parameters);
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder()
				.title("Culture后端服务API说明文档")
				.description("The document has integrated Culture's APIs of MicroServices by each module.")
				.license("Apache 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
				.termsOfServiceUrl("")
				.version(VERSION)
				.contact(new Contact("Lee", "http://localhost:8080/api-service/services/swagger-ui.html", "174492779@qq.com"))
				.build();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/**");
	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}
}
