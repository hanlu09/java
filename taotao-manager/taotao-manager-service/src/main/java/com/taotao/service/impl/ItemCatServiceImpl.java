package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.ItemCatService;

/**
 * 商品分类管理Service
 * <p>Title: ItemCatServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<Map> getItemCatList(long parentId) {
		//根据parentId查询子节点列表 
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//转换成EasyUI需要的数据
		List<Map> resultList = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			Map node = new HashMap<>();
			//id
			node.put("id", tbItemCat.getId());
			//text
			node.put("text", tbItemCat.getName());
			//state
			node.put("state", tbItemCat.getIsParent()?"closed":"open");
			//添加到列表
			resultList.add(node);
		}
		return resultList;
	}

}
