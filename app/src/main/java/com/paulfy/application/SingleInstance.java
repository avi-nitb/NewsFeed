package com.paulfy.application;

import com.paulfy.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

public class SingleInstance {
    private static final SingleInstance ourInstance = new SingleInstance();

    public static SingleInstance getInstance() {
        return ourInstance;
    }

    private SingleInstance() {
    }
    private List<NewsModel.Data> news= new ArrayList<>();
    private List<NewsModel.Data> popularNews= new ArrayList<>();

    public List<NewsModel.Data> getPopularNews() {
        return popularNews;
    }

    public void setPopularNews(List<NewsModel.Data> popularNews) {
        this.popularNews = popularNews;
    }

    public List<NewsModel.Data> getNews()
    {
        return news;
    }

    public void setNews(List<NewsModel.Data> news)
    {
        this.news = news;
    }

//    private List<NewsModel.Data> new_newsList= new ArrayList<>();

//    public List<NewsModel.Data> getNew_newsList()
//    {
//        return new_newsList;
//    }

//    public void setNew_newsList(List<NewsModel.Data> new_newsList)
//    {
//        this.new_newsList = new_newsList;
//    }

    public NewsModel.Data getDataToLoad() {
        return dataToLoad;
    }

    public void setDataToLoad(NewsModel.Data dataToLoad) {
        this.dataToLoad = dataToLoad;
    }

    private NewsModel.Data dataToLoad;

}
