package cn.djrj.jrjrm.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.djrj.jrjrm.util.QueryResult;

/**
 * Dao 层通用接口
 * @author lynn
 */
public interface BaseDao {
	
	/**
	 * 保存实体对象
	 * @param entity
	 */
	public void save(Object entity);
	
	/**
	 * 更新实体对象
	 * @param entity
	 */
	public void update(Object entity);
	/**
	 * 通过ID更新实体对象
	 * @param entity
	 */
	public void update(String id,Object entity);
	/**
	 * 删除实体类 支持级联删除
	 * @param entity
	 */
	public void delete(Object entity);
	/**
	 * 通过实体的ID删除实体对象  不支持级联删除
	 * @param entityClass 实体类
	 * @param entityId
	 */
	public <T> void delete(Class<T> entityClass,Object entityId);
	
	/**
	 * 删除实体ID数组，删除多个实体对象
	 * @param entityClass 实体类
	 * @param entityIds
	 */
	public <T> void delete(Class<T> entityClass,Object[] entityIds);
	
	/**
	 * 通过实体类ID 查找实体
	 * @param entityClass 实体类
	 * @param entityId 实体对象ID
	 * @return
	 */
	public <T> T find(Class<T> entityClass,Object entityId);
	
	public <T> T getSingleDate(Class<T> entityClass,String whereStr,ArrayList<Object> queryParames);
	
	/**
	 * 获取分页数据
	 * @param entityClass   实体类
	 * @param firstIndex	开始索引
	 * @param maxReasult	需要一次查询的记录条数
	 * @param whereStr      hql语句中的 where 条件语句，写的时候不用在带 where 关键字
	 * @param orderBy       分组时参考的字段
	 * @param queryParames  查询参数，用来动态绑定的，防止sql注入
	 * 
	 * 用map的目的是进行多重分组排序
	 * LinkedHashMap类有先后顺序的，安添加进去的先后顺序
	 * order by key1 desc ,key2 asc,key3````
	 * Map集合的Key值是用来排序的字段，value值是用来指明desc或asc
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





















