package bg.sofia.uni.fmi.mjt.git.test;

import bg.sofia.uni.fmi.mjt.git.Repository;
import bg.sofia.uni.fmi.mjt.git.Result;
import bg.sofia.uni.fmi.mjt.git.generator.HashGenerator;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class SampleRepositoryTest {
    private Repository repo;

    @Before
    public void setUp() {
        repo = new Repository();
    }


    @Test
    public void testLog() {
        repo.add("foo.txt");
        repo.commit("first");
        repo.remove("foo.txt");
        repo.commit("second");

        System.out.println(repo.log().getMessage());
    }

    @Test
    public void testCheckouBranchCheckoutCommit() {
        repo.add("foo.txt");
        repo.commit("first");
        repo.remove("foo.txt");
        repo.commit("second");

        String message = repo.checkoutBranch("non-existent").getMessage();
        assertEquals("branch non-existent does not exist", message);

        message = repo.checkoutCommit("non-existent").getMessage();
        assertEquals("commit non-existent does not exist", message);
    }

    @Test
    public void testAdd_MultipleFiles() {
        Result actual = repo.add("foo.txt", "bar.txt", "baz.txt");
        assertSuccess("added foo.txt, bar.txt, baz.txt to stage", actual);

        actual = repo.add("foo.txt", "bar.txt", "baz.txt");
        assertFail("'foo.txt' already exists", actual);
    }

    @Test
    public void testRemove_DoesNothingWhenAnyFileIsMissing() {
        repo.add("foo.txt", "bar.txt");

        Result actual = repo.remove("foo.txt", "baz.txt");
        assertFail("'baz.txt' did not match any files", actual);

        actual = repo.commit("After removal");
        assertSuccess("2 files changed", actual);
    }

    @Test
    public void testRemove1() {
        repo.add("foo.txt");
        repo.commit("first");

        String message = repo.remove("foo.txt").getMessage();
        assertEquals("added foo.txt for removal", message);

        message = repo.commit("second").getMessage();
        assertEquals("1 files changed", message);

        message = repo.add("foo.txt").getMessage();
        assertEquals("added foo.txt to stage", message);

        message = repo.commit("third").getMessage();
        assertEquals("1 files changed", message);
    }

    @Test
    public void testRemove2() {
        String message;

        repo.add("foo.txt");
        repo.commit("first");
        repo.remove("foo.txt");
        repo.commit("second");
        message = repo.commit("nothingToCommit").getMessage();

        assertEquals("nothing to commit, working tree clean", message);

        repo.checkoutCommit(repo.currBranch.getCommits().get(0).getHash());

        message = repo.remove("foo.txt").getMessage();
        assertEquals("added foo.txt for removal", message);
    }


    @Test
    public void testCheckoutBranch_CanSwitchBranches() {
        repo.add("src/Main.java");
        repo.commit("Add Main.java");

        repo.createBranch("dev");
        Result actual = repo.checkoutBranch("dev");
        assertSuccess("switched to branch dev", actual);

        repo.remove("src/Main.java");
        repo.commit("Remove Main.java");
        assertEquals("Remove Main.java", repo.getHead().getMessage());

        actual = repo.checkoutBranch("master");
        assertSuccess("switched to branch master", actual);
        assertEquals("Add Main.java", repo.getHead().getMessage());
    }

    @Test
    public void testHashGenerator() {
        String date = "Thu Oct 25 11:13 2018";
        String message = "Second commit";
        String result;
        try {
            result = HashGenerator.hexDigest(date + message);
        } catch (NoSuchAlgorithmException e) {
            result = "error";
        }

        assertEquals("b767221f31e87f225c7bd5175af9f7b91cbddc7f", result);
    }

    @Test
    public void testCreateBranchGetBranch() {
        assertEquals("master", repo.getBranch());

        String message = repo.createBranch("master").getMessage();
        assertEquals("branch master already exists", message);
    }

    private static void assertFail(String expected, Result actual) {
        assertFalse(actual.isSuccessful());
        assertEquals(expected, actual.getMessage());
    }

    private static void assertSuccess(String expected, Result actual) {
        assertTrue(actual.isSuccessful());
        assertEquals(expected, actual.getMessage());
    }

}