package br.appsmartbusiness.persistencia.dao.facede;

import java.util.List;

import br.com.app.smart.business.dao.interfaces.Entidade;


public interface IFacedeDAO<E extends Entidade> {

	public E registrar(E enEidade);

	public void registrarLista(List<E> list);

	public E editar(E enEidade);

	public void remover(E enEidade);

	public E buscar(Object id);

	public List<E> buscarTodos();

	public List<E> buscarPorIntervalo(int[] range);

	public long count();

}
