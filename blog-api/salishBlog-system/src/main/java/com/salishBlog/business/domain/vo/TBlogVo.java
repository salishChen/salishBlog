package com.salishBlog.business.domain.vo;

import com.salishBlog.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 博客视图对象 mall_package
 *
 * @author salish
 * @date 2022-07-21
 */
@Data
@ApiModel("博客视图对象")
public class TBlogVo {
	private static final long serialVersionUID = 1L;

	/** 博客id */
	@ApiModelProperty("博客id")
	private Long id;

	/** 标题 */
	@Excel(name = "标题")
	@ApiModelProperty("标题")
	private String title;
	/** 封面 */
	@Excel(name = "封面")
	@ApiModelProperty("封面")
	private String cover;
	/** 简介 */
	@Excel(name = "简介")
	@ApiModelProperty("简介")
	private String summary;
	/** 内容 */
	@Excel(name = "内容")
	@ApiModelProperty("内容")
	private String content;
	/** 标签id */
	@Excel(name = "标签id")
	@ApiModelProperty("标签id")
	private String tagId;
	/** 类型 */
	@Excel(name = "类型")
	@ApiModelProperty("类型")
	private String blogType;
	/** 内容类型 */
	@Excel(name = "内容类型")
	@ApiModelProperty("内容类型")
	private String contentType;
	/** 是否删除 */
	@Excel(name = "是否删除")
	@ApiModelProperty("是否删除")
	private Long isDelete;

}
