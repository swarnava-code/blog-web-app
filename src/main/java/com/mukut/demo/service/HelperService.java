package com.mukut.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class HelperService {
    public static String makeDataAndTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dateTimeFormatter.format(now);
    }

    public static String makeExcerpt(String content, int EXCERPT_LIMIT) {
        String excerpt = content.substring(0, Math.min(content.length(), EXCERPT_LIMIT)) + "...";
        return excerpt;
    }

    public List<String> makeListFromCSV(String string) {
        string = string.replaceAll(" ", "");
        List<String> itemList = Arrays.asList(string.split(","));
        return itemList;
    }
}
