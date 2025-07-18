package com.salishBlog.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.salishBlog.business.service.ITTagService;
import com.salishBlog.common.constant.Constants;
import com.salishBlog.common.core.domain.AjaxResult;
import com.salishBlog.common.utils.CosUtil;
import com.salishBlog.common.utils.ImageUtils;
import com.salishBlog.common.utils.StringUtils;
import com.salishBlog.common.utils.file.FileTypeUtils;
import com.salishBlog.common.utils.file.MimeTypeUtils;
import com.salishBlog.system.service.ISysConfigService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.salishBlog.business.domain.bo.TBlogAddBo;
import com.salishBlog.business.domain.bo.TBlogQueryBo;
import com.salishBlog.business.domain.bo.TBlogEditBo;
import com.salishBlog.business.domain.TBlog;
import com.salishBlog.business.mapper.TBlogMapper;
import com.salishBlog.business.domain.vo.TBlogVo;
import com.salishBlog.business.service.ITBlogService;

import java.io.File;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 博客Service业务层处理
 *
 * @author salish
 * @date 2022-07-21
 */
@Service
public class TBlogServiceImpl extends ServiceImpl<TBlogMapper, TBlog> implements ITBlogService {


    private final ITTagService tagService;
    private final ISysConfigService configService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public TBlogServiceImpl(ITTagService tagService, ISysConfigService configService) {
        this.tagService = tagService;
        this.configService = configService;
    }

    @Override
    public TBlogVo queryById(Long id) {
        TBlog db = this.baseMapper.selectById(id);
        return BeanUtil.toBean(db, TBlogVo.class);
    }

