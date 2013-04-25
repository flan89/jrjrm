package cn.djrj.jrjrm.crm.service.custom;

import java.text.ParseException;
import java.util.List;

import cn.djrj.jrjrm.dao.BaseDao;
/**
 * 用户管理 服务层 接口
 * @author lynn
 */
public interface CustomManageService extends BaseDao {
	
	public String saveRightsOfGroup(int groupId,String jsonData);
	public String saveRightsOfCustom(String userName,String jsonData) throws ParseException;
	public String removeCustomRights(String userName,final String rights);
	public List<String> findRightsGroupName(Long empId);
	public String geivRights(String cusName,String groupName);
	public String addNewCustom(String jsonData,String clientFrom) throws ParseException;
	public String showTodayLoginCus(final String companyId);
	public String deleteRightsOfGroup(Integer groupId,String ids);
	public List<String> getUserNamefromUserInfo();
	
}
