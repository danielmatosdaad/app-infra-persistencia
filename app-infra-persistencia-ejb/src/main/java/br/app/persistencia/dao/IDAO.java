package br.app.persistencia.dao;

import java.util.List;

import br.app.infra.base.exception.InfraEstruturaException;
import br.app.infra.base.exception.NegocioException;
import br.app.infra.base.interfaces.Entidade;

public interface IDAO<E extends Entidade> {

	public E registrar(E enEidade) throws InfraEstruturaException, NegocioException;

	public void registrarLista(List<E> list) throws InfraEstruturaException, NegocioException;

	public E editar(E enEidade) throws InfraEstruturaException, NegocioException;

	public void remover(E enEidade) throws InfraEstruturaException, NegocioException;

	public E buscar(Object id) throws InfraEstruturaException, NegocioException;

	public List<E> buscarTodos() throws InfraEstruturaException, NegocioException;

	public List<E> buscarPorIntervalo(int[] range) throws InfraEstruturaException, NegocioException;

	public long count() throws InfraEstruturaException, NegocioException;

}
