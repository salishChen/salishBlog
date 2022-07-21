package com.salishBlog.web.controller.business;

import java.util.List;
import java.util.Arrays;

import com.salishBlog.common.core.domain.entity.SysUser;
import com.salishBlog.common.core.domain.model.LoginUser;
import com.salishBlog.common.utils.DateUtils;
import com.salishBlog.common.utils.ServletUtils;
import com.salishBlog.framework.web.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.salishBlog.common.annotation.Log;
import com.salishBlog.common.core.controller.BaseController;
import com.salishBlog.common.core.domain.AjaxResult;
import com.salishBlog.common.enums.BusinessType;
import com.salishBlog.business.domain.vo.TTagVo;
import com.salishBlog.business.domain.bo.TTagQueryBo;
import com.salishBlog.business.domain.bo.TTagAddBo;
import com.salishBlog.business.domain.bo.TTagEditBo;
import com.salishBlog.business.service.ITTagService;
import com.salishBlog.common.utils.poi.ExcelUtil;
import com.salishBlog.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 标签Controller
 *
 * @author salishChen
 * @date 2022-07-21
 */
@Api(value = "标签控制器", tags = {"标签管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/tTag/tag")
public class TTagController extends BaseController {

    private final ITTagService iTTagService;
    @Autowired
    private TokenService tokenService;
    /**
     * 查询标签列表
     */
    @ApiOperation("查询标签列表")
    @PreAuthorize("@ss.hasPermi('tTag:tag:list')")
    @GetMapping("/list")
    public TableDataInfo<TTagVo> list(TTagQueryBo bo) {
        startPage();
        List<TTagVo> list = iTTagService.queryList(bo);
        return getDataTable(list);
    }

    /**
     * 导出标签列表
     */
    @ApiOperation("导出标签列表")
    @PreAuthorize("@ss.hasPermi('tTag:tag:export')")
    @Log(title = "标签", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult<TTagVo> export(TTagQueryBo bo) {
        List<TTagVo> list = iTTagService.queryList(bo);
        ExcelUtil<TTagVo> util = new ExcelUtil<TTagVo>(TTagVo.class);
        return util.exportExcel(list, "tag" );
    }

    /**
     * 获取标签详细信息
     */
    @ApiOperation("获取标签详细信息")
    @PreAuthorize("@ss.hasPermi('tTag:tag:query')")
    @GetMapping("/{id}")
    public AjaxResult<TTagVo> getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTTagService.queryById(id));
    }

    /**
     * 新增标签
     */
    @ApiOperation("新增标签")
    @PreAuthorize("@ss.hasPermi('tTag:tag:add')")
    @Log(title = "标签", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult<Void> add(@RequestBody TTagAddBo bo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        return toAjax(iTTagService.insertByAddBo(bo) ? 1 : 0);
    }

    /**
     * 修改标签
     */
    @ApiOperation("修改标签")
    @PreAuthorize("@ss.hasPermi('tTag:tag:edit')")
    @Log(title = "标签", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult<Void> edit(@RequestBody TTagEditBo bo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        return toAjax(iTTagService.updateByEditBo(bo) ? 1 : 0);
    }

    /**
     * 删除标签
     */
    @ApiOperation("删除标签")
    @PreAuthorize("@ss.hasPermi('tTag:tag:remove')")
    @Log(title = "标签" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iTTagService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
