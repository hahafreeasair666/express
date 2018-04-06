package com.ch999.express;

import com.ch999.express.admin.component.HandleStaticJson;
import com.ch999.express.admin.document.UserWalletBO;
import com.ch999.express.admin.entity.UserInfo;
import com.ch999.express.admin.mapper.UserInfoMapper;
import com.ch999.express.admin.repository.UserWalletBORepository;
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


@RunWith(SpringRunner.class)
@SpringBootTest
public class ExpressApplicationTests {

	@Resource
	private HandleStaticJson handleStaticJson;

	@Resource
	private UserInfoMapper userInfoMapper;

	@Resource
	private UserWalletBORepository userWalletBORepository;

	@Test
	public void contextLoads() throws IOException{
		/*List<ExpressVO> nefuExpressPoint = handleStaticJson.getNefuExpressPoint();
		nefuExpressPoint.get(1).getAddress();
		System.out.println();*/
		//List<UserInfo> test = userInfoMapper.test();
		//List<ExpressVO> address = MapTools.getAddressByKeyWord("东北林业大学");
		//MapTools.getDistanceByPosition("45.736677,126.670352", "45.720472871,126.6417633");
		//List<Map<String, Object>> simpleExpressPoint = handleStaticJson.getSimpleExpressPoint();
		//System.out.println();
		//userWalletBORepository.save(new UserWalletBO(1));
		UserWalletBO one = userWalletBORepository.findOne(1);
		one.setBalance(100.0);
		one.setIntegral(99999);
		userWalletBORepository.save(one);
	}

}
