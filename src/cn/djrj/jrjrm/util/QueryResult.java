package cn.djrj.jrjrm.util;

import java.util.List;

/**
 * ���������ҳ��ѯ�Ľ��
 * @author lynn
 * @param <T>	��Ҫ��ѯ��ʵ����
 */
public class QueryResult<T> {
	
	/**
	 * ��ѯ���Ľ������
	 */
	private List<T> resultList;
	/**
	 * ��ѯ�����ܽ����
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
