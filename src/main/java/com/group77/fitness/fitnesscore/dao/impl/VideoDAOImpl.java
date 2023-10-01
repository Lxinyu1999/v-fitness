package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.dao.VideoDAO;
import com.group77.fitness.fitnesscore.json.JsonDataFile;
import com.group77.fitness.fitnesscore.json.JsonReader;
import com.group77.fitness.fitnesscore.json.JsonWriter;
import com.group77.fitness.fitnesscore.media.MediaDirectory;
import com.group77.fitness.fitnesscore.media.MediaRemover;
import com.group77.fitness.fitnesscore.media.MediaWriter;
import com.group77.fitness.fitnesscore.vo.Video;
import com.group77.fitness.fitnesscore.vo.VideoJson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class VideoDAOImpl implements VideoDAO {
    private final VideoJson videoJson;

    public VideoDAOImpl () throws Exception {
        this.videoJson = (VideoJson) JsonReader.readJsonToObject(JsonDataFile.VIDEO);
    }

    private void saveVideoJson() throws IOException {
        JsonWriter.writeObjectToJson(JsonDataFile.VIDEO, this.videoJson);
    }

    @Override
    public boolean addVideo(Video video, String uploadVideoCoverPath, String uploadVideoFilePath)
            throws IOException { // IOException from this.saveVideoJson()
        // Generate the videoID
        video.setVideoID("SV" + (this.videoJson.getVideoIdCounter() + 1));

        // Assign the date and time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        video.setUploadTime(simpleDateFormat.format(new Date()));

        // Update VideoJson's videoIdCounter
        this.videoJson.setVideoIdCounter(this.videoJson.getVideoIdCounter() + 1);

        this.uploadVideoCover(uploadVideoCoverPath, video);
        this.uploadVideoFile(uploadVideoFilePath, video);

        this.videoJson.getVideoList().add(video);
        this.saveVideoJson();
        return true;
    }

    @Override
    public boolean deleteVideoByID(String videoID) throws IOException {  // IOException from this.saveVideoJson()
        Iterator<Video> videoIterator = this.videoJson.getVideoList().iterator();
        while (videoIterator.hasNext()) {
            Video video = videoIterator.next();
            if (video.getVideoID().equals(videoID)) {
                videoIterator.remove();
                if (!this.deleteVideoCover(videoID)) {
                    System.err.println("Delete video cover failed.");
                }
                if (!this.deleteVideoFile(videoID)) {
                    System.err.println("Delete video file failed.");
                }
                this.saveVideoJson();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean updateVideo(Video video) throws IOException {  // IOException from this.saveVideoJson()
        String videoID = video.getVideoID();

        ArrayList<Video> videoList = this.videoJson.getVideoList();
        for (int i = 0; i < videoList.size(); i++) {
            if (videoList.get(i).getVideoID().equals(videoID)) {
                videoList.set(i, video);
                this.saveVideoJson();
                return true;
            }
        }

        return false;
    }

    @Override
    public Video queryVideoByID(String videoID) {
        for (Video video : this.videoJson.getVideoList()) {
            if (video.getVideoID().equals(videoID)) {
                return video;
            }
        }

        return null;
    }

    @Override
    public ArrayList<Video> getVideoList() {
        return this.videoJson.getVideoList();
    }

    @Override
    public ArrayList<Video> getVideoListByCategory(String category) {
        ArrayList<Video> result = new ArrayList<>();

        for (Video video : this.videoJson.getVideoList()) {
            if (video.getCategory().equals(category)) {
               result.add(video);
            }
        }

        return result;
    }

    @Override
    public ArrayList<Video> getVideoListByCourseLevel(String level) {
        ArrayList<Video> result = new ArrayList<>();

        for (Video video : this.videoJson.getVideoList()) {
            if (video.getLevel().equals(level)) {
                result.add(video);
            }
        }

        return result;
    }

    @Override
    public boolean updateVideoCategory(String oldVal, String newVal) throws IOException {
        for (Video video : this.videoJson.getVideoList()) {
            if (video.getCategory().equals(oldVal)) {
                video.setCategory(newVal);
            }
        }
        this.saveVideoJson();
        return true;
    }

    @Override
    public boolean updateVideoLevel(String oldVal, String newVal) throws IOException {
        for (Video video : this.videoJson.getVideoList()) {
            if (video.getLevel().equals(oldVal)) {
                video.setLevel(newVal);
            }
        }
        this.saveVideoJson();
        return true;
    }

    @Override
    public boolean uploadVideoCover(String filePath, Video video) throws IOException {
        String newFilename = MediaWriter.copyFileToMediaDir(MediaDirectory.COVER, filePath, video.getVideoID());
        video.setCoverFilename(newFilename);    // Update the filename of cover
        return true;
    }

    @Override
    public boolean uploadVideoFile(String filePath, Video video) throws IOException {
        String newFilename = MediaWriter.copyFileToMediaDir(MediaDirectory.VIDEO, filePath, video.getVideoID());
        video.setVideoFilename(newFilename);    // Update the filename of video
        return true;
    }

    @Override
    public boolean deleteVideoCover(String videoID) throws IOException {
        MediaRemover.deleteMedia(MediaDirectory.COVER, videoID);
        return true;
    }

    @Override
    public boolean deleteVideoFile(String videoID) throws IOException {
        MediaRemover.deleteMedia(MediaDirectory.VIDEO, videoID);
        return true;
    }
}
