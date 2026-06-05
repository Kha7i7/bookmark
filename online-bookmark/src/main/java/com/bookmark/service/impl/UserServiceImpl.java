package com.bookmark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookmark.entity.User;
import com.bookmark.mapper.UserMapper;
import com.bookmark.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.servlet.http.HttpSession;

/**
 * 用户服务实现类
 * @author 开发者
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册新用户
     */
    @Override
    public String register(User user) {
        // 1. 验证用户名是否已存在
        User existingUser = userMapper.selectByUsername(user.getUsername());
        if (existingUser != null) {
            return "用户名已存在！";
        }

        // 2. 验证邮箱是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        if (userMapper.selectCount(queryWrapper) > 0) {
            return "邮箱已被注册！";
        }

        // 3. 密码加密（MD5）
        String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(encryptedPassword);

        // 4. 插入数据库
        int result = userMapper.insert(user);
        return result > 0 ? "注册成功！" : "注册失败，请重试！";
    }

    /**
     * 用户登录
     */
    @Override
    public String login(String username, String password, HttpSession session) {
        // 1. 查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return "用户名不存在！";
        }

        // 2. 验证密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            return "密码错误！";
        }

        // 3. 登录成功，保存Session
        session.setAttribute("currentUser", user);
        return "登录成功！";
    }

    /**
     * 用户注销
     */
    @Override
    public void logout(HttpSession session) {
        session.removeAttribute("currentUser");
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }
}