package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.dao.SeriesDAO;
import com.group77.fitness.fitnesscore.json.JsonDataFile;
import com.group77.fitness.fitnesscore.json.JsonReader;
import com.group77.fitness.fitnesscore.json.JsonWriter;
import com.group77.fitness.fitnesscore.media.MediaDirectory;
import com.group77.fitness.fitnesscore.media.MediaRemover;
import com.group77.fitness.fitnesscore.media.MediaWriter;
import com.group77.fitness.fitnesscore.vo.Series;
import com.group77.fitness.fitnesscore.vo.SeriesJson;
import com.group77.fitness.fitnesscore.vo.Video;
import com.group77.fitness.fitnesscore.vo.VideoJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class SeriesDAOImpl implements SeriesDAO {
    private SeriesJson seriesJson;

    public SeriesDAOImpl() throws Exception {  // JsonReader.readJsonToObject() may throw Exception
        this.seriesJson = (SeriesJson) JsonReader.readJsonToObject(JsonDataFile.SERIES);
    }

    private void saveSeriesJson() throws IOException {  // JsonWriter.writeObjectToJson() may throw IOException
        JsonWriter.writeObjectToJson(JsonDataFile.SERIES, this.seriesJson);
    }

    @Override
    public boolean addSeries(Series series) throws IOException {  // IOException from this.saveSeriesJson()
        // Update SeriesJson's seriesIdCounter
        this.seriesJson.setSeriesIdCounter(this.seriesJson.getSeriesIdCounter() + 1);

        series.setSeriesID("VS" + this.seriesJson.getSeriesIdCounter());  // Generate the seriesID
        this.seriesJson.getSeriesList().add(series);

        this.saveSeriesJson();
        return true;
    }

    @Override
    public boolean deleteSeriesByID(String seriesID) throws IOException {  // IOException from this.saveSeriesJson()
        Iterator<Series> seriesIterator = this.seriesJson.getSeriesList().iterator();
        while (seriesIterator.hasNext()) {
            if (seriesIterator.next().getSeriesID().equals(seriesID)) {
                seriesIterator.remove();
                if (!this.deleteSeriesCover(seriesID)) {
                    System.err.println("Delete series cover failed.");
                }
                this.saveSeriesJson();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean updateSeries(Series series) throws IOException {  // IOException from this.saveSeriesJson()
        String seriesID = series.getSeriesID();

        ArrayList<Series> seriesList = this.seriesJson.getSeriesList();
        for (int i = 0; i < seriesList.size(); i++) {
            if (seriesList.get(i).getSeriesID().equals(seriesID)) {
                seriesList.set(i, series);
                this.saveSeriesJson();
                return true;
            }
        }

        return false;
    }

    @Override
    public Series querySeriesByID(String seriesID) {
        for (Series series : this.seriesJson.getSeriesList()) {
            if (series.getSeriesID().equals(seriesID)) {
                return series;
            }
        }

        return null;
    }

    @Override
    public ArrayList<Video> getAllVideosByID(String seriesID) throws Exception {  // Exception from JsonReader.readJsonToObject()
        VideoJson videoJson = (VideoJson) JsonReader.readJsonToObject(JsonDataFile.VIDEO);
        ArrayList<Video> videoList = videoJson.getVideoList();
        ArrayList<Video> allVideos = new ArrayList<>();
        for (Video video : videoList) {
            if (video.getSeriesID().equals(seriesID)) {
                allVideos.add(video);
            }
        }

        return allVideos;
    }

    @Override
    public ArrayList<Series> getAllSeries() {
        return this.seriesJson.getSeriesList();
    }

    @Override
    public boolean uploadSeriesCover(String filePath, String seriesID) throws IOException {
        MediaWriter.copyFileToMediaDir(MediaDirectory.COVER, filePath, seriesID);
        return true;
    }

    @Override
    public boolean deleteSeriesCover(String seriesID) throws IOException {
        MediaRemover.deleteMedia(MediaDirectory.COVER, seriesID);
        return true;
    }
}
