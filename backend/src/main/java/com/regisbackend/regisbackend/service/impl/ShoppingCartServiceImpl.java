package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.common.MyBaseContext;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.ShoppingCartMapper;
import com.regisbackend.regisbackend.pojo.ShoppingCart;
import com.regisbackend.regisbackend.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 喵vamp
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    /**
     * 添加菜品/套餐到购物车
     *
     * @param shoppingCart 菜品/套餐信息
     * @return 添加结果
     */
    @Override
    public Result<ShoppingCart> addShoppingCart(ShoppingCart shoppingCart) {
        log.info("购物车数据:{}", shoppingCart);
        //设置用户id，指定当前是哪个用户的购物车数据
        shoppingCart.setUserId(MyBaseContext.getCurrentId());

        //创建查询条件
        //查询当前菜品或者套餐是否在购物车中
        //SQL:select * from shopping_cart where user_id = ? and dish_id/setmeal_id = ?
        ShoppingCart cart = getShoppingCart(shoppingCart);
        if (cart != null) {
            //判断新增的是否和原来口味一致(套餐才有口味)
            if (cart.getSetmealId() != null && cart.getDishFlavor().equals(shoppingCart.getDishFlavor())) {
                //如果相同，就在原来数量基础上加一
                cart.setNumber(cart.getNumber() + 1);
                return this.updateById(cart) ? Result.success(cart) : null;
            }
            //添加的是菜品
            cart.setNumber(cart.getNumber() + 1);
            return this.updateById(cart) ? Result.success(cart) : null;
        }
        //如果不存在或者和原先的口味不一致，则添加到购物车，数量默认就是一
        //设置创建时间
        shoppingCart.setCreateTime(LocalDateTime.now());
        this.save(shoppingCart);
        return Result.success(shoppingCart);
    }

    /**
     * 移除购物车的菜品或套餐
     *
     * @param shoppingCart 购物车的菜品或套餐
     * @return 移除结果
     */
    @Override
    public Result<String> removeDishOrSetmeal(ShoppingCart shoppingCart) {
        //获取当前购物车信息
        ShoppingCart current = getShoppingCart(shoppingCart);
        //如果移除的菜品或套餐数量大于1，则只需将数量-1
        if (current.getNumber() > 1) {
            current.setNumber(current.getNumber() - 1);
            return this.updateById(current) ? Result.success("移除成功") : null;
        }
        return this.removeById(current.getId()) ? Result.success("移除成功") : null;
    }

    /**
     * 展示启售菜品/套餐
     *
     * @return 展示列表
     */
    @Override
    public Result<List<ShoppingCart>> listShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, MyBaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        return Result.success(this.list(queryWrapper));
    }

    /**
     * 清空购物车
     *
     * @return 清空结果
     */
    @Override
    public Result<String> cleanShoppingCart() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, MyBaseContext.getCurrentId());
        return this.remove(queryWrapper) ? Result.success("清空成功") : null;
    }

    /**
     * 获取查询当前用户的菜品或套餐购物车
     *
     * @param shoppingCart 菜品或套餐信息
     * @return 购物车
     */
    public ShoppingCart getShoppingCart(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, MyBaseContext.getCurrentId());
        if (shoppingCart.getDishId() != null) {
            //清除的是菜品，查找菜品
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //清除的是套餐，查找套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        return this.getOne(queryWrapper);
    }
}
