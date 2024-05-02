package adcsistemas.loja_comprebem;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import adcsistemas.loja_comprebem.utils.ValidaCnpj;
import adcsistemas.loja_comprebem.utils.ValidaCpf;

@Profile("test")
@SpringBootTest(classes = LojaComprebemApplication.class)
public class TesteCPFCNPJ {

	public static void main(String[] args) {
		boolean isCnpj = ValidaCnpj.isCNPJ("28.058.787/0001-61");

		System.out.println("CNPJ Válido : " + isCnpj);

		boolean isCpf = ValidaCpf.isCPF("345.385.028-98");

		System.out.println("CPF Válido : " + isCpf);
	}

}

