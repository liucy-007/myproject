package com.inspur.db.controller;

import com.alibaba.fastjson.JSONObject;
import com.inspur.db.entity.User;
import com.inspur.db.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WX0liucy
 */
@RestController
@RequestMapping(value = "/user", produces = {"application/json;charset=UTF-8"})
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            userService.addUser(user);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "添加失败");
        }
        return result;
    }

    /*  @CrossOrigin跨源资源共享,2个参数：origins  ： 允许可访问的域列表 maxAge：准备响应前的缓存持续的最大时间（以秒为单位）。*/
    /**
     * login web
     * @param user user information
     * @return ResponseEntity
     * @throws Exception Exception
     */
    @GetMapping("/denglu")
    @CrossOrigin(origins = "*", maxAge = 3000)
    public ResponseEntity denglu(@RequestBody User user) throws Exception {
        JSONObject resultJson = new JSONObject();
        String passwordByUsername = userService.getPasswordByUsername(user.getUsername());
        log.info("调用参数[--user:{}--]", user);
        if (null == passwordByUsername) {
            resultJson.put("messqge", "用户名错误");
        } else if (!user.getPassword().equals(passwordByUsername)) {
            resultJson.put("messqge", "密码错误");
        }
        resultJson.put("success", true);
        return new ResponseEntity<>(resultJson.toJSONString(), HttpStatus.OK);
    }

}

