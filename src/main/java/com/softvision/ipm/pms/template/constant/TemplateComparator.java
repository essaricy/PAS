package com.softvision.ipm.pms.template.constant;

import java.util.Comparator;

import com.softvision.ipm.pms.template.entity.Template;

public class TemplateComparator {

    public static final Comparator<Template> BY_NAME = new Comparator<Template>() {
        @Override
        public int compare(Template template1, Template template2) {
            if (template1 == null || template2 == null) return 0;

            String name1 = template1.getName();
            String name2 = template2.getName();
            if (name1 == null || name2 == null) return 0;

            return name1.compareTo(name2);
        }
    };

    public static final Comparator<Template> BY_NAME_NUMBER = new Comparator<Template>() {
        @Override
        public int compare(Template template1, Template template2) {
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
