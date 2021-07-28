package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {
	
	private Filme filme;
	
	private FilmeBuilder() {}
	
	// chaining method, permite encadear com outros médotos
	// por ser um método estático, será o método de "entrada"
	// será chamado antes dos outros, permitindo assim o encadeamento
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(2);
		builder.filme.setNome("Filme 1");
		builder.filme.setPrecoLocacao(5.0);
		
		return builder;
	}
	
	// Outro método estático, porém com outro cenário
	// podemos criar diversos cenários de entrada
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
		// Retorna a própria instância de Filme Builder
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
