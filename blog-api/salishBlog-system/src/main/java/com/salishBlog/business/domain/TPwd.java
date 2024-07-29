package com.salishBlog.business.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 秘钥本对象 t_pwd
 *
 * @author salishChen
 * @date 2024-05-15
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_pwd")
public class TPwd implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 秘钥名 */
    private String keyName;

    /** 秘钥 */
    private String keyValue;

    /** 其他秘钥 */
    private String otherKeys;

}
