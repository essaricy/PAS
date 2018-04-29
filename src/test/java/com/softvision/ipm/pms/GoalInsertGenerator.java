package com.softvision.ipm.pms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class GoalInsertGenerator {

    public static void main(String[] args) throws Exception {
        //generate("Project Management");
        generate("Technical Solutions");
        //generate("Process Awareness & Compliance");
        //generate("Corporate Initiatives");
    }

    private static void generate(String fileName) throws IOException {
        File file = new File("src/test/resources/" + fileName + ".txt");
        List<String> goalParams = FileUtils.readLines(file, "UTF-8");
        System.out.println("goalParams=" + goalParams.size());
        List<String> cleanedUpParams = cleanUp(goalParams);
        List<String> unique = findDuplicates(cleanedUpParams);
        System.out.println("unique=" + unique.size());
        for (String string : unique) {
            //System.out.println(string);
            System.out.println("INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'), (select id from goal where name='" + fileName + "'), '" + string + "');");
        }
    }

    private static List<String> findDuplicates(List<String> goalParams) {
        List<String> unique = new ArrayList<>();
        for (int index = 0; index < goalParams.size(); index++) {
            String goalParam1 = goalParams.get(index);
            boolean duplicate=false;
            for (int jindex = 0; jindex < goalParams.size(); jindex++) {
                String goalParam2 = goalParams.get(jindex);
                if (index != jindex && goalParam1.equalsIgnoreCase(goalParam2)) {
                    System.err.println(goalParam1);
                    duplicate=true;
                    break;
                }
            }
            if (!duplicate) {
                //unique.add(goalParam1);
            }
            if (!unique.contains(goalParam1)) {
                unique.add(goalParam1);
            }
        }
        return unique;
    }

    private static List<String> cleanUp(List<String> goalParams) {
        for (int index = 0; index < goalParams.size(); index++) {
            String goalParam = goalParams.get(index);
        //for (String goalParam : goalParams) {
            goalParam=goalParam.replaceFirst("^[0-9\\.]+(?!$)", "").trim();
            if (goalParam.endsWith(".")) {
                goalParam=goalParam.substring(0, goalParam.length()-1);
            }
            goalParams.set(index, goalParam);
            //System.out.println(goalParam);
        }
        return goalParams;
    }

}
