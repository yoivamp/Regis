package com.regisbackend.regisbackend.controller;

import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.ShoppingCart;
import com.regisbackend.regisbackend.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 喵vamp
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加菜品/套餐到购物车
     *
     * @param shoppingCart 菜品/套餐信息
     * @return 添加结果
     */
    @PostMapping("/add")
    @CacheEvict(value = "shoppingCartCache",allEntries = true)
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.addShoppingCart(shoppingCart);
    }

    /**
     * 移除购物车的菜品或套餐
     *
     * @param shoppingCart 购物车的菜品或套餐
     * @return 移除结果
     */
    @PostMapping("/sub")
    @CacheEvict(value = "shoppingCartCache",allEntries = true)
    public Result<String> remove(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.removeDishOrSetmeal(shoppingCart);
    }

    /**
     * 展示启售菜品/套餐
     *
     * @return 展示列表
     */
    @GetMapping("/list")
    @Cacheable("shoppingCartCache")
    public Result<List<ShoppingCart>> list() {
        return shoppingCartService.listShoppingCart();
    }

    /**
     * 清空购物车
     *
     * @return 清空结果
     */
    @DeleteMapping("/clean")
    @CacheEvict(value = "shoppingCartCache",allEntries = true)
    public Result<String> clean() {
        return shoppingCartService.cleanShoppingCart();
    }

}
