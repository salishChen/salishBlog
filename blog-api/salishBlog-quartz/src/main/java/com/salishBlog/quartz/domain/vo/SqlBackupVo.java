package com.salishBlog.quartz.domain.vo;

import com.salishBlog.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 数据库备份视图对象 mall_package
 *
 * @author salishChen
 * @date 2024-04-23
 */
@Data
@ApiModel("数据库备份视图对象")
public class SqlBackupVo {
	private static final long serialVersionUID = 1L;

	/** id */
	@ApiModelProperty("id")
	private Long id;

	/** 名称 */
	@Excel(name = "名称")
	@ApiModelProperty("名称")
	private String sqlName;
	/** 数据库名称 */
	@Excel(name = "数据库名称")
	@ApiModelProperty("数据库名称")
	private String dbName;
	/** 数据库ip */
	@Excel(name = "数据库ip")
	@ApiModelProperty("数据库ip")
	private String sqlIp;
	/** 用户名 */
	@Excel(name = "用户名")
	@ApiModelProperty("用户名")
	private String sqlUsername;
	/** 密码 */
	@Excel(name = "密码")
	@ApiModelProperty("密码")
	private String sqlPassword;
	/** 定时规则 */
	@Excel(name = "定时规则")
	@ApiModelProperty("定时规则")
	private String sqlCron;
	/** 备份数量限制 */
	@Excel(name = "备份数量限制")
	@ApiModelProperty("备份数量限制")
	private Long fileLimit;
	/** 数据库类型 */
	@Excel(name = "数据库类型")
	@ApiModelProperty("数据库类型")
	private String type;
	/** 备份状态 0=正常,1=暂停 */
	@Excel(name = "备份状态")
	@ApiModelProperty("备份状态")
	private String status;

}
