package cn.djrj.jrjrm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 客户 权限组 实体类
 * @author lynn
 */
@Entity
@Table(name="cus_rightsGroup")
public class CusRightsGroup implements Serializable {
	
	private static final long serialVersionUID = -5219134306940046414L;
	private Integer id;				
	private String name;						//组名
	private Long empId;						//所属用户的ID
	private Date createDate;					//创建日期
	private String describe;					//组的描述
	private Set<CusRightsInfo> rightsInfo;		//保存的权限
	
	
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(nullable=false,length=50,unique=true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable=false)
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	@Column(nullable=false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(length=250)
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	@Column(nullable=false)
	@OneToMany(mappedBy="groupId",cascade=CascadeType.REMOVE,fetch=FetchType.LAZY)
	public Set<CusRightsInfo> getRightsInfo() {
		return rightsInfo;
	}
	public void setRightsInfo(Set<CusRightsInfo> rightsInfo) {
		this.rightsInfo = rightsInfo;
	}
	
	
	//////////////////////////////////////////////
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CusRightsGroup other = (CusRightsGroup) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
