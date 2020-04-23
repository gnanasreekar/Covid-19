package com.rgs.covid_19_indian;

public class MOdel {
    String active;
    String confirmed;
    String deaths;
    String recovered;
    String state;
    String deltaconformed;
    String deltadeath;
    String deltarecovered;

    public MOdel(String active, String confirmed, String deaths, String recovered, String state, String deltaconformed, String deltadeath, String deltarecovered) {
        this.active = active;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.state = state;
        this.deltaconformed = deltaconformed;
        this.deltadeath = deltadeath;
        this.deltarecovered = deltarecovered;
    }

    public String getActive() {
        return active;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getState() {
        return state;
    }

    public String getDeltaconformed() {
        return deltaconformed;
    }

    public String getDeltadeath() {
        return deltadeath;
    }

    public String getDeltarecovered() {
        return deltarecovered;
    }
}
