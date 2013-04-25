package cn.djrj.jrjrm.crm.service.custom.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.djrj.jrjrm.crm.service.custom.CustomManageService;
import cn.djrj.jrjrm.dao.DaoSupport;
import cn.djrj.jrjrm.model.CusRightsGroup;
import cn.djrj.jrjrm.model.CusRightsInfo;
import cn.djrj.jrjrm.model.Custom;
import cn.djrj.jrjrm.model.CustomRights;

/**
 * 用户管理 服务层 实现类 
 * @author lynn
 */
@Transactional
@Service
public class CustomManageServiceBean extends DaoSupport implements CustomManageService{
	
	
	/**
	 * 保存 添加/修改后 权限组中的权限
	 * 
	 * 此处需要批量添加的数据比较少，不需要使用强制刷新清空session缓存来特意处理
	 * 如果对于大批量的 添加数据时，可以使用 每插入 一定数量的 数据后 强制刷新关闭一次session
	 * session.save(medicine);
	 * 批插入的对象立即写入数据库并释放内存  
	 * if (i % 10 == 0) {  
     *	session.flush();  
     *	session.clear();  
     *}  
	 * @param groupId
	 * @param jsonData
	 * @return
	 * @throws Exception 
	 */
	@Override
	@Transactional
	public String saveRightsOfGroup(int groupId, String jsonData){
		
		JSONObject rigthsJson=JSONObject.fromObject(jsonData);
		JSONArray array=JSONArray.fromObject(rigthsJson.get("rows"));
		List<String> existRight=getExistRightsValueOfGroup(groupId);
		
		JSONObject tempObj=null;
		for(int i=0;i<array.size();i++){
			tempObj=JSONObject.fromObject(array.get(i));
			String newRights=tempObj.getString("rightsValue");
			Integer effectDate=tempObj.getInt("effectDate");
			
			CusRightsInfo groupRights=null;
			if(existRight.contains(newRights)){
				String whereStr=" o.groupId=? and o.rightsValue=?";
				ArrayList<Object> queryParames=new ArrayList<Object>();
				queryParames.add(groupId);
				queryParames.add(newRights);
				groupRights=this.getSingleDate(CusRightsInfo.class, whereStr, queryParames);
				groupRights.setEffectDate(effectDate);
				this.update(groupRights);
				
			}else {
				groupRights=new CusRightsInfo(groupId, tempObj.getString("rightsName"), newRights, effectDate);
				this.save(groupRights);
			}
		}
		return "success";
	}
	
	/**
	 * 获取某个用户组中已存在的权限值
	 * @param groupId
	 * @return	
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	private List<String> getExistRightsValueOfGroup(final Integer groupId){
		
		return this.hibernateTemplate.execute(new HibernateCallback<List<String>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException,SQLException {
				
				String hql="select o.rightsValue from  CusRightsInfo o where o.groupId=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, groupId);
				return query.list();
			}
			
		});
	}
	
	/**
	 * 获取用户已有的权限
	 * @param userName
	 * @return
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	private List<String> getExistRightsValueOfCustom(final String userName){
		
		return this.hibernateTemplate.execute(new HibernateCallback<List<String>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException,SQLException {
				
				String hql="select o.info from  CustomRights o where o.userName=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, userName);
				return query.list();
			}
			
		});
	}
	
	/**
	 * 保存编辑后的用户权限
	 */
	@Override
	@Transactional
	public String saveRightsOfCustom(String userName,String jsonData) throws ParseException{
		
		JSONObject rigthsJson=JSONObject.fromObject(jsonData);
		JSONArray array=JSONArray.fromObject(rigthsJson.get("rows"));
		List<String> existRight=getExistRightsValueOfCustom(userName);
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		
		Date endTime=null;
		JSONObject tempObj=null;
		for(int i=0;i<array.size();i++){
			tempObj=JSONObject.fromObject(array.get(i));
			String newRights=tempObj.getString("info");
			endTime=df.parse(tempObj.getString("endTime"));
			
			CustomRights cusRights=null;
			if(existRight.contains(newRights)){
				String whereStr=" o.userName=? and o.info=?";
				ArrayList<Object> queryParames=new ArrayList<Object>();
				queryParames.add(userName);
				queryParames.add(newRights);
				cusRights=this.getSingleDate(CustomRights.class, whereStr, queryParames);
				cusRights.setEndTime(endTime);
				cusRights.setModified_at(new Date());
				this.update(cusRights);
				
			}else {
				cusRights=new CustomRights(userName, newRights, df.parse(tempObj.getString("startTime")), endTime, new Date());
				this.save(cusRights);
			}
		}
		return "success";
	}
	
