package com.group77.fitness.fitnesscore.dao.impl;

import com.group77.fitness.fitnesscore.dao.AccountDAO;
import com.group77.fitness.fitnesscore.dao.SearchDAO;
import com.group77.fitness.fitnesscore.dao.SeriesDAO;
import com.group77.fitness.fitnesscore.dao.VideoDAO;
import com.group77.fitness.fitnesscore.vo.*;

import java.util.ArrayList;

public class SearchDAOImpl implements SearchDAO {
    private final AccountDAO accountDAO = new AccountDAOImpl();
    private final VideoDAO videoDAO = new VideoDAOImpl();
    private final SeriesDAO seriesDAO = new SeriesDAOImpl();

    public SearchDAOImpl() throws Exception {}

    private String createAccountItem(Account account) {
        if (account instanceof Manager) return String.format("[Manager] %s", account.getAccountID());
        if (account instanceof Trainer) return String.format("[Trainer] %s", account.getAccountID());
        if (account instanceof Member) return String.format("[Member] %s", account.getAccountID());

        return "";
    }

    private String createVideoItem(Video video) {
        return String.format("[Video] %s - %s", video.getVideoID(), video.getDisplayName());
    }

    private String createSeriesItem(Series series) {
        return String.format("[Series] %s - %s", series.getSeriesID(), series.getSeriesName());
    }

    @Override
    public ArrayList<String> searchByKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        ArrayList<String> result = new ArrayList<>();

        ArrayList<Account> accounts = this.accountDAO.getAllAccounts();
        for (Account account : accounts) {
            if (account.getAccountID().toLowerCase().contains(keyword)) result.add(this.createAccountItem(account));
        }

        ArrayList<Video> videos = this.videoDAO.getVideoList();
        for (Video video : videos) {
            if (video.getVideoID().toLowerCase().contains(keyword) ||
                    video.getDisplayName().toLowerCase().contains(keyword))
                result.add(this.createVideoItem(video));
        }

        ArrayList<Series> series = this.seriesDAO.getAllSeries();
        for (Series eachSeries : series) {
            if (eachSeries.getSeriesID().toLowerCase().contains(keyword) ||
                    eachSeries.getSeriesName().toLowerCase().contains(keyword))
                result.add(this.createSeriesItem(eachSeries));
        }

        return result;
    }
}
