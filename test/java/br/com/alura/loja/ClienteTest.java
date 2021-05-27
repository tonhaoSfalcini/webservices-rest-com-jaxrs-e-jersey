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
import com.thoughtworks.xstream.XStream;

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
		this.target = client.target("http://www.mocky.io");
	}
	
	@After
	public void serverStop() {
		server.serverStop();
	}
	
	@Test
	public void testaQueAConexaoComOServidorFunciona() {
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
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
		String conteudo = this.target.path("/carrinhos/1").request().get(String.class);
		System.out.println(conteudo);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
		
		Carrinho carrinho = ((Carrinho)new XStream().fromXML(conteudo));
		Assert.assertEquals(carrinho.getRua(), ("Rua Vergueiro 3185, 8 andar"));
	}
	
	@Test
	public void testaQueBuscarUmProjetoTrazOProjetoEsperado() {
		String conteudo = this.target.path("/projetos/1").request().get(String.class);
		System.out.println(conteudo);
		Assert.assertTrue(conteudo.contains("<nome>Minha loja"));
		
		Projeto projeto = ((Projeto)new XStream().fromXML(conteudo));
		Assert.assertEquals(projeto.getNome(), ("Minha loja"));
		
	}

	@Test
	public void testaInserirUmCarrinho() {
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        String xml = carrinho.toXML();

        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

        Response response = this.target.path("/carrinhos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
	}

}
