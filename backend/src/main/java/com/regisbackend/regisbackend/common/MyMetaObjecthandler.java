package com.regisbackend.regisbackend.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 *
 * @author 喵vamp
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    /**
     * 插入数据时自动填充公共字段
     *
     * @param metaObject 当前对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]" + metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", MyBaseContext.getCurrentId());
        metaObject.setValue("updateUser", MyBaseContext.getCurrentId());
    }

    /**
     * 更新数据时自动填充公共字段
     *
     * @param metaObject 当前对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]" + metaObject.toString());

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", MyBaseContext.getCurrentId());
    }
}
