package cn.djrj.jrjrm.crm.action.custom;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.djrj.jrjrm.crm.dto.custom.CustomLoginLogDto;
import cn.djrj.jrjrm.crm.service.custom.CustomManageService;
import cn.djrj.jrjrm.model.CusLoginLog;
import cn.djrj.jrjrm.model.Employee;
import cn.djrj.jrjrm.oa.action.employee.EmployeeAction;
import cn.djrj.jrjrm.util.QueryResult;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 处理 客户登录日志查询的  控制层
 * @author lynn
 */
@Controller
@Scope("prototype")
public class CustomLoginLogAction extends ActionSupport implements ServletResponseAware,ModelDriven<CustomLoginLogDto>{
	
	private static final long serialVersionUID = 2351794889180325757L;
	private static Logger logger=Logger.getLogger(EmployeeAction.class);
	private HttpServletResponse response;
	private CustomManageService service;
	private CustomLoginLogDto dto=new CustomLoginLogDto();
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	@Resource(name="customManageServiceBean")
	public void setService(CustomManageService service) {
		this.service = service;
	}
	@Override
	public CustomLoginLogDto getModel() {
		return this.dto;
	}
	
	/**
	 * 显示所有用户登录日志的分页列表
	 * @throws ParseException 
	 */
	public void showCusLoginLog() throws ParseException{
		
		response.setContentType("text/plain; charset=UTF-8");
		
		Employee employee=(Employee) ServletActionContext.getRequest().getSession().getAttribute("employee");
		
		if(employee==null || employee.getCompanyId().trim().equals("")){
			logger.info("未登录的用户，不允许进行相应操作");
			return;
		}
		String companyId=employee.getCompanyId().trim();
		
		StringBuffer where=new StringBuffer(" o.userName in (select c.userName from cn.djrj.jrjrm.model.Custom c where c.clientFrom like ?) ");
		ArrayList<Object> queryParames=new ArrayList<Object>();
		queryParames.add("%"+companyId+"%");
		String userName=dto.getUserName();
		String oType=dto.getInOut();
		String osTime=dto.getOsTime();
		String oeTime=dto.getOeTime();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(userName!=null && !userName.equals("")){
			userName=userName.trim();
			where.append(" and o.userName like ? ");
			queryParames.add("%"+userName+"%");
		}
		if(oType!=null && !oType.equals("")){
			Boolean type=Boolean.valueOf(oType);
			where.append(" and o.operateType=?");
			queryParames.add(type);
		}
		if(osTime!=null && !osTime.equals("")){
			Date sTime=df.parse(osTime+" 00:00:00");
			where.append(" and o.operateTime>=?");
			queryParames.add(sTime);
		}
		if(oeTime!=null && !oeTime.equals("")){
			Date eTime=df.parse(oeTime+" 23:59:59");
			where.append(" and o.operateTime<=?");
			queryParames.add(eTime);
		}
		String whereStr=where.toString();
		
		
		LinkedHashMap<String, String> orderBy=new LinkedHashMap<String, String>();		
		orderBy.put("operateTime ", "desc");
		
		QueryResult<CusLoginLog> qr=service.getScrollData(CusLoginLog.class,(dto.getPage()-1)*dto.getRows(), dto.getRows(), whereStr, queryParames, orderBy);
		
		Map<String,Object> jsonMap=new HashMap<String, Object>();
		jsonMap.put("total", qr.getTotalRecord());
		jsonMap.put("rows", qr.getResultList());
		JSONObject json=JSONObject.fromObject(jsonMap);
		
		logger.debug("查询出的分页结果："+json.toString());
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write(json.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(">> 出现IO异常  <<");
		}finally{
			if(out!=null)out.close();
		}
		
	}
	
	/**
	 * 显示今日登录用户
	 */
	public void showTodayLoginCus(){
		
		response.setContentType("text/plain; charset=UTF-8");
		
		Employee employee=(Employee) ServletActionContext.getRequest().getSession().getAttribute("employee");
		
		if(employee==null || employee.getCompanyId().trim().equals("")){
			logger.info("未登录的用户，不允许进行相应操作");
			return;
		}
		String companyId=employee.getCompanyId().trim();
		
		String number=service.showTodayLoginCus(companyId);
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write(number);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(">> 出现IO异常  <<");
		}finally{
			if(out!=null)out.close();
		}
	}
	
}




















