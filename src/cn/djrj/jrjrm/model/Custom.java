package cn.djrj.jrjrm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 客户实体类
 * @author lynn
 */
@Entity
@Table(name="userinfo")
public class Custom implements Serializable{
	
	private static final long serialVersionUID = -3228529424009345217L;
	private Integer id;
	private String userName;
	private String passWord;
	private String registFrom;	//注册来源
	private String openId;		//相应微博账号的唯一ID（新浪或QQ产生）
	private Date startTime;		//账号有效期开始时间
	private Date endTime;		//账号有效期结束时间
	
	private Integer sex;
	private String admin;
	private Date expireDate;
	private String minver;
	private Date lastTime;
	private String info;
	private String online;
	private String ip;
	private String netCard;
	private String lastMonth;
	
	private String huming;
	private String name;
	private String ucName;
	private String telephone;
	
	private Float payMoney;
	private Date payTime;
	private String seller;
	private String funcinfo;
	private Integer userType;	//用户类型（0为注册用户，1为手机验证，2为开通用户）
	private Date updatedAt;
	
	private String flag;		//改账号是否可用（1为停用）
	private String clientFrom;	//服务商id(判断当前用户属于哪个服务商)
	private String syncinfo;
	
	//////////////////////////////////////
	
	public Custom() {}
	
	public Custom(String userName, String passWord, Date startTime,
			Date endTime, String telephone, Integer userType, String flag,
			String clientFrom) {
		
		this.userName = userName;
		this.passWord = passWord;
		this.startTime = startTime;
		this.endTime = endTime;
		this.telephone = telephone;
		this.userType = userType;
		this.flag = flag;
		this.clientFrom = clientFrom;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(length=40,nullable=false,unique=true)
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(length=50)
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getRegistFrom() {
		return registFrom;
	}
	public void setRegistFrom(String registFrom) {
		this.registFrom = registFrom;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
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
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	@Transient
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	@Transient
	public String getMinver() {
		return minver;
	}
	public void setMinver(String minver) {
		this.minver = minver;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Transient
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNetCard() {
		return netCard;
	}
	public void setNetCard(String netCard) {
		this.netCard = netCard;
	}
	@Transient
	public String getLastMonth() {
		return lastMonth;
	}
	public void setLastMonth(String lastMonth) {
		this.lastMonth = lastMonth;
	}
	@Transient
	public String getHuming() {
		return huming;
	}
	public void setHuming(String huming) {
		this.huming = huming;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUcName() {
		return ucName;
	}
	public void setUcName(String ucName) {
		this.ucName = ucName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Float getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Float payMoney) {
		this.payMoney = payMoney;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getFuncinfo() {
		return funcinfo;
	}
	public void setFuncinfo(String funcinfo) {
		this.funcinfo = funcinfo;
	}
	@Column(nullable=false)
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	@Column(nullable=false)
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Column(nullable=false)
	public String getClientFrom() {
		return clientFrom;
	}
	public void setClientFrom(String clientFrom) {
		this.clientFrom = clientFrom;
	}
	@Transient
	public String getSyncinfo() {
		return syncinfo;
	}
	public void setSyncinfo(String syncinfo) {
		this.syncinfo = syncinfo;
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
		Custom other = (Custom) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
















