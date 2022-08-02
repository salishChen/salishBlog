package com.salishBlog.business.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 标签对象 t_tag
 *
 * @author salishChen
 * @date 2022-07-21
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_tag")
public class TTag implements Serializable {

private static final long serialVersionUID=1L;


    /** 标签id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标签 */
    private String tag;

    /** 标签色彩 */
    private String tagColor;

    /** 访问次数 */
    private Integer times;

}
