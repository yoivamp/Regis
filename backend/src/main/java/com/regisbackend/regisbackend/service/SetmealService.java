package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.SetmealDto;
import com.regisbackend.regisbackend.pojo.Setmeal;

import java.util.List;

/**
 * @author 喵vamp
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐
     *
     * @param setmealDto 新增套餐信息
     * @return 添加结果
     */
    Result<String> saveWithDish(SetmealDto setmealDto);

    /**
     * 修改当前套餐起售状态
     *
     * @param current 当前状态
     * @param ids     选中套餐id
     * @return 修改结果
     */
    Result<String> changeStatus(Long current, List<Long> ids);

    /**
     * 套餐分页及条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @param name     套餐名称
     * @return 查询结果
     */
    Result<Page<SetmealDto>> pageWithSetmeal(int page, int pageSize, String name);

    /**
     * 根据套餐id删除套餐
     *
     * @param ids 选中套餐id
     * @return 删除结果
     */
    Result<String> deleteWithDish(List<Long> ids);
}
