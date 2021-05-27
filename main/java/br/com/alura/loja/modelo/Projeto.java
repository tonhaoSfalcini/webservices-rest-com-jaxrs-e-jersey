package br.com.alura.loja.modelo;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

public class Projeto {

	private long id;
	private String nome;
	private Integer anoInicio;

	public Projeto() {
		
	}
	
	public Projeto(long id, String nome, Integer anoInicio) {
		super();
		this.id = id;
		this.nome = nome;
		this.anoInicio = anoInicio;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getAnoInicio() {
		return anoInicio;
	}

	public void setAnoInicio(Integer anoInicio) {
		this.anoInicio = anoInicio;
	}

	public String toXML() {
		return new XStream().toXML(this);
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}

}