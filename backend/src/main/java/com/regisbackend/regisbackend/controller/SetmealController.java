package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.SetmealDto;
import com.regisbackend.regisbackend.pojo.Setmeal;
import com.regisbackend.regisbackend.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 喵vamp
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     *
     * @param setmealDto 新增套餐信息
     * @return 添加结果
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        return setmealService.saveWithDish(setmealDto);
    }

    /**
     * 修改当前套餐起售状态
     *
     * @param current 当前状态
     * @param ids     选中套餐id
     * @return 修改结果
     */
    @PostMapping("/status/{current}")
    public Result<String> changeStatus(@PathVariable Long current, @RequestParam List<Long> ids) {
        return setmealService.changeStatus(current, ids);
    }

    /**
     * 套餐分页及条件查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @param name     套餐名称
     * @return 查询结果
     */
    @GetMapping("/page")
    public Result<Page<SetmealDto>> page(int page, int pageSize, String name) {
        return setmealService.pageWithSetmeal(page, pageSize, name);
    }

    /**
     * 根据id查询套餐信息
     *
     * @param id 套餐id
     * @return 查询结果
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> getById(@PathVariable Long id) {
        return setmealService.getByIdWithDish(id);
    }


    @GetMapping("/list")
    public Result<List<Setmeal>> getCategoryId(Setmeal setmeal) {
        return setmealService.getByCategoryId(setmeal);
    }

    /**
     * 修改套餐
     *
     * @param setmealDto 新增套餐信息
     * @return 修改结果
     */
    @PutMapping
    @Transactional
    public Result<String> update(@RequestBody SetmealDto setmealDto) {
        return setmealService.updateWithDish(setmealDto);
    }

    /**
     * 根据套餐id删除套餐
     *
     * @param ids 选中套餐id
     * @return 删除结果
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        return setmealService.deleteWithDish(ids);
    }
}
