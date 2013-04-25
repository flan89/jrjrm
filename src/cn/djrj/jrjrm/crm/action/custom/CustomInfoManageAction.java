package cn.djrj.jrjrm.crm.action.custom;

import it.sauronsoftware.base64.Base64;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.djrj.jrjrm.crm.dto.custom.CustomManageDto;
import cn.djrj.jrjrm.crm.service.custom.CustomManageService;
import cn.djrj.jrjrm.model.Custom;
import cn.djrj.jrjrm.model.CustomRights;
import cn.djrj.jrjrm.model.Employee;
import cn.djrj.jrjrm.oa.action.employee.EmployeeAction;
import cn.djrj.jrjrm.util.PropertyGrid;
import cn.djrj.jrjrm.util.QueryResult;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 客户信息管理 控制层
 * 主要负责 客户信息的 CRUD操作 
 * @author lynn
 */
@Controller
@Scope("prototype")
public class CustomInfoManageAction extends ActionSupport implements ServletResponseAware,ModelDriven<CustomManageDto>{
	
	private static final long serialVersionUID = -5728870937344733685L;
	private static Logger logger=Logger.getLogger(EmployeeAction.class);
	private HttpServletResponse response;
	private CustomManageService service;
	private CustomManageDto dto=new CustomManageDto();
	
