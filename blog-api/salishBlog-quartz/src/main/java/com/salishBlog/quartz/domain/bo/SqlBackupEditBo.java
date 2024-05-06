package com.salishBlog.quartz.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


/**
 * 数据库备份编辑对象 sql_backup
 *
 * @author salishChen
 * @date 2024-04-23
 */
@Data
@ApiModel("数据库备份编辑对象")
public class SqlBackupEditBo {


    /** id */
    @ApiModelProperty("id")
    @NotEmpty(message = "id不能为空")
    private Long id;

    /** 名称 */
    @ApiModelProperty("名称")
    @NotEmpty(message = "名称不能为空")
    private String sqlName;

    /** 数据库名称 */
    @ApiModelProperty("数据库名称")
    @NotEmpty(message = "数据库名称不能为空")
    private String dbName;

    /** 数据库ip */
    @ApiModelProperty("数据库ip")
    @NotEmpty(message = "IP不能为空")
    @Pattern(regexp = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}",message = "请输入正确IP地址")
    private String sqlIp;

    /** 用户名 */
    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空")
    private String sqlUsername;

    /** 密码 */
    @ApiModelProperty("密码")
    @NotEmpty(message = "密码不能为空")
    private String sqlPassword;

    /** 定时规则 */
    @ApiModelProperty("定时规则")
    @NotEmpty(message = "定时规则不能为空")
    private String sqlCron;

    /** 备份数量限制 */
    @ApiModelProperty("备份数量限制")
    private Long fileLimit;

    /** 数据库类型 */
    @ApiModelProperty("数据库类型")
    @NotEmpty(message = "数据库类型不能为空")
    private String type;

    /** 备份状态 0=正常,1=暂停 */
    @ApiModelProperty("备份状态")
    private String status;
}
