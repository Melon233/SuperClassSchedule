package com.bamboo.module;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

//获取txt文本数据，解析数据到内存
public class ClassSchedule {

    // 课程类
    public static class Course {
        int week;// 星期几
        String courseName;// 课程名
        int rank;// 课程位置
        String coursePosition;// 课程地点
        boolean[] hasCourseWeek = new boolean[20];// 课程周数
        String teacher;// 课程教师
        int score;
        int courseTime;
    }

    public static LocalDate termStartDate;
    public static int curWeek = -1;
    public static Course[][] classDataMatrix = new Course[7][5];
    private static String[] colors = new String[] { "royalblue", "mediumseagreen", "goldenrod", "coral", "lightcoral", "violet", "mediumpurple", "cornflowerblue" };

    public static String getname(int i, int j) {
        return classDataMatrix[i][j].courseName;
    }

    public static int getscore(int i, int j) {
        return classDataMatrix[i][j].score;
    }

    public static int getscoursetime(int i, int j) {
        return classDataMatrix[i][j].courseTime;
    }

    public static boolean gethascourse(int i, int j, int k) {
        return classDataMatrix[i][j].hasCourseWeek[k];
    }

    public static Course getClassDataMatrix(int i, int j) {
        return classDataMatrix[i][j];
    }

    // 枚举周数
    enum Week {
        星期一, 星期二, 星期三, 星期四, 星期五, 星期六, 星期日
    }

    // 枚举课程单日排位
    enum Rank {
        Class1("1-2"), Class2("3-4"), Class3("5-6"), Class4("7-8");

        private String value;

        Rank(String val) {
            this.value = val;
        }

        // 枚举转换为排位
        public static int getcolorIndex(String target) throws Exception {
            for (Rank rank : Rank.values()) {
                if (rank.value.equals(target)) {
                    return rank.ordinal();
                }
            }
            return -1;
        }
    }

