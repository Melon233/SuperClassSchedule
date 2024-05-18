package com.bamboo.module;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Settings {
    public static void ImportClassScheduleLabelClicked(TextArea classTextArea, TextField termStartDateTextField, GridPane classScheduleGridPane, Label curWeekLabel, GridPane weekDateGridPane) {
        try {
            Files.write(Paths.get("CurWeekData.txt"), "curWeek=1".getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get("ClassData.txt"), classTextArea.getText().getBytes(StandardCharsets.UTF_8));
            Files.write(Paths.get("TermStartDate.txt"), ("termStartDate=" + termStartDateTextField.getText()).getBytes(StandardCharsets.UTF_8));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
