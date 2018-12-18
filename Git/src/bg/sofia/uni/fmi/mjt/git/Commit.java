package bg.sofia.uni.fmi.mjt.git;

import bg.sofia.uni.fmi.mjt.git.generator.HashGenerator;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class Commit {
    private String  message;
    private String hash;
    private String date;

    public Commit(String message) {
        this.message = message;
        LocalDateTime creation = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm yyyy");
        date = creation.format(formatter);

        try {
            hash = HashGenerator.hexDigest(date + message);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Commit(Commit commit) {
        this.message = commit.getMessage();
        this.hash = commit.getHash();
        this.date = commit.date;
    }

    public String getHash() {
        return hash;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

}
