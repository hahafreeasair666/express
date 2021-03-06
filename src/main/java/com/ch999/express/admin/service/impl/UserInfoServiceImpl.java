package com.ch999.express.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.ExpressComment;
import com.ch999.express.admin.entity.ExpressOrder;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.mapper.UserInfoMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.ExpressCommentService;
import com.ch999.express.admin.service.ExpressOrderService;
import com.ch999.express.admin.service.UserInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ch999.express.admin.vo.CenterInfoMapperVO;
import com.ch999.express.admin.vo.CenterVO;
import com.ch999.express.admin.vo.MyCenterVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
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
    private ExpressCommentService expressCommentService;

    @Resource
    private UserWalletBORepository userWalletBORepository;

    @Override
    public Boolean checkCanUse(String type, String info) {
        return this.selectOne(new EntityWrapper<UserInfo>().eq(type, info)) == null;
    }

    @Override
    public Boolean insertUser(String userName, String pwd, String mobile) {
        return this.insert(new UserInfo(userName, pwd, mobile));
    }

    @Override
    public CenterVO getCenterInfo(Integer userId, UserInfo userInfo) {
        CenterVO centerVO;
        if (userId != null) {
            centerVO = new CenterVO();
            UserWalletBO one = userWalletBORepository.findOne(userId);
            UserInfo userInfo1 = this.selectById(userId);
            if(userInfo1 == null){
                return new CenterVO();
            }
            centerVO.setUserId(userId);
            centerVO.setAvatar(userInfo1.getAvatar());
            centerVO.setUserName(userInfo1.getUserName());
            centerVO.setCreditNum(one.getCreditNum());
            handleSource(centerVO,userId);
        } else {
            UserWalletBO one = userWalletBORepository.findOne(userInfo.getId());
            centerVO = new MyCenterVO();
            centerVO.setUserId(userInfo.getId());
            centerVO.setAvatar(userInfo.getAvatar());
            centerVO.setUserName(userInfo.getUserName());
            centerVO.setCreditNum(one.getCreditNum());
            ((MyCenterVO) centerVO).setMyBalance(one.getBalance());
            ((MyCenterVO) centerVO).setMyIntegral(one.getIntegral());
            Map<String, Object> myOrder = new HashMap<>();
            Map<String, Object> myPickUp = new HashMap<>();
            ((MyCenterVO) centerVO).setMyOrder(myOrder);
            ((MyCenterVO) centerVO).setMyPickUp(myPickUp);
            CenterInfoMapperVO centerInfoMapperVO = userInfoMapper.selectCenterInfo(userInfo.getId());
            myOrder.put("noPay",centerInfoMapperVO.getNotPay());
            myOrder.put("noOrder",centerInfoMapperVO.getNoOrder());
            myOrder.put("transport",centerInfoMapperVO.getTransport());
            myOrder.put("noComment",centerInfoMapperVO.getNotComment());
            myOrder.put("complete",centerInfoMapperVO.getComplete1());
            myPickUp.put("noComplete",centerInfoMapperVO.getNoComplete());
            myPickUp.put("complete",centerInfoMapperVO.getComplete2());
            handleSource(centerVO,userInfo.getId());
        }
        return centerVO;
    }

    private void handleSource(CenterVO centerVO,Integer userId){
        List<ExpressComment> commentList = expressCommentService.selectList(new EntityWrapper<ExpressComment>().eq("user_id", userId));
        if(CollectionUtils.isNotEmpty(commentList)){
            centerVO.setCommentCount(commentList.size());
            double score = 0.0;
            for (ExpressComment li : commentList) {
                score += li.getStar();
            }
            centerVO.setScore(score/commentList.size());
        }else {
            centerVO.setCommentCount(0);
            centerVO.setScore("暂无评分");
        }
    }
}
