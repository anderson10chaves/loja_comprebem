package adcsistemas.loja_comprebem.model.DadosPagamentoSaasAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal de retorno da cobranca API Asaas
 */

public class CobrancaGeradaSaasApiDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String object;
	
	private boolean hasMore;
	
	private Integer totalCount;
	
	private Integer limit;
	
	private Integer offset;
	
	private List<CobrancaGeradaSaasDataDTO> data = new ArrayList<CobrancaGeradaSaasDataDTO>();

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public List<CobrancaGeradaSaasDataDTO> getData() {
		return data;
	}

	public void setData(List<CobrancaGeradaSaasDataDTO> data) {
		this.data = data;
	}

}
