package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.pojo.Item;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 购物车管理Controller
 * <p>Title: CartController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class CartController {
	
	@Value("${CART_KEY}")
	private String CART_KEY;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	@Autowired
	private ItemService itemService;

	//添加购车处理
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,
			@RequestParam(defaultValue="1")Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//1、取商品id和商品数量
		//2、从cookie中取购物车列表
		List<TbItem> cartList = getCartList(request);
		//3、判断购物车列表中是否有此商品数据
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				//4、如果有数量相加
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
			}
		}		
		//5、如果没有根据商品id查询商品信息
		if (!flag) {
			TbItem tbItem = itemService.getItemById(itemId);
			//6、把商品信息添加到购物车，数量就是num的值
			tbItem.setNum(num);
			cartList.add(tbItem);
		}
		//7、把购物车列表添加到cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartList),
				CART_EXPIRE, true);
		//8、返回添加成功页面
		return "cartSuccess";
	}
	
	private List<TbItem> getCartList(HttpServletRequest request) {
		//使用CookieUtil工具类
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		if (StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}
		//如果cookie中有购物车列表
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request) {
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartList(request);
		//把商品列表传递给页面
		List<Item> itemList = new ArrayList<>();
		for (TbItem tbItem : cartList) {
			Item item = new Item(tbItem);
			itemList.add(item);
		}
		request.setAttribute("cartList", itemList);
		//返回逻辑视图
		return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateCartItem(@PathVariable Long itemId, @PathVariable Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartList(request);
		//查询到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				//更新商品数量
				tbItem.setNum(num);
				break;
			}
		}
		//把购车列表写入cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartList),
				CART_EXPIRE, true);
		//返回TaotaoResult
		return TaotaoResult.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, 
			HttpServletResponse response,HttpServletRequest request) {
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartList(request);
		//找到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				//删除对应的商品
				cartList.remove(tbItem);
				break;
			}
		}
		//写入cookie
		CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartList),
				CART_EXPIRE, true);
		//返回购物车列表页面
		return "redirect:/cart/cart.html";
	}
}