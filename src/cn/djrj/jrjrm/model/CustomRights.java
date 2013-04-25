package cn.djrj.jrjrm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 权限 实体类
 * @author lynn
 */
@Entity
@Table(name="user_rights")
public class CustomRights implements Serializable{
	
	private static final long serialVersionUID = -516273857844911514L;
	private Long id;
	private String userName;
	private String info;
	private Date startTime;
	private Date endTime;
	private Date created_at;
	private Date modified_at;
	
	
	
	public CustomRights() {}
	
	public CustomRights(String userName, String info, Date startTime,
			Date endTime, Date created_at) {
		
		this.userName = userName;
		this.info = info;
		this.startTime = startTime;
		this.endTime = endTime;
		this.created_at = created_at;
	}
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(length=40,nullable=false)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(length=10,nullable=false)
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Column(nullable=false)
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Column(nullable=false)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Column(nullable=false)
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getModified_at() {
		return modified_at;
	}
	public void setModified_at(Date modified_at) {
		this.modified_at = modified_at;
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
		CustomRights other = (CustomRights) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
