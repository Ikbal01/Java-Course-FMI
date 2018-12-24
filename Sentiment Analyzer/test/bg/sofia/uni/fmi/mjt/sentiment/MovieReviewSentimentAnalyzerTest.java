package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.Before;
import org.junit.Test;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class MovieReviewSentimentAnalyzerTest {
    private static final double PRECISION = 0.001;

    private MovieReviewSentimentAnalyzer sentimentAnalyzer;

    private InputStream reviewsStream;
    private InputStream stopwordsStream;
    private OutputStream resultStream;

    @Before
    public void init() throws FileNotFoundException {
        stopwordsStream = new FileInputStream("resources/stopwords.txt");
        reviewsStream = new FileInputStream("resources/reviews.txt");
        resultStream = new FileOutputStream("resources/reviews.txt", true);
        sentimentAnalyzer = new MovieReviewSentimentAnalyzer(stopwordsStream, reviewsStream, resultStream);
    }

    @Test
    public void testIsStopWordNegativeFromDictionary() {
        String assertMessage = "A word should not be incorrectly identified as a stopword," +
                " if it is not part of the stopwords list";
        assertFalse(assertMessage, sentimentAnalyzer.isStopWord("effects"));
    }

    @Test
    public void testIsStopWordNegativeNotFromDictionary() {
        String assertMessage = "A word should not be incorrectly identified as a stopword," +
                " if it is not part of the stopwords list";
        assertFalse(assertMessage, sentimentAnalyzer.isStopWord("stoyo"));
    }

    @Test
    public void testIsStopWordPositive() {
        assertTrue("Stop word not counted as stop word", sentimentAnalyzer.isStopWord("a"));
    }

    @Test
    public void testIsStopWord() {
        String word = "most";
        String message = "The word " + word + " should be stop word";
        assertTrue(message, sentimentAnalyzer.isStopWord(word));
    }

    @Test
    public void testIsNotStopWord() {
        String word = "bad";
        String message = "The word " + word + " should not be stop word";
        assertFalse(message, sentimentAnalyzer.isStopWord(word));
    }


    @Test
    public void testGetWordSentiment() {
        String word = "good";
        final int two = 2;
        String message = "The sentiment of " + word + " should be " + two;

        assertEquals(message, two, sentimentAnalyzer.getWordSentiment(word), PRECISION);
    }

    @Test
    public void testGetNotContainingWordSentiment() {
        String word = "NotContaining";
        final double defaultValue = -1.0;
        String message = "The sentiment of " + word + " should be default -1.0";

        assertEquals(message, defaultValue, sentimentAnalyzer.getWordSentiment(word), PRECISION);
    }

    @Test
    public void testGetReviewSentimentAsName() {
        String word = "bad";
        String sentimentAsName = "somewhat negative";
        String message = "The review sentiment of " + word + " should be " + sentimentAsName;

        assertEquals(message, sentimentAsName, sentimentAnalyzer.getReviewSentimentAsName(word));
    }

    @Test
    public void testGetReviewSentimentAsNameIfUnknown() {
        String word = "unknownWord";
        String sentimentAsName = "unknown";
        String message = "The review sentiment of " + word + " should be " + sentimentAsName;

        assertEquals(message, sentimentAsName, sentimentAnalyzer.getReviewSentimentAsName(word));
    }

    @Test
    public void testGetReviewSentiment() {
        String message = "Sentiment dictionary should be changed after appending new review";

        double beforeChange = sentimentAnalyzer.getReviewSentiment("bad");

        final int randomSentimentValue = 4;
        sentimentAnalyzer.appendReview("bad", randomSentimentValue);

        assertNotEquals(message, beforeChange, sentimentAnalyzer.getReviewSentiment("bad"));
    }

    @Test
    public void testGetReviewSentimentOfUnknownReview() {
        String review = "unknown review";
        final double sentiment = -1.0;

        String message = "The review sentiment of " + review + " should be " + sentiment;

        assertEquals(message, sentiment, sentimentAnalyzer.getReviewSentiment(review), PRECISION);
    }

    @Test
    public void testGetSentimentDictionarySize() {
        String message = "The size of sentiment dictionary should be increased after appending new review";
        int beforeAppendSize = sentimentAnalyzer.getSentimentDictionarySize();

        final int randomSentimentValue = 2;
        sentimentAnalyzer.appendReview("new review", randomSentimentValue);

        assertNotEquals(message, beforeAppendSize, sentimentAnalyzer.getSentimentDictionarySize());
    }

    @Test
    public void testGetMostFrequentWords() {
        List<String> mostFrequentWords = new LinkedList<>();
        mostFrequentWords.add("bad");
        String message = "Most frequent word should be " + ((LinkedList<String>) mostFrequentWords).getFirst();

        assertEquals(message, mostFrequentWords, sentimentAnalyzer.getMostFrequentWords(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMostFrequentWordsIfnIsNegativeShouldThrowIllegalArgException()  {
        final int randomNegativeNumber = -4;
        sentimentAnalyzer.getMostFrequentWords(randomNegativeNumber);
    }

    @Test
    public void testAppendReviewShouldChangeSentimentValues() {
        String word = "bad";
        final int randomSentiment = 3;
        String message = "Sentiment of " + word + " should be changed after appending";

        double beforeAppendSentValue = sentimentAnalyzer.getWordSentiment(word);

        sentimentAnalyzer.appendReview(word, randomSentiment);

        assertNotEquals(message, beforeAppendSentValue, sentimentAnalyzer.getWordSentiment(word));
    }

    @Test
    public void testGetReviewIfThereIsNoSuchSentiment() {
        final double sentiment = 1.119;

        assertNull(sentimentAnalyzer.getReview(sentiment));
    }
}
