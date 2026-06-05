package com.bookmark.service;

import com.bookmark.entity.User;
import javax.servlet.http.HttpSession;

/**
 * 用户服务接口
 * @author 开发者
 */
public interface IUserService {

    /**
     * 用户注册
     * @param user 用户对象
     * @return 注册结果
     */
    String register(User user);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param session HttpSession
     * @return 登录结果
     */
    String login(String username, String password, HttpSession session);

    /**
     * 用户注销
     * @param session HttpSession
     */
    void logout(HttpSession session);

    /**
     * 获取当前登录用户
     * @param session HttpSession
     * @return 用户对象
     */
    User getCurrentUser(HttpSession session);
}