    @Override
    public List<TBlogVo> queryList(TBlogQueryBo bo) {
        LambdaQueryWrapper<TBlog> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getTitle()), TBlog::getTitle, bo.getTitle());
        lqw.eq(StrUtil.isNotBlank(bo.getCover()), TBlog::getCover, bo.getCover());
        lqw.eq(StrUtil.isNotBlank(bo.getSummary()), TBlog::getSummary, bo.getSummary());
        lqw.eq(bo.getTagId() != null, TBlog::getTagId, bo.getTagId());
        lqw.eq(StrUtil.isNotBlank(bo.getBlogType()), TBlog::getBlogType, bo.getBlogType());
        lqw.eq(StrUtil.isNotBlank(bo.getContentType()), TBlog::getContentType, bo.getContentType());
        lqw.eq(bo.getIsDelete() != null, TBlog::getIsDelete, bo.getIsDelete());
        return entity2Vo(this.list(lqw));
    }

    @Override
    public List<TBlog> queryInfo(TBlog tBlog) {
        return this.baseMapper.selectBlogByTag(tBlog);
    }

    @Override
    public List<TBlog> selectBlogByTag(TBlog tBlog) {
        return this.baseMapper.selectBlogByTag(tBlog);
    }

    @Override
    public JSONObject blogStatistic() {
        JSONObject json = new JSONObject();

        // 获取当前日期
        LocalDate now = LocalDate.now();

        LocalDate yearFirstTime = now.minusDays(365);
        String yearFirst = yearFirstTime.format(formatter);
        String yearLast = now.format(formatter);
        // 获取当前年月
        YearMonth currentYearMonth = YearMonth.from(now);
        // 当前月的第一天
        LocalDate firstDate = currentYearMonth.atDay(1);
        // 当前月的最后一天
        LocalDate lastDate = currentYearMonth.atEndOfMonth();
        String firstDay = firstDate.toString();
        String lastDay = lastDate.toString();

        LambdaQueryWrapper<TBlog> lqw = Wrappers.lambdaQuery();
        lqw.between(TBlog::getCreateTime, firstDay, lastDay);
        json.put("monthTotal", this.baseMapper.selectCount(lqw));
        json.put("total", this.count());
        TBlogQueryBo tBlogQueryBo = new TBlogQueryBo();
        tBlogQueryBo.setFirstDay(yearFirst + " 00:00:00");
        tBlogQueryBo.setLastDay(yearLast + " 23:59:59");
        List<TBlog> tBlogs = this.baseMapper.dayStatistic(tBlogQueryBo);
        Map<String, String> collect = tBlogs.stream().collect(Collectors.toMap(TBlog::getTime, TBlog::getContent));
        JSONArray array = new JSONArray();
        for (int i=1;i<=365;i++){
            JSONArray object = new JSONArray();
            String key = yearFirstTime.plusDays(i).format(formatter);
            object.add(key);
            object.add(collect.getOrDefault(key, "0"));
            array.add(object);
        }

        json.put("dayCount",array);

        return json;
    }

    @Override
    public JSONObject blogIntroduction() {
        JSONObject json = new JSONObject();

        // 获取当前日期
        LocalDate now = LocalDate.now();

        LocalDate yearFirstTime = now.minusDays(365);
        String yearFirst = yearFirstTime.format(formatter);
        String yearLast = now.format(formatter);

        LambdaQueryWrapper<TBlog> lqw = Wrappers.lambdaQuery();
        lqw.between(TBlog::getCreateTime, yearFirst, yearLast);
        json.put("yearTotal", this.baseMapper.selectCount(lqw));
        json.put("total", this.count());

        LambdaQueryWrapper<TBlog> lqw2 = Wrappers.lambdaQuery();
        lqw2.orderByDesc(TBlog::getCreateTime);
        lqw2.last("limit 3");
        List<TBlog> tBlogs = this.list(lqw2);
        List<String> tagIds = tBlogs.stream().map(item -> Arrays.asList(item.getTagIds())).flatMap(item -> item.stream())
                .collect(Collectors.groupingBy(item -> item, Collectors.counting())).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3).map(item -> item.getKey()).collect(Collectors.toList());
        String collect = tagIds.stream().map(item -> tagService.getById(item).getTag()).collect(Collectors.joining(","));
        json.put("field", collect);

        return json;
    }

    @Override
    public AjaxResult interfile(TBlog tBlog) {
        List<TBlog> blogs = this.baseMapper.interfile(tBlog);
        Map<String, List<TBlog>> collect = blogs.stream().collect(Collectors.groupingBy(TBlog::getTime));
        List<List<TBlog>> collect1 = new ArrayList<>();

        List<String> collect2 = collect.entrySet().stream().map(item -> item.getKey()).sorted().collect(Collectors.toList());
        System.out.println(collect2);
        for (int i = collect2.size() - 1; i >= 0; i--) {
            collect1.add(collect.get(collect2.get(i)));
        }


//        ListIterator<Map.Entry<String, List<TBlog>>> li = new ArrayList<>(collect.entrySet()).listIterator(collect.size());
//
//        while (li.hasPrevious()) {
//            Map.Entry<String, List<TBlog>> entry = li.previous();
//            collect1.add(entry.getValue());
//        }

        JSONObject object1 = new JSONObject();
        object1.put("total", new PageInfo<>(blogs).getTotal());
        object1.put("data", collect1);

        return AjaxResult.success(object1);
    }

    /**
     * 实体类转化成视图对象
     *
     * @param collection 实体类集合
     * @return
     */
    private List<TBlogVo> entity2Vo(Collection<TBlog> collection) {
        List<TBlogVo> voList = collection.stream()
                .map(any -> BeanUtil.toBean(any, TBlogVo.class))
                .collect(Collectors.toList());
        if (collection instanceof Page) {
            Page<TBlog> page = (Page<TBlog>) collection;
            Page<TBlogVo> pageVo = new Page<>();
            BeanUtil.copyProperties(page, pageVo);
            pageVo.addAll(voList);
            voList = pageVo;
        }
        return voList;
    }

    @Override
    public Boolean insertByAddBo(TBlogAddBo bo) {
        if (bo.getTagId() != null && !bo.getTagId().equals("")) {
            String[] tagIds = bo.getTagId().split("'");
            for (String id : tagIds) {
                tagService.increaseTimes(Long.parseLong(id));
            }
        }
        TBlog add = BeanUtil.toBean(bo, TBlog.class);
        validEntityBeforeSave(add);
        return this.save(add);
    }

    @Override
    public TBlog insertByAddBoReturn(TBlogAddBo bo) {
        if (StrUtil.isNotBlank(bo.getTagId())) {
            String[] tagIds = bo.getTagId().split(",");
            for (String id : tagIds) {
                tagService.increaseTimes(Long.parseLong(id));
            }
        }
        TBlog add = BeanUtil.toBean(bo, TBlog.class);
        validEntityBeforeSave(add);
        this.save(add);
        return add;
    }

    @Override
    public Boolean updateByEditBo(TBlogEditBo bo) {
        TBlogVo tBlogVo = queryById(bo.getId());
        if (tBlogVo.getTagId() != null && !tBlogVo.getTagId().equals("")) {
            String[] tagIds = tBlogVo.getTagId().split(",");
            for (String id : tagIds) {
                tagService.decreaseTimes(Long.parseLong(id));
            }
        }
        if (bo.getTagId() != null && !bo.getTagId().equals("")) {
            String[] tagIds = bo.getTagId().split(",");
            for (String id : tagIds) {
                tagService.increaseTimes(Long.parseLong(id));
            }
        }
        TBlog update = BeanUtil.toBean(bo, TBlog.class);
        validEntityBeforeSave(update);
        return this.updateById(update);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(TBlog entity) {
        //TODO 做一些数据校验,如唯一约束
        List<String> urls = StringUtils.getUrls(entity.getContent());
        if (urls.size() > 0) {
            String secretId = configService.selectConfigByKey("cos.secret.id");
            String secretKey = configService.selectConfigByKey("cos.secret.key");
            String bucketName = configService.selectConfigByKey("cos.bucket");
            COSClient cosClient = CosUtil.initCos(secretId, secretKey);
            for (String url : urls) {
                if (ArrayUtils.contains(MimeTypeUtils.IMAGE_EXTENSION, FileTypeUtils.getFileType(url))) {
                    if (!url.contains(configService.selectConfigByKey("cos.bucket"))) {
                        try {
                            File file = ImageUtils.downloadImage(url);
                            String fileName = file.getPath();
                            String key = "/salishblog" + fileName;
                            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
                            cosClient.putObject(putObjectRequest);
                            String cosUrl = Constants.COS_URL + key;
                            entity.setContent(entity.getContent().replace(url, cosUrl));
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                }
            }

//            cosClient.shutdown();
        }
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        ids.forEach(blogId -> {
            TBlogVo tBlogVo = queryById(blogId);
            if (tBlogVo.getTagId() != null && !tBlogVo.getTagId().equals("")) {
                String[] tagIds = tBlogVo.getTagId().split("'");
                for (String id : tagIds) {
                    tagService.decreaseTimes(Long.parseLong(id));
                }
            }
        });
        return this.removeByIds(ids);
    }
}
