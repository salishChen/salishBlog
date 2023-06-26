package com.salishBlog.business.mapper;

import com.salishBlog.business.domain.TFillPit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 填坑Mapper接口
 *
 * @author salishChen
 * @date 2023-06-26
 */
public interface TFillPitMapper extends BaseMapper<TFillPit> {

    List<String> getTags();
}
