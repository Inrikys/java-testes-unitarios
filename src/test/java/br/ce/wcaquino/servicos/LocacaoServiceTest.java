package br.ce.wcaquino.servicos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
	
	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	// Executa antes dos testes
	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	// Executa depois dos testes
	@After
	public void tearDown() {
	}
	
	// Executa antes da classe LocacaoServiceTest ser instanciada
	@BeforeClass
	public static void setupClass() {
	}
	
	// Executa depois da classe LocacaoServiceTest ser destruida
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Test
	public void testeLocacao() throws Exception {

		// Cenario
		service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		
		// Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		// Error collector
		error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				CoreMatchers.is(true));

	}

	// VERIFICAR SE ESTÁ TRATANDO EXCEÇÕES
	// Forma elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {

		// Cenario
		Usuario usuario = new Usuario("Usuario 1");		
		Filme filme = new Filme("Filme 1", 0, 5.0);
		
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		// Ação
		service.alugarFilme(usuario, filmes);

		// Verificação
		// Através da Exception
	}

	// Forma robusta
	// Captura exceção e verifica mensagem
	@Test
	public void testeLocacao_filmeSemEstoque_2() {

		// Cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		try {
			// Ação
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lançado uma exceção");
		} catch (Exception e) {
			// Verificação
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}
	}

	// Forma recente
	@Test
	public void testeLocacao_filmeSemEstoque_3() throws Exception {

		// Cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
		
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		// Verificação antes da ação (O que é esperado da próxima ação)
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// Ação
		service.alugarFilme(usuario, filmes);
	}

	// Forma Robusta
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {

		// Cenario
		Filme filme = new Filme("Filme 1", 3, 5.0);
		
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuário vazio"));
		}
	}
	
	// Forma recente
	@Test
	public void testLocacao_filmeVazio() throws FilmeSemEstoqueException, LocadoraException {
		
		// Cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		// Verificação antes da ação (O que é esperado da próxima ação)
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		// Ação
		service.alugarFilme(usuario, null);

	}

}
