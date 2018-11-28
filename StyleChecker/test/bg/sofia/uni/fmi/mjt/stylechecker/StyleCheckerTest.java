package bg.sofia.uni.fmi.mjt.stylechecker;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class StyleCheckerTest {
    private StyleChecker checker;
    private StyleChecker tempChecker;
    private String properties;

    @Before
    public void setup() {
        checker = new StyleChecker();
    }

    private void oneLineCodeCheck(StyleChecker checker, String line, String expected) {
        ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        checker.checkStyle(input, output);
        String actual = new String(output.toByteArray());

        assertEquals(expected, actual.trim());
    }

    @Test
    public void testFalseWildcardsAreNotAllowed() {
        properties = "wildcard.import.check.active=false";
        tempChecker = new StyleChecker(new ByteArrayInputStream(properties.getBytes()));

        String line = "import java.util.*;";

        oneLineCodeCheck(tempChecker, line, line);
    }

    @Test
    public void testFalseOpeningBrackets() {
        properties = "opening.bracket.check.active=false";
        tempChecker = new StyleChecker(new ByteArrayInputStream(properties.getBytes()));

        String line = "public static void sayHello()\n{";

        oneLineCodeCheck(tempChecker, line, line);
    }

    @Test
    public void testFalseLengthOfLineShouldNotExceed100Characters() {
        properties = "length.of.line.check.active=false\n" + "line.length.limit=130";
        tempChecker = new StyleChecker(new ByteArrayInputStream(properties.getBytes()));

        String line = "String hello = \"Hey, it's Hannah, Hannah Baker." +
                " That's right. Don't adjust your... whatever device you're " +
                "listening to this on. It's me, live and in stereo.\";";

        oneLineCodeCheck(tempChecker, line, line);
    }

    @Test
    public void testFalseOnlyOneStatementPerLine() {
        properties = "statements.per.line.check.active=false";
        tempChecker = new StyleChecker(new ByteArrayInputStream(properties.getBytes()));

        String line = "sayHello();sayHello();sayHello();sayHello();";

        oneLineCodeCheck(tempChecker, line, line);
    }


    @Test
    public void testWildcardsAreNotAllowed() {

        String line = "import java.util.*;";
        String expected = StyleChecker.WILD_CARDS_ARE_NOT_ALLOWED + "\n" + line;

        oneLineCodeCheck(checker, line, expected);
    }

    @Test
    public void testOpeningBrackets() {

        String line = "public static void sayHello()\n{";
        String expected = "public static void sayHello()\n" +
                "// FIXME Opening brackets should be placed on the same line as the declaration\n" +
                "{";

        oneLineCodeCheck(checker, line, expected);
    }

    @Test
    public void testLengthOfLineShouldNotExceed100Characters() {

        String line = "String hello = \"Hey, it's Hannah, Hannah Baker." +
                " That's right. Don't adjust your... whatever device you're " +
                "listening to this on. It's me, live and in stereo.\";";

        String expected = checker.lengthOfLineShouldNotExceed + "\n" + line;

        oneLineCodeCheck(checker, line, expected);
    }

    @Test
    public void testOnlyOneStatementPerLine() {

        String line = "sayHello();sayHello();sayHello();sayHello();";
        String expected = StyleChecker.ONE_STATEMENT_PER_LINE + "\n" + line;

        oneLineCodeCheck(checker, line, expected);

    }

}