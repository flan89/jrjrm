package cn.djrj.jrjrm.oa.action.employee;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.djrj.jrjrm.model.Employee;
import cn.djrj.jrjrm.oa.service.employee.EmployeeService;
import cn.djrj.jrjrm.util.QueryResult;
import cn.djrj.jrjrm.util.VerificationCode;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 雇员管理 控制层 Action
 * 
 * @scope 属性配不配都是原型（prototype）的，
 * 		      即使显示的配成单例（singleton）的也不起效
 * 		      可以使用 init-method属性测试
 * @author lynn
 */
@Controller
@Scope("prototype")
public class EmployeeAction extends ActionSupport{
	
	private static final long serialVersionUID = 2929238253438816398L;
	private EmployeeService service;
	private static Logger logger=Logger.getLogger(EmployeeAction.class);
	
	@Resource(name="employeeServiceBean")
	public void setService(EmployeeService service) {
		this.service = service;
	}
	
	@PostConstruct
	public void init(){
		logger.info("init  start ·····");
	}

	public void showList(){
		
		
		QueryResult<Employee> qr=service.getScrollData(Employee.class, -1, -1);
		
		List<Employee> employees=qr.getResultList();
		
		for(Employee e : employees){
			System.out.println(e.getUserName());
		}
		
	}
	
	public void showCodeImg() throws IOException{
		
		VerificationCode vcode=new VerificationCode();
		
		logger.info(vcode.getImgCode(ServletActionContext.getResponse()));
		
	}
	
	@PreDestroy
	public void destroy(){
		System.out.println("start destroy·····");
	}
	
}















