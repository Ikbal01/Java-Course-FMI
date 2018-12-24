package bg.sofia.uni.fmi.mjt.sentiment;

import bg.sofia.uni.fmi.mjt.sentiment.interfaces.SentimentAnalyzer;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

    private enum Sentiment {
        NEGATIVE("negative"),
        SOMEWHAT_NEGATIVE("somewhat negative"),
        NEUTRAL("neutral"),
        SOMEWHAT_POSITIVE("somewhat positive"),
        POSITIVE("positive");

        String sentScore;
        private Sentiment(String sentScore) {
            this.sentScore = sentScore;
        }
    }

    private HashSet<String> stopwords;
    private HashMap<String, WordSpecification> sentimentDictionary;

    private List<String> reviews;

    private OutputStream reviewsOutput;

    public MovieReviewSentimentAnalyzer(InputStream stopwordsInput,
                                        InputStream reviewsInput, OutputStream reviewsOutput) {

        this.reviewsOutput = reviewsOutput;

        reviews = new LinkedList<>();

        readStopwords(stopwordsInput);
        readReviews(reviewsInput);
    }

    private void readStopwords(InputStream stopwordsInput) {
        stopwords = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stopwordsInput))) {

            String stopword;

            while ((stopword = reader.readLine()) != null) {
                stopwords.add(stopword);
            }

        } catch (IOException ioEx) {
            throw new RuntimeException("Error occurred while reading stopwords", ioEx);
        }
    }

    private void readReviews(InputStream reviewsInput) {

        sentimentDictionary = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(reviewsInput))) {

            String review;
            while ((review = reader.readLine()) != null) {

                readReview(review);
                String[] arr = review.split(" ", 2);
                reviews.add(arr[1]);
            }

        } catch (IOException ioEx) {
            throw new RuntimeException("Error occurred while reading reviews", ioEx);
        }
    }

    private void readReview(String review) {
        List<String> currWords = split(review);

        double sentiment = Double.parseDouble(currWords.get(0));
        currWords.remove(0);

        putToSentimentDictionary(currWords, sentiment);
    }

    private List<String> split(String review) {
        review = review.toLowerCase();
        List<String> currWords = new LinkedList<>(Arrays.asList(review.split("[\\W]")));
        currWords.removeAll(Collections.singletonList(""));
        currWords.removeAll(stopwords);

        return currWords;
    }

    private void putToSentimentDictionary(List<String> words, double sentiment) {

        for (String word : words) {
            if (sentimentDictionary.containsKey(word)) {
                updateWordSpecification(word, sentiment);

            } else {
                WordSpecification wordSpecification = new WordSpecification(sentiment, 1);
                sentimentDictionary.put(word, wordSpecification);
            }
        }
    }

    private void updateWordSpecification(String word, double sentiment) {
        WordSpecification wordSpec = sentimentDictionary.get(word);

        int newFrequency = wordSpec.getFrequency() + 1;
        double newSentiment = ((wordSpec.getSentiment() * wordSpec.getFrequency()) + sentiment) / newFrequency;

        wordSpec.setSentiment(newSentiment);
        wordSpec.setFrequency(newFrequency);
    }

    @Override
    public double getReviewSentiment(String review) {
        if (review == null) {
            return -1.0;
        }
        List<String> words = split(review);

        double sentiments = 0;
        int count = 0;

        for (String word : words) {
            if (sentimentDictionary.containsKey(word)) {
                sentiments += sentimentDictionary.get(word).getSentiment();
                count++;
            }
        }

        return count != 0 ? sentiments / count : -1.0;
    }

    @Override
    public String getReviewSentimentAsName(String review) {
        if (review != null) {

            int reviewSentiment = (int) Math.round(getReviewSentiment(review));

            if (reviewSentiment != -1.0) {
                return Sentiment.values()[reviewSentiment].sentScore;
            }
        }

        return "unknown";
    }

    @Override
    public double getWordSentiment(String word) {
        word = word.toLowerCase();

        if (sentimentDictionary.containsKey(word)) {
            return sentimentDictionary.get(word).getSentiment();
        } else {
            return -1.0;
        }
    }

    @Override
    public String getReview(double sentimentValue) {

        for (String review : reviews) {
            double sentiment = getReviewSentiment(review);
            final double precision = 0.001;

            if (Math.abs(sentiment - sentimentValue) < precision) {
                return review;
            }
        }

        return null;
    }

    @Override
    public Collection<String> getMostFrequentWords(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        } else {

            int limit = sentimentDictionary.size() - n;
            if (n >= sentimentDictionary.size()) {
                limit = 0;
            }

            return sentimentDictionary.entrySet().stream()
                    .sorted(Comparator.comparing(x -> x.getValue().getFrequency()))
                    .map(Map.Entry::getKey)
                    .skip(limit)
                    .limit(sentimentDictionary.size())
                    .collect(Collectors.toList());
        }

    }

    @Override
    public Collection<String> getMostPositiveWords(int n) {

        int limit = sentimentDictionary.size() - n;
        if (n >= sentimentDictionary.size()) {
            limit = 0;
        }

        return sentimentDictionary.entrySet().stream()
                .sorted(Comparator.comparing(x -> x.getValue().getSentiment()))
                .map(Map.Entry::getKey)
                .skip(limit)
                .limit(sentimentDictionary.size())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<String> getMostNegativeWords(int n) {

        return sentimentDictionary.entrySet().stream()
                .sorted(Comparator.comparing(x -> x.getValue().getSentiment()))
                .map(Map.Entry::getKey)
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public void appendReview(String review, int sentimentValue) {
        String newReview = sentimentValue + " " + review;
        readReview(newReview);
        reviews.add(review);

        try {
            reviewsOutput.write(((newReview) + System.lineSeparator()).getBytes());
            reviewsOutput.flush();

        } catch (IOException ioExc) {
            throw new RuntimeException("Error occurred while writing reviews", ioExc);
        }
    }

    @Override
    public int getSentimentDictionarySize() {
        return sentimentDictionary.size();
    }

    @Override
    public boolean isStopWord(String word) {
        return stopwords.contains(word);
    }
}
