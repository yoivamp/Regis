package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.utils.EmployeeHolder;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.AddressBookMapper;
import com.regisbackend.regisbackend.pojo.AddressBook;
import com.regisbackend.regisbackend.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 喵vamp
 */
@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    /**
     * 新增地址
     *
     * @param addressBook 地址信息
     * @return 新增结果
     */
    @Override
    public Result<AddressBook> saveAddressBook(AddressBook addressBook) {
        addressBook.setUserId(EmployeeHolder.getCurrentId());
        log.info("addressBook:{}", addressBook);
        return this.save(addressBook) ? Result.success(addressBook) : null;
    }

    /**
     * 设置默认地址
     *
     * @param addressBook 地址信息
     * @return 设置结果
     */
    @Override
    public Result<AddressBook> setDefault(AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, EmployeeHolder.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);

        //SQL:update address_book set is_default = 0 where user_id = ?
        this.update(wrapper);
        addressBook.setIsDefault(1);

        //SQL:update address_book set is_default = 1 where id = ?
        return this.updateById(addressBook) ? Result.success(addressBook) : null;
    }

    @Override
    public Result<AddressBook> updateAddressBook(AddressBook addressBook) {
        return this.updateById(addressBook) ? Result.success(addressBook) : null;
    }

    /**
     * 查询默认地址
     *
     * @return 默认地址
     */
    @Override
    public Result<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, EmployeeHolder.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = this.getOne(queryWrapper);
        return addressBook == null ? Result.error("没有找到该对象") : Result.success(addressBook);
    }

    /**
     * 查询指定用户的全部地址
     *
     * @param addressBook 指定用户
     * @return 地址信息
     */
    @Override
    public Result<List<AddressBook>> listAddressBook(AddressBook addressBook) {
        addressBook.setUserId(EmployeeHolder.getCurrentId());
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return Result.success(this.list(queryWrapper));
    }

    /**
     * 删除地址
     *
     * @param ids 地址信息id
     * @return 删除结果
     */
    @Override
    @Transactional
    public Result<String> deleteAddressBook(Long ids) {
        return this.removeById(ids) ? Result.success("删除地址成功") : null;
    }
}
