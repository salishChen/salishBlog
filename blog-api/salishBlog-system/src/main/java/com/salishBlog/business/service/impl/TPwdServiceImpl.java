package com.salishBlog.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.salishBlog.business.domain.bo.TPwdAddBo;
import com.salishBlog.business.domain.bo.TPwdQueryBo;
import com.salishBlog.business.domain.bo.TPwdEditBo;
import com.salishBlog.business.domain.TPwd;
import com.salishBlog.business.mapper.TPwdMapper;
import com.salishBlog.business.domain.vo.TPwdVo;
import com.salishBlog.business.service.ITPwdService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 秘钥本Service业务层处理
 *
 * @author salishChen
 * @date 2024-05-15
 */
@Service
public class TPwdServiceImpl extends ServiceImpl<TPwdMapper, TPwd> implements ITPwdService {

    @Override
    public TPwdVo queryById(Long id){
        TPwd db = this.baseMapper.selectById(id);
        return BeanUtil.toBean(db, TPwdVo.class);
    }

    @Override
    public List<TPwdVo> queryList(TPwdQueryBo bo) {
        LambdaQueryWrapper<TPwd> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getKeyName()), TPwd::getKeyName, bo.getKeyName());
        return entity2Vo(this.list(lqw));
    }

    /**
    * 实体类转化成视图对象
    *
    * @param collection 实体类集合
    * @return
    */
    private List<TPwdVo> entity2Vo(Collection<TPwd> collection) {
        List<TPwdVo> voList = collection.stream()
                .map(any -> BeanUtil.toBean(any, TPwdVo.class))
                .collect(Collectors.toList());
        if (collection instanceof Page) {
            Page<TPwd> page = (Page<TPwd>)collection;
            Page<TPwdVo> pageVo = new Page<>();
            BeanUtil.copyProperties(page,pageVo);
            pageVo.addAll(voList);
            voList = pageVo;
        }
        return voList;
    }

    @Override
    public Boolean insertByAddBo(TPwdAddBo bo) {
        TPwd add = BeanUtil.toBean(bo, TPwd.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public Boolean updateByEditBo(TPwdEditBo bo) {
        TPwd update = BeanUtil.toBean(bo, TPwd.class);
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(TPwd entity){
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(ids);
    }
}
