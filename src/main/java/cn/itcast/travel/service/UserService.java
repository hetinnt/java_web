package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册用户
     * @param user
     * @return
     */
    boolean regist(User user);

    /**
     * 激活用户
     * @param code 激活码
     * @return
     */
    boolean active(String code);

    /**
     * 登录方法
     * @param user 存储用户名密码
     * @return
     */
    User login(User user);
}
