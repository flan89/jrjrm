package cn.djrj.jrjrm.crm.dto.custom;

/**
 * 处理日志查询 时的数据传输对象
 * @author lynn
 */
public class CustomLoginLogDto {
	
	private int page;			//当前第几页
	private int rows;			//每页显示的记录数
	
	private String userName;	//用户名
	private String inOut;		//登录/登出
	private String osTime;		//操作开始时间
	private String oeTime;		//操作结束时间
	
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getInOut() {
		return inOut;
	}
	public void setInOut(String inOut) {
		this.inOut = inOut;
	}
	public String getOsTime() {
		return osTime;
	}
	public void setOsTime(String osTime) {
		this.osTime = osTime;
	}
	public String getOeTime() {
		return oeTime;
	}
	public void setOeTime(String oeTime) {
		this.oeTime = oeTime;
	}
	
}
