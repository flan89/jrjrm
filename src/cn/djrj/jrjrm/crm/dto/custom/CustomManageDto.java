package cn.djrj.jrjrm.crm.dto.custom;

/**
 * 客户管理操作中的 数据传输对象
 * @author Administrator
 *
 */
public class CustomManageDto {
	
	private int id;
	private Long empId;			//工作人员账号ID
	
	private int page;			//当前第几页
	private int rows;			//每页显示的记录数
	
	private String userName;	//用户名
	private String telephone;	//电话
	private String userType;	//用户类型
	private String flag;		//用户状态
	
	private String info;		//用户权限值
	
	private String rsTime;		//注册时间范围   开始
	private String reTime;		//注册时间范围    结束
	private String esTime;		//账号到期时间范围  开始
	private String eeTime;		//账号到期时间范围  结束
	private String rightsJson;	//用户权限 的json数据
	private String infoJson;	//用户基本信息
	
	private String groupName;	//权限组名称
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getRsTime() {
		return rsTime;
	}
	public void setRsTime(String rsTime) {
		this.rsTime = rsTime;
	}
	public String getReTime() {
		return reTime;
	}
	public void setReTime(String reTime) {
		this.reTime = reTime;
	}
	public String getEsTime() {
		return esTime;
	}
	public void setEsTime(String esTime) {
		this.esTime = esTime;
	}
	public String getEeTime() {
		return eeTime;
	}
	public void setEeTime(String eeTime) {
		this.eeTime = eeTime;
	}
	public String getRightsJson() {
		return rightsJson;
	}
	public void setRightsJson(String rightsJson) {
		this.rightsJson = rightsJson;
	}
	public String getInfoJson() {
		return infoJson;
	}
	public void setInfoJson(String infoJson) {
		this.infoJson = infoJson;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}





