package com.xdx.common.common;

import com.github.pagehelper.PageHelper;
import com.xdx.entitys.pojo.SyUser;
import com.xdx.mapper.user.SyUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这里面封装一些Service公用的方法
 */
@Component
public class MyCommonService {

    @Autowired
    private SyUserMapper userMapper;

    private static Map<String,SyUser> userMap = new ConcurrentHashMap<>(1000);

    /**
     * 使用pageHelper 默认当前页 10
     * @param pageNum
     */
    public void startPage(int pageNum){
        startPage( pageNum,  10);
    }
    public void startPage(int pageNum, int pageSize){
        PageHelper.startPage( pageNum,  pageSize);
    }

    /**
     * 获取当前用户
     */
    public SyUser getCurUser(){
        SyUser user = userMap.get(getToken());
        if (user == null){
            user = userMapper.selectOne(new SyUser().setWxOpenId(getToken()));
            userMap.put(user.getWxOpenId(), user);
        }
        if (userMap.size() > 10000){
            userMap.clear();
        }
        return user;
    }

    /**
     * 获取token
     *
     * @return 获取Session
     * @author 小道仙
     * @date 2020年5月5日
     */
    public String getToken(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getHeader("token");
        return token;
    }
}
