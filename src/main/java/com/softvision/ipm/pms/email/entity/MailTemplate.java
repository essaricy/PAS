package com.softvision.ipm.pms.email.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name="email_template")
public class MailTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	
	private String name;

	private String fileName;
	
	private String buttonUrl;
	
	private String toMail;
	
	private String fromMail;
	
	private String ccMail;
	
	private String subject;

	@Override
	public String toString() {
		return "EmailTemplate [id=" + id + ", name=" + name + ", fileName=" + fileName + ", buttonUrl=" + buttonUrl
				+ ", toMail=" + toMail + ", fromMail=" + fromMail + "subject " + subject +" ccMail"+ ccMail +"]";
	}
	
}