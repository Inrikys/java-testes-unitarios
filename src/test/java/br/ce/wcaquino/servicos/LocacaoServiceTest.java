package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;

	// MOCKS
	private SPCService spc;
	private LocacaoDAO dao;
	private EmailService email;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// Executa antes dos testes
	@Before
	public void setup() {
		service = new LocacaoService();

		dao = Mockito.mock(LocacaoDAO.class);
		// injeção de dependencia dentro de LocacaoService
		service.setLocacaoDAO(dao);

		spc = Mockito.mock(SPCService.class);
		// injeção de dependencia dentro de LocacaoService
		service.setSPCService(spc);

		email = Mockito.mock(EmailService.class);
		service.setEmailService(email);

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
	public void deveAlugarFilme() throws Exception {

		// Roda o teste apenas se a condição for verdadeira
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenario
		// Objeto instanciado na função setup
		// service = new LocacaoService();

		// Usuario usuario = new Usuario("Usuario 1");
		// Utilizando Data Builder
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		// Filme filme = new Filme("Filme 1", 2, 5.0);
		// Utilizando Data Builder
		Filme filme = FilmeBuilder.umFilme().comValor(4.0).agora();

		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		// Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		// Error collector
		error.checkThat(locacao.getValor(), CoreMatchers.is(4.0));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				CoreMatchers.is(true));

		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
	}

	// VERIFICAR SE ESTÁ TRATANDO EXCEÇÕES
	// Forma elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque_1() throws Exception {

		// Cenario
		Usuario usuario = new Usuario("Usuario 1");

		// Filme filme = new Filme("Filme 1", 0, 5.0);
		// Utilizando Data Builder
		Filme filme = FilmeBuilder.umFilme().semEstoque().agora();

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
	public void naoDeveAlugarFilmeSemEstoque_2() {

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
	public void naoDeveAlugarFilmeSemEstoque_3() throws Exception {

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
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {

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
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cenario
		Usuario usuario = new Usuario("Usuario 1");

		// Verificação antes da ação (O que é esperado da próxima ação)
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// Ação
		service.alugarFilme(usuario, null);

	}

	@Test
	public void devePagar75PcNoFilme3() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		Filme f1 = new Filme("Filme 1", 2, 4.0);
		Filme f2 = new Filme("Filme 2", 2, 4.0);
		Filme f3 = new Filme("Filme 3", 2, 4.0);

		List<Filme> filmes = Arrays.asList(f1, f2, f3);

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verificação
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(11.0));
	}

	@Test
	public void devePagar50PcNoFilme4() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		Filme f1 = new Filme("Filme 1", 2, 4.0);
		Filme f2 = new Filme("Filme 2", 2, 4.0);
		Filme f3 = new Filme("Filme 3", 2, 4.0);
		Filme f4 = new Filme("Filme 4", 2, 4.0);

		List<Filme> filmes = Arrays.asList(f1, f2, f3, f4);

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verificação
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(13.0));
	}

	@Test
	public void devePagar25PcNoFilme5() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		Filme f1 = new Filme("Filme 1", 2, 4.0);
		Filme f2 = new Filme("Filme 2", 2, 4.0);
		Filme f3 = new Filme("Filme 3", 2, 4.0);
		Filme f4 = new Filme("Filme 4", 2, 4.0);
		Filme f5 = new Filme("Filme 4", 2, 4.0);

		List<Filme> filmes = Arrays.asList(f1, f2, f3, f4, f5);

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verificação
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(14.0));
	}

	@Test
	public void devePagar0PcNoFilme6() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		Filme f1 = new Filme("Filme 1", 2, 4.0);
		Filme f2 = new Filme("Filme 2", 2, 4.0);
		Filme f3 = new Filme("Filme 3", 2, 4.0);
		Filme f4 = new Filme("Filme 4", 2, 4.0);
		Filme f5 = new Filme("Filme 5", 2, 4.0);
		Filme f6 = new Filme("Filme 6", 2, 4.0);

		List<Filme> filmes = Arrays.asList(f1, f2, f3, f4, f5, f6);

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verificação
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(14.0));
	}

	@Test
	public void naoDeveDevolverFilmeNoDomingoAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

		// Roda o teste apenas se a condição for verdadeira
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");
		Filme f1 = new Filme("Filme 1", 2, 4.0);
		List<Filme> filmes = Arrays.asList(f1);

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verificação
		// boolean isMonday = DataUtils.verificarDiaSemana(resultado.getDataRetorno(),
		// Calendar.MONDAY);
		// Assert.assertTrue(isMonday);

		// Usando assertivas personalizadas
		// Assert.assertThat(resultado.getDataRetorno(), new
		// DiaSemanaMatcher(Calendar.MONDAY));
		Assert.assertThat(resultado.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(resultado.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}

	// MOCKITO
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException {

		// Cenário
		// utilizando data builder
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// Quando a função possuiNegativacao passando usuario
		// for chamada, retorne true
		// compara através do equals e hash code, ou seja
		// se os usuarios, de acordo com a entidade, tiverem o mesmo nome
		// a regra se aplica a todos
		//Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		// formula com usuário genérico
		Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		// Ação
		try {
			service.alugarFilme(usuario, filmes);

			// Verificacao
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuário negativado"));
		}

		// Verifica se o método possuiNegativacao foi chamado e recebeu o
		// usuario correto como parâmetro
		// Não necessário, pois o Mockito.when acima retorna true se a função foi
		// chamada
		// exemplo apenas para fins acadêmicos
		Mockito.verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() throws FilmeSemEstoqueException, LocadoraException {
		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();

		// Locações com atraso
		Locacao l1 = LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario).agora();
		// duas locações
		Locacao l3 = LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario3).agora();
		Locacao l4 = LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario3).agora();

		// Locação em dia
		Locacao l2 = LocacaoBuilder.umLocacao().comUsuario(usuario2).agora();

		List<Locacao> locacoes = Arrays.asList(l1, l2, l3, l4);

		// quando a função obterLocacoesPendenter for chamada, retornará locacoes
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// Ação
		service.notificarAtrasos();

		// Verificação

		// Verifica que a função notificar atraso foi chamada
		// e se o usuário correto foi passado por parâmetro
		// verifica também quantas vezes o usuário foi passado por parametro
		Mockito.verify(email).notificarAtraso(usuario);

		// Usuário 3 fez duas locações
		// pelo menos 1 email foi enviado
		Mockito.verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		// exatamente 2 emails foram enviados
		// Mockito.verify(email, Mockito.times(2)).notificarAtraso(usuario3);
		// pelo menos 2 emails foram enviados
		// Mockito.verify(email, Mockito.atLeast(2)).notificarAtraso(usuario3);
		// no máximo 5 emails foram enviados
		// Mockito.verify(email, Mockito.atMost(5)).notificarAtraso(usuario3);

		// Usuario 2 não deve receber e-mail, pois está em dia
		Mockito.verify(email, Mockito.never()).notificarAtraso(usuario2);

		// Verificação genérica sem validação de usuários específicos
		Mockito.verify(email, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));

		// Verificar se não houve mais chamadas para a classe email
		// Se houve 3 locações enviadas, deve haver 3 chamadas, caso contrário
		// ocorrerá uma falha
		Mockito.verifyNoMoreInteractions(email);

		// Verificar se uma classe que não é para ser chamada não foi chamada
		// exemplificado apenas para fins academicos, pois spc não faz parte do escopo
		// desse teste
		// Mockito.verifyZeroInteractions(spc);
	}

}
