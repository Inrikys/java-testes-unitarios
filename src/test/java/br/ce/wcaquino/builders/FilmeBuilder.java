package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {
	
	private Filme filme;
	
	private FilmeBuilder() {}
	
	// chaining method, permite encadear com outros m�dotos
	// por ser um m�todo est�tico, ser� o m�todo de "entrada"
	// ser� chamado antes dos outros, permitindo assim o encadeamento
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(2);
		builder.filme.setNome("Filme 1");
		builder.filme.setPrecoLocacao(5.0);
		
		return builder;
	}
	
	// Outro m�todo est�tico, por�m com outro cen�rio
	// podemos criar diversos cen�rios de entrada
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(0);
		builder.filme.setNome("Filme 1");
		builder.filme.setPrecoLocacao(5.0);
		
		return builder;
	}
	
	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		// Retorna a pr�pria inst�ncia de Filme Builder
		return this;
	}
	
	public FilmeBuilder comValor(Double valor) {
		filme.setPrecoLocacao(valor);
		return this;
	}
	
	public Filme agora() {
		return filme;
	}
}
