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
			return invock.invoke();	//������ģʽ��������������������
		}else {
			ServletActionContext.getResponse().sendRedirect("/jrjrm/login.jsp");
			//return Action.LOGIN;				//���ص�¼ҳ��
			return null;						//���ص�¼ҳ��
		}
		
	}

}
