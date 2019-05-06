package top.ezttf.ad.service;

import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.vo.CreativeRequest;
import top.ezttf.ad.vo.CreativeResponse;

/**
 * @author yuwen
 * @date 2019/1/22
 */
public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request) throws AdException;


}
