package top.ezttf.ad.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ezttf.ad.constant.Constants;
import top.ezttf.ad.dao.AdUserRepository;
import top.ezttf.ad.exception.AdUserException;
import top.ezttf.ad.pojo.AdUser;
import top.ezttf.ad.service.IUserService;
import top.ezttf.ad.util.CommonUtils;
import top.ezttf.ad.vo.CreateUserRequest;
import top.ezttf.ad.vo.CreateUserResponse;

/**
 * @author yuwen
 * @date 2019/1/20
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final AdUserRepository adUserRepository;

    @Autowired
    public UserServiceImpl(AdUserRepository adUserRepository) {
        this.adUserRepository = adUserRepository;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public CreateUserResponse createUser(CreateUserRequest request) throws AdUserException {
        if (!request.validate()) {
            throw new AdUserException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdUser oldUser = adUserRepository.findByUsername(request.getUsername());
        if (oldUser != null) {
            throw new AdUserException(Constants.ErrorMsg.SAME_NAME_USER_ERROR);
        }
        AdUser newUser = adUserRepository.save(
                new AdUser(request.getUsername(), CommonUtils.sha512(request.getUsername()))
        );
        return new CreateUserResponse(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getToken(),
                newUser.getCreateTime(),
                newUser.getUpdateTime()
        );
    }
}
