package top.ezttf.ad.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.service.IUserService;
import top.ezttf.ad.vo.CreateUserRequest;
import top.ezttf.ad.vo.CreateUserResponse;

/**
 * @author yuwen
 * @date 2019/1/22
 */
@Slf4j
@RestController
public class UserController {

    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) throws AdException {
        log.info("ad-sponsor: createUser -> {}", JSON.toJSONString(request));
        return iUserService.createUser(request);
    }
}
