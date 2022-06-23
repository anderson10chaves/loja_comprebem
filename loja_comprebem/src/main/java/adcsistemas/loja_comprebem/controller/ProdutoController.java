package adcsistemas.loja_comprebem.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import adcsistemas.loja_comprebem.exception.ExceptionLojaComprebem;
import adcsistemas.loja_comprebem.model.Produto;
import adcsistemas.loja_comprebem.repository.ProdutoRepository;
import adcsistemas.loja_comprebem.service.SendEmailService;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private SendEmailService sendEmailService;

	@ResponseBody
	@PostMapping(value = "/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaComprebem, MessagingException, IOException {

		if(produto.getTipoUnidade() == null || produto.getTipoUnidade().trim().isEmpty()) {
			throw new ExceptionLojaComprebem("O Tipo da Unidade do Produto é Obrigatório");
		}
		
		if(produto.getNome().length() < 10) {
			throw new ExceptionLojaComprebem("Nome do Produto deve contér no minimo 10 caracteres");
		}
		
		if(produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaComprebem("A Empresa deve ser informada");
		}
		
		if (produto.getId() == null) {
			List<Produto> produtos = produtoRepository.pesquisaProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());
			
			if (!produtos.isEmpty()) {
				throw new ExceptionLojaComprebem("Produto já existe com essa descrição: " + produto.getNome());
			}
			
			if(produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
				throw new ExceptionLojaComprebem("A Categoria Produto dever ser informada");
			}
			
			if(produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
				throw new ExceptionLojaComprebem("A Marca Produto dever ser informada.");
			}
		}
		
		if(produto.getQtdEstoque() < 1) {
			throw new ExceptionLojaComprebem("Produto em estoque esta com 1 item.");
		}

		if(produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0) {
			throw new ExceptionLojaComprebem("Uma Imagem do Produto deve ser inserida.");
		}
		
		if(produto.getImagens().size() < 3) {
			throw new ExceptionLojaComprebem("Imagem do produto deve conter no minímo 3 e máximo 6 imagens.");
		}
		
		if(produto.getId() == null) {
			
			for(int x = 0; x < produto.getImagens().size(); x++) {
				produto.getImagens().get(x).setProduto(produto);
				produto.getImagens().get(x).setEmpresa(produto.getEmpresa());
				
				String base64Image = "";
				
				if(produto.getImagens().get(x).getImagemOriginal().contains("data:image")) {
					base64Image = produto.getImagens().get(x).getImagemOriginal().split(",")[1];
				} else {
					base64Image = produto.getImagens().get(x).getImagemOriginal();
				}
				
				byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
				
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
				
				if(bufferedImage != null) {
					
					int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
					int largura = Integer.parseInt("800");
					int altura = Integer.parseInt("600");
					
					BufferedImage resizedImage = new BufferedImage(largura, altura, type);
					Graphics2D g = resizedImage.createGraphics();
					g.drawImage(resizedImage, 0, 0, largura, altura, null);
					g.dispose();
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(resizedImage, "png", baos);
					
					String miniImgBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
					
					produto.getImagens().get(x).setImagemMiniatura(miniImgBase64);
					
					bufferedImage.flush();
					resizedImage.flush();
					baos.flush();
					baos.close();
				}
				
			}
		}
		
		Produto produtoSalvo = produtoRepository.save(produto);
		
		if(produto.getAlertaQtdEstoque()  && produto.getQtdEstoque() < 1) {
			
			StringBuilder html = new StringBuilder();
			html.append("<h2>").append("Produto: " + produto.getNome())
				.append(" estoque esta baixo: " + produto.getQtdEstoque());
			html.append("<p> Id Produto..: ").append(produto.getId()).append("</p>");
			
			if(produto.getEmpresa().getEmail() != null) {
				sendEmailService.enviarEmailHtml("Produto sem estoque", html.toString(), produto.getEmpresa().getEmail());
			}
			
			
		} 

		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "/deleteProduto")
	public ResponseEntity<?> deleteProduto(@RequestBody Produto produto) {
		
		if(produtoRepository.findById(produto.getId()).isEmpty() == false) {
			return new ResponseEntity<>("Produto já removido", HttpStatus.OK);
		}

		produtoRepository.deleteById(produto.getId());

		return new ResponseEntity<>("Produto excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "/deleteProdutoId/{id}")
	public ResponseEntity<?> deleteAcessoId(@PathVariable("id") Long id) {
		
		if(produtoRepository.findById(id).isPresent() == false) {
			return new ResponseEntity<>("Produto já removido", HttpStatus.OK);
		}

		produtoRepository.deleteById(id);

		return new ResponseEntity<>("Produto excluído com sucesso", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaProdutoId/{id}")
	public ResponseEntity<?> pesquisaProdutoId(@PathVariable("id") Long id) throws ExceptionLojaComprebem {

		Produto produto = produtoRepository.findById(id).orElse(null);

		if (produto == null) {
			throw new ExceptionLojaComprebem("Produto não encontrado com código: " + id);
		}

		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "/pesquisaProdutoDesc/{desc}")
	public ResponseEntity<List<Produto>> pesquisaProdutoDesc(@PathVariable("nome") String nome) {

		List<Produto> produto = produtoRepository.pesquisaProdutoNome(nome.toUpperCase());

		return new ResponseEntity<List<Produto>>(produto, HttpStatus.OK);
	}

}
