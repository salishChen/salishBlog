package com.salishBlog.business.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客对象 t_blog
 *
 * @author salish
 * @date 2022-07-21
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_blog")
public class TBlogInfo implements Serializable {

private static final long serialVersionUID=1L;


    /** 博客id */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /** 标题 */
    private String title;
    /** 封面 */
    private String cover;
    /** 简介 */
    private String summary;
    /** 标签id */
    private String tagId;
    @TableField(exist = false)
    private String[] tagIds;
    /** 类型 */
    private String blogType;
    /** 内容类型 */
    private String contentType;
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
    /** 是否删除 */
    private Long isDelete;

    public String[] getTagIds(){
        if (tagId==null){
            return null;
        }
        return tagId.split(",");
    }
    public String[] setTagIds(){
        if (tagId==null){
            return null;
        }
        tagId = tagIds.toString();
        return tagIds;
    }

}
