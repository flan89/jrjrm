package cn.djrj.jrjrm.util;

import org.apache.struts2.ServletActionContext;

import cn.djrj.jrjrm.model.Employee;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class LoginInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = -446680554268702946L;

	@Override
	protected String doIntercept(ActionInvocation invock) throws Exception {
		
		Employee employee=(Employee) ServletActionContext.getRequest().getSession().getAttribute("employee");
		
		if(employee!=null){
			return invock.invoke();	//责任链模式继续调用其他的拦截器
		}else {
			ServletActionContext.getResponse().sendRedirect("/jrjrm/login.jsp");
			//return Action.LOGIN;				//返回登录页面
			return null;						//返回登录页面
		}
		
	}

}
