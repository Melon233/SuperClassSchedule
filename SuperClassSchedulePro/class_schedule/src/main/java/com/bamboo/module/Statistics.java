package com.bamboo.module;

import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

public class Statistics {

    public static int curweek = 1;

    public static void initializeStatistics(BarChart<String, Number> chart1, BarChart<String, Number> chart2) {
        chart1.setLegendVisible(false);
        chart2.setLegendVisible(false);
        curweek = 1;
        setstatistics_week(chart1, 1);
        setstatistics_score(chart2);
    }

    public static void setstatistics_week(BarChart<String, Number> chart, int week) {
        chart.getData().clear();
        chart.setTitle("每周课程数");
        Series<String, Number> series1 = new Series<String, Number>();
        int[] coursesum = new int[7];
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 5; j++) {
                if (ClassSchedule.getClassDataMatrix(i, j) != null) {
                    if (!ClassSchedule.gethascourse(i, j, week - 1))
                        continue;
                    coursesum[i]++;
                }
            }
        series1.getData().add(new XYChart.Data<String, Number>("周一", coursesum[0]));
        series1.getData().add(new XYChart.Data<String, Number>("周二", coursesum[1]));
        series1.getData().add(new XYChart.Data<String, Number>("周三", coursesum[2]));
        series1.getData().add(new XYChart.Data<String, Number>("周四", coursesum[3]));
        series1.getData().add(new XYChart.Data<String, Number>("周五", coursesum[4]));
        series1.getData().add(new XYChart.Data<String, Number>("周六", coursesum[5]));
        series1.getData().add(new XYChart.Data<String, Number>("周日", coursesum[6]));

        chart.getData().addAll(Arrays.asList(series1));
        for (XYChart.Series<String, Number> series : chart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                displayLabelForData(data);
            }
        }
    }

    public static void setstatistics_month(BarChart<String, Number> chart) {
        chart.setTitle("每月课程数");
        chart.getData().clear();
        Series<String, Number> series1 = new Series<String, Number>();
        int[] coursesum = new int[5];
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 5; j++) {
                if (ClassSchedule.getClassDataMatrix(i, j) != null) {
                    if (ClassSchedule.gethascourse(i, j, 0))
                        coursesum[0]++;
                    for (int k = 1; k < 5; k++)
                        if (ClassSchedule.gethascourse(i, j, k))
                            coursesum[1]++;
                    for (int k = 5; k < 10; k++)
                        if (ClassSchedule.gethascourse(i, j, k))
                            coursesum[2]++;
                    for (int k = 10; k < 14; k++)
                        if (ClassSchedule.gethascourse(i, j, k))
                            coursesum[3]++;
                    for (int k = 14; k < 18; k++)
                        if (ClassSchedule.gethascourse(i, j, k))
                            coursesum[4]++;
                }
            }
        series1.getData().add(new XYChart.Data<String, Number>("二月", coursesum[0]));
        series1.getData().add(new XYChart.Data<String, Number>("三月", coursesum[1]));
        series1.getData().add(new XYChart.Data<String, Number>("四月", coursesum[2]));
        series1.getData().add(new XYChart.Data<String, Number>("五月", coursesum[3]));
        series1.getData().add(new XYChart.Data<String, Number>("六月", coursesum[4]));
        chart.getData().addAll(Arrays.asList(series1));
        for (XYChart.Series<String, Number> series : chart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                displayLabelForData(data);
            }
        }
    }

    public static void setstatistics_score(BarChart<String, Number> chart) {
        chart.getData().clear();
        chart.setTitle("学分");
        Series<String, Number> series1 = new Series<String, Number>();
        Vector<Number> score = new Vector<Number>();
        Vector<String> name = new Vector<String>();
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 5; j++) {
                if (ClassSchedule.getClassDataMatrix(i, j) != null) {
                    name.add(ClassSchedule.getname(i, j));
                    score.add(ClassSchedule.getscore(i, j));
                }
            }

        Iterator<String> it1 = name.iterator();
        Iterator<Number> it2 = score.iterator();
        while (it1.hasNext()) {
            String nm = it1.next();
            Number sc = it2.next();
            series1.getData().add(new XYChart.Data<String, Number>(nm, sc));

        }
        chart.getData().addAll(Arrays.asList(series1));
        for (XYChart.Series<String, Number> series : chart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                displayLabelForData(data);
            }
        }

    }

    public static void setstatistics_coursetime(BarChart<String, Number> chart) {
        chart.getData().clear();
        chart.setTitle("学时");
        Series<String, Number> series1 = new Series<String, Number>();
        Vector<Number> time = new Vector<Number>();
        Vector<String> name = new Vector<String>();
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 5; j++) {
                if (ClassSchedule.getClassDataMatrix(i, j) != null) {
                    name.add(ClassSchedule.getname(i, j));
                    time.add(ClassSchedule.getscoursetime(i, j));
                }
            }
        Iterator<String> it1 = name.iterator();
        Iterator<Number> it2 = time.iterator();
        while (it1.hasNext()) {
            String nm = it1.next();
            Number tm = it2.next();
            series1.getData().add(new XYChart.Data<String, Number>(nm, tm));

        }
        chart.getData().addAll(Arrays.asList(series1));
        for (XYChart.Series<String, Number> series : chart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                displayLabelForData(data);
            }
        }

    }

    private static void displayLabelForData(XYChart.Data<String, Number> data) {
        final Label dataLabel = new Label(data.getYValue().toString());
        dataLabel.setTextFill(Color.WHITE);
        dataLabel.setAlignment(Pos.CENTER);
        dataLabel.setContentDisplay(ContentDisplay.TOP);
        dataLabel.setTextAlignment(TextAlignment.CENTER);
        dataLabel.setMouseTransparent(true);
        dataLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 5px;");
        dataLabel.setTranslateY(-10);
        ((StackPane) data.getNode()).getChildren().add(dataLabel);
    }

}
