package com.bamboo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.bamboo.module.ClassSchedule;

public class App extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/MainWindow.fxml"));
        Scene scene = new Scene(root);
        String Theme = Files.readString(Paths.get("Theme.txt"));
        scene.getStylesheets().add(getClass().getResource("css/" + Theme).toExternalForm());
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(new Image("file:icon-small.png"));
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // 保存用户数据到本地
        ClassSchedule.saveCurWeekData();
    }
}