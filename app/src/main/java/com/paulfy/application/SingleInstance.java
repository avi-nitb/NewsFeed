package com.paulfy.application;

import com.paulfy.model.NewsModel;

public class SingleInstance {
    private static final SingleInstance ourInstance = new SingleInstance();

    public static SingleInstance getInstance() {
        return ourInstance;
    }

    private SingleInstance() {
    }

    public NewsModel.Data getDataToLoad() {
        return dataToLoad;
    }

    public void setDataToLoad(NewsModel.Data dataToLoad) {
        this.dataToLoad = dataToLoad;
    }

    private NewsModel.Data dataToLoad;

}
