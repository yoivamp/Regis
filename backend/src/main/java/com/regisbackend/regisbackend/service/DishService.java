package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.DishDto;
import com.regisbackend.regisbackend.pojo.Dish;

import java.util.List;

/**
 * @author 喵vamp
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品
     *
     * @param dishDto 菜品信息
     * @return 添加结果
     */
    Result<String> saveWithFlavor(DishDto dishDto);

    /**
     * 菜品分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @param name     查询菜品名称
     * @return 查询结果
     */
    Result<Page<DishDto>> pageDish(int page, int pageSize, String name);


    /**
     * 根据id回显菜品信息
     *
     * @param id 菜品id
     * @return 回显信息
     */
    Result<DishDto> getByIdWithFlavor(Long id);


    /**
     * 查询所有可用菜品
     *
     * @param dish 菜品
     * @return 查询结果
     */
    Result<List<DishDto>> listWithInsert(Dish dish);

    /**
     * 修改菜品起售状态
     *
     * @param current 当前起售状态
     * @param ids     选中菜品id
     * @return 修改结果
     */
    Result<String> changeStatus(Long current, List<Long> ids);

    /**
     * 修改菜品信息
     *
     * @param dishDto 新增的菜品信息
     * @return 修改结果
     */
    Result<String> updateWithFlavor(DishDto dishDto);

    /**
     * 删除菜品
     *
     * @param ids 菜品id
     * @return 删除结果
     */
    Result<String> deleteDish(List<Long> ids);
}
