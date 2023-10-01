package com.group77.fitness.fitnesscore.dao;

import com.group77.fitness.fitnesscore.vo.Video;

import java.io.IOException;
import java.util.ArrayList;

public interface VideoDAO {
    boolean addVideo(Video video, String uploadVideoCoverPath, String uploadVideoFilePath) throws IOException;
    boolean deleteVideoByID(String videoID) throws IOException;
    boolean updateVideo(Video video) throws IOException;
    Video queryVideoByID(String videoID);

    ArrayList<Video> getVideoList();
    ArrayList<Video> getVideoListByCategory(String category);
    ArrayList<Video> getVideoListByCourseLevel(String level);

    boolean updateVideoCategory(String oldVal, String newVal) throws IOException;
    boolean updateVideoLevel(String oldVal, String newVal) throws IOException;

    // ------------------------ File operations ------------------------
    // Upload an image as the cover of the video
    boolean uploadVideoCover(String filePath, Video video) throws IOException;
    boolean uploadVideoFile(String filePath, Video video) throws IOException;
    boolean deleteVideoCover(String videoID) throws IOException;
    boolean deleteVideoFile(String videoID) throws IOException;
}
