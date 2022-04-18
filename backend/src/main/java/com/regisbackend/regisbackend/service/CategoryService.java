package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 喵vamp
 */
public interface CategoryService extends IService<Category> {
    /**
     * 新增分类
     *
     * @param category 分类信息
     * @return 新增结果
     */
    Result<String> saveCategory(Category category);

    /**
     * 分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @return 查询数据
     */
    Result<Page<Category>> pageCategory(int page, int pageSize);

    /**
     * 返回菜品分类
     * @param category 分类信息
     * @return 分类列表
     */
    Result<List<Category>> getCategoryList(Category category);

    /**
     * 删除分类信息
     *
     * @param id 分类信息id
     * @return 删除结果
     */
    Result<String> deleteCategory(Long id);

    /**
     * 修改分类信息
     *
     * @param category 更新后的员工信息
     * @return 修改结果
     */
    @Transactional
    Result<String> updateCategory(Category category);
}