	/**
	 * 删除用户权限
	 * @param userName
	 * @param rights
	 * @return
	 */
	@Override
	@Transactional
	public String removeCustomRights(final String userName,final String rights){
		
		return this.hibernateTemplate.execute(new HibernateCallback<String>() {

			@Override
			public String doInHibernate(Session session) throws HibernateException, SQLException {
				
				if(userName==null || userName.equals("")){
					return "false";
				}
				StringBuffer temp=new StringBuffer();
				String[] infos=rights.split(",");
				for(int i=0;i<infos.length;i++){
					temp.append("?,");
				}
				String inStr=temp.toString().substring(0,temp.toString().length()-1);
				String hql="delete from CustomRights o where o.info in("+inStr+")  and o.userName=?";
				Query query= session.createQuery(hql);
				
				for(int i=0;i<infos.length;i++){
					query.setParameter(i, infos[i]);
				}
				query.setParameter(infos.length, userName);
				query.executeUpdate();
				return "success";
			}
		});
	}
	
	/**
	 * 查询出工作人员自定义权限组名字
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public List<String> findRightsGroupName(final Long empId){
		
		return this.hibernateTemplate.execute(new HibernateCallback<List<String>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
				
				String hql="select o.name from CusRightsGroup o where o.empId=?";
				Query query= session.createQuery(hql);
				query.setParameter(0, empId);
				return query.list();
			}

		});
	}
	
	/**
	 * 为用户授予 权限组 中权限
	 */
	@Transactional
	public String geivRights(String cusName,String groupName){
		
		List<String> existRight=getExistRightsValueOfCustom(cusName);
		
		
		String whereStr=" o.name=?";
		ArrayList<Object> queryParames=new ArrayList<Object>();
		queryParames.add(groupName);
		CusRightsGroup rGroup=this.getSingleDate(CusRightsGroup.class, whereStr, queryParames);
		Set<CusRightsInfo> rights=rGroup.getRightsInfo();  
		
		Calendar cal=Calendar.getInstance();
		Date startTime=cal.getTime();
		for(CusRightsInfo info : rights){
			if(!existRight.contains(info.getRightsValue())){
				cal.add(Calendar.DATE,info.getEffectDate());
				Date endTime=cal.getTime();
				CustomRights newRights=new CustomRights(cusName, info.getRightsValue(), startTime, endTime, startTime);
				this.save(newRights);
				cal.add(Calendar.DATE, -info.getEffectDate());
			}
		}
		return  "success";
	}
	
	/*@Transactional
	public String geivRights(String cusName,String groupName){
		
		List<String> existRight=getExistRightsValueOfCustom(cusName);
		
		String whereStr=" o.name=?";
		ArrayList<Object> queryParames=new ArrayList<Object>();
		queryParames.add(groupName);
		CusRightsGroup rGroup=this.getSingleDate(CusRightsGroup.class, whereStr, queryParames);
		Set<CusRightsInfo> rights=rGroup.getRightsInfo();  
		
		
		String swhere=" o.userName=?";
		ArrayList<Object> parames=new ArrayList<Object>();
		parames.add(cusName);
		Custom c=this.getSingleDate(Custom.class, swhere, parames);
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(c.getStartTime());
		Date startTime=cal.getTime();
		for(CusRightsInfo info : rights){
			if(!existRight.contains(info.getRightsValue())){
				cal.add(Calendar.DATE,info.getEffectDate());
				Date endTime=cal.getTime();
				CustomRights newRights=new CustomRights(cusName, info.getRightsValue(), startTime, endTime, startTime);
				this.save(newRights);
				cal.add(Calendar.DATE, -info.getEffectDate());
			}
		}
		return  "success";
	}*/
	
	
	/**
	 * 查询出用户表中未过期的用户（废弃，回复数据时临时使用）
	 * @return
	 */
	public List<String> getUserNamefromUserInfo(){
		/*return this.hibernateTemplate.execute(new HibernateCallback<List<String>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<String> doInHibernate(Session session) throws HibernateException, SQLException {
				
				String hql="select c.userName from cn.djrj.jrjrm.model.Custom c where c.clientFrom like ? and c.endTime> ?";
				Query query=session.createQuery(hql);
				query.setParameter(0, "%27%");
				query.setParameter(1, new Date());
				
				return query.list();
			}
		});*/
		return null;
	}
	
