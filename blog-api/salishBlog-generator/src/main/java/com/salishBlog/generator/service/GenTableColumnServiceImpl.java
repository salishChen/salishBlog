package com.salishBlog.generator.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.salishBlog.generator.domain.GenTable;
import com.salishBlog.generator.domain.GenTableColumn;
import com.salishBlog.generator.mapper.GenTableColumnMapper;
import com.salishBlog.generator.mapper.GenTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hutool.core.convert.Convert;

/**
 * 业务字段 服务层实现
 *
 * @author
 */
@Service
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, GenTableColumn> implements IGenTableColumnService
{

	@Override
	public List<GenTableColumn> selectDbTableColumnsByName(String tableName) {
		return this.baseMapper.selectDbTableColumnsByName(tableName);
	}

	/**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
	@Override
	public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId)
	{
	    return this.baseMapper.selectGenTableColumnListByTableId(tableId);
	}

    /**
     * 新增业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
	@Override
	public int insertGenTableColumn(GenTableColumn genTableColumn)
	{
	    return this.baseMapper.insertGenTableColumn(genTableColumn);
	}

	/**
     * 修改业务字段
     *
     * @param genTableColumn 业务字段信息
     * @return 结果
     */
	@Override
	public int updateGenTableColumn(GenTableColumn genTableColumn)
	{
	    return this.baseMapper.updateGenTableColumn(genTableColumn);
	}

	@Override
	public int deleteGenTableColumns(List<GenTableColumn> genTableColumns) {
		return this.baseMapper.deleteGenTableColumns(genTableColumns);
	}

	/**
     * 删除业务字段对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteGenTableColumnByIds(String ids)
	{
		return this.baseMapper.deleteGenTableColumnByIds(Convert.toLongArray(ids));
	}
}
