package br.ce.wcaquino.servicos;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testeLocacao() throws Exception {

		// Cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// A��o
		Locacao locacao = service.alugarFilme(usuario, filme);

		// Verifica��o
		// Error collector
		error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				CoreMatchers.is(true));

	}

	// VERIFICAR SE EST� TRATANDO EXCE��ES
	// Forma elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {

		// Cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		// A��o
		service.alugarFilme(usuario, filme);

		// Verifica��o
		// Atrav�s da Exception
	}

	// Forma robusta
	// Captura exce��o e verifica mensagem
	@Test
	public void testeLocacao_filmeSemEstoque_2() {

		// Cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		try {
			// A��o
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lan�ado uma exce��o");
		} catch (Exception e) {
			// Verifica��o
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}
	}

	// Forma recente
	@Test
	public void testeLocacao_filmeSemEstoque_3() throws Exception {

		// Cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		// Verifica��o antes da a��o (O que � esperado da pr�xima a��o)
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// A��o
		service.alugarFilme(usuario, filme);
	}

	// Forma Robusta
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {

		// Cenario
		LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 1", 3, 5.0);

		try {
			service.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usu�rio vazio"));
		}
	}
	
	// Forma recente
	@Test
	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
		// Cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		
		// Verifica��o antes da a��o (O que � esperado da pr�xima a��o)
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		// A��o
		service.alugarFilme(usuario, null);

	}

}
