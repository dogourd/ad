package top.ezttf.ad.dao;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.ezttf.ad.exception.AdUserException;
import top.ezttf.ad.service.IUserService;
import top.ezttf.ad.vo.CreateUserRequest;
import top.ezttf.ad.vo.CreateUserResponse;

/**
 * @author yuwen
 * @date 2019/1/28
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DaoTest {

    @Autowired
    private IUserService iUserService;

    @Test
    public void testAdUserRepository() {
        try {
            CreateUserResponse user = iUserService.createUser(new CreateUserRequest("大胖"));
            log.debug("test create user success!");
        } catch (AdUserException e) {
            log.error("test create user error {}", e);
        }
    }
}
