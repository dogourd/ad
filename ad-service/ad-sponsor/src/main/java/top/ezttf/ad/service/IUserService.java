package top.ezttf.ad.service;

import top.ezttf.ad.exception.AdUserException;
import top.ezttf.ad.vo.CreateUserRequest;
import top.ezttf.ad.vo.CreateUserResponse;

/**
 * @author yuwen
 * @date 2019/1/20
 */
public interface IUserService {

    /**
     * 创建用户
     * @param request 创建用户请求
     * @return
     * @throws AdUserException
     */
    CreateUserResponse createUser(CreateUserRequest request) throws AdUserException;

}
