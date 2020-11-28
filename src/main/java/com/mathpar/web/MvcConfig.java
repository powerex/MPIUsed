/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.web;

import com.mathpar.web.controller.PageArgumentResolver;
import com.mathpar.web.servlets.Image;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageArgumentResolver());
    }

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("1024KB"));
        factory.setMaxRequestSize(DataSize.parse("1024KB"));
        return factory.createMultipartConfig();
    }

    @Bean
    public ServletRegistrationBean delegateServiceExporterServlet() {
        return new ServletRegistrationBean(new Image(), "/servlet/image");
    }
}
