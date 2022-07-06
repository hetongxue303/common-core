package com.hetongxue.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hetongxue.system.domain.User;
import com.hetongxue.system.mapper.UserMapper;
import com.hetongxue.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 用户业务实现
 * @ClassNmae: UserServiceImpl
 * @Author: 何同学
 * @DateTime: 2022-07-04 16:47
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> getUserAll() {
        return userMapper.selectList(null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUserByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User getUserByUid(Long uid) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("id", uid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePasswordByUid(User user) {
        return userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserByUid(User user) {
        return userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByUid(Long uid) {
        return userMapper.deleteById(uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(User user) {
        return userMapper.insert(user);
    }

}