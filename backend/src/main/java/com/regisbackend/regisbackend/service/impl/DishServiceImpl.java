package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.CategoryMapper;
import com.regisbackend.regisbackend.dao.DishMapper;
import com.regisbackend.regisbackend.dto.DishDto;
import com.regisbackend.regisbackend.pojo.Category;
import com.regisbackend.regisbackend.pojo.Dish;
import com.regisbackend.regisbackend.pojo.DishFlavor;
import com.regisbackend.regisbackend.service.DishFlavorService;
import com.regisbackend.regisbackend.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 喵vamp
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增菜品
     *
     * @param dishDto 菜品信息
     * @return 添加结果
     */
    @Override
    public Result<String> saveWithFlavor(DishDto dishDto) {
        log.info(dishDto.toString());
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        //获取菜品id
        Long dishId = dishDto.getId();
        //设置菜品口味对应菜品id
        List<DishFlavor> listFlavor = dishDto.getFlavors().stream()
                .map(item -> {
                    item.setDishId(dishId);
                    return item;
                }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(listFlavor);
        return Result.success("添加菜品成功");
    }

    /**
     * 菜品分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @param name     查询菜品名称
     * @return 查询结果
     */
    @Override
    public Result<Page<DishDto>> pageDish(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.like(Strings.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        this.page(dishPage, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(dishPage, dishDtoPage);
        //获取查询到的dish的list
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map(item -> {
            //将当前dish复制给dishDto
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            //获取dish中的分类id对应的分类信息
            Category category = categoryMapper.selectById(item.getCategoryId());
            //设置dishDto的分类名称
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        //设置dishDto的records
        dishDtoPage.setRecords(list);
        return Result.success(dishDtoPage);
    }

    /**
     * 根据id回显菜品信息
     *
     * @param id 菜品id
     * @return 回显信息
     */
    @Override
    public Result<DishDto> getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        //将dish赋值到dishDto中
        BeanUtils.copyProperties(dish, dishDto);
        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        //获取口味信息列表
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        //给dishDto的口味列表赋值
        dishDto.setFlavors(flavors);
        return Result.success(dishDto);
    }

    /**
     * 修改菜品信息
     * @param dishDto 新增的菜品信息
     * @return 修改结果
     */
    @Override
    @Transactional
    public Result<String> updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        //为新增的口味数据添加对应的菜品id
        flavors.forEach(item -> item.setDishId(dishDto.getId()));
        //更新口味表
        return dishFlavorService.saveBatch(flavors) ? Result.success("修改成功") : null;
    }

}
