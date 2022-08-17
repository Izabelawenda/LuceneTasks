package Lucene.Task3;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

public class ConcatenationTokenFilter extends TokenFilter{

    private final String delimiter;
    private final StringBuffer concatenatedTokensBuffer;
    private final CharTermAttribute charTermAttribute;

    protected ConcatenationTokenFilter(TokenStream input) {
        super(input);
        this.delimiter = " ";
        this.concatenatedTokensBuffer = new StringBuffer();
        this.charTermAttribute = addAttribute(CharTermAttribute.class);
    }

    protected ConcatenationTokenFilter(TokenStream input, String diff_delimiter) {
        super(input);
        this.delimiter = diff_delimiter;
        this.concatenatedTokensBuffer = new StringBuffer();
        this.charTermAttribute = addAttribute(CharTermAttribute.class);
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if(!input.incrementToken()){
            return false;
        }
        do{
            char[] buffer = charTermAttribute.buffer();

            // new buffer is declared in order to omit null values in the original buffer (it's buffer's free space)
            int length = charTermAttribute.length();
            char[] newBuffer = new char[length];
            System.arraycopy(buffer, 0, newBuffer, 0, length);

            // add new tokens to the concatenation StringBuffer
            concatenatedTokensBuffer.append(String.valueOf(newBuffer)).append(delimiter);
            charTermAttribute.setEmpty();
        }while(input.incrementToken());

        // remove redundant delimiter at the end of the concatenation
        concatenatedTokensBuffer.delete(concatenatedTokensBuffer.length() - delimiter.length(), concatenatedTokensBuffer.length());

        int start = 0;
        int end = concatenatedTokensBuffer.length();

        char[] arr = new char[end - start];
        concatenatedTokensBuffer.getChars(start, end, arr, 0);

        charTermAttribute.setEmpty();

        // following line is responsible for returning token outside the filter
        charTermAttribute.copyBuffer(arr, 0, concatenatedTokensBuffer.length());

        return true;
    }
}
