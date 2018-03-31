package br.appsmartbusiness.persistencia.dao.facede;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.app.smart.business.dao.interfaces.Entidade;

public abstract class AbstractFacade<T extends Entidade> implements IFacedeDAO {

	private Class<T> entityClass;

	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected abstract EntityManager getEntityManager();

	public Entidade registrar(Entidade entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public void registrarLista(List list) {

		for (Object t : list) {
			registrar((Entidade) t);
		}
	}

	public Entidade editar(Entidade entity) {
		getEntityManager().merge(entity);

		return entity;
	}

	public void removerLista(List list) {

		for (Object t : list) {
			remover((Entidade) t);
		}
	}

	public void remover(Entidade entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
	}

	public T buscar(Object id) {
		T resultado = getEntityManager().find(entityClass, id);
		return resultado;

	}

	public List<T> buscarTodos() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));

		List<T> resultado = getEntityManager().createQuery(cq).getResultList();

		return resultado;
	}

	public List<T> buscarPorIntervalo(int[] range) {

		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);

		List<T> resultado = q.getResultList();
		return resultado;
	}

	public long count() {

		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(getEntityManager().getCriteriaBuilder().count(rt));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		long resultado = ((Long) q.getSingleResult()).longValue();
		return resultado;
	}

	public List<?> queryList(String queryText, Object[] parameters) {
		return query(queryText, parameters).getResultList();
	}

	public void executeUpdate(String sqlCommand, Object[] parameters) {
		Query query = query(sqlCommand, parameters);
		query.executeUpdate();
	}

	private Query query(String queryText, Object[] parameters) {
		Query query = getEntityManager().createQuery(queryText);
		if (parameters != null) {
			int i = 1;
			for (Object parameter : parameters) {
				if (parameter == null)
					throw new IllegalArgumentException(
							"Binding parameter at position " + i + " can not be null: " + queryText);
				query.setParameter(i, parameter);
				i++;
			}
		}
		return query;
	}

}
