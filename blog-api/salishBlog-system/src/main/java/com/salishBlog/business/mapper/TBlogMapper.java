package com.salishBlog.business.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.salishBlog.business.domain.TBlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.salishBlog.business.domain.bo.TBlogQueryBo;

import java.util.List;

/**
 * 博客Mapper接口
 *
 * @author salish
 * @date 2022-07-21
 */
public interface TBlogMapper extends MPJBaseMapper<TBlog> {

    List<TBlog> selectBlogByTag(TBlog tBlog);

    List<TBlog> interfile(TBlog tBlog);

    List<TBlog> queryInfo(TBlog tBlog);

    List<TBlog> dayStatistic(TBlogQueryBo tBlog);
}
