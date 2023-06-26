package com.salishBlog.business.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.salishBlog.common.core.domain.BaseEntity;


/**
 * 填坑分页查询对象 t_fill_pit
 *
 * @author salishChen
 * @date 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("填坑分页查询对象")
public class TFillPitQueryBo extends BaseEntity {

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


	/** 内容 */
	@ApiModelProperty("内容")
	private String content;
	/** 标签 */
	@ApiModelProperty("标签")
	private String tag;

}
