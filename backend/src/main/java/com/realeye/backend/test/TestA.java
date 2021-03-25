package com.realeye.backend.test;

import com.realeye.backend.entity.APIKey;
import com.realeye.backend.service.APIKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class TestA {

    @Resource
    private APIKeyService apiKeyService;

    @Test
    public void test1() {
        APIKey one = apiKeyService.getOne(null);
        System.out.println("one = " + one);
    }
}
