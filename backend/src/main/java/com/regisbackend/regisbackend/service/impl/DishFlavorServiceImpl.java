package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.dao.DishFlavorMapper;
import com.regisbackend.regisbackend.pojo.DishFlavor;
import com.regisbackend.regisbackend.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 喵vamp
 */
@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
