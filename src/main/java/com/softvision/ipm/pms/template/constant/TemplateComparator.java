package com.softvision.ipm.pms.template.constant;

import java.util.Comparator;

import com.softvision.ipm.pms.template.model.TemplateDto;

public class TemplateComparator {

    public static final Comparator<TemplateDto> BY_NAME = new Comparator<TemplateDto>() {
        @Override
        public int compare(TemplateDto template1, TemplateDto template2) {
            if (template1 == null || template2 == null) return 0;

            String name1 = template1.getName();
            String name2 = template2.getName();
            if (name1 == null || name2 == null) return 0;

            return name1.compareTo(name2);
        }
    };

    public static final Comparator<TemplateDto> BY_NAME_NUMBER = new Comparator<TemplateDto>() {
        @Override
        public int compare(TemplateDto template1, TemplateDto template2) {
            if (template1 == null || template2 == null) return 0;

            String name1 = template1.getName();
            String name2 = template2.getName();
            if (name1 == null || name2 == null) return 0;

            return extractInt(name1) - extractInt(name2);
        }
        int extractInt(String name) {
            String num = name.replaceAll("\\D", "");
            // return 0 if no digits found
            return num.isEmpty() ? 0 : Integer.parseInt(num);
        }
    };

}
