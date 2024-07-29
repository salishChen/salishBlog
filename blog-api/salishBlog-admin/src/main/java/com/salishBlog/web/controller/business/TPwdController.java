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
import com.salishBlog.business.domain.vo.TPwdVo;
import com.salishBlog.business.domain.bo.TPwdQueryBo;
import com.salishBlog.business.domain.bo.TPwdAddBo;
import com.salishBlog.business.domain.bo.TPwdEditBo;
import com.salishBlog.business.service.ITPwdService;
import com.salishBlog.common.utils.poi.ExcelUtil;
import com.salishBlog.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 秘钥本Controller
 *
 * @author salishChen
 * @date 2024-05-15
 */
@Api(value = "秘钥本控制器", tags = {"秘钥本管理"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("/business/pwd")
public class TPwdController extends BaseController {

    private final ITPwdService iTPwdService;
    @Autowired
    private TokenService tokenService;
    /**
     * 查询秘钥本列表
     */
    @ApiOperation("查询秘钥本列表")
    @PreAuthorize("@ss.hasPermi('business:pwd:list')")
    @GetMapping("/list")
    public TableDataInfo<TPwdVo> list(TPwdQueryBo bo) {
        startPage();
        List<TPwdVo> list = iTPwdService.queryList(bo);
        return getDataTable(list);
    }

    /**
     * 导出秘钥本列表
     */
    @ApiOperation("导出秘钥本列表")
    @PreAuthorize("@ss.hasPermi('business:pwd:export')")
    @Log(title = "秘钥本", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult<TPwdVo> export(TPwdQueryBo bo) {
        List<TPwdVo> list = iTPwdService.queryList(bo);
        ExcelUtil<TPwdVo> util = new ExcelUtil<TPwdVo>(TPwdVo.class);
        return util.exportExcel(list, "pwd" );
    }

    /**
     * 获取秘钥本详细信息
     */
    @ApiOperation("获取秘钥本详细信息")
    @PreAuthorize("@ss.hasPermi('business:pwd:query')")
    @GetMapping("/{id}")
    public AjaxResult<TPwdVo> getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTPwdService.queryById(id));
    }

    /**
     * 新增秘钥本
     */
    @ApiOperation("新增秘钥本")
    @PreAuthorize("@ss.hasPermi('business:pwd:add')")
    @Log(title = "秘钥本", businessType = BusinessType.INSERT)
    @PostMapping()
    public AjaxResult<Void> add(@RequestBody TPwdAddBo bo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        return toAjax(iTPwdService.insertByAddBo(bo) ? 1 : 0);
    }

    /**
     * 修改秘钥本
     */
    @ApiOperation("修改秘钥本")
    @PreAuthorize("@ss.hasPermi('business:pwd:edit')")
    @Log(title = "秘钥本", businessType = BusinessType.UPDATE)
    @PutMapping()
    public AjaxResult<Void> edit(@RequestBody TPwdEditBo bo) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        return toAjax(iTPwdService.updateByEditBo(bo) ? 1 : 0);
    }

    /**
     * 删除秘钥本
     */
    @ApiOperation("删除秘钥本")
    @PreAuthorize("@ss.hasPermi('business:pwd:remove')")
    @Log(title = "秘钥本" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iTPwdService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
