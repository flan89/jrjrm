package cn.djrj.jrjrm.crm.action.custom;

import it.sauronsoftware.base64.Base64;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.djrj.jrjrm.crm.dto.custom.CustomRightsManageDto;
import cn.djrj.jrjrm.crm.service.custom.CustomManageService;
import cn.djrj.jrjrm.model.CusRightsGroup;
import cn.djrj.jrjrm.model.CusRightsInfo;
import cn.djrj.jrjrm.model.Employee;
import cn.djrj.jrjrm.model.SoftAllRights;
import cn.djrj.jrjrm.oa.action.employee.EmployeeAction;
import cn.djrj.jrjrm.util.QueryResult;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 客户权限管理  处理创建权限组 ，授权等操作
 * @author lynn
 */
@Controller
@Scope("prototype")
public class CustomRightsManageAction extends ActionSupport implements ServletResponseAware,ModelDriven<CustomRightsManageDto>{
	
	private static final long serialVersionUID = -7356820654798431675L;
	private static Logger logger=Logger.getLogger(EmployeeAction.class);
	private HttpServletResponse response;
	private CustomManageService service;
	private CustomRightsManageDto dto=new CustomRightsManageDto();
	

	@Resource(name="customManageServiceBean")
	public void setService(CustomManageService service) {
		this.service = service;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	
	@Override
	public CustomRightsManageDto getModel() {
		return this.dto;
	}
	
	/**
	 * 显示权限组列表
	 */
	public void showRightsGroup(){
		
		response.setContentType("text/plain; charset=UTF-8");
		
		Employee employee=(Employee) ServletActionContext.getRequest().getSession().getAttribute("employee");
		if(employee==null){
			logger.info("未登录的非法用户，不允许查询相关数据！");
			return;
		}
		
		LinkedHashMap<String, String> orderBy=new LinkedHashMap<String, String>();		
		orderBy.put("createDate", "desc");
		
		ArrayList<Object> queryParames=new ArrayList<Object>();
		String whereStr=" o.empId=?";
		queryParames.add(employee.getId());
		
		QueryResult<CusRightsGroup> qr=service.getScrollData(CusRightsGroup.class, (dto.getPage()-1)*dto.getRows(), dto.getRows(), whereStr, queryParames, orderBy);
		
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
	 * 显示某个权限组中的所有权限
	 */
	public void showRightsOfGroup(){
		
		CusRightsGroup crg=service.find(CusRightsGroup.class, dto.getGroupId());
		//对Set集合进行排序 
		TreeSet<CusRightsInfo> ts=new TreeSet<CusRightsInfo>(crg.getRightsInfo());
		
		Map<String,Object> jsonMap=new HashMap<String, Object>();
		jsonMap.put("total", crg.getRightsInfo().size());
		jsonMap.put("rows", ts);
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
	 * 显示软件产品中的所有权限
	 */
	public void showAllRights(){
		
		response.setContentType("text/plain; charset=UTF-8");
		
		String whereStr=" o.enabel=?";
		ArrayList<Object> queryParams=new ArrayList<Object>();
		queryParams.add(true);
		
		LinkedHashMap<String, String> orderBy=new LinkedHashMap<String, String>();		
		orderBy.put("rightsValue", "asc");
		
		QueryResult<SoftAllRights> qr=service.getScrollData(SoftAllRights.class, (dto.getPage()-1)*dto.getRows(), dto.getRows(),whereStr,queryParams,orderBy);
		
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
	 * 保存编辑好后的 权限组中权限
	 */
	public void saveRightsOfGroup(){
		response.setContentType("text/plain; charset=UTF-8");
		
		String jsonData=Base64.decode(dto.getEdiJson(), "gb2312");
		logger.debug(jsonData);
		String state=service.saveRightsOfGroup(dto.getGroupId(),jsonData);
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write(state);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(">> 出现IO异常  <<");
		}finally{
			if(out!=null)out.close();
		}
	}
	
	/**
	 * 保存 新添加的权限组信息 或者修改后的内容
	 */
	public void saveInfoOfGroup(){
		
		response.setContentType("text/plain; charset=UTF-8");
		
		String jsonData=Base64.decode(dto.getEdiJson(), "gb2312");
		logger.debug("前台发送的json数据"+jsonData);
		JSONObject obj=JSONObject.fromObject(jsonData);
		if(dto.getGroupId()==0){
			CusRightsGroup group =new CusRightsGroup();
			group.setName(obj.getString("name"));
			group.setDescribe(obj.getString("describe"));
			group.setEmpId(obj.getLong("empId"));
			group.setCreateDate(new Date());
			service.save(group);
		}else {
			CusRightsGroup group = service.find(CusRightsGroup.class, dto.getGroupId());
			group.setName(obj.getString("name"));
			group.setDescribe(obj.getString("describe"));
			service.update(group);
		}
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write("success");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(">> 出现IO异常  <<");
		}finally{
			if(out!=null)out.close();
		}
	}
	
	/**
	 * 删除员工创建的 权限组
	 */
	public void removeGroupInfo(){
		
		response.setContentType("text/plain; charset=UTF-8");
		
		String state="false";
		if(dto.getGroupId()>0){
			CusRightsGroup group=service.find(CusRightsGroup.class, dto.getGroupId());
			service.delete(group);
			state="success";
		}else {
			logger.info("请求参数错误，未提供需要删除的权限组id！");
			state="false";
		}
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write(state);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(">> 出现IO异常  <<");
		}finally{
			if(out!=null)out.close();
		}
	}
	
	/**
	 * 删除某个权限组中的 权限
	 */
	public void removeRightOfGroup(){
		
		response.setContentType("text/plain; charset=UTF-8");
		String idStr=Base64.decode(dto.getIds(), "gb2312");
		
		String state=service.deleteRightsOfGroup(dto.getGroupId(), idStr);
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write(state);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(">> 出现IO异常  <<");
		}finally{
			if(out!=null)out.close();
		}
	}
	
}























