package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.AddressBook;

import java.util.List;

/**
 * @author 喵vamp
 */
public interface AddressBookService extends IService<AddressBook> {

    /**
     * 新增地址
     *
     * @param addressBook 地址信息
     * @return 新增结果
     */
    Result<AddressBook> saveAddressBook(AddressBook addressBook);

    /**
     * 设置默认地址
     *
     * @param addressBook 地址信息
     * @return 设置结果
     */
    Result<AddressBook> setDefault(AddressBook addressBook);

    Result<AddressBook> updateAddressBook(AddressBook addressBook);

    /**
     * 查询默认地址
     * @return 默认地址
     */
    Result<AddressBook> getDefault();

    /**
     * 查询指定用户的全部地址
     *
     * @param addressBook 指定用户
     * @return 地址信息
     */
    Result<List<AddressBook>> listAddressBook(AddressBook addressBook);

    /**
     * 删除地址
     *
     * @param ids 地址信息id
     * @return 删除结果
     */
    Result<String> deleteAddressBook(Long ids);
}
