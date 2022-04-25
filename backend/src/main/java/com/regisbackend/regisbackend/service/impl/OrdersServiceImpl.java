package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.common.CustomException;
import com.regisbackend.regisbackend.common.MyBaseContext;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.OrderMapper;
import com.regisbackend.regisbackend.dto.OrdersDto;
import com.regisbackend.regisbackend.pojo.*;
import com.regisbackend.regisbackend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
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
        //  orders.setUserName(user.getName());
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

    /**
     * 再来一单
     *
     * @param orders 订单id
     * @return 跳转首页
     */
    @Override
    public Result<String> againOrder(Orders orders) {
        return Result.success("跳转成功");
    }

    /**
     * 分页查询订单
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @return 查询page
     */
    @Override
    public Result<Page<OrdersDto>> pageOrderDetail(int page, int pageSize) {
        //准备三张表的page
        Page<OrdersDto> ordersDtoPage = new Page<>();
        Page<OrderDetail> orderDetailPage = new Page<>(page, pageSize);
        Page<Orders> ordersPage = new Page<>(page, pageSize);

        //查询order表，并赋值给orderDto的page
        LambdaQueryWrapper<Orders> ordersWrapper = new LambdaQueryWrapper<>();
        ordersWrapper.eq(Orders::getUserId, MyBaseContext.getCurrentId());
        this.page(ordersPage, ordersWrapper);
        BeanUtils.copyProperties(ordersPage, ordersDtoPage);

        //查询orderDetai表，并遍历给orderDto设置
        List<Orders> ordersRecords = ordersPage.getRecords();
        List<OrdersDto> ordersDtoList = ordersRecords.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            //根据遍历的order表的id查询orderDetail表
            LambdaQueryWrapper<OrderDetail> detaiWrapper = new LambdaQueryWrapper<>();
            detaiWrapper.eq(OrderDetail::getOrderId, item.getId());
            orderDetailService.page(orderDetailPage, detaiWrapper);
            List<OrderDetail> detailRecords = orderDetailPage.getRecords();
            //设置ordersDto的orderDetail列表
            ordersDto.setOrderDetails(detailRecords);
            return ordersDto;
        }).collect(Collectors.toList());

        //设置ordersDto的page的records
        ordersDtoPage.setRecords(ordersDtoList);
        return Result.success(ordersDtoPage);
    }

    /**
     * 后端管理订单的分页及条件查询
     *
     * @param page      当前页码
     * @param pageSize  每页条目数
     * @param number    订单号
     * @param orderTime 下单时间
     * @param endTime   截止时间
     * @return 查询结果
     */
    @Override
    public Result<Page<OrdersDto>> pageBackend(int page, int pageSize, String number, LocalDateTime orderTime, LocalDateTime endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        //查询order表，并赋值给orderDto的page
        LambdaQueryWrapper<Orders> ordersWrapper = new LambdaQueryWrapper<>();
        //查询订单号
        ordersWrapper.eq(Strings.isNotEmpty(number), Orders::getNumber, number);
        //查询下单时间在所给时间之间
        ordersWrapper.between(orderTime != null && endTime != null, Orders::getOrderTime, orderTime, endTime);
        //查询订单信息
        this.page(ordersPage, ordersWrapper);

        BeanUtils.copyProperties(ordersPage, ordersDtoPage);
        return Result.success(ordersDtoPage);
    }


    /**
     * 派送订单
     *
     * @param orders 订单id和状态
     * @return 派送结果
     */
    @Override
    public Result<String> sendOrder(Orders orders) {
        if (orders.getStatus() < 5) {
            orders.setStatus(orders.getStatus() + 1);
        }
        return this.updateById(orders) ? Result.success("已派送") : null;
    }
}
