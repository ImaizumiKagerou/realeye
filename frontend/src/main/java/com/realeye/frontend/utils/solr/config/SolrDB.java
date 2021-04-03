package com.realeye.frontend.utils.solr.config;

import com.realeye.frontend.entity.DomainTable;
import com.realeye.frontend.entity.IpTable;
import com.realeye.frontend.entity.UrlTable;
import com.realeye.frontend.service.DomainTableService;
import com.realeye.frontend.service.IpTableService;
import com.realeye.frontend.service.UrlTableService;
import com.realeye.frontend.service.SurveillanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class SolrDB {

    @Resource
    private DomainTableService domainTableService;

    @Resource
    private IpTableService ipTableService;

    @Resource
    private UrlTableService urlTableService;

    @Resource
    private SurveillanceService surveillanceService;

    @Resource
    private SolrClient solrClient;

    //    @PostConstruct
    public void createDB() throws IOException, SolrServerException {
        log.info("开始重建数据库");
        solrClient.deleteByQuery("*:*");

        List<DomainTable> list = domainTableService.list();
        list.forEach(DomainTable::mapSolr);

        List<IpTable> list1 = ipTableService.list();
        list1.forEach(IpTable::mapSolr);

        List<UrlTable> list2 = urlTableService.list();
        list2.removeIf(urlTable -> StringUtils.isBlank(urlTable.getUrl()));
        list2.forEach(UrlTable::mapSolr);

        solrClient.addBeans(list);
        solrClient.addBeans(list1);
        solrClient.addBeans(list2);
        solrClient.commit();
        log.info("重建完成");

    }

}
