package com.salishBlog.business.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.salishBlog.common.core.domain.BaseEntity;


/**
 * 博客分页查询对象 t_blog
 *
 * @author salish
 * @date 2022-07-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("博客分页查询对象")
public class TBlogQueryBo extends BaseEntity {

	/** 分页大小 */
	@ApiModelProperty("分页大小")
	private Integer pageSize;
	/** 当前页数 */
	@ApiModelProperty("当前页数")
	private Integer pageNum;
	/** 排序列 */
	@ApiModelProperty("排序列")
	private String orderByColumn;
	/** 排序的方向desc或者asc */
	@ApiModelProperty(value = "排序的方向", example = "asc,desc")
	private String isAsc;


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
	/** 是否删除 */
	@ApiModelProperty("是否删除")
	private Long isDelete;
	private String firstDay;
	private String lastDay;

}
