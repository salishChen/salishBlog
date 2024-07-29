package com.salishBlog.business.domain.vo;

import com.salishBlog.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 秘钥本视图对象 mall_package
 *
 * @author salishChen
 * @date 2024-05-15
 */
@Data
@ApiModel("秘钥本视图对象")
public class TPwdVo {
	private static final long serialVersionUID = 1L;

	/** id */
	@ApiModelProperty("id")
	private Long id;

	/** 秘钥名 */
	@Excel(name = "秘钥名")
	@ApiModelProperty("秘钥名")
	private String keyName;
	/** 秘钥 */
	@Excel(name = "秘钥")
	@ApiModelProperty("秘钥")
	private String keyValue;
	/** 其他秘钥 */
	@Excel(name = "其他秘钥")
	@ApiModelProperty("其他秘钥")
	private String otherKeys;

}
