package Lucene.Task3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class CustomAnalyzer extends Analyzer {

    private final String concatenationDelimiter;

    //list of stop words
    final List<String> stopWords = Arrays.asList(
            "about", "after", "all", "also", "a", "an", "and", "another", "any", "are", "as", "at", "be", "$", "because", "been", "before", "being", "between", "but", "by", "came", "can", "come", "could", "did", "do", "does", "each", "else",
            "for", "from", "get", "got", "has", "had", "he", "have", "her", "here", "him", "himself", "his", "how", "if", "in", "into", "is", "it", "its", "just", "like", "make", "many", "me", "might", "more", "most", "much", "must", "my",
            "never", "no", "not", "now", "of", "on", "only", "or", "other", "our", "out", "over", "re", "said", "same", "see", "should", "since", "so", "some", "still", "such", "take", "than", "that", "the", "their", "them", "then", "there",
            "these", "they", "this", "those", "through", "to", "too", "under", "up", "use", "very", "want", "was", "way", "we", "well", "were", "what", "when", "where", "which", "while", "who", "will", "with", "would", "you", "your"
    );

    //default "space" delimiter
    public CustomAnalyzer() {
        this.concatenationDelimiter = " ";
    }

    //custom delimiter
    public CustomAnalyzer(String concatenationDelimiter) {
        this.concatenationDelimiter = concatenationDelimiter;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new WhitespaceTokenizer();
        TokenStream tokenStream = new StopFilter(tokenizer, StopFilter.makeStopSet(stopWords));
        //create concatenationTokenFilter with custom delimiter
        tokenStream = new ConcatenationTokenFilter(tokenStream, concatenationDelimiter);
        return new TokenStreamComponents(tokenizer, tokenStream);
    }

    public static void main(String[] args) throws IOException {
        //text to tokenize
        final String text = "Create a new LowerCaseFilter, that normalizes token text to lower case.";
        //analyzer with custom delimiter
        CustomAnalyzer analyzer = new CustomAnalyzer("_");
        TokenStream tokenStream = analyzer.tokenStream("fieldName", new StringReader(text));

        //getting CharTermAttribute from our token stream
        CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);

        try {
            tokenStream.reset();
            //print all tokens from the stream
            while (tokenStream.incrementToken()) {
                System.out.println(termAtt.toString());
            }

            tokenStream.end();
        } finally {
            tokenStream.close();
        }
    }
}
