package com.daniel.springcloudeslucene.service;

import com.daniel.springcloudeslucene.util.LuceneUtil;
import com.daniel.springcloudeslucene.util.Settings;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DocumentService {

    /**
     * 索引文档
     */
    public void indexDoc(String indexName, String strDocument) throws IOException {
        IndexWriter indexWriter = LuceneUtil.createIndex(Settings.LUCENE_DATA_DIR + indexName);

        Document document = LuceneUtil.json2Document(strDocument);
        indexWriter.addDocument(document);

        LuceneUtil.closeIndex(indexWriter);
    }
}
