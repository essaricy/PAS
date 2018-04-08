package com.softvision.ipm.pms.web.config;

import java.util.Properties;

import javax.mail.Session;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateDetail;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;

@Configuration
public class ExternalBeanConfiguration {

	@Value("${app.security.ldap.url}") private String ldapUrl;

	@Value("${app.security.ldap.authentication}") private String authenticationUsed;

	@Value("${app.security.ldap.domain}") private String domain;

	@Value("${app.email.host}") private String emailHost;

	// TODO: 
	// 1. Create LDAP Connection source
	// 2. Free Marker template
	@Bean
	public freemarker.template.Configuration getEmailConfiguration() {
		// Freemarker configuration object
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_25);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
		return configuration;
	}

	@Bean
    public LdapContextSource contextSource () {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl(ldapUrl);
        //contextSource.setBase(env.getRequiredProperty("ldap.base"));
        //contextSource.setUserDn(env.getRequiredProperty("ldap.user"));
        //contextSource.setPassword(env.getRequiredProperty("ldap.password"));
        return contextSource;
    }

	@Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());        
    }

	@Bean
	public Session getEmailSession() {
		Properties PROPERTIES = new Properties();
		PROPERTIES.setProperty("mail.smtp.host", emailHost);
		// Get the default Session object.
		return Session.getDefaultInstance(PROPERTIES);
	}

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
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		mapper.typeMap(Template.class, TemplateDto.class)
		.addMapping(src -> src.getTemplateHeaders(), TemplateDto::setHeaders)
		//.addMapping(src -> src.getUpdatedBy(), TemplateDto::setUpdatedBy);
		.addMapping(src -> src.getUpdatedBy(), (dest, value) -> dest.getUpdatedBy().setEmployeeId(1));

		mapper.typeMap(TemplateHeader.class, TemplateHeaderDto.class)
		.addMapping(src -> src.getGoal().getId(), TemplateHeaderDto::setGoalId)
		.addMapping(src -> src.getGoal().getName(), TemplateHeaderDto::setGoalName)
		.addMapping(src -> src.getTemplateDetails(), TemplateHeaderDto::setDetails);

		mapper.typeMap(TemplateDetail.class, TemplateDetailDto.class)
		.addMapping(src -> src.getGoalParam().getId(), TemplateDetailDto::setParamId)
		.addMapping(src -> src.getGoalParam().getName(), TemplateDetailDto::setParamName);

		mapper.typeMap(TemplateDto.class, Template.class)
		.addMapping(src -> src.getHeaders(), Template::setTemplateHeaders)
		.addMapping(src -> src.getUpdatedBy().getEmployeeId(), Template::setUpdatedBy)
		;

		mapper.typeMap(TemplateHeaderDto.class, TemplateHeader.class)
		.addMapping(src -> src.getGoalId(), (dest, value) -> dest.getGoal().setId((Long) value))
		.addMapping(src -> src.getGoalName(), (dest, value) -> dest.getGoal().setName((String) value))
		.addMapping(src -> src.getDetails(), TemplateHeader::setTemplateDetails);

		mapper.typeMap(TemplateDetailDto.class, TemplateDetail.class)
		.addMapping(src -> src.getParamId(), (dest, value) -> dest.getGoalParam().setId((Long) value))
		.addMapping(src -> src.getParamName(), (dest, value) -> dest.getGoalParam().setName((String) value));

		mapper.typeMap(GoalDto.class, TemplateHeaderDto.class)
		.addMappings(m -> m.skip(TemplateHeaderDto::setId))
		.addMapping(src -> src.getId(), TemplateHeaderDto::setGoalId)
		.addMapping(src -> src.getName(), TemplateHeaderDto::setGoalName)
		.addMapping(src -> src.getParams(), TemplateHeaderDto::setDetails);

		mapper.typeMap(GoalParamDto.class, TemplateDetailDto.class)
		.addMappings(m -> m.skip(TemplateDetailDto::setId))
		.addMapping(src -> src.getId(), TemplateDetailDto::setParamId)
		.addMapping(src -> src.getName(), TemplateDetailDto::setParamName)
		.addMapping(src -> src.getApplicable(), TemplateDetailDto::setApply);

		return mapper;
	}

}
