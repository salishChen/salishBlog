package com.salishBlog.business.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;


/**
 * 博客编辑对象 t_blog
 *
 * @author salish
 * @date 2022-07-21
 */
@Data
@ApiModel("博客编辑对象")
public class TBlogEditBo {


    /** 博客id */
    @ApiModelProperty("博客id")
    private Long id;

    /** 标题 */
    @ApiModelProperty("标题")
    private String title;

    /** 封面 */
    @ApiModelProperty("封面")
    private String cover;

    /** 简介 */
    @ApiModelProperty("简介")
    private String summary;

    /** 内容 */
    @ApiModelProperty("内容")
    private String content;

    /** 标签id */
    @ApiModelProperty("标签id")
    private String tagId;

    /** 类型 */
    @ApiModelProperty("类型")
    private String blogType;

    /** 内容类型 */
    @ApiModelProperty("内容类型")
    private String contentType;

    /** 更新时间 */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 更新人 */
    @ApiModelProperty("更新人")
    private String updateBy;

    /** 是否删除 */
    @ApiModelProperty("是否删除")
    private Long isDelete;
}
