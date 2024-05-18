package com.bamboo.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.bamboo.module.ClassSchedule;
import com.bamboo.module.DaySchedule;
import com.bamboo.module.Festival;
import com.bamboo.module.Note;
import com.bamboo.module.Settings;
import com.bamboo.module.Statistics;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainWindowController {
    // 主页
    private List<String> tips = new ArrayList<>();
    private int currentTipIndex = -1;
    @FXML
    private Label homepageLabel;
    @FXML
    private Pane homepagePane;
    @FXML
    private Label tipLabel;
    @FXML
    private Label c1;
    @FXML
    private Label c2;
    @FXML
    private Label c3;
    @FXML
    private Label c4;
    @FXML
    private Label hasClassLabel;
    @FXML
    private Pane FestivalPane;
    @FXML
    private Label festivalLabel;
    @FXML
    private Label today;
    @FXML
    private Label nextfestival;

    // 课程表
    @FXML
    private Label classScheduleLabel;
    @FXML
    private VBox classSchedulePane;
    @FXML
    private GridPane classScheduleGridPane;
    @FXML
    public Label curWeekLabel;
    @FXML
    private Label previousWeekLabel;
    @FXML
    private Label nextWeekLabel;
    @FXML
    private GridPane weekDateGridPane;
    @FXML
    private ScrollPane scrollPane;
    // 便签
    @FXML
    private Label noteLabel;
    @FXML
    private GridPane notePane;
    @FXML
    private TextField noteTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private VBox notesListPane;
    @FXML
    private Label newNoteLabel;
    @FXML
    private Label deleteNoteLabel;
    @FXML
    private Label saveNoteLabel;
    @FXML
    private Label searchLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField fontsizeTextField;
    // 统计
    @FXML
    private Label statisticsLabel;
    @FXML
    private GridPane statisticsPane;
    @FXML
    private BarChart<String, Number> chart1;
    @FXML
    private Label statistics_pre;
    @FXML
    private Label statistics_next;
    @FXML
    private BarChart<String, Number> chart2;
    @FXML
    private Label statistics_score;
    @FXML
    private Label statistics_coursetime;

    // 设置
    @FXML
    private Label themeChange;
    @FXML
    private Label settingsLabel;
    @FXML
    private Pane settingsPane;
    @FXML
    private Label importLabel;
    @FXML
    private TextArea classTextArea;
    @FXML
    private TextField termStartDateTextField;
    // 界面控制
    @FXML
    private VBox sidePane;
    @FXML
    private GridPane titlePane;
    @FXML
    private Label closeLabel;
    @FXML
    private ImageView closeIcon;
    @FXML
    private StackPane stackPane;

    private static ObservableList<Node> paneManagement;
    private static int curPane = 0;
    private double xOffset;
    private double yOffset;
    public Label[] dayScheduleLabels;

    // 初始化资源
    @FXML
    void initialize() throws Exception {
        dayScheduleLabels = new Label[] { c1, c2, c3, c4 };

        ClassSchedule.initializeClassSchedule(classScheduleGridPane.getChildren(), curWeekLabel, weekDateGridPane.getChildren());
        if (ClassSchedule.curWeek != -1) {
            DaySchedule.initializeDaySchedule(dayScheduleLabels, hasClassLabel);
            ClassSchedule.setWeekClass(classScheduleGridPane.getChildren(), curWeekLabel, DaySchedule.realWeek, weekDateGridPane.getChildren());
            Statistics.initializeStatistics(chart1, chart2);
        }
        Note.initializeNotesList(notesListPane, noteTextField, noteTextArea);

        loadTipsFromFile("tipData.txt");
        showNextTip();

        paneManagement = stackPane.getChildren();
        closeLabel.setOnMouseClicked(event -> Platform.exit());
        homepageLabel.setOnMouseClicked(event -> switchPane(0));
        classScheduleLabel.setOnMouseClicked(event -> switchPane(1));

        tipLabel.setOnMouseClicked(event -> {
            showNextTip();
        });
        noteLabel.setOnMouseClicked(event -> switchPane(2));
        statisticsLabel.setOnMouseClicked(event -> switchPane(3));
        settingsLabel.setOnMouseClicked(event -> switchPane(4));
        nextWeekLabel.setOnMouseClicked(event -> ClassSchedule.setWeekClass(classScheduleGridPane.getChildren(), curWeekLabel, ClassSchedule.curWeek + 1, weekDateGridPane.getChildren()));
        previousWeekLabel.setOnMouseClicked(event -> ClassSchedule.setWeekClass(classScheduleGridPane.getChildren(), curWeekLabel, ClassSchedule.curWeek - 1, weekDateGridPane.getChildren()));
        newNoteLabel.setOnMouseClicked(event -> Note.newNote(notesListPane, noteTextField, noteTextArea));
        deleteNoteLabel.setOnMouseClicked(event -> Note.deleteNote(notesListPane, deleteNoteLabel, noteTextField, noteTextArea));
        saveNoteLabel.setOnMouseClicked(event -> Note.saveNote(notesListPane, noteTextField, noteTextArea));
        fontsizeTextField.setOnAction(event -> Note.setFontsize(fontsizeTextField, noteTextArea));
        searchLabel.setOnMouseClicked(event -> Note.searchNote(searchTextField, noteTextField, noteTextArea));
        importLabel.setOnMouseClicked(event -> {
            Settings.ImportClassScheduleLabelClicked(classTextArea, termStartDateTextField, classScheduleGridPane, curWeekLabel, weekDateGridPane);
            ClassSchedule.initializeClassSchedule(classScheduleGridPane.getChildren(), curWeekLabel, weekDateGridPane.getChildren());
            DaySchedule.initializeDaySchedule(dayScheduleLabels, hasClassLabel);
            Statistics.initializeStatistics(chart1, chart2);
            ClassSchedule.setWeekClass(classScheduleGridPane.getChildren(), curWeekLabel, DaySchedule.realWeek, weekDateGridPane.getChildren());
        });
        themeChange.setOnMouseClicked(event -> switchTheme());

        titlePane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        titlePane.setOnMouseDragged(event -> {
            titlePane.getScene().getWindow().setX(event.getScreenX() - xOffset);
            titlePane.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
        // Festival
        LocalDate currentDate = LocalDate.now();
        // 格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        today.setText(formattedDate);
        Festival.initializeFestival(nextfestival);
        closeIcon.setImage(new Image("file:closeIcon.png"));
        closeIcon.setFitWidth(12);
        closeIcon.setFitWidth(12);
    }

    // 界面切换控制
    public static void switchPane(int nextPane) {
        paneManagement.get(curPane).setVisible(false);
        paneManagement.get(nextPane).setVisible(true);
        curPane = nextPane;
    }

    public void switchTheme() {
        // 获取当前scene的Stylesheet
        Scene scene = classSchedulePane.getScene();
        ObservableList<String> stylesheets = scene.getStylesheets();

        if (stylesheets.isEmpty()) {
            return; // 如果没有样式表，直接返回
        }

        String curTheme = stylesheets.get(0);
        int lastSlashIndex = curTheme.lastIndexOf('/');
        curTheme = curTheme.substring(lastSlashIndex + 1);

        // 改变Stylesheet
        String newTheme;
        if ("DarkTheme.css".equals(curTheme)) {
            newTheme = "/com/bamboo/css/LightTheme.css";
        } else if ("LightTheme.css".equals(curTheme)) {
            newTheme = "/com/bamboo/css/DarkTheme.css";
        } else {
            return;
        }

        URL resource = getClass().getResource(newTheme);
        if (resource == null) {
            return;
        }
        stylesheets.clear();
        stylesheets.add(resource.toExternalForm());
        String Theme = newTheme.substring(16);
        try {
            Files.write(Paths.get("Theme.txt"), Theme.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 统计
    public void on_statistics_pre() {
        if (Objects.equals(statistics_pre.getText(), "上一周")) {
            if (Statistics.curweek > 1) {
                Statistics.curweek--;
                Statistics.setstatistics_week(chart1, Statistics.curweek);
                chart1.setTitle(new String("第" + Statistics.curweek + "周课程节数"));
            }
        }
    }

    public void on_statistics_next() {
        if (Objects.equals(statistics_next.getText(), "下一周")) {
            if (Statistics.curweek < 20) {
                Statistics.curweek++;
                Statistics.setstatistics_week(chart1, Statistics.curweek);
                chart1.setTitle(new String("第" + Statistics.curweek + "周课程节数"));
            }
        }
    }

    public void on_statistics_week() {
        statistics_next.setText(new String("下一周"));
        statistics_pre.setText(new String("上一周"));
        Statistics.setstatistics_week(chart1, 1);
        Statistics.curweek = 1;
    }

    public void on_statistics_month() {
        statistics_next.setText(new String(" "));
        statistics_pre.setText(new String(" "));
        Statistics.setstatistics_month(chart1);
    }

    public void on_statistics_score() {
        Statistics.setstatistics_score(chart2);
    }

    public void on_statistics_time() {
        Statistics.setstatistics_coursetime(chart2);
    }

    private void loadTipsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("*")) {
                    tips.add(sb.toString().trim());
                    sb.setLength(0);
                } else {
                    sb.append(line).append("\n");
                }
            }
            // Add the last tip
            if (sb.length() > 0) {
                tips.add(sb.toString().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showNextTip() {
        if (!tips.isEmpty()) {
            Random random = new Random();
            int newIndex = random.nextInt(tips.size());
            while (newIndex == currentTipIndex) {
                newIndex = random.nextInt(tips.size());
            }
            currentTipIndex = newIndex;
            tipLabel.setText(tips.get(currentTipIndex));
        } else {
            tipLabel.setText("No tips available");
        }
    }
}
