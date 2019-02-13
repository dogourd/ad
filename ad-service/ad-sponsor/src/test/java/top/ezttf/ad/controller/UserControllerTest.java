package top.ezttf.ad.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import top.ezttf.ad.exception.AdUserException;
import top.ezttf.ad.service.IUserService;
import top.ezttf.ad.vo.CommonResponse;
import top.ezttf.ad.vo.CreateUserRequest;
import top.ezttf.ad.vo.CreateUserResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuwen
 * @date 2019/2/13
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private RestTemplate template;

    @Autowired
    private IUserService iUserService;

    @Test
    public void testCreateUser() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "qinyi");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);
        CommonResponse response = template.postForEntity("http://127.0.0.1:7000/ad-sponsor/create/user",
                entity,
                CommonResponse.class).getBody();
        log.debug(JSON.toJSONString(response));
    }

    @Test
    public void testService() throws AdUserException {
        CreateUserResponse response = iUserService.createUser(new CreateUserRequest(
                "qinyi"
        ));
        log.debug(JSON.toJSONString(response));
    }
}
