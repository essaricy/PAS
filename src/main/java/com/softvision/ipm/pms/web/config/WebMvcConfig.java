package com.softvision.ipm.pms.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/dashboard").setViewName("dashboard");
        registry.addViewController("/blank").setViewName("blank");
        registry.addViewController("/403").setViewName("403");

        // Views with ADMIN privileges
        registry.addViewController("/admin/cycles/list").setViewName("admin-cycle-list");
        registry.addViewController("/admin/cycles/manage").setViewName("admin-cycle-manage");
        registry.addViewController("/admin/goal/list").setViewName("admin-goal-list");
        registry.addViewController("/admin/goal/manage").setViewName("admin-goal-manage");
        registry.addViewController("/admin/template/list").setViewName("admin-template-list");
        registry.addViewController("/admin/template/manage").setViewName("admin-template-manage");
        registry.addViewController("/admin/template/assign").setViewName("admin-template-assign");

        registry.addViewController("/admin/employee/mgmt").setViewName("admin-employee-mgmt");
        registry.addViewController("/admin/employee/directory").setViewName("admin-employee-directory");
        registry.addViewController("/admin/employee/sync").setViewName("admin-employee-sync");
        registry.addViewController("/admin/employee/roles/list").setViewName("admin-employee-roles-list");
        registry.addViewController("/admin/employee/roles/manage").setViewName("admin-employee-roles-manage");

        registry.addViewController("/admin/reports").setViewName("admin-reports");
        registry.addViewController("/admin/report/appraisal/status/cycle").setViewName("admin-report-appraisal-status-cycle");
        registry.addViewController("/admin/report/appraisal/status/phase/assigned").setViewName("admin-report-appraisal-status-phase-assigned");
        registry.addViewController("/admin/report/appraisal/status/phase/unassigned").setViewName("admin-report-appraisal-status-phase-unassigned");
        registry.addViewController("/admin/report/appraisal/overview").setViewName("admin-report-appraisal-overview");

        registry.addViewController("/manager/assignment/active").setViewName("manager-assignment-active");
        registry.addViewController("/manager/assignment/list").setViewName("manager-assignment-list");
        registry.addViewController("/manager/assessment").setViewName("manager-assessment");
        registry.addViewController("/manager/reports").setViewName("manager-reports");
        registry.addViewController("/manager/report/score/cycle").setViewName("manager-report-score-cycle");
        registry.addViewController("/manager/report/score/phase").setViewName("manager-report-score-phase");

        registry.addViewController("/employee/assignment/active").setViewName("employee-assignment-active");
        registry.addViewController("/employee/assignment/list").setViewName("employee-assignment-list");
        registry.addViewController("/employee/assessment").setViewName("employee-assessment");

        registry.addViewController("support/list").setViewName("support-page");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        registry.viewResolver(resolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
        	//.addResourceLocations("classpath:favicon.ico")
        	.addResourceLocations("classpath:/AdminBSBMaterialDesign")
        	.addResourceLocations("classpath:/fonts")
        	.addResourceLocations("classpath:/scripts")
        	.addResourceLocations("classpath:/images")
            ;
    }
}