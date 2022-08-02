package com.salishBlog.business.domain.vo;

import com.salishBlog.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 标签视图对象 mall_package
 *
 * @author salishChen
 * @date 2022-07-21
 */
@Data
@ApiModel("标签视图对象")
public class TTagVo {
	private static final long serialVersionUID = 1L;

	/** 标签id */
	@ApiModelProperty("标签id")
	private Long id;

	/** 标签 */
	@Excel(name = "标签")
	@ApiModelProperty("标签")
	private String tag;
	/** 标签色彩 */
	@Excel(name = "标签色彩")
	@ApiModelProperty("标签色彩")
	private String tagColor;
	/** 访问次数 */
	@Excel(name = "访问次数" )
	@ApiModelProperty("访问次数")
	private Integer times;

}
