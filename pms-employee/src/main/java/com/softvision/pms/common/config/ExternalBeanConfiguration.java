package com.softvision.pms.common.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class ExternalBeanConfiguration {

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper() {
		    @Override
		    public <D> D map(Object source, Class<D> destinationType) {
		        if (source == null) {
		            return null;
		        }
		        return super.map(source, destinationType);
		    }
		};
		mapper.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE).setMatchingStrategy(MatchingStrategies.STRICT);
		return mapper;
	}

	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
        	.apis(RequestHandlerSelectors.basePackage("com.softvision.pms"))
                .paths(regex("/.*"))
                .build()
                //.apiInfo(metaData())
                ;
    }

	/*private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "PMS REST API",
                "PMS REST API for Employee",
                "1.0",
                "Terms of service",
                new Contact("John Thompson", "https://springframework.guru/about/", "john@springfrmework.guru"),
               "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0");
        return apiInfo;
    }*/

}
