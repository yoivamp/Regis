package com.regisbackend.regisbackend.dto;


import com.regisbackend.regisbackend.pojo.OrderDetail;
import com.regisbackend.regisbackend.pojo.Orders;
import lombok.Data;

import java.util.List;


/**
 * @author 喵vamp
 */
@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
