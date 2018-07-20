package com.ch999.express;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.ch999.express.admin.component.HandleStaticJson;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.Recharge;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.mapper.UserInfoMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
import com.ch999.express.admin.service.RechargeService;
import com.ch999.express.admin.task.PickUpTimeTask;
import com.ch999.express.admin.vo.ExpressVO;
import com.ch999.express.common.MapTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpressApplicationTests {

	@Resource
	private HandleStaticJson handleStaticJson;

	@Resource
	private UserInfoMapper userInfoMapper;

	@Resource
	private UserWalletBORepository userWalletBORepository;

	@Resource
	private RechargeService rechargeService;

//	@Test
//	public void contextLoads() throws IOException{
//		/*List<ExpressVO> nefuExpressPoint = handleStaticJson.getNefuExpressPoint();
//		nefuExpressPoint.get(1).getAddress();
//		System.out.println();*/
//		//List<UserInfo> test = userInfoMapper.test();
//		//List<ExpressVO> address = MapTools.getAddressByKeyWord("东北林业大学");
//		//MapTools.getDistanceByPosition("45.736677,126.670352", "45.720472871,126.6417633");
//		//List<Map<String, Object>> simpleExpressPoint = handleStaticJson.getSimpleExpressPoint();
//		//System.out.println();
//		//userWalletBORepository.save(new UserWalletBO(1));
//		//erWalletBORepository.deleteAll();
//		UserWalletBO one = new UserWalletBO();
//		one.setUserId(3);
//		one.setBalance(10.0);
//		one.setIntegral(0);
//		one.setCreditNum(100);
//		userWalletBORepository.save(one);
//	}
//
//	@Test
//	public void addRecharge(){
//		for(int i = 0; i < 100; i++){
//            double random = Math.random();
//            Recharge recharge = new Recharge(IdWorker.get32UUID());
//            if(random < 0.25){
//				recharge.setPrice(50.0);
//			}else if(random >= 0.25 && random < 0.5){
//                recharge.setPrice(30.0);
//            }else if(random >= 0.5 && random < 0.75){
//                recharge.setPrice(20.0);
//            }else {
//                recharge.setPrice(10.0);
//            }
//            rechargeService.insert(recharge);
//		}
//	}
	@Test
	public void addRecharge(){
		/*UserWalletBO one = userWalletBORepository.findOne(5);
		one.setCreditNum(100);
		userWalletBORepository.save(one);*/
	}
}
