package com.salishBlog.business.mapper;

import com.salishBlog.business.domain.TTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 标签Mapper接口
 *
 * @author salishChen
 * @date 2022-07-21
 */
public interface TTagMapper extends BaseMapper<TTag> {
    boolean increaseTimes(Long id);
    boolean decreaseTimes(Long id);
}
