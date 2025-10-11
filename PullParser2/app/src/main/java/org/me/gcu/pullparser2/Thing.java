package org.me.gcu.pullparser2;

import androidx.annotation.NonNull;

public class Thing {
    private String bolt;
    private int boltMetric;
    private int boltLength;
    private String nut;
    private String washer;
    private String description;

    public Thing(){}
    public Thing(String bolt, int boltMetric, int boltLength, String nut, String washer, String description) {
        this.bolt = bolt;
        this.boltMetric = boltMetric;
        this.boltLength = boltLength;
        this.nut = nut;
        this.washer = washer;
        this.description = description;
    }

    public String getBolt() { return bolt; }
    public void setBolt(String bolt) { this.bolt = bolt; }

    public int getBoltMetric() { return boltMetric; }
    public void setBoltMetric(int boltMetric) { this.boltMetric = boltMetric; }

    public int getBoltLength() { return boltLength; }
    public void setBoltLength(int boltLength) { this.boltLength = boltLength; }

    public String getNut() { return nut; }
    public void setNut(String nut) { this.nut = nut; }

    public String getWasher() { return washer; }
    public void setWasher(String washer) { this.washer = washer; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @NonNull
    @Override
    public String toString() {
        return "Thing{" +
                "description='" + description + '\'' +
                ", bolt='" + bolt + '\'' +
                ", boltMetric=" + boltMetric +
                ", boltLength=" + boltLength +
                ", nut='" + nut + '\'' +
                ", washer='" + washer + '\'' +
                '}';
    }
}
