package com.realeye.frontend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolrResultVO {

    private String idStr;

    private String content;

    private String source;

    private String stamp;

    private Long updateTime;

}
