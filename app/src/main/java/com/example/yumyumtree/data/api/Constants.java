package com.example.yumyumtree.data.api;

public class Constants {

    private static Constants constants;
    private boolean dataLoading;

    private Constants() {
    }

    public synchronized static Constants getInstance() {
        if (constants == null) {
            constants = new Constants();
        }
        return constants;
    }

    public boolean getDataLoading() {
        return dataLoading;
    }

    public void setDataLoading(boolean dataLoading) {
        this.dataLoading = dataLoading;
    }
}
