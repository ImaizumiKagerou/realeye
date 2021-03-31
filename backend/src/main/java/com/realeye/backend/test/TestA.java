package com.realeye.backend.test;

import com.realeye.backend.entity.APIKey;
import com.realeye.backend.service.APIKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
