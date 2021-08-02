package br.ce.wcaquino.builders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoBuilder {

	private Locacao locacao;

	private LocacaoBuilder() {
	}

	public static LocacaoBuilder umLocacao() throws FilmeSemEstoqueException, LocadoraException {
		LocacaoBuilder builder = new LocacaoBuilder();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		builder.locacao = new Locacao();
		builder.locacao.setFilmes(filmes);
		builder.locacao.setUsuario(usuario);
		builder.locacao.setDataLocacao(new Date());
		builder.locacao.setDataRetorno(DataUtils.adicionarDias(new Date(), 1));
		builder.locacao.setValor(5.0);
		
		return builder;
	}
	
	public LocacaoBuilder comUsuario(Usuario usuario) {
		locacao.setUsuario(usuario);
		return this;
	}
	
	public LocacaoBuilder comListaFilmes(Filme... filmes) {
		locacao.setFilmes(Arrays.asList(filmes));
		return this;
	}
	
	public LocacaoBuilder comDataLocacao(Date dataLocacao) {
		locacao.setDataLocacao(dataLocacao);
		return this;
	}
	
	public LocacaoBuilder comDataRetorno(Date dataRetorno) {
		locacao.setDataRetorno(dataRetorno);
		return this;
	}

	public Locacao agora() {
		return locacao;
	}

}
