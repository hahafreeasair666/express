package com.ch999.express.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.mapper.UserInfoMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.UserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ch999.express.admin.vo.CenterVO;
import com.ch999.express.admin.vo.MyCenterVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2018-04-02
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserWalletBORepository userWalletBORepository;

    @Override
    public Boolean checkCanUse(String type, String info) {
        return this.selectOne(new EntityWrapper<UserInfo>().eq(type,info)) == null;
    }

    @Override
    public Boolean insertUser(String userName, String pwd, String mobile) {
        return this.insert(new UserInfo(userName,pwd,mobile));
    }

    @Override
    public CenterVO getCenterInfo(Integer userId,UserInfo userInfo) {
        CenterVO centerVO;
        if(userId != null){
            centerVO = new CenterVO();
            UserWalletBO one = userWalletBORepository.findOne(userId);
            UserInfo userInfo1 = this.selectById(userId);
            centerVO.setUserId(userId);
            centerVO.setAvatar(userInfo1.getAvatar());
            centerVO.setUserName(userInfo1.getUserName());
            centerVO.setCreditNum(one.getCreditNum());
        }else {
            Map<String, Object> stringIntegerMap = userInfoMapper.selectCenterInfo(userInfo.getId());
            UserWalletBO one = userWalletBORepository.findOne(userInfo.getId());
            centerVO = new MyCenterVO();
            centerVO.setUserId(userInfo.getId());
            centerVO.setAvatar(userInfo.getAvatar());
            centerVO.setUserName(userInfo.getUserName());
            centerVO.setCreditNum(one.getCreditNum());
            ((MyCenterVO) centerVO).setMyOrder(stringIntegerMap.get("orderNum").toString());
            ((MyCenterVO) centerVO).setMyPickUp(stringIntegerMap.get("pickUpNum").toString());
            ((MyCenterVO) centerVO).setMyBalance(one.getBalance());
            ((MyCenterVO) centerVO).setMyIntegral(one.getIntegral());
        }
        return centerVO;
    }
}
