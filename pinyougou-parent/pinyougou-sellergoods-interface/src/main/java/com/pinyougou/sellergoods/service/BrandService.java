package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;
import entity.Result;

/**
 * 品牌接口
 * @author hwc
 *
 */
public interface BrandService {
	
	
	public List<TbBrand> findAll();
	
	/**
	 * 分页查询所有品牌
	 * @param pageNum 当前页码
	 * @param pageSize 每页显示的条数
	 * @return
	 */
	public PageResult<TbBrand> findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加品牌
	 * @param brand
	 */
	public void add(TbBrand brand);
	
	/**
	 * 通过id查找品牌
	 * @param id
	 * @return
	 */
	public TbBrand findOne(Long id);
	
	/**
	 * 修改品牌信息
	 * @param brand
	 */
	public void update(TbBrand brand);
	
	/**
	 * 删除品牌
	 * @param ids
	 */
	public void delete(Long[] ids);
	
	
	/**
	 * 条件分页查询
	 * @param brand
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageResult<TbBrand> findPage(TbBrand brand,int pageNum, int pageSize);
	
	/**
	 * 品牌下拉框数据
	 * @return
	 */
	List<Map> selectOptionList();
}
