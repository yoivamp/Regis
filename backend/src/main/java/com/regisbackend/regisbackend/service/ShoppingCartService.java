package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.ShoppingCart;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 喵vamp
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加菜品/套餐到购物车
     *
     * @param shoppingCart 菜品/套餐信息
     * @return 添加结果
     */
    Result<ShoppingCart> addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 移除购物车的菜品或套餐
     *
     * @param shoppingCart 购物车的菜品或套餐
     * @return 移除结果
     */
    Result<String> removeDishOrSetmeal(ShoppingCart shoppingCart);

    /**
     * 展示启售菜品/套餐
     *
     * @return 展示列表
     */
    Result<List<ShoppingCart>> listShoppingCart();

    /**
     * 清空购物车
     *
     * @return 清空结果
     */
    Result<String> cleanShoppingCart();
}
