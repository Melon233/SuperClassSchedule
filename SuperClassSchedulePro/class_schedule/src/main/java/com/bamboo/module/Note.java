package com.bamboo.module;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Note {
    private static File curFile;
    private static File notesFolder;
    private static File[] noteFiles;

    public static void initializeNotesList(VBox notesListPane, TextField noteTextField, TextArea noteTextArea) {
        try {
            noteFiles = new File[300];
            notesFolder = new File("Notes");
            updateNotesList(notesListPane, noteTextField, noteTextArea);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void updateNotesList(VBox notesListPane, TextField noteTextField, TextArea noteTextArea) {
        try {
            noteFiles = notesFolder.listFiles();
            notesListPane.getChildren().clear();
            for (File noteFile : noteFiles) {
                Label noteLabel = new Label(noteFile.getName().split("\\.")[0]);
                noteLabel.setOnMouseClicked(event -> setCurNote(noteFile, noteTextField, noteTextArea));
                noteLabel.getStyleClass().add("clickedLabel");
                noteLabel.setPrefHeight(75);
                noteLabel.setPrefWidth(300);
                notesListPane.getChildren().add(noteLabel);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void setCurNote(File noteFile, TextField noteTextField, TextArea noteTextArea) {
        try {
            noteTextField.setText(noteFile.getName().split("\\.")[0]);
            noteTextArea.setText(Files.readString(Paths.get(noteFile.getPath())));
            curFile = noteFile;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void saveNote(VBox notesListPane, TextField noteTextField, TextArea noteTextArea) {
        try {
            curFile.renameTo(new File("Notes/" + noteTextField.getText() + ".txt"));
            Files.write(Paths.get("Notes/" + noteTextField.getText() + ".txt"), noteTextArea.getText().getBytes(StandardCharsets.UTF_8));
            updateNotesList(notesListPane, noteTextField, noteTextArea);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteNote(VBox notesListPane, Label curNoteLabel, TextField noteTextField,
            TextArea noteTextArea) {
        try {
            curFile.delete();
            noteTextField.clear();
            noteTextArea.clear();
            updateNotesList(notesListPane, noteTextField, noteTextArea);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void newNote(VBox notesListPane, TextField noteTextField, TextArea noteTextArea) {
        try {
            Label label = new Label("新便签");
            noteTextField.setText("新便签");
            noteTextArea.setText("");

            File file = Files.write(Paths.get("Notes/" + noteTextField.getText() + ".txt"),
                    noteTextArea.getText().getBytes(StandardCharsets.UTF_8)).toFile();
            label.setOnMouseClicked(event -> setCurNote(file, noteTextField, noteTextArea));
            label.setPrefHeight(100);
            label.setPrefWidth(200);
            label.getStyleClass().add("clickedLabel");
            notesListPane.getChildren().add(label);
            updateNotesList(notesListPane, noteTextField, noteTextArea);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void searchNote(TextField searchTextField, TextField noteTextField, TextArea noteTextArea) {
        try {
            for (File noteFile : noteFiles) {
                if (noteFile.getName().contains(searchTextField.getText())) {
                    setCurNote(noteFile, noteTextField, noteTextArea);
                    return;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void setFontsize(TextField fontsizeTextField, TextArea noteTextArea) {
        try {
            noteTextArea.setStyle("-fx-font-size: " + fontsizeTextField.getText() + "px;");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
