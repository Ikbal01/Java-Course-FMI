package bg.sofia.uni.fmi.mjt.git;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Branch {
    private String name;
    private LinkedList<Commit> commits;
    private LinkedList<HashSet<String>> files;

    public Branch(String name) {
        this.name = name;
        commits = new LinkedList<>();
        files = new LinkedList<>();
    }

    public Branch(String name, LinkedList<Commit> commits, LinkedList<HashSet<String>> files) {
        this.name = name;
        this.commits = new LinkedList<>();
        this.files = new LinkedList<>();

        for (Commit commit : commits) {
            this.commits.add(new Commit(commit));
        }

        HashSet<String> temp;
        for (HashSet<String> fileSet : files) {
            temp = new HashSet<>(fileSet);
            this.files.add(temp);
        }
    }

    public Commit getHead() {
        return commits.peekLast();
    }

    public LinkedList<Commit> getCommits() {
        return commits;
    }

    public LinkedList<HashSet<String>> getFiles() {
        return files;
    }

    public String getName() {
        return name;
    }

    public void addCommit(Commit commit, HashSet<String> committedFiles) {
        commits.add(commit);
        HashSet<String> temp = new HashSet<>(committedFiles);
        files.add(temp);
    }
}
