import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatternMatcherTest {
    @Test
    public void testMatch_WithoutSpecialChars11() {
        assertTrue(PatternMatcher.match("abcdef", "a****ef"));
    }

    @Test
    public void testMatch_WithoutSpecialChars12() {
        assertTrue(PatternMatcher.match("abcdef", "a****?ef"));
    }

    @Test
    public void testMatch_WithoutSpecialChars13() {
        assertTrue(PatternMatcher.match("abcdef", "a****?ef"));
    }
    @Test
    public void testMatch_WithoutSpecialChars() {
        assertTrue(PatternMatcher.match("abcdef", "a****?ef"));
    }

    @Test
    public void testMatch_WithoutSpecialChars1() {
        assertTrue(PatternMatcher.match("abcdef", "***********a"));
    }
    @Test
    public void testMatch_WithoutSpecialChars2() {
        assertTrue(PatternMatcher.match("abcdef", "ab?"));
    }
    @Test
    public void testMatch_WithoutSpecialChars3() {
        assertTrue(PatternMatcher.match("abcdef", "ab?*"));
    }

    @Test
    public void testMatch_WithoutSpecialChars4() {
        assertTrue(PatternMatcher.match("abcdef", "ab?******"));
    }

    @Test
    public void testMatch_WithoutSpecialChars5() {
        assertTrue(PatternMatcher.match("abcdef", "**?ef"));
    }

    @Test
    public void testMatch_WithoutSpecialChars6() {
        assertTrue(PatternMatcher.match("abcdef", "?**?ef"));
    }

    @Test
    public void testMatch_WithoutSpecialChars7() {
        assertTrue(PatternMatcher.match("abcdef", "a*?ef"));
    }

    @Test
    public void testMatch_WithoutSpecialChars8() {
        assertTrue(PatternMatcher.match("abcdef", "abc"));
    }

    @Test
    public void testMatch_WithoutSpecialChars9() {
        assertTrue(PatternMatcher.match("fp", "f*"));
    }

    @Test
    public void testMatch_WithoutSpecialChars10() {
        assertTrue(PatternMatcher.match("abcdef", "*f"));
    }


    @Test
    public void testMatch_WithSingleQuestionMark() {
        assertFalse(PatternMatcher.match("abcdef", "?t"));
    }

    @Test
    public void testMatch_WithSingleStarMark() {
        assertFalse(PatternMatcher.match("abcdef", "????de?"));
    }

    @Test
    public void testMatch_AllSpecialChars() {
        assertFalse(PatternMatcher.match("abcdef", "a?cd*g"));
    }
}