    // 课程关键数据提取
    public static void classDataExtract() {
        try {
            String classText = Files.readString(Paths.get("ClassData.txt"));
            Vector<String> hasClassDays = new Vector<>();// 有课的星期
            Vector<String> singleClassData = new Vector<>();// 单日各课程信息
            // 匹配有课的星期
            for (int i = 0; i < 7; i++) {
                Matcher dayMatcher = Pattern.compile(Week.values()[i].name()).matcher(classText);
                if (dayMatcher.find()) {
                    hasClassDays.add(dayMatcher.group());
                }
            }
            // 匹配有课日的各课程信息，不匹配最后一个有课日
            for (int i = 0; i < hasClassDays.size() - 1; i++) {
                Matcher classMatcher = Pattern
                        .compile(String.format("%s\n(.*)\n%s", hasClassDays.get(i), hasClassDays.get(i + 1)),
                                Pattern.DOTALL)
                        .matcher(classText);
                while (classMatcher.find()) {
                    singleClassData.add(classMatcher.group(1));
                }
            }
            // 匹配最后一个有课日
            Matcher finalMatcher = Pattern.compile(String.format("%s\n(.*)", hasClassDays.getLast()), Pattern.DOTALL)
                    .matcher(classText);
            if (finalMatcher.find()) {
                singleClassData.add(finalMatcher.group(1));
            }
            // 匹配并装载各日各课程信息
            for (int i = 0; i < singleClassData.size(); i++) {
                // 提取单课数据
                Matcher dayInfoMatcher = Pattern
                        .compile("(\\d-\\d).\n(.*?)\\* 周数：(.*?) 校区:未来城校区 上课地点：(.*?) 教师 ：(.*?) .*?总学时：(\\d+) 学分:(\\d+)")
                        .matcher(singleClassData.get(i));
                // 装载单日
                while (dayInfoMatcher.find()) {
                    Course course = new Course();
                    course.week = Week.valueOf(hasClassDays.get(i)).ordinal() + 1;
                    course.rank = Rank.getcolorIndex(dayInfoMatcher.group(1)) + 1;
                    course.courseName = dayInfoMatcher.group(2);
                    course.hasCourseWeek = getRange(dayInfoMatcher.group(3).replaceAll("周", ""));
                    course.coursePosition = dayInfoMatcher.group(4);
                    course.teacher = dayInfoMatcher.group(5);
                    course.courseTime = Integer.parseInt(dayInfoMatcher.group(6));
                    course.score = Integer.parseInt(dayInfoMatcher.group(7));
                    classDataMatrix[course.week - 1][course.rank - 1] = course;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // 导出某周课程
    public static void setWeekClass(ObservableList<Node> classLabelList, Label curWeekLabel, int week, ObservableList<Node> curWeekGridPane) {
        try {
            if (week > 20 || week < 1) {
                return;
            }
            curWeek = week;
            DateTimeFormatter outPutFormatter = DateTimeFormatter.ofPattern("MM/dd");
            LocalDate weekStartDate = termStartDate.plusDays((curWeek - 1) * 7);
            for (int i = 0; i < 7; i++) {
                Label.class.cast(curWeekGridPane.get(i)).setText(weekStartDate.plusDays(i).format(outPutFormatter));
            }
            HashMap<String, String> colorMap = new HashMap<>();
            int colorIndex = 0;

            curWeekLabel.setText(String.format("第%d周", curWeek));
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    Label label = Label.class.cast(classLabelList.get(i * 7 + j));
                    Course course = classDataMatrix[j][i];
                    label.setText(null);
                    label.setStyle(null);
                    if (course == null) {
                        continue;
                    }
                    if (course.hasCourseWeek[curWeek - 1]) {
                        String labelText = String.format("%s\n%s\n%s", course.courseName, course.coursePosition, course.teacher);
                        GridPane.setMargin(label, new Insets(2));// @bug
                        label.setText(labelText);
                        if (!colorMap.containsKey(course.courseName)) {
                            colorMap.put(course.courseName, colors[colorIndex++]);
                        }
                        label.setStyle("-fx-background-color:" + colorMap.get(course.courseName));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // 初始化课表
    public static void initializeClassSchedule(ObservableList<Node> classLabelList, Label curWeekLabel, ObservableList<Node> curWeekGridPane) {
        curWeek = getCurWeekData();
        if (curWeek == -1)
            return;
        termStartDate = getTermStartDate();
        classDataExtract();
    }

    // 课程周范围解析
    public static boolean[] getRange(String totalString) {
        boolean[] hasCourseWeek = new boolean[20];
        int range1, range2;
        String[] splitRangeStrings = totalString.split(",");
        for (String splitRangeString : splitRangeStrings) {
            String[] range = splitRangeString.split("-");
            range1 = Integer.parseInt(range[0]);
            if (range.length == 1)
                range2 = range1;
            else
                range2 = Integer.parseInt(range[1]);
            for (int i = range1; i <= range2; i++) {
                hasCourseWeek[i - 1] = true;
            }
        }
        return hasCourseWeek;
    }

    // 获取文件周数数据
    public static int getCurWeekData() {
        try {
            String appDataString = Files.readString(Paths.get("CurWeekData.txt"));
            Matcher curWeekMatcher = Pattern.compile("curWeek=(-?\\d+)").matcher(appDataString);
            if (curWeekMatcher.find()) {
                return Integer.parseInt(curWeekMatcher.group(1));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    // 获取开始日期
    public static LocalDate getTermStartDate() {
        try {
            String termStartDateString = Files.readString(Paths.get("TermStartDate.txt"));
            Matcher termStartDateMatcher = Pattern.compile("termStartDate=(-?\\d+)").matcher(termStartDateString);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            if (termStartDateMatcher.find()) {
                return LocalDate.parse(termStartDateMatcher.group(1), formatter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 保存当前周数数据
    public static void saveCurWeekData() {
        try {
            Files.write(Paths.get("CurWeekData.txt"), String.format("curWeek=%d",
                    ClassSchedule.curWeek).getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}