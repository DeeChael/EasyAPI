package org.ezapi.lottery;

public class Item {

    private final String id;

    private double probability;

    public Item(String id, double probability) {
        this.id = id;
        this.probability = probability;
    }

    public String getId() {
        return id;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return id;
    }
}