	@Resource(name="customManageServiceBean")
	public void setService(CustomManageService service) {
		this.service = service;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	
	@Override
	public CustomManageDto getModel() {
		return this.dto;
	}
	
	/**
	 * 用于分页列表 展示所有用户信息
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void showCustomList() throws ParseException{
		
		response.setContentType("text/plain; charset=UTF-8");
		
		Employee employee=(Employee) ServletActionContext.getRequest().getSession().getAttribute("employee");
		ArrayList<Object> queryParames=new ArrayList<Object>();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String companyId=employee.getCompanyId().trim();
		if(companyId==null || companyId.equals("")){
			logger.info("未登录的非法用户，不允许询相应数据！");
			return;
		}
		StringBuffer whereStr=new StringBuffer(" o.clientFrom like ? ");
		queryParames.add("%"+companyId+"%");
		
		if(dto.getUserName()!=null && !dto.getUserName().equals("")){
			whereStr.append(" and o.userName like ? ");
			queryParames.add("%"+dto.getUserName()+"%");
		}
		if(dto.getTelephone()!=null && !dto.getTelephone().equals("")){
			whereStr.append(" and o.telephone like ?");
			queryParames.add("%"+dto.getTelephone()+"%");
		}
		if(dto.getUserType()!=null && !dto.getUserType().equals("")){
			whereStr.append(" and o.userType=?");
			queryParames.add(Integer.valueOf(dto.getUserType()));
		}
		if(dto.getFlag()!=null && !dto.getFlag().equals("")){
			whereStr.append(" and o.flag=?");
			queryParames.add(dto.getFlag());
		}
		if(dto.getRsTime()!=null && !dto.getRsTime().equals("")){
			whereStr.append(" and o.startTime>=?");
			Date tempDate=df.parse(dto.getRsTime()+" 00:00:00");
			queryParames.add(tempDate);
		}
		if(dto.getReTime()!=null && !dto.getReTime().equals("")){
			whereStr.append(" and o.startTime<=?");
			Date tempDate=df.parse(dto.getReTime()+" 23:59:59");
			queryParames.add(tempDate);
		}
		if(dto.getEsTime()!=null && !dto.getEsTime().equals("")){
			whereStr.append(" and o.endTime>=?");
			Date tempDate=df.parse(dto.getEsTime()+" 00:00:00");
			queryParames.add(tempDate);
		}
		if(dto.getEeTime()!=null && !dto.getEeTime().equals("")){
			whereStr.append(" and o.endTime<=?");
			Date tempDate=df.parse(dto.getEeTime()+" 23:59:59");
			queryParames.add(tempDate);
		}
		
		
		LinkedHashMap<String, String> orderBy=new LinkedHashMap<String, String>();		
		orderBy.put("startTime", "desc");
		
		QueryResult<Custom> qr=service.getScrollData(Custom.class, (dto.getPage()-1)*dto.getRows(), dto.getRows(), whereStr.toString(), queryParames, orderBy);
		
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
	 * 添加新用户
	 * @throws ParseException 
	 */
	public void addNewCustom() throws ParseException{
		
		response.setContentType("text/plain; charset=UTF-8");
		String jsonData=Base64.decode(dto.getInfoJson(), "gb2312");
		logger.debug(jsonData.toString());
		Employee employee=(Employee) ServletActionContext.getRequest().getSession().getAttribute("employee");
		if(employee!=null){
			String clientFrom=employee.getCompanyId();
			String state=service.addNewCustom(jsonData, clientFrom);
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
		}else {
			logger.info("未登录的非法用户，不允许进行相应操作！");
		}
	}
	
	/**
	 * 显示添加新用户的页面
	 */
	public void showNewCustomPage(){
		response.setContentType("text/plain; charset=utf-8");
		
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		List<PropertyGrid> list=new ArrayList<PropertyGrid>();
		
		JSONObject sexCombobox=JSONObject.fromObject("{\"type\":\"combobox\",\"options\":{\"data\":[{\"value\":\"女\",\"text\":\"女\"},{\"value\":\"男\",\"text\":\"男\"}],\"panelHeight\":\"auto\"}}");
		JSONObject flagCombobox=JSONObject.fromObject("{\"type\":\"combobox\",\"options\":{\"data\":[{\"value\":\"停用\",\"text\":\"停用\"},{\"value\":\"启用\",\"text\":\"启用\"}],\"panelHeight\":\"auto\"}}");
		JSONObject typeCombobox=JSONObject.fromObject("{\"type\":\"combobox\",\"options\":{\"data\":[{\"value\":\"注册用户\",\"text\":\"注册用户\"},{\"value\":\"认证用户\",\"text\":\"认证用户\"},{\"value\":\"付费用户\",\"text\":\"付费用户\"}],\"panelHeight\":\"auto\"}}");
		
		list.add(new PropertyGrid("用户账号", "","必填信息","text"));
		list.add(new PropertyGrid("密    码", "","必填信息","text"));
		list.add(new PropertyGrid("开始时间", "","必填信息","datebox"));
		list.add(new PropertyGrid("结束时间", "","必填信息","datebox"));
		list.add(new PropertyGrid("用户类型", "","必填信息",typeCombobox));
		list.add(new PropertyGrid("缴费金额", "","必填信息","numberbox"));
		list.add(new PropertyGrid("用户状态", "","必填信息",flagCombobox));
		list.add(new PropertyGrid("用户来源", "","必填信息","text"));
		
		list.add(new PropertyGrid("手机号码", "","选填信息","numberbox"));
		list.add(new PropertyGrid("性    别", "","选填信息",sexCombobox));
		list.add(new PropertyGrid("付款日期", "","选填信息","datebox"));
		
		jsonMap.put("total", 11);
		jsonMap.put("rows", list);
		JSONObject json=JSONObject.fromObject(jsonMap);
		
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
	 * 显示某个用户详细信息 用于编辑
	 */
	public void showCustomDetail(){
		
		response.setContentType("text/plain; charset=utf-8");
		
		Custom custom=service.find(Custom.class, dto.getId());
		
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String startTime=df.format(custom.getStartTime());
		String endTime=df.format(custom.getEndTime());
		
		String userType="注册用户";
		if(custom.getUserType()==0){
			userType="注册用户";
		}else if (custom.getUserType()==1) {
			userType="认证用户";
		}else if (custom.getUserType()==2) {
			userType="付费用户";
		}
		
		String flag="启用";
		if(custom.getFlag().equals("1")){
			flag="停用";
		}
		
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		List<PropertyGrid> list=new ArrayList<PropertyGrid>();
		
		JSONObject flagCombobox=JSONObject.fromObject("{\"type\":\"combobox\",\"options\":{\"data\":[{\"value\":\"停用\",\"text\":\"停用\"},{\"value\":\"启用\",\"text\":\"启用\"}],\"panelHeight\":\"auto\"}}");
		JSONObject typeCombobox=JSONObject.fromObject("{\"type\":\"combobox\",\"options\":{\"data\":[{\"value\":\"注册用户\",\"text\":\"注册用户\"},{\"value\":\"认证用户\",\"text\":\"认证用户\"},{\"value\":\"付费用户\",\"text\":\"付费用户\"}],\"panelHeight\":\"auto\"}}");
		
		list.add(new PropertyGrid("<font color=\"red\">用户账号</font>", custom.getUserName(),"详细信息","null"));
		list.add(new PropertyGrid("密码", custom.getPassWord(),"详细信息","text"));
		list.add(new PropertyGrid("手机号码", custom.getTelephone(),"详细信息","numberbox"));
		list.add(new PropertyGrid("<font color=\"red\">开始时间</font>", startTime,"详细信息","null"));
		list.add(new PropertyGrid("结束时间", endTime,"详细信息","datebox"));
		list.add(new PropertyGrid("用户类型", userType,"详细信息",typeCombobox));
		list.add(new PropertyGrid("缴费金额", custom.getPayMoney(),"详细信息","numberbox"));
		list.add(new PropertyGrid("用户状态", flag,"详细信息",flagCombobox));
		
		jsonMap.put("total", custom.getClass().getFields().length);
		jsonMap.put("rows", list);
		JSONObject json=JSONObject.fromObject(jsonMap);
		
		logger.debug("用户详细信息："+json.toString());
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write(json.toString());
			out.flush();
		} catch (IOException e) {
			logger.error(">> 出现IO异常  <<");
			e.printStackTrace();
		}finally{
			if(out!=null)out.close();
		}
	}
	
	/**
	 * 显示某个用户所拥有的权限
	 */
	public void showCustomRights(){
		
		if(dto.getUserName()==null || dto.getUserName().trim().equals("")){
			logger.info("用户名为空，不能查询出对应的权限数据！");
			return;
		}
		response.setContentType("text/plain; charset=utf-8");
		String whereStr=" o.userName=?";
		ArrayList<Object> queryParams=new ArrayList<Object>();
		queryParams.add(dto.getUserName());
		QueryResult<CustomRights> rights=service.getScrollData(CustomRights.class, (dto.getPage()-1)*dto.getRows(),dto.getRows(), whereStr, queryParams);
		
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		
		JSONArray array=JSONArray.fromObject(rights.getResultList());
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i=0;i<array.size();i++){
			
			JSONObject obj=JSONObject.fromObject(array.get(i));
			JSONObject endObj=JSONObject.fromObject(obj.get("endTime"));
			JSONObject startObj=JSONObject.fromObject(obj.get("startTime"));
			Date d=new Date(endObj.getLong("time"));
			String endTime=df.format(d);
			d=new Date(startObj.getLong("time"));
			String startTime=df.format(d);
			obj.put("endTime", endTime);
			obj.put("startTime", startTime);
			array.set(i, obj);
		}
		
		jsonMap.put("total", rights.getTotalRecord());
		jsonMap.put("rows", array);
		
		JSONObject json=JSONObject.fromObject(jsonMap);
		logger.debug("用户拥有的权限数据："+json.toString());
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write(json.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null)out.close();
		}
		
	}
	
	/**
	 * 显示软件产品所有功能的权限
	 */
	public void showAllRights(){
		
		response.setContentType("text/plain; charset=utf-8");
		
		QueryResult<CustomRights> rights=service.getScrollData(CustomRights.class, (dto.getPage()-1)*dto.getRows(),dto.getRows());
		
		Map<String,Object> jsonMap=new HashMap<String, Object>();
		jsonMap.put("total", rights.getTotalRecord());
		jsonMap.put("rows", rights.getResultList());
		JSONObject json=JSONObject.fromObject(jsonMap);
		
		PrintWriter out=null;
		try {
			out=response.getWriter();
			out.write(json.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null)out.close();
		}
		
	}
	
	/**
	 * 处理 启用/停用  账号  操作
	 * @throws IOException 
	 */
	public void isFreeze(){
		
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out=null;
		String flag=dto.getFlag().trim();
		
		try {
			out = response.getWriter();
			if(flag!=null && !flag.equals("") && dto.getId()>0){
				Custom custom=service.find(Custom.class, dto.getId());
				custom.setFlag(flag);
				service.update(custom);
				
				out.write("success");
				out.flush();
			}else {
				logger.info("请求参数为空或缺失，非正常访问！");
				out.write("false");
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null)out.close();
		}
	}
	
	/**
	 * 保存用户基本信息
	 * @throws ParseException 
	 */
	public void saveCustomInfo() throws ParseException{
		
		response.setContentType("text/plain; charset=UTF-8");
		
		int id=dto.getId();
		if(id<=0){
			logger.info("保存用户信息时用户ID为空，非正常访问，中断操作！");
			return;
		}
		Custom cus=service.find(Custom.class, dto.getId());
		String jsonData=Base64.decode(dto.getInfoJson(), "gb2312");
		JSONObject infoJson=JSONObject.fromObject(jsonData);
		logger.debug(infoJson.toString());
		
		cus.setPassWord(infoJson.getString("passWord"));
		cus.setTelephone(infoJson.getString("telphone"));
		
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date endTime=df.parse(infoJson.getString("endTime"));
		cus.setEndTime(endTime);
		
		float payMoney=(float) infoJson.getDouble("paymoney");
		cus.setPayMoney(payMoney);
		
		String flag="0";
		String tflag=(String)infoJson.get("flag");
		if(tflag.equals("启用")){
			flag="0";
		}else if (tflag.equals("停用")) {
			flag="1";
		}
		cus.setFlag(flag);
		
		int userType=0;
		String ttype=(String)infoJson.get("userType");
		if(ttype.equals("注册用户")){
			userType=0;
		}else if (ttype.equals("认证用户")) {
			userType=1;
		}else if (ttype.equals("付费用户")) {
			userType=2;
		}
		cus.setUserType(userType);
		service.update(cus);
		
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
	 * 保存编辑后的用户权限
	 */
	public void saveCustomRights(){
		
		response.setContentType("text/plain; charset=UTF-8");
		
		String jsonData=Base64.decode(dto.getRightsJson(), "gb2312");
		logger.debug(jsonData);
		String state=null;
		try {
			state=service.saveRightsOfCustom(dto.getUserName(), jsonData);
		} catch (ParseException e) {
			e.printStackTrace();
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
	 * 删除用户权限
	 */
	public void removeCustomRights(){
		
		response.setContentType("text/plain; charset=UTF-8");
		String userName=dto.getUserName().trim();
		String info = dto.getInfo().trim();
		PrintWriter out=null;
		if(userName!=null && !userName.equals("") && info!=null && !info.equals("")){
			String state=service.removeCustomRights(userName,info);
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
		}else {
			logger.info("请求参数为空，非正常请求！");
			try {
				out=response.getWriter();
				out.write("false");
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(">> 出现IO异常  <<");
			}finally{
				if(out!=null)out.close();
			}
		}
	}
	
	/**
	 * 查询出工作人员自定义权限组名字
	 */
	public void showRightsGroupName(){
		
		response.setContentType("text/plain; charset=UTF-8");
		List<String> groupName=service.findRightsGroupName(dto.getEmpId());
		JSONArray json=JSONArray.fromObject(groupName);
		
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
	 * 为用户授予 权限组 中权限
	 */
	public void geivRights(){
		
		response.setContentType("text/plain; charset=UTF-8");
		
		String state=service.geivRights(dto.getUserName(), dto.getGroupName());
		
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

























