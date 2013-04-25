package cn.djrj.jrjrm.util;

import java.util.List;

/**
 * 用来保存分页查询的结果
 * @author lynn
 * @param <T>	需要查询的实体类
 */
public class QueryResult<T> {
	
	/**
	 * 查询出的结果集合
	 */
	private List<T> resultList;
	/**
	 * 查询出的总结果数
	 */
	private long totalRecord;
	
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultLis) {
		this.resultList = resultLis;
	}
	public long getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}
	
}
