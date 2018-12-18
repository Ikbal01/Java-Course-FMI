package bg.sofia.uni.fmi.mjt.git;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class Repository {

    private HashSet<String> files;
    private HashSet<Branch> branches;
    public Branch currBranch;

    public Repository() {
        branches = new HashSet<>();
        currBranch = new Branch("master");
        branches.add(currBranch);

        files = new HashSet<>();
    }

    public Result add(String... files) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String file : files) {
            if (this.files.contains(file)) {
                stringBuilder.append("'");
                stringBuilder.append(file);
                stringBuilder.append("' already exists");

                return new Result(false, stringBuilder.toString());
            }
        }

        stringBuilder.append("added ");
        stringBuilder.append(String.join(", ", files));
        stringBuilder.append(" to stage");

        this.files.addAll(Arrays.asList(files));

        return new Result(true, stringBuilder.toString());
    }

    public Result commit(String message) {
        int changes = findDifference(this.files, currBranch.getFiles().peekLast());

        if (changes == 0) {
            return new Result(false, "nothing to commit, working tree clean");
        }

        Commit newCommit = new Commit(message);
        currBranch.addCommit(newCommit, this.files);

        return new Result(true, changes + " files changed");
    }

    public Result remove(String... files) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = files.length - 1; i >= 0 ; i--) {
            if (!this.files.contains(files[i])) {
                stringBuilder.append("\'");
                stringBuilder.append(files[i]);
                stringBuilder.append("\' did not match any files");

                return new Result(false, stringBuilder.toString());
            }
        }

        this.files.removeAll(Arrays.asList(files));

        stringBuilder.append("added ");
        stringBuilder.append(String.join(", ", files));
        stringBuilder.append(" for removal");

        return new Result(true, stringBuilder.toString());
    }

    public Commit getHead() {
        return currBranch.getHead();
    }

    public Result log() {
        StringBuilder stringBuilder = new StringBuilder();
        LinkedList<Commit> commits = currBranch.getCommits();

        if (commits.isEmpty()) {
            stringBuilder.append("branch ");
            stringBuilder.append(currBranch.getName());
            stringBuilder.append(" does not have any commits yet");

            return new Result(false, stringBuilder.toString());
        }
        for (int i = commits.size() - 1; i >= 0; i--) {
            Commit commit = commits.get(i);
            stringBuilder.append("commit ");
            stringBuilder.append(commit.getHash());
            stringBuilder.append("\nDate: ");
            stringBuilder.append(commit.getDate());
            stringBuilder.append("\n\n\t");
            stringBuilder.append(commit.getMessage());
            if (i > 0) {
                stringBuilder.append("\n\n");
            }
        }

        return new Result(true, stringBuilder.toString());
    }

    public String getBranch() {
        return currBranch.getName();
    }

    public Result createBranch(String name) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Branch branch : branches) {
            if (branch.getName().equals(name)) {
                stringBuilder.append("branch ");
                stringBuilder.append(name);
                stringBuilder.append(" already exists");

                return new Result(false, stringBuilder.toString());
            }
        }

        Branch branch = new Branch(name, currBranch.getCommits(), currBranch.getFiles());
        branches.add(branch);

        stringBuilder.append("created branch ");
        stringBuilder.append(name);

        return new Result(true, stringBuilder.toString());
    }

    public Result checkoutBranch(String name) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Branch branch : branches) {
            if (branch.getName().equals(name)) {
                currBranch = branch;
                stringBuilder.append("switched to branch ");
                stringBuilder.append(currBranch.getName());
                return new Result(true, stringBuilder.toString());
            }
        }

        stringBuilder.append("branch ");
        stringBuilder.append(name);
        stringBuilder.append(" does not exist");

        return new Result(false, stringBuilder.toString());
    }

    public Result checkoutCommit(String hash) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean exists = false;
        int index = 0;

        LinkedList<Commit> commits = currBranch.getCommits();
        LinkedList<HashSet<String>> currBranchFiles = this.currBranch.getFiles();
        for (int i = 0; i < commits.size(); i++) {
            if (commits.get(i).getHash().equals(hash)) {
                exists = true;
                index = i;
                break;
            }
        }

        if (!exists) {
            stringBuilder.append("commit ");
            stringBuilder.append(hash);
            stringBuilder.append(" does not exist");
            return new Result(false, stringBuilder.toString());
        }

        for (int i = commits.size() - 1; i > index; i--) {
            commits.remove(i);
            currBranchFiles.remove(i);
        }

        files.clear();
        files.addAll(this.currBranch.getFiles().peekLast());

        stringBuilder.append("HEAD is now at ");
        stringBuilder.append(hash);

        return new Result(true, stringBuilder.toString());
    }

    public int findDifference(HashSet<String> filesOne, HashSet<String> filesTwo) {
        int counter = 0;

        if (filesOne == null && filesTwo == null) {
            return 0;
        } else if (filesOne == null) {
            return filesTwo.size();
        } else if (filesTwo == null) {
            return filesOne.size();
        }

        for (String file : filesOne) {
            if (filesTwo.contains(file)) {
                counter++;
            }
        }

        return (filesOne.size() + filesTwo.size()) - counter * 2;
    }
}
