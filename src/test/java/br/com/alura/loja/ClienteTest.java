package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alura.loja.Servidor;
import com.google.gson.Gson;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;

public class ClienteTest {

	Servidor server;
	Client client;
	WebTarget target;
	
	@Before
	public void startServer() {
		this.server = new Servidor();
		this.server.serverStart(server.getUri(), server.getResourceConfig());
		
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
		this.client = ClientBuilder.newClient(config);
		this.target = client.target("http://localhost:8888");
	}
	
	@After
	public void serverStop() {
		server.serverStop();
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperadoEmJson() {
		String conteudo = this.target.path("/carrinhos/json/1").request().get(String.class);
		System.out.println(conteudo);
		
		Carrinho carrinho = new Gson().fromJson(conteudo, Carrinho.class);
		Assert.assertEquals(carrinho.getRua(), ("Rua Vergueiro 3185, 8 andar"));
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		Carrinho carrinho = this.target.path("/carrinhos/1").request().get(Carrinho.class);
		Assert.assertEquals(carrinho.getRua(), ("Rua Vergueiro 3185, 8 andar"));
	}
	
	@Test
	public void testaQueBuscarUmProjetoTrazOProjetoEsperado() {
		Projeto projeto = this.target.path("/projetos/1").request().get(Projeto.class);
		Assert.assertEquals(projeto.getNome(), ("Minha loja"));
		
	}

	@Test
	public void testaInserirUmCarrinho() {
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");

        Entity<Carrinho> entity = Entity.entity(carrinho, MediaType.APPLICATION_XML);

        Response response = this.target.path("/carrinhos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
	}
	
	@Test
	public void testaAlterarUmProduto() {
		Carrinho carrinho = new CarrinhoDAO().busca(1L);
		Produto produto = new Produto(3467L, "Videogame 4", 4000, 327);
		
		String xml = produto.toXML();
		
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		
		Response response = this.target.path("/carrinhos/"+carrinho.getId()+"/produtos/"+produto.getId()).request().put(entity);
		Assert.assertEquals(200, response.getStatus());
		
		Carrinho carrinhoNovo = this.target.path("carrinhos/"+carrinho.getId()).request().get(Carrinho.class);
		Assert.assertEquals(carrinhoNovo.getProdutos().get(1).getQuantidade(), 327);
	}

}
