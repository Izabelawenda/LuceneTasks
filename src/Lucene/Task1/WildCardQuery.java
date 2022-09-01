package Lucene.Task1;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class WildCardQuery {

    public static void main(String[] args) throws IOException {

        Analyzer analyzer = new StandardAnalyzer();

        ArrayList<String> arrayOfUserQueries = new ArrayList<>();
        createWildcardQueries(arrayOfUserQueries);
        System.out.println(arrayOfUserQueries);

        // todo: Let us create a dedicated component Indexer
        /*
        public class Indexer {


            // directory where Lucene files are sotred
            private final Directory directory;

            public Indexer(Directory directory) {
                this.directory = directory
            }

            // index documents from file
            // return number of indexed documents
            public int indexDocuments(String pathToFile) {
                ...
                // code similar to what there is in addDocumentsToIndex(iwriter)
                ...
            }
        }

        // than here, we simply can utilize Indexer, here to hide all implementation details from the main class
        // + hold the logic in one place that will give us opportunity to extend the logic
         */
        //Index documents with our string paths
        Path indexPath = Files.createTempDirectory("tempIndex");
        Directory directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        addDocumentsToIndex(iwriter);
        iwriter.close();


        // todo: Let us create a dedicated component Searcher
        /*
        public class Searcher {


            // directory where Lucene files are sotred
            private final Directory directory;

            public Indexer(Directory directory) {
                this.directory = directory
                // initialize index readers
            }

            // Search by phrase with wildCard queries
            // return List of documents (contents)
            public List<String> searchWildCard(String searchPhrase) {
                ...

            }
        }

        // This will hide the implementation details from main class
        // + will give opportunity reuse the same component for other tasks, by simply adding new methods
         */
        // Now search the index:
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        // Parse a wildcard query that searches for "transformed user queries":
        for (String arrayOfUserQuery : arrayOfUserQueries) {
            Query query = new WildcardQuery(new Term("content", arrayOfUserQuery));
            ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;

            // todo: instead of simply looking by eyes, let us add a jUnit test to assert results
            /*
             * Something like
             *
             * ...
             * @Test
             * public void testWildCardQueriesTask1() {
             *
             *   Directory dir = ... // in memory directory
             *   Indexer indexer = // init indexer
             *   indexer.index("docs_for_wildcard_test.json") // any format
             *
             *      Searcher searcher = // init searcher
             *   expectedList = List.of("doc1", "doc2");
             *   actualList = searcher.searchWildCard("query1");
             *   assertEquals(expectedList, actualList)
             *   ...
             * }
             * ...
             */
            System.out.println("Search terms found in :: " + hits.length + " files");
            System.out.println(Arrays.toString(Arrays.stream(hits).toArray()));
        }

        ireader.close();
        directory.close();
        IOUtils.rm(indexPath);
    }

    public static void createWildcardQueries(ArrayList<String> arrayOfUserQueries){

        //taking user input query and transforming it to wild card query adding asterisks between every char.
        /*
        If we want to we can reuse searcher component here in order to provide results right after user input provided.
         */
        Scanner scanner = new Scanner(System.in);
        String userQuery;
        do{
            String newString = "*";
            userQuery = scanner.nextLine();

            for(int i =0;i<userQuery.length();i++){

                newString += userQuery.charAt(i) + "*";
            }
            if(!userQuery.isEmpty()){
                arrayOfUserQueries.add(newString);
            }
        }while(!userQuery.isEmpty());
    }

    public static void addDocumentsToIndex(IndexWriter iwriter) throws IOException {
        Document doc1 = new Document();
        Document doc2 = new Document();
        Document doc3 = new Document();
        doc1.add(new StringField("content", "lucene/queryparser/docs/xml/img/plus.gif", Field.Store.YES));
        doc2.add(new StringField("content", "lucene/queryparser/docs/xml/img/join.gif", Field.Store.YES));
        doc3.add(new StringField("content", "lucene/queryparser/docs/xml/img/minusbottom.gif", Field.Store.YES));
        iwriter.addDocument(doc1);
        iwriter.addDocument(doc2);
        iwriter.addDocument(doc3);
    }
}
