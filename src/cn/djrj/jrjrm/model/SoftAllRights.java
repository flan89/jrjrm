package cn.djrj.jrjrm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 软件产品的所有权限 实体类
 * @author lynn
 */
@Entity
@Table(name="sys_softRights")
public class SoftAllRights implements Serializable{
	
	private static final long serialVersionUID = -5442035621593678572L;
	private Integer id;
	private String rightsName;			//权限名称
	private String rightsValue;			//权限值
	private Integer effectDate;			//建议体验期
	private Boolean enabel;				//权限是否可用
	
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(nullable=false,length=50)
	public String getRightsName() {
		return rightsName;
	}
	public void setRightsName(String rightsName) {
		this.rightsName = rightsName;
	}
	@Column(nullable=false,unique=true)
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
	@Column(nullable=false)
	public Boolean getEnabel() {
		return enabel;
	}
	public void setEnabel(Boolean enabel) {
		this.enabel = enabel;
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
		SoftAllRights other = (SoftAllRights) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}





