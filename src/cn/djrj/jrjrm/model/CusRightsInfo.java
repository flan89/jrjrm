package cn.djrj.jrjrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cus_rightsInfo")
public class CusRightsInfo implements Serializable,Comparable<CusRightsInfo>{
	
	private static final long serialVersionUID = -3213418446175033211L;
	private Integer id;
	private Integer groupId;
	private String rightsName;
	private String rightsValue;		//权限值
	private Integer effectDate;			//体验时间
	
	public CusRightsInfo() {}
	
	public CusRightsInfo(Integer groupId, String rightsName,
			String rightsValue, Integer effectDate) {
		this.groupId = groupId;
		this.rightsName = rightsName;
		this.rightsValue = rightsValue;
		this.effectDate = effectDate;
	}

	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(nullable=false)
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	@Column(nullable=false,length=50)
	public String getRightsName() {
		return rightsName;
	}
	public void setRightsName(String rightsName) {
		this.rightsName = rightsName;
	}
	@Column(nullable=false)
	public String getRightsValue() {
		return rightsValue;
	}
	public void setRightsValue(String rightsValue) {
		this.rightsValue = rightsValue;
	}
	@Column(nullable=false)
	public Integer getEffectDate() {
		return effectDate;
	}
	public void setEffectDate(Integer effectDate) {
		this.effectDate = effectDate;
	}
	
	
	
	
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
		CusRightsInfo other = (CusRightsInfo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(CusRightsInfo o) {
		int value1=Integer.valueOf(this.rightsValue);
		int value2=Integer.valueOf(o.rightsValue);
		return value1-value2;
	}
	
}










