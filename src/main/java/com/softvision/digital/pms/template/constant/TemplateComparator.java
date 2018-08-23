package com.softvision.digital.pms.template.constant;

import java.util.Comparator;

import com.softvision.digital.pms.template.model.TemplateDto;

public class TemplateComparator {

	private TemplateComparator() {}

	public static final Comparator<TemplateDto> BY_NAME = ((TemplateDto template1, TemplateDto template2) -> {
		if (template1 == null || template2 == null) {
			return 0;
		}

        String name1 = template1.getName();
        String name2 = template2.getName();
        if (name1 == null || name2 == null) {
        	return 0;
        }
        return name1.compareTo(name2);
	}); 

    public static final Comparator<TemplateDto> BY_NAME_NUMBER = ((TemplateDto template1, TemplateDto template2) -> {
    	if (template1 == null || template2 == null) {
    		return 0;
    	}

    	String name1 = template1.getName();
    	String name2 = template2.getName();
    	if (name1 == null || name2 == null) {
    		return 0;
    	}

    	String num1 = name1.replaceAll("\\D", "");
    	int value1=num1.isEmpty() ? 0 : Integer.parseInt(num1);

    	String num2 = name2.replaceAll("\\D", "");
    	int value2=num2.isEmpty() ? 0 : Integer.parseInt(num2);

    	return value1 - value2;
    });

}
