package com.realeye.frontend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.solr.client.solrj.beans.Field;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "url_table")
public class UrlTable {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "url_index")
    private String urlIndex;

    @TableField(value = "stamp")
    @Field("stamp")
    private String stamp;

    @TableField(value = "update_time")
    private String updateTime;

    @TableField(value = "`source`")
    private String source;

    @TableField(value = "url")
    @Field("content")
    private String url;

    @Field("id")
    @TableField(exist = false)
    private String idStr;

    @Field("update_time")
    @TableField(exist = false)
    private Long updateTimeLong;

    @Field("source")
    @TableField(exist = false)
    private Integer sourceId;

    private Long parseUpdateTime() {
        //Sun Mar 28 00:42:53 2021
        String pattern = "E MMM dd HH:mm:ss yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        try {
            return sdf.parse(updateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public void mapSolr() {
        idStr = "url_" + id;
        updateTimeLong = parseUpdateTime();
        sourceId = Integer.parseInt(source);
    }
}
