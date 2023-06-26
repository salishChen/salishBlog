package com.salishBlog.business.domain.vo;

import com.salishBlog.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 填坑视图对象 mall_package
 *
 * @author salishChen
 * @date 2023-06-26
 */
@Data
@ApiModel("填坑视图对象")
public class TFillPitVo {
	private static final long serialVersionUID = 1L;

	/** id */
	@ApiModelProperty("id")
	private Long id;

	/** 内容 */
	@Excel(name = "内容")
	@ApiModelProperty("内容")
	private String content;
	/** 标签 */
	@Excel(name = "标签")
	@ApiModelProperty("标签")
	private String tag;

}
