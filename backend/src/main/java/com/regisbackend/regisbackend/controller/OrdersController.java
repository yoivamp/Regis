package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.OrderDetail;
import com.regisbackend.regisbackend.pojo.Orders;
import com.regisbackend.regisbackend.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 喵vamp
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     *
     * @param orders 订单信息
     * @return 下单结果
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders) {
        return ordersService.submitOrders(orders);
    }

    @GetMapping("/userPage")
    public Result<Page<OrderDetail>> page(int page, int pageSize){
        return ordersService.pageOrderDetail(page,pageSize);
    }
}