	/**
	 * 添加新用户 
	 * @param jsonData	前台传入的新用户信息
	 * @param clientFrom	该客户所属的代理商 id
	 * @return
	 * @throws ParseException
	 */
	public String addNewCustom(String jsonData,String clientFrom) throws ParseException{
		
		JSONObject jsonObj=JSONObject.fromObject(jsonData);
					
		String userName=jsonObj.getString("userName");
		String passWord=jsonObj.getString("passWord");
		
		if(!userName.equals("") && !passWord.equals("")){
			
			Calendar cal=Calendar.getInstance();
			Date today=cal.getTime();
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			
			Date sTime=today;
			String sTempTime=jsonObj.getString("startTime");
			if(!sTempTime.equals("")){
				sTime=df.parse(sTempTime);
			}
			cal.add(Calendar.DATE, 6);
			Date eTime=cal.getTime();
			String eTempTime=jsonObj.getString("endTime");
			if(!eTempTime.equals("")){
				eTime=df.parse(eTempTime);
			}
			Date pTime=today;
			String pTempTime=jsonObj.getString("payTime");
			if(!pTempTime.equals("")){
				pTime=df.parse(pTempTime);
			}
			
			String flag="1";
			String tflag=jsonObj.getString("flag");
			if(tflag.equals("启用")){
				flag="0";
			}else if (tflag.equals("停用")) {
				flag="1";
			}
			int sex=0;
			String tSex=jsonObj.getString("sex");
			if(tSex.equals("女")){
				sex=0;
			}else if (tSex.equals("男")) {
				sex=1;
			}
			int userType=0;
			String ttype=jsonObj.getString("userType");
			if(ttype.equals("注册用户")){
				userType=0;
			}else if (ttype.equals("认证用户")) {
				userType=1;
			}else if (ttype.equals("付费用户")) {
				userType=2;
			}
			
			float pMoney=0;
			String pTempMoney=jsonObj.getString("payMoney");
			if(!pTempMoney.equals("")){
				pMoney=Float.valueOf(pTempMoney);
			}
			
			String cFrom=jsonObj.getString("clientFrom");
			cFrom= cFrom.equals("")?clientFrom:cFrom;
			
			String telephone =jsonObj.getString("telephone");
			
			Custom cus=new Custom(userName, passWord, sTime, eTime, telephone, userType, flag, cFrom);
			cus.setPayMoney(pMoney);
			cus.setSex(sex);
			cus.setPayTime(pTime);
			this.save(cus);

			return "success";
		}else {
			return "false";
		}
	}
	
	/**
	 * 显示今日登录的用户;
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public String showTodayLoginCus(final String companyId){
		
		return this.hibernateTemplate.execute(new HibernateCallback<String>() {

			@Override
			public String doInHibernate(Session session) throws HibernateException, SQLException {
				
				String hql="select count(distinct o.userName) from cn.djrj.jrjrm.model.CusLoginLog o where o.operateTime between ? and ? " +
						" and o.userName in(select c.userName from cn.djrj.jrjrm.model.Custom c where c.clientFrom=?) ";
				Query query=session.createQuery(hql);
				
				Calendar cal=Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				query.setParameter(0, cal.getTime());
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				cal.set(Calendar.MILLISECOND, 999);
				query.setParameter(1, cal.getTime());
				query.setParameter(2, companyId);

				String num=query.uniqueResult().toString();
				return num;
			}
		});
	}
	
	/**
	 * 删除权限组中权限
	 */
	@Transactional
	public String deleteRightsOfGroup(final Integer groupId,final String idStr){
		
		return this.hibernateTemplate.execute(new HibernateCallback<String>() {

			@Override
			public String doInHibernate(Session session) throws HibernateException, SQLException {
				
				if(groupId<=0){
					return "false";
				}
				
				String[] ids=idStr.split(",");
				if(ids.length<=0 || groupId<=0){
					return "false";
				}
				Set<String> idSet=new HashSet<String>();
				CollectionUtils.addAll(idSet, ids);
				StringBuffer wherein=new StringBuffer();
				for(int i=0;i<idSet.size();i++){
					wherein.append("?,");
				}
				String whereStr=wherein.toString().substring(0,wherein.toString().length()-1);
				String hql="delete from cn.djrj.jrjrm.model.CusRightsInfo  o  where  o.rightsValue in("+whereStr+") and  o.groupId=?";
				Query query=session.createQuery(hql);
				int i=0;
				Iterator<String> infos=idSet.iterator();
				while(infos.hasNext()){
					String id=infos.next();
					query.setParameter(i, id);
					i++;
				}
				query.setParameter(idSet.size(), groupId);
				query.executeUpdate();
				return "success";
			}
		});
	}
	
}









