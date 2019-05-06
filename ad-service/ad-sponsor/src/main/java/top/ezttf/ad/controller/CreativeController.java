package top.ezttf.ad.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.service.ICreativeService;
import top.ezttf.ad.vo.CreativeRequest;
import top.ezttf.ad.vo.CreativeResponse;

/**
 * @author yuwen
 * @date 2019/1/22
 */
@Slf4j
@RestController
public class CreativeController {

    private final ICreativeService iCreativeService;

    @Autowired
    public CreativeController(ICreativeService iCreativeService) {
        this.iCreativeService = iCreativeService;
    }


    @PostMapping("/create/creative")
    public CreativeResponse createCreative(@RequestBody CreativeRequest request) throws AdException {
        log.info("ad-sponsor: createCreative -> {}", JSON.toJSONString(request));
        return iCreativeService.createCreative(request);
    }
}
