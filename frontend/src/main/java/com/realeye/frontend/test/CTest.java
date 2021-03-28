package com.realeye.frontend.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class CTest {

    @Resource
    private SolrClient solrClient;

    @Test
    public void aaa() {


    }
}
