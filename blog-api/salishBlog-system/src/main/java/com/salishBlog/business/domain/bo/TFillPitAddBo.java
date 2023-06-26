package com.salishBlog.business.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;



/**
 * 填坑添加对象 t_fill_pit
 *
 * @author salishChen
 * @date 2023-06-26
 */
@Data
@ApiModel("填坑添加对象")
public class TFillPitAddBo {

    /** 内容 */
    @ApiModelProperty("内容")
    private String content;
    /** 标签 */
    @ApiModelProperty("标签")
    private String tag;
    /** 创建时间 */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 创建人 */
    @ApiModelProperty("创建人")
    private String createBy;
    /** 更新时间 */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /** 更新人 */
    @ApiModelProperty("更新人")
    private String updateBy;
}
