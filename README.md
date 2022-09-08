# Regis
# 外卖点单系统

## 1.项目说明

-   采用SpringBoot、MyBatis-Plus框架，开发的前后端分离外卖点单系统，支持Redis缓存，使用nginx实现前后端分离
-   后台地址：http://192.168.119.133:8080/backend/index.html
-   





## 2.目录结构



## 3.项目特点



## 4.技术选型

-   核心框架：Spring Boot 2.6.6
-   持久层框架：MyBatis Plus 3.4.2
-   数据库连接池：Druid 1.2
-   日志管理：Log
-   页面交互：Vue2.x





## 5.开发环境



|      开发工具      |       说明        |
| :----------------: | :---------------: |
|        IDEA        |  Java开发工具IDE  |
|      Navicat       | MySQL远程连接工具 |
| RESP-GUI for Redis | Redis远程连接工具 |
|     FinalShell     | Linux远程连接工具 |





| 开发环境 |  版本  |
| :------: | :----: |
|   JDK    |  1.8   |
|  MySQL   | 8.0.26 |
|  Redis   | 6.0.5  |
|  Maven   |  3.8   |



注：查看技术版本

```sh
jdk:
java,javac,java -version
mavne：
maven -version
git:
git --version
```





## 6.项目截图



## 7.部署项目

### 部署架构

![image-20220905093805260](D:\Notes\prcture\image-20220905093805260.png)



### 部署环境说明

**服务器：**

-   192.168.119.130

    ​				//Nginx:部署前端项目，配置反向代理

    ​				Redis：缓存中间件

-   192.168.119.132

    ​			Mysql：主从复制的主库

-   192.168.119.133

    ​			jdk：运行Java项目

    ​			git：版本控制工具

    ​			maven：项目构建工具

    ​			Mysql：主从复制的从库



### 部署步骤

服务器部署：

-   -   192.168.119.130

        启动docker

        ```sh
        systemctl stop firewalld
        systemctl start docker
        ```

    -    运行nginx,redis容器

    -             ```sh
        docker ps -a
        docker start id
                  ```



-   -   192.168.119.132

    -   启动Mysql

    -   ```sh
        systemctl stop firewalld
        systemctl start mysqld   
        ```

        

-   192.168.119.133

    -   ​	启动MySQL

            ```sh
        systemctl stop firewalld
        systemctl start mysqld
            ```

    -   启动主从复制

    -   ```sh
        mysql -uroot -p
        stop slave;          //如果之前配置过主从复制
        start slave;
        ```

    -   查看是否配置成功

    -   ```sh
        show slave status \G
        ```

    -   <img src="D:\Notes\prcture\image-20220905085724623.png" alt="image-20220905085724623" style="zoom:150%;" />



注：数据库变化时重新配置

主库mysql执行

```sql
show master status;
```

得到：

![image-20220905085134945](D:\Notes\prcture\image-20220905085134945.png)



从库mysql执行

```sql
CHANGE MASTER TO
MASTER_HOST='192.168.119.132',
MASTER_USER='slave',
MASTER_PASSWORD='slave',
MASTER_LOG_FILE='mysql-bin.000005',
MASTER_LOG_POS=156;
```



启动后端：

'192.168.119.133' 服务器：

```sh
cd /tmp/run
java -jar reggie_take_out-1.0-SNAPSHOT.jar
```

浏览器地址：http://192.168.119.130:8080/#/login



启动前端

http://192.168.119.133:8080/front/index.html

http://192.168.119.133:8080/backend/index.html

