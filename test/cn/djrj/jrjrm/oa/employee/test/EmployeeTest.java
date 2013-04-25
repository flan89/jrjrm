package cn.djrj.jrjrm.oa.employee.test;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.djrj.jrjrm.model.Employee;
import cn.djrj.jrjrm.oa.service.employee.EmployeeService;
import cn.djrj.jrjrm.util.QueryResult;

/**
 * 测试类
 * @author lynn
 */
public class EmployeeTest {
	
	private ClassPathXmlApplicationContext ctx;
	private Logger logger;
	
	@Before
	public void before(){
		ctx=new ClassPathXmlApplicationContext("beans.xml");
		logger=Logger.getLogger(EmployeeTest.class);
	}
	
	@Test
	public void testSave(){
		
		EmployeeService service= (EmployeeService) ctx.getBean("employeeServiceBean");
		Employee employee=new Employee("flan1", "888999", "027");
		service.save(employee);
		Employee employee2=new Employee("flan2", "888999", "027");
		service.save(employee2);
		Employee employee3=new Employee("flan3", "888999", "027");
		service.save(employee3);
		Employee employee4=new Employee("flan4", "888999", "027");
		service.save(employee4);
		
	}
	
	@Test
	public void testDelete(){
		EmployeeService service= (EmployeeService) ctx.getBean("employeeServiceBean");
		
		service.delete(Employee.class, new Long(1));
	}
	
	@Test
	public void testDeleteIDs(){
		EmployeeService service= (EmployeeService) ctx.getBean("employeeServiceBean");
		
		Object[] o=new Object[]{new Long(1),new Long(2),new Long(3)};
		
		service.delete(Employee.class, o);
	}
	
	@Test
	public void testFind(){
		
		EmployeeService service= (EmployeeService) ctx.getBean("employeeServiceBean");
		Employee e= service.find(Employee.class, new Long(2));
		System.out.println(e.getUserName());
	}
	
	@Test
	public void testPageing(){
		
		EmployeeService service= (EmployeeService) ctx.getBean("employeeServiceBean");
		QueryResult<Employee> ess=service.getScrollData(Employee.class, 0, 3, null, null, null);
		for(Employee e : ess.getResultList()){
			System.out.println(e.getUserName());
		}
		System.out.println(ess.getTotalRecord());
	}
	
	@Test
	public void testGetSingleDate(){
		EmployeeService service= (EmployeeService) ctx.getBean("employeeServiceBean");
		String whereStr=" o.userName=? and o.passWord=?";
		ArrayList<Object> os=new ArrayList<Object>();
		os.add("flan1");
		os.add("888999");
		
		Employee e=service.getSingleDate(Employee.class, whereStr, os);
		
		logger.info(e.getUserName());
	}

	
}


















