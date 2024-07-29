package com.salishBlog.business.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;



/**
 * 秘钥本添加对象 t_pwd
 *
 * @author salishChen
 * @date 2024-05-15
 */
@Data
@ApiModel("秘钥本添加对象")
public class TPwdAddBo {

    /** 秘钥名 */
    @ApiModelProperty("秘钥名")
    private String keyName;
    /** 秘钥 */
    @ApiModelProperty("秘钥")
    private String keyValue;
    /** 其他秘钥 */
    @ApiModelProperty("其他秘钥")
    private String otherKeys;
}
