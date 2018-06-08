package com.aloautoworks.alo.models;

public class mainlistfeed {

    private  String subtitle;
    private  String title;
    private int drawable;

    public mainlistfeed() {
    }

    public mainlistfeed(int drawable ,String title, String subtitle) {
        this.drawable = drawable;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawable() {
        return drawable;
    }

}
