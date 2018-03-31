package br.app.persistencia.dao;

import java.util.List;

import br.app.infra.base.dto.FiltroDTO;
import br.app.infra.base.exception.InfraEstruturaException;
import br.app.infra.base.exception.InfraException;
import br.app.infra.base.exception.NegocioException;
import br.app.infra.base.interfaces.DTO;
import br.app.infra.base.interfaces.IServico;

public interface IServicoDAO<T extends DTO> extends IServico<T> {

	public void salvar(T dto) throws NegocioException, InfraEstruturaException;

	public void excluir(long id) throws NegocioException, InfraEstruturaException;

	public void atualizar(T dot) throws NegocioException, InfraEstruturaException;

	public T buscarPorId(long id) throws NegocioException, InfraEstruturaException;

	public List<T> buscarTodos() throws NegocioException, InfraEstruturaException;

	public List<T> filtrar(FiltroDTO filtro) throws NegocioException, InfraException;

	public List<T> listaComFiltro(FiltroDTO filtro, int resultados, int pagina) throws NegocioException, InfraException;

}
