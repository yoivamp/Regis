package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.common.CustomException;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.CategoryMapper;
import com.regisbackend.regisbackend.dao.SetmealMapper;
import com.regisbackend.regisbackend.dto.SetmealDto;
import com.regisbackend.regisbackend.pojo.Category;
import com.regisbackend.regisbackend.pojo.Setmeal;
import com.regisbackend.regisbackend.pojo.SetmealDish;
import com.regisbackend.regisbackend.service.SetmealDishService;
import com.regisbackend.regisbackend.service.SetmealService;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增套餐
     *
     * @param setmealDto 新增套餐信息
     * @return 添加结果
     */
    @Override
    @Transactional
    public Result<String> saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);
        //获取添加套餐中的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //设置添加套餐中菜品对应的套餐id
        setmealDishes.forEach(item -> item.setSetmealId(setmealDto.getId()));
        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
        return Result.success("添加套餐成功");
    }

    /**
     * 修改当前套餐起售状态
     *
     * @param current 当前状态
     * @param ids     选中套餐id
     * @return 修改结果
     */
    @Override
    @Transactional
    public Result<String> changeStatus(Long current, List<Long> ids) {
        //获取需要修改状态的套餐列表
        List<Setmeal> setmealList = this.listByIds(ids);
        //遍历修改状态
        setmealList.forEach(item -> item.setStatus(item.getStatus() == 1 ? 0 : 1));
        return this.updateBatchById(setmealList) ? Result.success("修改状态成功") : null;
    }

    /**
     * 套餐分页及条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @param name     套餐名称
     * @return 查询结果
     */
    @Override
    public Result<Page<SetmealDto>> pageWithSetmeal(int page, int pageSize, String name) {
        //分页构造器对象
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        //添加查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(name), Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //执行查询
        this.page(setmealPage, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(setmealPage, setmealDtoPage);
        //更新套餐中的记录
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryMapper.selectById(categoryId);
            if (category != null) {
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return Result.success(setmealDtoPage);
    }

    /**
     * 根据id查询套餐信息
     *
     * @param id 套餐id
     * @return 查询结果
     */
    @Override
    public Result<SetmealDto> getByIdWithDish(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        //查询套餐基本信息，从setmeal表查询
        Setmeal setmeal = this.getById(id);

        //将setmeal赋值到setmealDto中
        BeanUtils.copyProperties(setmeal, setmealDto);

        //查询当前套餐对应的菜品信息，从setmeal_dish表查询，通过对应套餐id
        //设置查询条件，因为setmeal_dish中一个套餐id对应多条记录
        LambdaQueryWrapper<SetmealDish> setmealDishWrapper = new LambdaQueryWrapper<>();
        setmealDishWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishService.list(setmealDishWrapper);

        //给setmealDto的菜品列表赋值
        setmealDto.setSetmealDishes(setmealDishList);

        //获取套餐分类，从category表查询，通过category_id
        Category category = categoryMapper.selectById(setmeal.getCategoryId());

        //给setmealDto的分类名称赋值
        setmealDto.setCategoryName(category.getName());
        return Result.success(setmealDto);
    }

    @Override
    public Result<List<Setmeal>> getByCategoryId(Setmeal setmeal) {
        //设置查询条件(分类id，起售状态)
        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        setmealWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        setmealWrapper.orderByDesc(Setmeal::getUpdateTime);
        return Result.success(this.list(setmealWrapper));
    }

    /**
     * 修改套餐
     *
     * @param setmealDto 新增套餐信息
     * @return 修改结果
     */
    @Override
    public Result<String> updateWithDish(SetmealDto setmealDto) {
        //更新新增的菜品,更新setmeal_dish对应套餐id中的菜品
        //清理当前套餐对应菜品数据---setmeal_dish表的delete操作
        //创建查询setmeal_dish中对应套餐id所有的菜品
        LambdaQueryWrapper<SetmealDish> setmealDishWrapper = new LambdaQueryWrapper<>();
        setmealDishWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(setmealDishWrapper);
        //添加当前提交过来的菜品数据---setmeal_dish表的insert操作
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        //添加菜品对应的套餐id
        setmealDishList.forEach(item -> item.setSetmealId(setmealDto.getId()));
        setmealDishService.saveBatch(setmealDishList);
        //更新套餐中的分类id
        //从category表查询条件为套餐分类名称
        LambdaQueryWrapper<Category> categoryWrapper = new LambdaQueryWrapper<>();
        categoryWrapper.eq(Strings.isNotEmpty(setmealDto.getCategoryName()), Category::getName, setmealDto.getCategoryName());
        Category category = categoryMapper.selectOne(categoryWrapper);
        setmealDto.setCategoryName(category.getName());
        //更新setmeal表基本信息
        this.updateById(setmealDto);
        return Result.success("修改套餐成功");
    }

    /**
     * 根据套餐id删除套餐
     *
     * @param ids 选中套餐id
     * @return 删除结果
     */
    @Transactional
    @Override
    public Result<String> deleteWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> setmealWrapper = new LambdaQueryWrapper<>();
        setmealWrapper.in(Setmeal::getId, ids);
        setmealWrapper.eq(Setmeal::getStatus, 1);
        if (this.count(setmealWrapper) > 0) {
            //不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，无法删除");
        }
        //可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);
        //删除关系表中的数据----setmeal_dish
        LambdaQueryWrapper<SetmealDish> setmealDishWrapper = new LambdaQueryWrapper<>();
        setmealDishWrapper.in(SetmealDish::getSetmealId, ids);
        return setmealDishService.remove(setmealDishWrapper) ? Result.success("删除套餐成功") : null;
    }


}
