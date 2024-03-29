package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.DishDto;
import com.regisbackend.regisbackend.pojo.Dish;
import com.regisbackend.regisbackend.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 *
 * @author 喵vamp
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDto 菜品信息
     * @return 添加结果
     */
    @PostMapping
    @CacheEvict(value = "dishCache",allEntries = true)
    public Result<String> save(@RequestBody DishDto dishDto) {
        return dishService.saveWithFlavor(dishDto);
    }

    /**
     * 菜品分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @param name     查询菜品名称
     * @return 查询结果
     */
    @GetMapping("/page")
    public Result<Page<DishDto>> page(int page, int pageSize, String name) {
        return dishService.pageDish(page, pageSize, name);
    }

    /**
     * 根据id回显菜品信息
     *
     * @param id 菜品id
     * @return 回显信息
     */
    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id) {
        return dishService.getByIdWithFlavor(id);
    }

    /**
     * 查询所有可用菜品
     *
     * @param dish 菜品
     * @return 查询结果
     */
    @GetMapping("/list")
    @Cacheable(value = "dishCache",key = "#dish.categoryId+'_'+#dish.status")
    public Result<List<DishDto>> list(Dish dish) {
        return dishService.listWithInsert(dish);
    }

    /**
     * 修改菜品起售状态
     * @param current 当前起售状态
     * @param ids 选中菜品id
     * @return 修改结果
     */
    @CacheEvict(value = "dishCache",allEntries = true)
    @PostMapping("/status/{current}")
    public Result<String> changeStatus(@PathVariable Long current, @RequestParam List<Long> ids) {
        return dishService.changeStatus(current, ids);
    }

    /**
     * 修改菜品信息
     *
     * @param dishDto 新增的菜品信息
     * @return 修改结果
     */
    @PutMapping
    @CacheEvict(value = "dishCache",allEntries = true)
    public Result<String> update(@RequestBody DishDto dishDto) {
        return dishService.updateWithFlavor(dishDto);
    }

    /**
     * 删除菜品
     * @param ids 菜品id
     * @return 删除结果
     */
    @DeleteMapping
    @CacheEvict(value = "dishCache",allEntries = true)
    public Result<String> delete(@RequestParam List<Long> ids) {
        return dishService.deleteDish(ids);
    }
}

