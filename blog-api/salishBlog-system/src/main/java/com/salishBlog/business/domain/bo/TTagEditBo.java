package com.salishBlog.business.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 标签编辑对象 t_tag
 *
 * @author salishChen
 * @date 2022-07-21
 */
@Data
@ApiModel("标签编辑对象")
public class TTagEditBo {


    /** 标签id */
    @ApiModelProperty("标签id")
    private Long id;

    /** 标签 */
    @ApiModelProperty("标签")
    private String tag;

    /** 标签色彩 */
    @ApiModelProperty("标签色彩")
    private String tagColor;

    /** 访问次数 */
    @ApiModelProperty("访问次数")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date times;
}
