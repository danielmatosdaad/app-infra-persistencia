package br.appsmartbusiness.persistencia.service;

import java.util.ArrayList;
import java.util.List;

import br.appsmartbusiness.persistencia.conversores.Conversor;
import br.appsmartbusiness.persistencia.dao.facede.IFacedeDAO;
import br.com.app.smart.business.dao.interfaces.Entidade;
import br.com.app.smart.business.dto.DTO;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;

public class ServiceDAO {

	public static <T extends DTO, E extends Entidade> T adiconar(IFacedeDAO<E> facedeDAO, Class<E> classeEntidade,
			T dto) throws InfraEstruturaException, NegocioException {

		E entidadeConvertida = Conversor.converter(dto, classeEntidade);
		facedeDAO.registrar(entidadeConvertida);
		dto.setId(entidadeConvertida.getId());
		return dto;
	}

	public static <T extends DTO, E extends Entidade> List adiconar(IFacedeDAO<E> facedeDAO, Class<E> classeEntidade,
			List listaDto) throws InfraEstruturaException, NegocioException {

		for (Object object : listaDto) {
			adiconar(facedeDAO, classeEntidade, (T) object);
		}

		return listaDto;
	}

	public static <T extends DTO, E extends Entidade> T alterar(IFacedeDAO<E> facedeDAO, Class<E> classeEntidade, T dto)
			throws InfraEstruturaException, NegocioException {
		E entidadeConvertida = Conversor.converter(dto, classeEntidade);
		facedeDAO.editar(entidadeConvertida);
		return dto;
	}

	public static <T extends DTO, E extends Entidade> void remover(IFacedeDAO<E> facedeDAO, Class<E> classeEntidade,
			T dto) throws InfraEstruturaException, NegocioException {

		E entidadeConvertida = Conversor.converter(dto, classeEntidade);
		facedeDAO.remover(entidadeConvertida);

	}

	public <T extends DTO, E extends Entidade> void removerPorId(IFacedeDAO<E> facedeDAO, Long id)
			throws InfraEstruturaException, NegocioException {
		// TODO Auto-generated method stub

	}

	public static <T extends DTO, E extends Entidade> List bustarTodos(IFacedeDAO<E> facedeDAO, Class<T> classeDTO)
			throws InfraEstruturaException, NegocioException {

		List<Entidade> lista = (List<Entidade>) facedeDAO.buscarTodos();

		List<T> listaDTO = new ArrayList<T>();

		for (Entidade e : lista) {
			T dtoConvertido = Conversor.converter(e, classeDTO);
			listaDTO.add(dtoConvertido);
		}

		return listaDTO;
	}

	public static <T extends DTO, E extends Entidade> T bustarPorID(IFacedeDAO<E> facedeDAO, Class<T> classeDTO,
			Long id) throws InfraEstruturaException, NegocioException {
		Entidade e = facedeDAO.buscar(id);
		if (e != null) {

			T dto = Conversor.converter(e, classeDTO);
			return dto;
		}
		return null;
	}

	public static <T extends DTO, E extends Entidade> List<T> bustarPorIntervaloID(IFacedeDAO<E> facedeDAO,
			Class<T> classeDTO, int[] range) throws InfraEstruturaException, NegocioException {

		List<Entidade> lista = (List<Entidade>) facedeDAO.buscarPorIntervalo(range);
		List<T> listaDTO = new ArrayList<T>();

		for (Entidade e : lista) {
			T dto = Conversor.converter(e, classeDTO);
			listaDTO.add(dto);
		}

		return listaDTO;
	}

}
