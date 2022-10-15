package com.daniel.springcloudeslucene.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class LuceneUtil {

    public static void main(String[] args) throws IOException {
        String indexDir = "E:\\es\\lucene\\data\\products";
        IndexWriter indexWriter = createIndex(indexDir);
        closeIndex(indexWriter);
        String strDocument = "{'songName':'this be love cloud','singer':'Daniel','lyrics':'Cloud This Be Love,Woke Up This Morning Just Set In My Bed.'}";
        indexDoc(indexDir, strDocument);
        query(indexDir, "cloud");
    }

    /**
     * 创建索引
     */
    public static IndexWriter createIndex(String indexDir) throws IOException {
            //准备目录
            FSDirectory directory = FSDirectory.open(Paths.get(indexDir));
            //准备分词器
            StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
            //准备config
            IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
            //创建索引
            return new IndexWriter(directory, config);
    }

    public static void closeIndex(IndexWriter indexWriter) throws IOException {
        //关闭索引
        indexWriter.close();
    }

    /**
     * 索引文档
     */
    public static void indexDoc(String indexDir, String strDocument) throws IOException {
        IndexWriter indexWriter = createIndex(indexDir);

        Document document = json2Document(strDocument);
        indexWriter.addDocument(document);

        closeIndex(indexWriter);
    }

    public static Document json2Document(String strDocument) {
        Document document = new Document();

        JSONObject jsonDocument = JSONObject.parseObject(strDocument);
        Set<String> keys = jsonDocument.keySet();

        for (String key : keys) {
            document.add(new TextField(key, jsonDocument.getString(key), Field.Store.YES));
        }

        return document;
    }


    /**
     * 搜索文档
     */
    public static void query(String indexDir, String queryString) throws IOException {
        String result = "";

        FSDirectory directory = FSDirectory.open(Paths.get(indexDir));
        DirectoryReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Query query = new QueryBuilder(analyzer).createPhraseQuery("songName", queryString);
        TopDocs hits = searcher.search(query, 10);

        ScoreDoc[] scoreDocs = hits.scoreDocs;
        System.out.println("查询到文档数量:" + scoreDocs.length);
        for (ScoreDoc scoreDoc : scoreDocs) {
            //拿到文档
            Document doc = searcher.doc(scoreDoc.doc);
            List<IndexableField> fields = doc.getFields();

            for (IndexableField field : fields) {
                result += field.name() + ":" + field.stringValue() + "     ";
            }
            System.out.println(result);
        }
        reader.close();

    }
}
