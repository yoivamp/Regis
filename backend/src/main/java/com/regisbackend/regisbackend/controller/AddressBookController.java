package com.regisbackend.regisbackend.controller;

import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.AddressBook;
import com.regisbackend.regisbackend.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 *
 * @author 喵vamp
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param addressBook 地址信息
     * @return 新增结果
     */
    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook) {
        return addressBookService.saveAddressBook(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook 地址信息
     * @return 设置结果
     */
    @PutMapping("default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        return addressBookService.setDefault(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return Result.success(addressBook);
        } else {
            return Result.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     *
     * @return 默认地址
     */
    @GetMapping("default")
    public Result<AddressBook> getDefault() {
        return addressBookService.getDefault();
    }

    /**
     * 查询指定用户的全部地址
     *
     * @param addressBook 指定用户
     * @return 地址信息
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook) {
        return addressBookService.listAddressBook(addressBook);
    }
}
