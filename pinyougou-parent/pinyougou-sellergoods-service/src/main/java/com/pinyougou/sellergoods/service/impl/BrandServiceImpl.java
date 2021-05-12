package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;
import entity.Result;

@Service
public class BrandServiceImpl implements BrandService {
	
	@Autowired
	private TbBrandMapper tbBrandMapper;
	
	/**
	 * 查询所有品牌
	 */
	@Override
	public List<TbBrand> findAll() {
		
		
		return tbBrandMapper.selectByExample(null);
	}
	
	/**
	 * 分页显示品牌列表
	 */
	@Override
	public PageResult<TbBrand> findPage(int pageNum, int pageSize) {
		
		PageHelper.startPage(pageNum, pageSize);
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
		return new PageResult<TbBrand>(page.getTotal(),page.getResult());
	}
	
	
	/**
	 * 增加品牌
	 */
	@Override
	public void add(TbBrand brand) {
		
		tbBrandMapper.insert(brand);
	}
	
	/**
	 * 通过id查询品牌
	 */
	@Override
	public TbBrand findOne(Long id) {
		
		return tbBrandMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 修改品牌信息
	 */
	@Override
	public void update(TbBrand brand) {
		
		tbBrandMapper.updateByPrimaryKey(brand);
		
	}
	
	/**
	 * 删除品牌
	 */
	@Override
	public void delete(Long[] ids) {
		for (Long id : ids) {
			tbBrandMapper.deleteByPrimaryKey(id);
		}
		
	}
	
	/**
	 * 条件分页查询
	 */
	@Override
	public PageResult<TbBrand> findPage(TbBrand brand, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbBrandExample example = new TbBrandExample();
		Criteria criteria = example.createCriteria();
		if (brand!=null) {
			if (brand.getName()!=null && brand.getName().length()>0) {
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			
			if (brand.getFirstChar()!=null && brand.getFirstChar().length()>0) {
				criteria.andFirstCharEqualTo(brand.getFirstChar());
			
			}
			
		}
		Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(example);
		return new PageResult<TbBrand>(page.getTotal(),page.getResult());
	}

}
