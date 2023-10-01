package com.group77.fitness.fitnesscore.media;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

// 将媒体文件复制到相应的数据目录(同时进行改名), 在进行上传时使用
public class MediaWriter {
    public static String copyFileToMediaDir(MediaDirectory mediaDirectory, String filePath, String ID) throws IOException {
        // IOException from FileUtils.copyFile()

        String newFilename = ID + '.' + FilenameUtils.getExtension(filePath);
        FileUtils.copyFile(new File(filePath), new File(mediaDirectory.getPath() + newFilename));
        return newFilename;
    }
}
