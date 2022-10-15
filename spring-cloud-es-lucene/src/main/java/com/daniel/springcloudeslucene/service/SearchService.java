package com.daniel.springcloudeslucene.service;

import com.daniel.springcloudeslucene.util.LuceneUtil;
import com.daniel.springcloudeslucene.util.Settings;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchService {

    /**
     * 搜索文档
     */
    public void query(String indexName, String queryString) throws IOException {
        LuceneUtil.query(Settings.LUCENE_DATA_DIR + indexName, queryString);
    }

}
