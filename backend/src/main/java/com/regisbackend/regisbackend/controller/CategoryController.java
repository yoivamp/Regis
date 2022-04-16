package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.Category;
import com.regisbackend.regisbackend.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 *
 * @author 喵vamp
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryImpl;

    /**
     * 新增分类
     *
     * @param category 分类信息
     * @return 新增结果
     */
    @PostMapping
    public Result<String> save(@RequestBody Category category) {
        return categoryImpl.saveCategory(category);
    }

    /**
     * 分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @return 查询数据
     */
    @GetMapping("/page")
    public Result<Page<Category>> page(int page, int pageSize) {
        return categoryImpl.pageCategory(page, pageSize);
    }

    /**
     * 删除分类信息
     *
     * @param id 分类信息id
     * @return 删除结果
     */
    @DeleteMapping
    public Result<String> delete(Long id) {
        return categoryImpl.deleteCategory(id);
    }

    /**
     * 修改分类信息
     * @param category 更新后的员工信息
     * @return 修改结果
     */
    @PutMapping
    public Result<String> update(@RequestBody Category category){
        return categoryImpl.updateCategory(category);
    }
}
