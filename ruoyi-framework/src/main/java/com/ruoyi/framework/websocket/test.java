package com.ruoyi.framework.websocket;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: yangyang
 * @Date: 2022/12/08/12:08
 * @Description:
 */
@RestController
@RequestMapping("/websocket")
public class test {
@Autowired
private WebSocketService webSocketService;
@Autowired
private SysUserMapper sysUserMapper;
    @GetMapping(value = "/{id}")
    public void getInfo(@PathVariable("id") String id)
    {
        List<SysUser> list = sysUserMapper.findUserList(0L);
        for (SysUser user : list) {
            String s = JSON.toJSONString(id);
            webSocketService.sendMessage(user.getUserName(),s);
        }


    }
}
