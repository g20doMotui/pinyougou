package com.pinyougou.sellergoods.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;

import entity.PageResult;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        // 设置新增的商品spu审核状态为未审核
        goods.getGoods().setAuditStatus("0");
        goodsMapper.insert(goods.getGoods());
        // 设置商品描述的id为商品spu的id
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        // 插入商品描述
        goodsDescMapper.insert(goods.getGoodsDesc());


        saveItemList(goods);



    }

    // 保存sku列表
    private void saveItemList(Goods goods) {
        if ("1".equals(goods.getGoods().getIsEnableSpec())){
            List<TbItem> itemList = goods.getItemList();

            for (TbItem tbItem : itemList) {
                //标题
                String title = goods.getGoods().getGoodsName();
                String spec = tbItem.getSpec();
                Map<String, Object> map = JSON.parseObject(spec);
                for (String key : map.keySet()) {

                    title += ""+map.get(key);

                    tbItem.setTitle(title);
                }

                // 是否开启规格都需要设置的属性
                itemSetValue(goods,tbItem);

                itemMapper.insert(tbItem);

            }

        }else{// 不启用规格 插入一条默认sku
            TbItem tbItem = new TbItem();
            // 商品spu名称作为标题
            tbItem.setImage(goods.getGoods().getGoodsName());
            //价格
            tbItem.setPrice(goods.getGoods().getPrice());
            //状态
            tbItem.setIsDefault("1");
            //库存数量
            tbItem.setNum(9999);

            tbItem.setSpec("{}");

            itemSetValue(goods,tbItem);


            itemMapper.insert(tbItem);

        }

    }


    /**
     * 是否开启规格都需要设置的属性
     * @param goods
     * @param tbItem
     */
    private void itemSetValue(Goods goods,TbItem tbItem){


        //商品SPU编号
        tbItem.setGoodsId(goods.getGoods().getId());
        //商家编号
        tbItem.setSellerId(goods.getGoods().getSellerId());
        //商品分类编号（3级
        tbItem.setCategoryid(goods.getGoods().getCategory3Id());
        //创建日期
        tbItem.setCreateTime(new Date());
        //修改日期
        tbItem.setUpdateTime(new Date());
        //品牌名称
        TbBrand tbBrand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        tbItem.setBrand(tbBrand.getName());
        //分类名称
        TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
        tbItem.setCategory(tbItemCat.getName());
        //商家名称
        TbSeller tbSeller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        tbItem.setSeller(tbSeller.getNickName());
        //图片地址（取spu的第一个图片）
        String itemImages = goods.getGoodsDesc().getItemImages();
        List<Map> imageList = JSON.parseArray(itemImages, Map.class);

        if (imageList.size()>0){
            String url = (String) imageList.get(0).get("url");
            tbItem.setImage(url);
        }


    }


    /**
     * 修改
     */
    @Override
    public void update(Goods goods) {

        // 经过修改的商品需要把auditStatus重新设置为未审核状态
        goods.getGoods().setAuditStatus("0");

        // 更新tbgoods
        goodsMapper.updateByPrimaryKey(goods.getGoods());
        // 更新tbgoodsdesc
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());
        // 跟新sku列表数据
        // 删除原有的sku列表
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);

        // 保存新的sku列表
        saveItemList(goods);




    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public Goods findOne(Long id) {
        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);
        // 读取goodsdesc
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(tbGoodsDesc);
        // 读取spec
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);

        List<TbItem> items = itemMapper.selectByExample(example);
        goods.setItemList(items);

        return goods;
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            // 逻辑删除
            tbGoods.setIsDelete("1");
            goodsMapper.updateByPrimaryKey(tbGoods);
        }
    }


    /**
     * 查询指定商家的商品列表
     * @param goods
     * @param pageNum 当前页 码
     * @param pageSize 每页记录数
     * @return
     */
    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();
        // 是否删除为null值
        criteria.andIsDeleteIsNull();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                //criteria.andSellerIdLike("%" + goods.getSellerId() + "%");
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusLike("%" + goods.getAuditStatus() + "%");
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 更新审核状态
     * @param ids
     * @param status
     */
    public void updateStatus(Long[] ids, String status){

        for (Long id : ids) {
            TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            tbGoods.setAuditStatus(status);
            goodsMapper.updateByPrimaryKey(tbGoods);

        }


    }

}
