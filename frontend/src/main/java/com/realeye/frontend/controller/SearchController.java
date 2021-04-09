package com.realeye.frontend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.realeye.frontend.entity.*;
import com.realeye.frontend.service.APIKeyService;
import com.realeye.frontend.service.DomainTableService;
import com.realeye.frontend.service.IpTableService;
import com.realeye.frontend.service.UrlTableService;
import com.realeye.frontend.utils.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/search")
@Validated
public class SearchController {

    @Resource
    private SolrClient solrClient;

    @Resource
    private APIKeyService apiKeyService;

    @Resource
    private IpTableService ipTableService;

    @Resource
    private UrlTableService urlTableService;

    @Resource
    private DomainTableService domainTableService;

    @GetMapping("/main")
    public ResultBody normalSearch(@NotBlank String keyword, @NotNull Integer pageNum, @NotNull Integer pageSize) throws IOException, SolrServerException {

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("keyword:" + keyword);
//        solrQuery.setSort("update_time", SolrQuery.ORDER.desc);
        //设置查询的条数
        solrQuery.setRows(pageSize);
        //设置查询的开始
        solrQuery.setStart((pageNum - 1) * pageSize);
        //设置高亮
        solrQuery.setHighlight(true);
        //设置高亮的字段
        solrQuery.addHighlightField("content");
        //设置高亮的样式
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        if (keyword.contains(" ")) {
            String[] s = keyword.split(" ");
            //*,sum(termfreq(content,*203*),termfreq(content,*114*),termfreq(content,*83*)) desc,update_time desc
            StringBuilder sb = new StringBuilder();
            sb.append("*,");
            sb.append("sum(");

            for (int i = 0; i < s.length; i++) {
                s[i] = "termfreq(content,*" + s[i] + "*)";
            }
            String join = StringUtils.join(s, ",");

            sb.append(join);
            sb.append(") desc");
            solrQuery.setFields(sb.toString());
        }

        List<SolrResultVO> list = new ArrayList<>();

        QueryResponse response = solrClient.query(solrQuery);
        for (SolrDocument result : response.getResults()) {

            String type = getStampType(result.getFieldValue("stamp").toString());

            SolrResultVO build = SolrResultVO.builder()
                    .idStr(result.getFieldValue("id").toString())
                    .content(result.getFieldValue("content").toString())
                    .stamp(result.getFieldValue("stamp").toString())
                    .source(result.getFieldValue("source").toString())
                    .updateTime(Long.parseLong(result.getFieldValue("update_time").toString()))
                    .type(type)
                    .build();
            list.add(build);
        }

        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        list.forEach(solrResultVO -> {
            Map<String, List<String>> listMap = highlighting.get(solrResultVO.getIdStr());
            if (null != listMap) {
                solrResultVO.setContent(listMap.get("content").get(0));
            }
            String[] s = solrResultVO.getIdStr().split("_");
            solrResultVO.setTitle(s[0]);
        });

        MyPageVO myPageVO = MyPageVO.builder()
                .currPage(pageNum)
                .hasNextPage(response.getResults().getNumFound() - response.getResults().getStart() > pageSize)
                .total(response.getResults().getNumFound())
                .data(list)
                .build();

        return ResultBody.newSuccessInstance(myPageVO);
    }

    @GetMapping("/api")
    public ResultBody APISearch(@NotNull String apikey, @NotNull String keyword, @NotNull Integer pageNum, @NotNull Integer pageSize) throws IOException, SolrServerException {

        QueryWrapper<APIKey> q = new QueryWrapper<>();
        q.eq("apikey", apikey);
        APIKey one = apiKeyService.getOne(q);
        if (one == null) {
            return ResultBody.newErrorInstance(403, "apikey有误");
        }

        return ResultBody.newSuccessInstance(normalSearch(keyword, pageNum, pageSize));
    }

    @GetMapping("/stamp")
    public ResultBody StampAbout(@NotBlank String stamp) {
        switch (stamp) {
            case "attacker":
            case "email spammer":
            case "unknown":
                //ip
                Page<IpTable> ipPage = new Page<>(1, 5);
                QueryWrapper<IpTable> ipq = new QueryWrapper<>();
                ipq.orderByDesc("update_time");
                List<IpTable> ipList = ipTableService.page(ipPage,ipq).getRecords();
                List<Map<String,String>> list = new ArrayList<>(5);
                for (IpTable ipTable : ipList) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("updateTime",ipTable.getUpdateTime());
                    map.put("content",ipTable.getIp());
                    list.add(map);
                }
                return ResultBody.newSuccessInstance(list);
            case "Phish":
                //url
                Page<UrlTable> urlPage = new Page<>(1, 5);
                QueryWrapper<UrlTable> urlq = new QueryWrapper<>();
                urlq.orderByDesc("update_time");
                List<UrlTable> urlList = urlTableService.page(urlPage,urlq).getRecords();
                list = new ArrayList<>(5);
                for (UrlTable urlTable : urlList) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("updateTime",urlTable.getUpdateTime());
                    map.put("content",urlTable.getUrl());
                    list.add(map);
                }
                return ResultBody.newSuccessInstance(list);
            case "BlackList":
            default:
                //domain
                Page<DomainTable> domainPage = new Page<>(1, 5);
                QueryWrapper<DomainTable> domainq = new QueryWrapper<>();
                domainq.orderByDesc("update_time");
                List<DomainTable> domainList = domainTableService.page(domainPage,domainq).getRecords();
                list = new ArrayList<>(5);
                for (DomainTable domainTable : domainList) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("updateTime",domainTable.getUpdateTime());
                    map.put("content",domainTable.getDomain());
                    list.add(map);
                }
                return ResultBody.newSuccessInstance(list);
        }
    }

    private String getStampType(String stamp) {
        switch (stamp) {
            case "Phish":
                return "钓鱼网站";
            case "email spammer":
                return "垃圾邮件";
            case "attacker":
                return "有攻击行为";
            case "BlackList":
                return "有黑名单行为";
            case "unknown":
            default:
                return "存在可疑行为";
        }
    }
}
