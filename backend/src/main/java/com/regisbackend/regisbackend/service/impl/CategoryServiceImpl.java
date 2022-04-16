package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.common.CustomException;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.CategoryMapper;
import com.regisbackend.regisbackend.pojo.Category;
import com.regisbackend.regisbackend.pojo.Dish;
import com.regisbackend.regisbackend.pojo.Setmeal;
import com.regisbackend.regisbackend.service.CategoryService;
import com.regisbackend.regisbackend.service.DishService;
import com.regisbackend.regisbackend.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 喵vamp
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    /**
     * 新增分类
     *
     * @param category 分类信息
     * @return 新增结果
     */
    public Result<String> saveCategory(Category category) {
        log.info("category:{}", category);
        return save(category) ? Result.success("新增分类成功") : null;
    }

    /**
     * 分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @return 查询数据
     */
    public Result<Page<Category>> pageCategory(int page, int pageSize) {
        //创建分页构造器
        Page<Category> categoryPage = new Page<>(page, pageSize);
        //创建条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //执行查询
        page(categoryPage, queryWrapper);
        return Result.success(categoryPage);
    }

    /**
     * 删除分类信息
     *
     * @param id 分类信息id
     * @return 删除结果
     */
    public Result<String> deleteCategory(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        if (dishService.count(dishQueryWrapper) > 0) {
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        if (setmealService.count(setmealQueryWrapper) > 0) {
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除分类
        return removeById(id) ? Result.success("分类信息删除成功") : null;
    }

    /**
     * 修改分类信息
     *
     * @param category 更新后的员工信息
     * @return 修改结果
     */
    public Result<String> updateCategory(Category category) {
        log.info("修改分类信息：{}", category);
        return updateById(category) ? Result.success("修改分类信息成功") : null;
    }
}
