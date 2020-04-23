package com.rgs.covid_19_indian.dist;

public class Model_dist {

    String confirmed;
    String key;

    public Model_dist(String confirmed, String key) {
        this.confirmed = confirmed;
        this.key = key;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getKey() {
        return key;
    }
}
