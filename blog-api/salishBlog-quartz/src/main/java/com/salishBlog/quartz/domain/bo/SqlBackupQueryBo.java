package com.salishBlog.quartz.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.salishBlog.common.core.domain.BaseEntity;


/**
 * 数据库备份分页查询对象 sql_backup
 *
 * @author salishChen
 * @date 2024-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("数据库备份分页查询对象")
public class SqlBackupQueryBo extends BaseEntity {

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



}
