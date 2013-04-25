package cn.djrj.jrjrm.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.djrj.jrjrm.util.QueryResult;

/**
 * Dao ��ͨ�ýӿ�
 * @author lynn
 */
public interface BaseDao {
	
	/**
	 * ����ʵ�����
	 * @param entity
	 */
	public void save(Object entity);
	
	/**
	 * ����ʵ�����
	 * @param entity
	 */
	public void update(Object entity);
	/**
	 * ͨ��ID����ʵ�����
	 * @param entity
	 */
	public void update(String id,Object entity);
	/**
	 * ɾ��ʵ���� ֧�ּ���ɾ��
	 * @param entity
	 */
	public void delete(Object entity);
	/**
	 * ͨ��ʵ���IDɾ��ʵ�����  ��֧�ּ���ɾ��
	 * @param entityClass ʵ����
	 * @param entityId
	 */
	public <T> void delete(Class<T> entityClass,Object entityId);
	
	/**
	 * ɾ��ʵ��ID���飬ɾ�����ʵ�����
	 * @param entityClass ʵ����
	 * @param entityIds
	 */
	public <T> void delete(Class<T> entityClass,Object[] entityIds);
	
	/**
	 * ͨ��ʵ����ID ����ʵ��
	 * @param entityClass ʵ����
	 * @param entityId ʵ�����ID
	 * @return
	 */
	public <T> T find(Class<T> entityClass,Object entityId);
	
	public <T> T getSingleDate(Class<T> entityClass,String whereStr,ArrayList<Object> queryParames);
	
	/**
	 * ��ȡ��ҳ����
	 * @param entityClass   ʵ����
	 * @param firstIndex	��ʼ����
	 * @param maxReasult	��Ҫһ�β�ѯ�ļ�¼����
	 * @param whereStr      hql����е� where ������䣬д��ʱ�����ڴ� where �ؼ���
	 * @param orderBy       ����ʱ�ο����ֶ�
	 * @param queryParames  ��ѯ������������̬�󶨵ģ���ֹsqlע��
	 * 
	 * ��map��Ŀ���ǽ��ж��ط�������
	 * LinkedHashMap�����Ⱥ�˳��ģ�����ӽ�ȥ���Ⱥ�˳��
	 * order by key1 desc ,key2 asc,key3````
	 * Map���ϵ�Keyֵ������������ֶΣ�valueֵ������ָ��desc��asc
	 * 
	 * @return
	 */
	public <T> QueryResult<T> getScrollData(Class<T> entityClass, int firstIndex,int maxReasult, 
			String whereStr, ArrayList<Object> queryParames, LinkedHashMap<String, String> orderBy);
	
	public <T> QueryResult<T> getScrollData(Class<T> entityClass,String whereStr, ArrayList<Object> queryParames);
	
	public <T> QueryResult<T> getScrollData(Class<T> entityClass, int firstIndex,int maxReasult);
	
	public <T> QueryResult<T> getScrollData(Class<T> entityClass, int firstIndex,int maxReasult, 
			String whereStr, ArrayList<Object> queryParames);
	
	public <T> QueryResult<T> getScrollData(Class<T> entityClass, int firstIndex,int maxReasult, 
			LinkedHashMap<String, String> orderBy);
}





















