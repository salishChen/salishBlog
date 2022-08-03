package com.salishBlog.business.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.salishBlog.common.core.domain.BaseEntity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 标签分页查询对象 t_tag
 *
 * @author salishChen
 * @date 2022-07-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("标签分页查询对象")
public class TTagQueryBo extends BaseEntity {

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


	/** 标签 */
	@ApiModelProperty("标签")
	private String tag;
	/** 标签色彩 */
	@ApiModelProperty("标签色彩")
	private String tagColor;
	/** 访问次数 */
	@ApiModelProperty("访问次数")
	private Integer times;
	/** 是否删除 */
	@ApiModelProperty("是否排序")
	private Boolean timesAsc;
}
