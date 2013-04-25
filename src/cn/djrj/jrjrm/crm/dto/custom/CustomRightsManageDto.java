package cn.djrj.jrjrm.crm.dto.custom;

/**
 * 客户权限管理 的数据传输对象
 * @author lynn
 */
public class CustomRightsManageDto {
	
	private int groupId;		//权限组ID
	private int page;			//当前第几页
	private int rows;			//每页显示的记录数
	
	private long empId;			//员工账号的ID
	private String ids;			//需要删除的权限组中的权限ID
	private String ediJson;		//权限组中的权限数据 json格式
	
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
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
	public String getEdiJson() {
		return ediJson;
	}
	public void setEdiJson(String ediJson) {
		this.ediJson = ediJson;
	}
	public long getEmpId() {
		return empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	
}







