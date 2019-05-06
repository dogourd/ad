package top.ezttf.ad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ezttf.ad.constant.Constants;
import top.ezttf.ad.dao.AdUserRepository;
import top.ezttf.ad.dao.CreativeRepository;
import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.exception.CreativeException;
import top.ezttf.ad.pojo.AdUser;
import top.ezttf.ad.pojo.Creative;
import top.ezttf.ad.service.ICreativeService;
import top.ezttf.ad.vo.CreativeRequest;
import top.ezttf.ad.vo.CreativeResponse;

import java.util.Optional;

/**
 * @author yuwen
 * @date 2019/1/22
 */
@Service
public class CreativeServiceImpl implements ICreativeService {

    private final CreativeRepository creativeRepository;

    private final AdUserRepository adUserRepository;

    @Autowired
    public CreativeServiceImpl(CreativeRepository creativeRepository, AdUserRepository adUserRepository) {
        this.creativeRepository = creativeRepository;
        this.adUserRepository = adUserRepository;
    }

    @Override
    public CreativeResponse createCreative(CreativeRequest request) throws AdException {
        if (!request.isValidate()) {
            throw new CreativeException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional<AdUser> adUser = adUserRepository.findById(request.getUserId());
        if (!adUser.isPresent()) {
            throw new CreativeException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        Creative creative = creativeRepository.save(request.convertToEntity());
        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
