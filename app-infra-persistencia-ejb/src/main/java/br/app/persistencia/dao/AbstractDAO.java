package br.app.persistencia.dao;

import java.util.List;
import java.util.Map;

import br.app.infra.base.dto.TipoOperacaoFiltroDTO;
import br.app.infra.base.exception.InfraEstruturaException;
import br.app.infra.base.exception.NegocioException;
import br.app.infra.base.interfaces.EntidadeVO;
import br.app.infra.persistencia.builder.ConsultaCriteriaBuilder;
import br.app.infra.persistencia.conversores.Conversor;
import br.app.persistencia.dao.IDAO;
import br.app.infra.base.interfaces.Entidade;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class AbstractDAO<T extends Entidade> implements IDAO<T> {

	private Class<T> entityClass;

	public AbstractDAO(Class<T> entityClass) {
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

	public void removerComCriteria(CriteriaDelete<T> criteria) {
		int result = getEntityManager().createQuery(criteria).executeUpdate();
	}

	public void removerPorId(long id) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaDelete<T> delete = cb.createCriteriaDelete(this.entityClass);
		CriteriaQuery<T> criteria = cb.createQuery(this.entityClass);
		Root<T> root = criteria.from(this.entityClass);
		delete.where(root.get("id").in(id));
		removerComCriteria(delete);
	}

	public T buscarPorId(long id) {

		return getEntityManager().find(this.entityClass, id);
	}

	@Override
	public List<T> buscarTodos() {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(this.entityClass);

		Root<T> from = query.from(this.entityClass);
		CriteriaQuery<T> select = query.select(from);

		TypedQuery<T> typedQuery = getEntityManager().createQuery(select);
		List<T> results = typedQuery.getResultList();
		return results;
	}

	public List<T> listaComCriteria(CriteriaQuery<T> criteria) {
		return getEntityManager().createQuery(criteria).getResultList();
	}

	public List<T> listaPaginadaComCriteria(CriteriaQuery<T> criteria, int pagina, int resultados) {
		TypedQuery<T> query = getEntityManager().createQuery(criteria);
		query.setFirstResult((pagina - 1) * resultados);
		query.setMaxResults(resultados);
		return query.getResultList();
	}

	public T consultaComCriteria(CriteriaQuery<T> criteria) {
		return getEntityManager().createQuery(criteria).getSingleResult();
	}

	public List<T> listaPaginadaComFiltro(Map<String, String> campoValores, TipoOperacaoFiltroDTO tipoOperacao,
			int pagina, int resultados) {
		if (campoValores == null || campoValores.isEmpty() || tipoOperacao == null) {

			return null;
		}
		CriteriaQuery<T> criteria = ConsultaCriteriaBuilder.criarConsulta(getEntityManager(), entityClass, campoValores,
				tipoOperacao);
		TypedQuery<T> query = getEntityManager().createQuery(criteria);
		query.setFirstResult((pagina - 1) * resultados);
		query.setMaxResults(resultados);
		return query.getResultList();
	}

	public List<T> filtrar(Map<String, String> campoValores, TipoOperacaoFiltroDTO tipoOperacao) {

		if (campoValores == null || campoValores.isEmpty() || tipoOperacao == null) {

			return null;
		}
		CriteriaQuery<T> criteria = ConsultaCriteriaBuilder.criarConsulta(getEntityManager(), entityClass, campoValores,
				tipoOperacao);

		return listaComCriteria(criteria);
	}

}
