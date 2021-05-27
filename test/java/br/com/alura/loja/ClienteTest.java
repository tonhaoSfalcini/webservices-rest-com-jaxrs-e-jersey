package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	
	@Before
	public void startServer() {
		server = new Servidor();
		server.serverStart(server.getUri(), server.getResourceConfig());
	}
	
	@After
	public void serverStop() {
		server.serverStop();
	}
	
	@Test
	public void testaQueAConexaoComOServidorFunciona() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://www.mocky.io");
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperadoEmJson() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8888");
		String conteudo = target.path("/carrinhos/json/1").request().get(String.class);
		System.out.println(conteudo);
		
		Carrinho carrinho = new Gson().fromJson(conteudo, Carrinho.class);
		Assert.assertEquals(carrinho.getRua(), ("Rua Vergueiro 3185, 8 andar"));
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8888");
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		System.out.println(conteudo);
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
		
		Carrinho carrinho = ((Carrinho)new XStream().fromXML(conteudo));
		Assert.assertEquals(carrinho.getRua(), ("Rua Vergueiro 3185, 8 andar"));
	}
	
	@Test
	public void testaQueBuscarUmProjetoTrazOProjetoEsperado() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8888");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		System.out.println(conteudo);
		Assert.assertTrue(conteudo.contains("<nome>Minha loja"));
		
		Projeto projeto = ((Projeto)new XStream().fromXML(conteudo));
		Assert.assertEquals(projeto.getNome(), ("Minha loja"));
		
	}

	@Test
	public void testaInserirUmCarrinho() {
		Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8888");
        
        Carrinho carrinho = new Carrinho();
        carrinho.adiciona(new Produto(314L, "Tablet", 999, 1));
        carrinho.setRua("Rua Vergueiro");
        carrinho.setCidade("Sao Paulo");
        String xml = carrinho.toXML();

        Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

        Response response = target.path("/carrinhos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
	}

}
