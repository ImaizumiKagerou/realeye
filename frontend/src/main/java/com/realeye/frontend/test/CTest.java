package com.realeye.frontend.test;

import com.realeye.frontend.controller.SearchController;
import com.realeye.frontend.utils.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
@Slf4j
public class CTest {

    @Resource
    private SolrClient solrClient;

    @Resource
    private SearchController searchController;

    @Test
    public void aaa() throws IOException, SolrServerException {

        ResultBody resultBody = searchController.normalSearch("123", 1, 10);
        System.out.println(resultBody);
    }
}
