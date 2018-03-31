package br.app.infra.persistencia.service;

import java.util.ArrayList;
import java.util.List;

import br.app.infra.base.exception.InfraEstruturaException;
import br.app.infra.base.exception.NegocioException;
import br.app.infra.base.interfaces.EntidadeVO;
import br.app.infra.persistencia.conversores.Conversor;
import br.app.persistencia.dao.IDAO;
import br.app.infra.base.interfaces.Entidade;

public class ServiceDAO {

	public static <T extends EntidadeVO, E extends Entidade> T adiconar(IDAO<E> dao, Class<E> classeEntidade,
			T entidadeVO) throws InfraEstruturaException, NegocioException {

		E entidadeConvertida = Conversor.converter(entidadeVO, classeEntidade);
		dao.registrar(entidadeConvertida);
		entidadeVO.setId(entidadeConvertida.getId());
		return entidadeVO;
	}

	public static <T extends EntidadeVO, E extends Entidade> List<T> adiconar(IDAO<E> dao, Class<E> classeEntidade,
			List<T> listaEntidadeVO) throws InfraEstruturaException, NegocioException {

		for (T entidade : listaEntidadeVO) {
			adiconar(dao, classeEntidade, entidade);
		}

		return listaEntidadeVO;
	}

	public static <T extends EntidadeVO, E extends Entidade> T alterar(IDAO<E> facedeDAO, Class<E> classeEntidade,
			T dto) throws InfraEstruturaException, NegocioException {
		E entidadeConvertida = Conversor.converter(dto, classeEntidade);
		facedeDAO.editar(entidadeConvertida);
		return dto;
	}

	public static <T extends EntidadeVO, E extends Entidade> void remover(IDAO<E> dao, Class<E> classeEntidade, T dto)
			throws InfraEstruturaException, NegocioException {

		E entidadeConvertida = Conversor.converter(dto, classeEntidade);
		dao.remover(entidadeConvertida);

	}

	public <T extends EntidadeVO, E extends Entidade> void removerPorId(IDAO<E> facedeDAO, Long id)
			throws InfraEstruturaException, NegocioException {
		// TODO Auto-generated method stub

	}

	public static <T extends EntidadeVO, E extends Entidade> List<T> bustarTodos(IDAO<E> dao, Class<T> classeVO)
			throws InfraEstruturaException, NegocioException {

		List<E> lista = (List<E>) dao.buscarTodos();

		List<T> listaDTO = new ArrayList<T>();

		for (E e : lista) {
			T dtoConvertido = Conversor.converter(e, classeVO);
			listaDTO.add(dtoConvertido);
		}

		return listaDTO;
	}

	public static <T extends EntidadeVO, E extends Entidade> T bustarPorID(IDAO<E> dao, Class<T> classeVO, Long id)
			throws InfraEstruturaException, NegocioException {
		Entidade e = dao.buscar(id);
		if (e != null) {

			T dto = Conversor.converter(e, classeVO);
			return dto;
		}
		return null;
	}

	public static <T extends EntidadeVO, E extends Entidade> List<T> bustarPorIntervaloID(IDAO<E> dao,
			Class<T> classeVO, int[] range) throws InfraEstruturaException, NegocioException {

		List<E> lista = (List<E>) dao.buscarPorIntervalo(range);
		List<T> listaDTO = new ArrayList<T>();

		for (E e : lista) {
			T dto = Conversor.converter(e, classeVO);
			listaDTO.add(dto);
		}

		return listaDTO;
	}

}
