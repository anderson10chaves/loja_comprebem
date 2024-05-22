package adcsistemas.loja_comprebem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.Acesso;
import adcsistemas.loja_comprebem.repository.AcessoRepository;
import adcsistemas.loja_comprebem.service.AcessoService;

@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;

	@Autowired
	private AcessoRepository acessoRepository;

	@ResponseBody
	@PostMapping(value = "/salvarAcesso")
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionLojaComprebem {

		if (acesso.getId() != null) {
			List<Acesso> acessos = acessoRepository.buscarAcessoDesc(acesso.getDescricao().toUpperCase());

			if (!acessos.isEmpty()) {
				throw new ExceptionLojaComprebem("Acesso já existe com essa descrição: " + acesso.getDescricao());
			}
		}

		Acesso acessoSalvo = acessoService.save(acesso);

		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/deleteAcesso")
	public ResponseEntity<String> deleteAcesso(@RequestBody Acesso acesso) {
		
		if(acessoRepository.findById(acesso.getId()).isPresent() == false) {
			return new ResponseEntity<String>("Acesso já removido", HttpStatus.OK);
		}

		acessoRepository.deleteById(acesso.getId());

		return new ResponseEntity<String>(new Gson().toJson("Acesso excluído com sucesso"), HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteAcessoId/{id}")
	public ResponseEntity<String> deleteAcessoId(@PathVariable("id") Long id) {
		
		if(acessoRepository.findById(id).isPresent() == false) {
			return new ResponseEntity<String>("Acesso já removido", HttpStatus.OK);
		}

		acessoRepository.deleteById(id);

		return new ResponseEntity<String>(new Gson().toJson("Acesso excluído com sucesso"), HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/consultaAcessoId/{id}")
	public ResponseEntity<?> consultaAcessoId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		Acesso acesso = acessoRepository.findById(id).orElse(null);

		if (acesso == null) {
			throw new ExceptionLojaComprebem("Acesso não encontrado com código: " + id);
		}

		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/consultaAcessoDesc/{descricao}")
	public ResponseEntity<List<Acesso>> consultaAcessoDesc(@PathVariable("desc") String descricao) {

		List<Acesso> acesso = acessoRepository.buscarAcessoDesc(descricao.toUpperCase());

		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/listaAcesso/{idEmpresa}/{pagina}")
	public ResponseEntity<List<Acesso>> page(@PathVariable("idEmpresa") Long idEmpresa,
			@PathVariable("pagina") Integer pagina){
		
		Pageable pageable = PageRequest.of(pagina, 6, Sort.by("descricao"));
		
		List<Acesso> lista = acessoRepository.findPage(idEmpresa, pageable);
		
		return new ResponseEntity<List<Acesso>>(lista, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "/qtdPaginaAcesso/{idEmpresa}")
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa){
		Integer qtdPagina = acessoRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaAcessoDescEmpresa/{descricao}/{empresa}")
	public ResponseEntity<List<Acesso>> pesquisaAcessoDescEmpresa(@PathVariable("descricao") String descricao,
			@PathVariable("empresa") Long empresa) {
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDescEmpresa(descricao.toUpperCase(), empresa);

		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}
}
