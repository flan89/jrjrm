package cn.djrj.jrjrm.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.djrj.jrjrm.util.QueryResult;

/**
 * 对通用Dao层的实现
 * @author lynn
 */
@Repository
@Transactional
public abstract class DaoSupport  implements BaseDao {
	

	protected HibernateTemplate hibernateTemplate;
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

	@Override
	public void save(Object entity) {
		this.hibernateTemplate.save(entity);
	}

	@Override
	public void update(Object entity) {
		this.hibernateTemplate.update(entity);
	}
	
	@Override
	public void update(String id,Object entity){
		this.hibernateTemplate.update(id,entity);
	}
	
	@Override
	public void delete(Object entity){
		this.hibernateTemplate.delete(entity);
	}
	
	@Override
	public <T> void delete(final Class<T> entityClass, final Object entityId) {
		
		this.hibernateTemplate.execute(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException,SQLException {
				
				Query query=session.createQuery("delete from "+entityClass.getName()+" o where o.id=?");
				query.setParameter(0, entityId);
				query.executeUpdate();
				return null;
			}
		});
	}

	@Override
	public <T> void delete(final Class<T> entityClass, final Object[] entityIds) {
		this.hibernateTemplate.execute(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException,SQLException {
				
				StringBuffer tempWhere=new StringBuffer();
				String whereStr=null;
				for(int i=0;i<entityIds.length;i++){
					tempWhere.append("?,");
				}
				whereStr=tempWhere.toString().substring(0, tempWhere.toString().length()-1);
				String hql="delete from "+entityClass.getName()+" o where o.id in ("+whereStr+")";
						
				Query query=session.createQuery(hql);
				
				for(int i=0;i<entityIds.length;i++){
					query.setParameter(i, entityIds[i]);
				}
				
				query.executeUpdate();
				return null;
			}
		});
	}

	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public <T> T find(Class<T> entityClass, Object entityId) {
		return this.hibernateTemplate.get(entityClass,  (Serializable) entityId);
	}
	
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public <T> T getSingleDate(final Class<T> entityClass, final String whereStr,final ArrayList<Object> queryParames) {
		
		
		return this.hibernateTemplate.execute(new HibernateCallback<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T doInHibernate(Session session) throws HibernateException,SQLException {
				
				String hql=" select o from "+entityClass.getName()+" o "+(whereStr == null ? "" :" where "+ whereStr );
				Query query=session.createQuery(hql);
				
				if(whereStr!=null && queryParames !=null){
					for(int i=0;i<queryParames.size();i++){
						query.setParameter(i, queryParames.get(i));
					}
				}
				return (T) query.uniqueResult();
			}
		});
	}

	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public <T> QueryResult<T> getScrollData(final Class<T> entityClass,
			final int firstIndex, final int maxReasult, final String whereStr,
			final ArrayList<Object> queryParames, final LinkedHashMap<String, String> orderBy) {
		
		final QueryResult<T> qr=new QueryResult<T>();
		
		this.hibernateTemplate.execute(new HibernateCallback<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T doInHibernate(Session session) throws HibernateException,SQLException {
				
				String hql=" select o from "+entityClass.getName()+" o "+(whereStr == null ? "" :" where "+ whereStr )+ buildOrderBy(orderBy);
				
				Query query=session.createQuery(hql);
				
				if(whereStr!=null && queryParames !=null){
					for(int i=0;i<queryParames.size();i++){
						query.setParameter(i, queryParames.get(i));
					}
				}
				if(firstIndex != -1 && maxReasult != -1){
					query.setMaxResults(maxReasult).setFirstResult(firstIndex);
				}
				qr.setResultList(query.list());
				
				query=session.createQuery("select count(*) from "+entityClass.getName()+" o "+ (whereStr == null ? "" :" where "+ whereStr ));
				if(whereStr!=null && queryParames !=null){
					for(int i=0;i<queryParames.size();i++){
						query.setParameter(i, queryParames.get(i));
					}
				}
				qr.setTotalRecord((Long) query.uniqueResult());
				
				return null;
			}
		});
		
		return qr;
	}
	
	@Override
	public <T> QueryResult<T> getScrollData(Class<T> entityClass,String whereStr, ArrayList<Object> queryParames) {
		
		return this.getScrollData(entityClass, -1, -1, whereStr, queryParames, null);
	}


	@Override
	public <T> QueryResult<T> getScrollData(Class<T> entityClass,int firstIndex, int maxReasult) {
		return this.getScrollData(entityClass, firstIndex, maxReasult, null, null, null);
	}


	@Override
	public <T> QueryResult<T> getScrollData(Class<T> entityClass,int firstIndex, int maxReasult, String whereStr,
			ArrayList<Object> queryParames) {
		
		return this.getScrollData(entityClass, firstIndex, maxReasult, whereStr, queryParames, null);
	}


	@Override
	public <T> QueryResult<T> getScrollData(Class<T> entityClass,int firstIndex, int maxReasult,
			LinkedHashMap<String, String> orderBy) {
		
		return this.getScrollData(entityClass, firstIndex, maxReasult, null, null, orderBy);
	}


	/**
	 * 组装order by 语句
	 * @param orderBy
	 * @return
	 */
	protected String buildOrderBy(LinkedHashMap<String, String> orderBy){
		
		StringBuffer orderByql=new StringBuffer("");
		
		if(orderBy!=null && orderBy.size()>0){
			orderByql.append(" order by");
			for(String key : orderBy.keySet()){
				orderByql.append(" o.").append(key).append(" ").append(orderBy.get(key)).append(",");
			}
			orderByql.deleteCharAt(orderByql.length()-1);
		}
		return orderByql.toString();
	}
	
	
	
}

















