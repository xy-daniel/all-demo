package com.daniel.springcloudeslucene.controller;

import com.daniel.springcloudeslucene.service.DocumentService;
import com.daniel.springcloudeslucene.service.IndexService;
import com.daniel.springcloudeslucene.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class IndexController {

    @Autowired
    IndexService indexService;
    @Autowired
    DocumentService documentService;
    @Autowired
    SearchService searchService;

    @GetMapping("/index/create/{indexName}")
    public String create(@PathVariable("indexName") String indexName, String document) throws IOException {
        documentService.indexDoc(indexName, document);
        return "success";
    }

    @GetMapping("/index/search/{indexName}")
    public String search(@PathVariable("indexName") String indexName, String queryStr) throws IOException {
        searchService.query(indexName, queryStr);
        return "success";
    }



}
