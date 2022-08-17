package Lucene.Task2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ProximitySearch {

    public static void main(String[] args) throws IOException, ParseException {

        Analyzer analyzer = new StandardAnalyzer();

        List<KeyValuePairs> listOfDocs = new ArrayList<>();
        List<KeyValuePairs> listOfQueries = new ArrayList<>();
        List<String> listOfProximityQueries = new ArrayList<>();

        createKeyValuePairs(listOfDocs, listOfQueries);
        createProximitySearchQueries(listOfQueries, listOfProximityQueries);

        Path indexPath = Files.createTempDirectory("tempIndex");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);


        for (KeyValuePairs listOfDoc : listOfDocs) {
            Document doc = new Document();
            String text = listOfDoc.getKey();
            //System.out.println(text);
            String title = listOfDoc.getValue();
            doc.add(new Field("content", text, TextField.TYPE_STORED));
            doc.add(new Field("title", title, TextField.TYPE_STORED));
            iwriter.addDocument(doc);
        }
        iwriter.close();

        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        // Parse a simple query that searches for "something that u want to search":
        QueryParser parser = new QueryParser("content", analyzer);
        for(String proximityQuery : listOfProximityQueries){
            Query query = parser.parse(proximityQuery);
            ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
            System.out.println(proximityQuery + " found in :: " + hits.length + " files");
            System.out.println(Arrays.toString(Arrays.stream(hits).toArray()));
        }

        ireader.close();
        directory.close();
        IOUtils.rm(indexPath);
    }

    //BONUS QUESTIONS
    //Question 1
    /*How will you modify the query used in the exercise to support prefix search for the last word in the input strings
    so that the file with content "make a long story short" would be matched with "long story sho" (edit distance should
    be still checked as well)?*/
    //I would change it to WildCardQuery Search. And Search for "*long story sho*". If we look for a certain sentence with
    //last word 'changable' we don't have to check edit distance, so text can be saved in a StringField without position of words.
    //Question 2
    /*How will you modify the query used in the exercise to match files by input strings within a given edit distance and
    the same order of words (i.e. word permutations are not allowed)?*/
    //
    //

    public static void createProximitySearchQueries(List<KeyValuePairs> listOfQueries, List<String> listOfProximityQueries){

        for (KeyValuePairs listOfQuery : listOfQueries) {
            String proximityQuery = "\"";
            proximityQuery += listOfQuery.getValue() + "\"" + "~" + listOfQuery.getKey();
            listOfProximityQueries.add(proximityQuery);
        }
    }

    public static void createKeyValuePairs(List<KeyValuePairs> listOfDocs, List<KeyValuePairs> listOfQueries){

        KeyValuePairs file1 = new KeyValuePairs("file1", "to be or not to be that is the question");
        KeyValuePairs file2 = new KeyValuePairs("file2", "make a long story short");
        KeyValuePairs file3 = new KeyValuePairs("file3", "see eye to eye");

        listOfDocs.add(file1);
        listOfDocs.add(file2);
        listOfDocs.add(file3);

        KeyValuePairs object1 = new KeyValuePairs("to be not", "1");
        KeyValuePairs object2 = new KeyValuePairs("to or to", "1");
        KeyValuePairs object3 = new KeyValuePairs("to", "1");
        KeyValuePairs object4 = new KeyValuePairs("long story short", "0");
        KeyValuePairs object5 = new KeyValuePairs("long short", "0");
        KeyValuePairs object6 = new KeyValuePairs("long short", "1");
        KeyValuePairs object7 = new KeyValuePairs("story long", "1");
        KeyValuePairs object8 = new KeyValuePairs("story long", "2");

        listOfQueries.add(object1);
        listOfQueries.add(object2);
        listOfQueries.add(object3);
        listOfQueries.add(object4);
        listOfQueries.add(object5);
        listOfQueries.add(object6);
        listOfQueries.add(object7);
        listOfQueries.add(object8);
    }
}
