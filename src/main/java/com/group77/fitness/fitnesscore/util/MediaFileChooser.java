package com.group77.fitness.fitnesscore.util;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class MediaFileChooser {
    private final FileChooser fileChooser = new FileChooser();

    public String chooseImageFile(String title, ImageView imageView) {
        this.fileChooser.setTitle(title);
        this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = this.fileChooser.showOpenDialog(imageView.getScene().getWindow());
        if (file != null) {     // If user clicks the "cancel" button, the file object will be null
            String filePath = file.getPath();
            imageView.setImage(new Image("file:///" + filePath));   // URL-formatted local file path
            return filePath;
        }
        return null;
    }

    public String chooseVideoFile(String title, TextField textField) {
        this.fileChooser.setTitle(title);
        this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Videos", "*.*"),
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("MOV", "*.mov"),
                new FileChooser.ExtensionFilter("AVI", "*.avi")
        );

        File file = this.fileChooser.showOpenDialog(textField.getScene().getWindow());
        if (file != null) {
            String filePath = file.getPath();
            textField.setText(filePath);
            return filePath;
        }
        return null;
    }
}
