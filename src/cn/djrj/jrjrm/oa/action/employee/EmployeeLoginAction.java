package cn.djrj.jrjrm.oa.action.employee;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.djrj.jrjrm.model.Employee;
import cn.djrj.jrjrm.oa.service.employee.EmployeeService;
import cn.djrj.jrjrm.util.VerificationCode;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 控制雇员登录操作
 */
@Controller
@Scope("prototype")
public class EmployeeLoginAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	
	private static final long serialVersionUID = -323247126756144669L;
	private String userName;
	private String passWord;
	private String varifyCode;
	private EmployeeService service;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getVarifyCode() {
		return varifyCode;
	}
	public void setVarifyCode(String varifyCode) {
		this.varifyCode = varifyCode;
	}
	
	@Resource(name="employeeServiceBean")
	public void setService(EmployeeService service) {
		this.service = service;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	
	/**
	 * 简单用户登录，不带单点登录和限制重复登录功能
	 * @return
	 */
	public String simpleLogin(){
		
		String vCode=(String) request.getSession().getAttribute("varifyCode");
		
		if(varifyCode!=null && !varifyCode.trim().equals("") && varifyCode.equals(vCode.toLowerCase())){
			if(userName!=null && !userName.trim().equals("") && passWord!=null && !passWord.trim().equals("")){
				
				String whereStr=" o.userName=? and o.passWord=?";
				ArrayList<Object> queryParames=new ArrayList<Object>();
				queryParames.add(this.userName);
				queryParames.add(this.passWord);
				Employee employee=service.getSingleDate(Employee.class, whereStr, queryParames);
				if(employee!=null){
					HttpSession session=ServletActionContext.getRequest().getSession();
					session.setAttribute("employee", employee);
					return SUCCESS;
				}else {
					return "false";
				}
			}else {
				return "false";
			}
		}else {
			return "false";
		}
		
	}
	
	public String simpleLogout(){
		
		return SUCCESS;
	}
	
	/**
	 * 显示验证码图片
	 * @throws IOException
	 */
	public void showVarifyCodeImg() throws IOException{
		
		VerificationCode vcode=new VerificationCode();
		HttpSession session=request.getSession();
		
		this.varifyCode=vcode.getImgCode(response);
		session.setAttribute("varifyCode", this.varifyCode);
		
	}
	
	
}

















