package bg.sofia.uni.fmi.mjt.sentiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class WordSpecification {
    private double sentiment;
    private int frequency;

    public WordSpecification(double sentiment, int frequency) {
        this.sentiment = sentiment;
        this.frequency = frequency;
    }

    public double getSentiment() {
        return sentiment;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setSentiment(double sentiment) {
        this.sentiment = sentiment;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
