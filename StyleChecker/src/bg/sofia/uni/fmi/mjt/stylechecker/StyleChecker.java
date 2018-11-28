package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.*;
import java.util.Properties;

/**
 * Used for static code checks of Java files.
 *
 * Depending on a stream from user-defined configuration or default values, it
 * checks if the following rules are applied:
 * <ul>
 * <li>there is only one statement per line;</li>
 * <li>the line lengths do not exceed 100 (or user-defined number of) characters;</li>
 * <li>the import statements do not use wildcards;</li>
 * <li>each opening block bracket is on the same line as the declaration.</li>
 * </ul>
 */
public class StyleChecker {
    private Properties properties;

    public static final String WILD_CARDS_ARE_NOT_ALLOWED =
            "// FIXME Wildcards are not allowed in import statements";
    public static final String ONE_STATEMENT_PER_LINE =
            "// FIXME Only one statement per line is allowed";
    public static final String OPENING_BRACKETS_SHOULD_BE_PLACED_ON_THE_SAME_LINE =
            "// FIXME Opening brackets should be placed on the same line as the declaration";
    public static final int IMPORT_LENGTH = 6;

    private String length;
    public String lengthOfLineShouldNotExceed;


    /**
     * Creates a bg.sofia.uni.fmi.mjt.stylechecker.StyleChecker with properties having the following default values:
     * <ul>
     * <li>{@code wildcard.import.check.active=true}</li>
     * <li>{@code statements.per.line.check.active=true}</li>
     * <li>{@code opening.bracket.check.active=true }</li>
     * <li>{@code length.of.line.check.active=true}</li>
     * <li>{@code line.length.limit=100}</li>
     * </ul>
     **/
    public StyleChecker() {
        properties = new Properties();
        setDefaultProperties();
        setLengthOfLineComment();
    }

    /**
     * Creates a bg.sofia.uni.fmi.mjt.stylechecker.StyleChecker with custom configuration, based on the content from
     * the given {@code inputStream}. If the stream does not contain any of the
     * properties, the missing ones get their default values.
     *
     * @param inputStream
     */
    public StyleChecker(InputStream inputStream) {
        properties = new Properties();
        setDefaultProperties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            setDefaultProperties();
        }

        if (properties.getProperty("length.of.line.check.active").equals("false")) {
            properties.setProperty("line.length.limit", "100");
        }

        setLengthOfLineComment();
    }

    /**
     * For each line from the given {@code source} InputStream writes fixme comment
     * for the violated rules (if any) with an explanation of the style error
     * followed by the line itself in the {@code output}.
     *
     * @param source
     * @param output
     */
    public void checkStyle(InputStream source, OutputStream output) {

        StringBuilder temp = new StringBuilder();
        StringBuilder result = new StringBuilder();
        String line;
        try {
            int data = source.read();
            while (data != -1) {

                while (data != '\n' && data != -1) {
                    temp.append((char)data);
                    data = source.read();
                }

                if (data == '\n') {
                    data = source.read();
                }

                line = checkLine(temp.toString());
                temp.setLength(0);

                result.append(line);
                result.append("\n");
            }
            output.write(result.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String checkLine(String line) {
        String temp = line.replaceAll("\\s+", "");

        String result = line;
        if (properties.getProperty("wildcard.import.check.active").equals("true")) {
            for (int i = temp.length() - 1; i >= 0; i--) {
                if (temp.charAt(i) == '*') {
                    result = addComment(result, WILD_CARDS_ARE_NOT_ALLOWED);
                    break;
                } else if (temp.charAt(i) != ';') {
                    break;
                }
            }
        }
        if (properties.getProperty("statements.per.line.check.active").equals("true")) {
            int count = line.length() - line.replace(";", "").length();

            if (count > 1) {
                for (int i = temp.length() - 1; i >= 0 ; i--) {
                    if (temp.charAt(i) == ';') {
                        count--;
                    } else {
                        break;
                    }
                }
                if (count > 0) {
                    result = addComment(result, ONE_STATEMENT_PER_LINE);
                }
            }
        }
        if (properties.getProperty("opening.bracket.check.active").equals("true")) {
            if (temp.length() > 0 && temp.charAt(0) == '{') {
                result = addComment(result, OPENING_BRACKETS_SHOULD_BE_PLACED_ON_THE_SAME_LINE);
            }
        }
        if (properties.getProperty("length.of.line.check.active").equals("true")) {
            if (temp.length() > IMPORT_LENGTH) {
                String subStr = temp.substring(0, IMPORT_LENGTH);
                if (line.trim().length() > Integer.parseInt(length) && (!subStr.equals("import"))) {
                    result = addComment(result, lengthOfLineShouldNotExceed);
                }
            }
        }

        return result;
    }

    private String addComment(String soruce, String comment) {
        return String.format("%s\n%s", comment, soruce);
    }

    private void setDefaultProperties() {
        properties.setProperty("wildcard.import.check.active", "true");
        properties.setProperty("statements.per.line.check.active", "true");
        properties.setProperty("opening.bracket.check.active", "true");
        properties.setProperty("length.of.line.check.active", "true");
        properties.setProperty("line.length.limit", "100");
    }

    private void setLengthOfLineComment() {
        length = properties.getProperty("line.length.limit");
        lengthOfLineShouldNotExceed = String.format("// FIXME Length of line should not exceed %s characters", length);
    }
}
