package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.DishDto;
import com.regisbackend.regisbackend.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
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
     * 修改菜品信息
     * @param dishDto 新增的菜品信息
     * @return 修改结果
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto) {
        return dishService.updateWithFlavor(dishDto);
    }

}

