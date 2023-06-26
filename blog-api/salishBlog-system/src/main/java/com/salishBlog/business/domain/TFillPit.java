package com.salishBlog.business.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 填坑对象 t_fill_pit
 *
 * @author salishChen
 * @date 2023-06-26
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_fill_pit")
public class TFillPit implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /** 内容 */
    private String content;

    /** 标签 */
    private String tag;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 更新人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

}
