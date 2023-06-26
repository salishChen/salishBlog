package com.salishBlog.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.salishBlog.business.domain.bo.TFillPitAddBo;
import com.salishBlog.business.domain.bo.TFillPitQueryBo;
import com.salishBlog.business.domain.bo.TFillPitEditBo;
import com.salishBlog.business.domain.TFillPit;
import com.salishBlog.business.mapper.TFillPitMapper;
import com.salishBlog.business.domain.vo.TFillPitVo;
import com.salishBlog.business.service.ITFillPitService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 填坑Service业务层处理
 *
 * @author salishChen
 * @date 2023-06-26
 */
@Service
public class TFillPitServiceImpl extends ServiceImpl<TFillPitMapper, TFillPit> implements ITFillPitService {

    @Override
    public TFillPitVo queryById(Long id){
        TFillPit db = this.baseMapper.selectById(id);
        return BeanUtil.toBean(db, TFillPitVo.class);
    }

    @Override
    public List<TFillPitVo> queryList(TFillPitQueryBo bo) {
        LambdaQueryWrapper<TFillPit> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getContent()), TFillPit::getContent, bo.getContent());
        lqw.eq(StrUtil.isNotBlank(bo.getTag()), TFillPit::getTag, bo.getTag());
        lqw.orderByDesc(TFillPit::getId);
        return entity2Vo(this.list(lqw));
    }

    /**
    * 实体类转化成视图对象
    *
    * @param collection 实体类集合
    * @return
    */
    private List<TFillPitVo> entity2Vo(Collection<TFillPit> collection) {
        List<TFillPitVo> voList = collection.stream()
                .map(any -> BeanUtil.toBean(any, TFillPitVo.class))
                .collect(Collectors.toList());
        if (collection instanceof Page) {
            Page<TFillPit> page = (Page<TFillPit>)collection;
            Page<TFillPitVo> pageVo = new Page<>();
            BeanUtil.copyProperties(page,pageVo);
            pageVo.addAll(voList);
            voList = pageVo;
        }
        return voList;
    }

    @Override
    public Boolean insertByAddBo(TFillPitAddBo bo) {
        TFillPit add = BeanUtil.toBean(bo, TFillPit.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public Boolean updateByEditBo(TFillPitEditBo bo) {
        TFillPit update = BeanUtil.toBean(bo, TFillPit.class);
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(TFillPit entity){
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return this.removeByIds(ids);
    }

    @Override
    public List<String> getTags(){
        return this.baseMapper.getTags();
    }
}
