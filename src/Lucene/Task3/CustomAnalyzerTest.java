package Lucene.Task3;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class CustomAnalyzerTest {

    private static final Logger LOGGER = Logger.getLogger(CustomAnalyzerTest.class.getName());
    private final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    private final PrintStream outputStream = System.out;


    //before, check if output stream is correct
    @Before
    public void checkStream() {
        System.setOut(new PrintStream(byteOutputStream));
    }
    //after, restore stream
    @After
    public void restoreStream() {
        System.setOut(outputStream);
    }

    //method with a default "space" delimiter
    public static void printConcatenatedText(String text) throws IOException {

        printConcatenatedText(text, " ");
    }

    //method with a custom delimiter
    public static void printConcatenatedText(String text, String delimiter) throws IOException {
        //analyzer with chosen delimiter
        CustomAnalyzer analyzer = new CustomAnalyzer(delimiter);
        //getting a token stream from our text
        TokenStream tokenStream = analyzer.tokenStream("fieldName", new StringReader(text));

        //getting CharTermAttribute from our token stream
        CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        try {
            tokenStream.reset();
            //print all tokens from the stream
            while (tokenStream.incrementToken()) {
                System.out.println(termAttribute.toString());
            }

            tokenStream.end();
        } finally {
            tokenStream.close();
        }
    }

    @Test
    public void whenDefaultDelimiterConcatenateWithSpace() throws IOException {
        String text = "this text is an example to concatenate \"connect\" tokens with a delimiter, default or chosen one.";
        printConcatenatedText(text);

        LOGGER.info("Concatenated text: " + byteOutputStream);

        assertEquals("text example concatenate \"connect\" tokens delimiter, default chosen one.\n", byteOutputStream.toString());
    }

    @Test
    public void whenCustomDelimiterConcatenateWithIt() throws IOException {
        String text = "this text is an example to concatenate \"connect\" tokens with a delimiter, default or chosen one.";
        String customDelimiter = "-";
        printConcatenatedText(text, customDelimiter);

        LOGGER.info("Concatenated text: " + byteOutputStream);

        assertEquals("text-example-concatenate-\"connect\"-tokens-delimiter,-default-chosen-one.\n", byteOutputStream.toString());
    }

    /*
    Let us add a test case where we will check our custom analyzer used with Lucene internals

    i.e. Create an Indexer with your custom analyzer set up
         Add a few documents to be indexed that will be analyzed f.e. with value "text is an example"
         In search part, make sure that this document is matched exactly (with TermQuery) for phrase "text example"
     */
}