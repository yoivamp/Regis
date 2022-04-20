package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.common.CustomException;
import com.regisbackend.regisbackend.common.MyBaseContext;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.OrderMapper;
import com.regisbackend.regisbackend.pojo.*;
import com.regisbackend.regisbackend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 喵vamp
 */
@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrdersService {


    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;


    /**
     * 用户下单
     *
     * @param orders 订单信息
     * @return 下单结果
     */
    @Override
    public Result<String> submitOrders(Orders orders) {
        //获得当前用户id
        Long userId = MyBaseContext.getCurrentId();
        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartWrapper);
        //异常判断
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }
        //查询用户数据
        User user = userService.getById(userId);
        //查询地址数据
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        //异常判断
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不能下单");
        }
        //订单和订单明细表数据设置

        //订单号
        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));


        //向订单表插入数据，一条数据
        this.save(orders);
        //向订单明细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);
        //清空购物车数据
        return shoppingCartService.remove(shoppingCartWrapper) ? Result.success("下单成功") : null;
    }

    @Override
    public Result<Page<OrderDetail>> pageOrderDetail(int page, int pageSize) {
        Page<OrderDetail> orderDetailPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersWrapper = new LambdaQueryWrapper<>();
        ordersWrapper.eq(Orders::getUserId, MyBaseContext.getCurrentId());
        List<Orders> list = this.list(ordersWrapper);
        //查询订单的订单明细页
        list.forEach(item->{
            LambdaQueryWrapper<OrderDetail> detaiWrapper = new LambdaQueryWrapper<>();
            detaiWrapper.eq(OrderDetail::getOrderId, item.getId());
            orderDetailService.page(orderDetailPage, detaiWrapper);
        });
        log.info("detail------->");
        orderDetailPage.getRecords().forEach(System.out::println);
        return Result.success(orderDetailPage);
    }


}
