package com.salishBlog.business.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;


import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 标签添加对象 t_tag
 *
 * @author salishChen
 * @date 2022-07-21
 */
@Data
@ApiModel("标签添加对象")
public class TTagAddBo {

    /** 标签 */
    @ApiModelProperty("标签")
    private String tag;
    /** 标签色彩 */
    @ApiModelProperty("标签色彩")
    private String tagColor;
    /** 访问次数 */
    @ApiModelProperty("访问次数")
    private Integer times;
}
