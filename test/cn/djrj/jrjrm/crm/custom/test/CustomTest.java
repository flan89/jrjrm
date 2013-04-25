package cn.djrj.jrjrm.crm.custom.test;

import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.djrj.jrjrm.crm.service.custom.CustomManageService;
import cn.djrj.jrjrm.model.CusRightsGroup;
import cn.djrj.jrjrm.model.CusRightsInfo;
import cn.djrj.jrjrm.oa.employee.test.EmployeeTest;

public class CustomTest {
	
	private ClassPathXmlApplicationContext ctx;
	private Logger logger;
	
	@Before
	public void before(){
		ctx=new ClassPathXmlApplicationContext("beans.xml");
		logger=Logger.getLogger(EmployeeTest.class);
	}
	
	
	@Test
	public void testOneToMany(){
		CustomManageService service=(CustomManageService) ctx.getBean("customManageServiceBean");
		
		CusRightsGroup crg=service.find(CusRightsGroup.class, 1);
		
		logger.debug(crg.getName());
		
		for(CusRightsInfo cri : crg.getRightsInfo()){
			logger.debug(cri.getRightsName());
		}
		
	}
	
	@Test
	public void testFindRightsGroupName(){
		CustomManageService service=(CustomManageService) ctx.getBean("customManageServiceBean");
		List<String> list=service.findRightsGroupName(1l);
		JSONArray json=JSONArray.fromObject(list);
		logger.info(json.toString());
	}
	
	@Test
	public void testGetTodayLoginCus(){
		CustomManageService service=(CustomManageService) ctx.getBean("customManageServiceBean");
		String count=service.showTodayLoginCus("27");
		logger.info(count);
	}
	
	/**
	 * 恢复数据中误操作删除掉的 用户权限数据 （不要在使用）
	 */
	@Test
	public void recoverDatabase(){
		
		/*CustomManageService service=(CustomManageService) ctx.getBean("customManageServiceBean");
		List<String> names= service.getUserNamefromUserInfo();
		
		for(String name : names){
			System.out.println(name);
			service.geivRights(name, "认证权限");
		}*/
		
	}
	
}















