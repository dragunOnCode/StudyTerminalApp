package com.example.studyterminalapp.utils;

import java.util.Arrays;
import java.util.List;

public class QuestionUtil {
    public static final String joinChoices(List<String> choiceSelectionList){
        if(choiceSelectionList !=null){
            StringBuilder choiceSelectionStringBuilder = new StringBuilder();
            for(String choice : choiceSelectionList){
                choiceSelectionStringBuilder.append(choice).append(QuestionConstant.CHOICES_SPLIT);
            }
            return choiceSelectionStringBuilder.toString();
        }
        throw new RuntimeException("拼接选项失败");
    }

    public static final List<String> splitChoicesStr(String choiceSelection){
        String [] selectionList = choiceSelection.split(QuestionConstant.CHOICES_SPLIT);
        return Arrays.asList(selectionList);
    }
}

