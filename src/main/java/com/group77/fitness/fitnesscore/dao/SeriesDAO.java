package com.group77.fitness.fitnesscore.dao;

import com.group77.fitness.fitnesscore.vo.Series;
import com.group77.fitness.fitnesscore.vo.Video;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public interface SeriesDAO {
    boolean addSeries(Series series) throws IOException;
    boolean deleteSeriesByID(String seriesID) throws IOException;
    boolean updateSeries(Series series) throws IOException;
    Series querySeriesByID(String seriesID);

    ArrayList<Video> getAllVideosByID(String seriesID) throws Exception;
    ArrayList<Series> getAllSeries();

    boolean uploadSeriesCover(String filePath, String seriesID) throws IOException;
    boolean deleteSeriesCover(String seriesID) throws IOException;
}
