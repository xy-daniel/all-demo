package com.daniel.springcloudeslucene.service;

import com.daniel.springcloudeslucene.util.LuceneUtil;
import com.daniel.springcloudeslucene.util.Settings;
import org.apache.lucene.index.IndexWriter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class IndexService {

    public void create(String indexName) throws IOException {
        IndexWriter indexWriter = LuceneUtil.createIndex(Settings.LUCENE_DATA_DIR + indexName);
        LuceneUtil.closeIndex(indexWriter);
    }

    public void delete(String indexName) {
        System.out.println("即将删除索引:" + indexName);
        throw new UnsupportedOperationException();
    }
}
