package br.app.infra.persistencia.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import br.app.infra.base.dto.TipoOperacaoFiltroDTO;
import br.app.util.PatternUtil;

public class ConsultaCriteriaBuilder {

	public static <T> CriteriaQuery<T> criarConsulta(EntityManager em, Class<T> clazz, Map<String, String> campoValores,
			TipoOperacaoFiltroDTO tipoOperacao) {

		if (campoValores == null || campoValores.isEmpty() || tipoOperacao == null) {

			return null;
		}
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		query.select(root);
		Set<String> campos = campoValores.keySet();
		List<Predicate> predicates = new ArrayList<Predicate>();

		switch (tipoOperacao) {
		case IGUAL:

			for (String campo : campos) {
				Expression expressao = root.get(campo);
				Predicate predicate = builder.equal(root.get(campo), campoValores.get(campo));
				predicates.add(predicate);
			}
			break;
		case CONTEM:

			for (String campo : campos) {

				String pattern = "%" + campoValores.get(campo) + "%";
				Expression expressao = root.get(campo);
				Predicate predicate = builder.like(expressao, pattern);
				predicates.add(predicate);

			}
			break;
		case INICIA_COM:

			for (String campo : campos) {

				String pattern = campoValores.get(campo) + "%";
				Expression expressao = root.get(campo);
				Predicate predicate = builder.like(expressao, pattern);
				predicates.add(predicate);

			}
			break;
		case TERMINA_COM:

			for (String campo : campos) {

				String pattern = "%" + campoValores.get(campo);
				Expression expressao = root.get(campo);
				Predicate predicate = builder.like(expressao, pattern);
				predicates.add(predicate);

			}
			break;

		case DIFERENTE:

			for (String campo : campos) {
				Expression expressao = root.get(campo);
				Predicate predicate = builder.notEqual(root.get(campo), campoValores.get(campo));
				predicates.add(predicate);
			}
			break;

		case ENTRE:

			Iterator<String> iterator = campos.iterator();

			for (String campo : campos) {

				String intervalos = campoValores.get(campo);
				String[] valores = intervalos.split(",");
				Expression expressao = root.get(campo);

				Predicate predicate = null;
				if (PatternUtil.isNumberPositivo(valores[0])) {
					predicate = builder.between(expressao, Long.valueOf(valores[0]), Long.valueOf(valores[1]));

				} else if (PatternUtil.isDataDDMMAAA(valores[0])) {
					predicate = builder.between(expressao, Long.valueOf(valores[0]), Long.valueOf(valores[1]));
				} else {

					predicate = builder.between(expressao, valores[0], valores[1]);

				}

				predicates.add(predicate);
			}
			break;

		default:
			break;
		}
		Object[] objetos = predicates.toArray();
		Predicate[] aPridicates = new Predicate[objetos.length];
		for (int i = 0; i < objetos.length; i++) {
			aPridicates[i] = (Predicate) objetos[i];
		}

		query.where(aPridicates);
		return query;
	}
	
	
	
}
