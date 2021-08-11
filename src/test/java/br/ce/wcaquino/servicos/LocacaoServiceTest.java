package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

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

// Configura��o do PowerMock
// Runner para PowerMock
@RunWith(PowerMockRunner.class)
// Preparar classe para utilizar PowerMock
@PrepareForTest({ LocacaoService.class })
public class LocacaoServiceTest {

	// MOCKS
	@Mock
	private SPCService spc;

	@Mock
	private LocacaoDAO dao;

	@Mock
	private EmailService email;

	// Classe onde os Mocks ser�o injetados
	@InjectMocks
	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// Executa antes dos testes
	@Before
	public void setup() {
		// Configurando inje��o de depend�ncia de Mocks na classe Locacao Service
		MockitoAnnotations.initMocks(this);
		service = PowerMockito.spy(service);
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

		// Roda o teste apenas se a condi��o for verdadeira
		// Posso resolver isso usando PowerMock
		// Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(),
		// Calendar.SATURDAY));

		// Cenario
		// Objeto instanciado na fun��o setup
		// service = new LocacaoService();

		// Usuario usuario = new Usuario("Usuario 1");
		// Utilizando Data Builder
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		// Filme filme = new Filme("Filme 1", 2, 5.0);
		// Utilizando Data Builder
		Filme filme = FilmeBuilder.umFilme().comValor(4.0).agora();

		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		// Solucionando problema com PowerMock utilizando em construtor
		// Quando tiver uma new Date() <- sem argumentos
		// Mockar a data escolhida (um dia que n�o seja s�bado)
		// PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28,
		// 4, 2017));

		// Solucionando problema com Power Mock usando m�todos est�ticos
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 28);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2017);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		// A��o
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verifica��o

		// Verificar se construtor foi chamado
		// expectativa padr�o -> ser chamado APENAS uma vez,
		// caso tenha sido executado mais de uma vez -> falha
		// PowerMockito.verifyNew(Date.class).withNoArguments();
		// Deve ser especificado a quantidade de vezes
		// PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();

		// Error collector
		error.checkThat(locacao.getValor(), CoreMatchers.is(4.0));

		// Exemplos
		// error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),
		// CoreMatchers.is(true));
		// error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(),
		// DataUtils.obterDataComDiferencaDias(1)),
		// CoreMatchers.is(true));

		// fun��o ehHoje() e ehHojeComDiferencialDias() utilizam DataUtils (foi
		// configurada para receber os valores
		// do Power Mock
		// error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		// error.checkThat(locacao.getDataRetorno(),
		// MatchersProprios.ehHojeComDiferencaDias(1));

		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)),
				CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)),
				CoreMatchers.is(true));
	}

	// VERIFICAR SE EST� TRATANDO EXCE��ES
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

		// A��o
		service.alugarFilme(usuario, filmes);

		// Verifica��o
		// Atrav�s da Exception
	}

	// Forma robusta
	// Captura exce��o e verifica mensagem
	@Test
	public void naoDeveAlugarFilmeSemEstoque_2() {

		// Cenario
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);

		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		try {
			// A��o
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lan�ado uma exce��o");
		} catch (Exception e) {
			// Verifica��o
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

		// Verifica��o antes da a��o (O que � esperado da pr�xima a��o)
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");

		// A��o
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
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usu�rio vazio"));
		}
	}

	// Forma recente
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {

		// Cenario
		Usuario usuario = new Usuario("Usuario 1");

		// Verifica��o antes da a��o (O que � esperado da pr�xima a��o)
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// A��o
		service.alugarFilme(usuario, null);

	}

	@Test
	public void devePagar75PcNoFilme3() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");

		Filme f1 = new Filme("Filme 1", 2, 4.0);
		Filme f2 = new Filme("Filme 2", 2, 4.0);
		Filme f3 = new Filme("Filme 3", 2, 4.0);

		List<Filme> filmes = Arrays.asList(f1, f2, f3);

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(11.0));
	}

	@Test
	public void devePagar50PcNoFilme4() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");

		Filme f1 = new Filme("Filme 1", 2, 4.0);
		Filme f2 = new Filme("Filme 2", 2, 4.0);
		Filme f3 = new Filme("Filme 3", 2, 4.0);
		Filme f4 = new Filme("Filme 4", 2, 4.0);

		List<Filme> filmes = Arrays.asList(f1, f2, f3, f4);

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(13.0));
	}

	@Test
	public void devePagar25PcNoFilme5() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");

		Filme f1 = new Filme("Filme 1", 2, 4.0);
		Filme f2 = new Filme("Filme 2", 2, 4.0);
		Filme f3 = new Filme("Filme 3", 2, 4.0);
		Filme f4 = new Filme("Filme 4", 2, 4.0);
		Filme f5 = new Filme("Filme 4", 2, 4.0);

		List<Filme> filmes = Arrays.asList(f1, f2, f3, f4, f5);

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(14.0));
	}

	@Test
	public void devePagar0PcNoFilme6() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");

		Filme f1 = new Filme("Filme 1", 2, 4.0);
		Filme f2 = new Filme("Filme 2", 2, 4.0);
		Filme f3 = new Filme("Filme 3", 2, 4.0);
		Filme f4 = new Filme("Filme 4", 2, 4.0);
		Filme f5 = new Filme("Filme 5", 2, 4.0);
		Filme f6 = new Filme("Filme 6", 2, 4.0);

		List<Filme> filmes = Arrays.asList(f1, f2, f3, f4, f5, f6);

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(14.0));
	}

	@Test
	public void naoDeveDevolverFilmeNoDomingoAoAlugarNoSabado() throws Exception {

		// Roda o teste apenas se a condi��o for verdadeira
		// Esse problema pode ser resolvido com PowerMock
		// Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(),
		// Calendar.SATURDAY));

		// Cen�rio
		Usuario usuario = new Usuario("Usuario 1");
		Filme f1 = new Filme("Filme 1", 2, 4.0);
		List<Filme> filmes = Arrays.asList(f1);

		// Solucionando problema com Power Mock usando Construtores
		// Quando tiver uma new Date() <- sem argumentos
		// Mockar a data escolhida (Data que retorna um s�bado)
		// PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29,
		// 4, 2017));

		// Solucionando problema com Power Mock usando m�todos est�ticos
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 29);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2017);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		// A��o
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verifica��o
		// boolean isMonday = DataUtils.verificarDiaSemana(resultado.getDataRetorno(),
		// Calendar.MONDAY);
		// Assert.assertTrue(isMonday);

		// Verifica��o do Power Mock usando m�todo est�tico
		PowerMockito.verifyStatic(Calendar.class, Mockito.times(2));
		Calendar.getInstance();

		// Usando assertivas personalizadas
		// Assert.assertThat(resultado.getDataRetorno(), new
		// DiaSemanaMatcher(Calendar.MONDAY));
		Assert.assertThat(resultado.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(resultado.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}

	// MOCKITO
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {

		// Cen�rio
		// utilizando data builder
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// Quando a fun��o possuiNegativacao passando usuario
		// for chamada, retorne true
		// compara atrav�s do equals e hash code, ou seja
		// se os usuarios, de acordo com a entidade, tiverem o mesmo nome
		// a regra se aplica a todos
		// Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);

		// formula com usu�rio gen�rico
		Mockito.when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		// A��o
		try {
			service.alugarFilme(usuario, filmes);

			// Verificacao
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usu�rio negativado"));
		}

		// Verifica se o m�todo possuiNegativacao foi chamado e recebeu o
		// usuario correto como par�metro
		// N�o necess�rio, pois o Mockito.when acima retorna true se a fun��o foi
		// chamada
		// exemplo apenas para fins acad�micos
		Mockito.verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() throws FilmeSemEstoqueException, LocadoraException {
		// Cen�rio
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();

		// Loca��es com atraso
		Locacao l1 = LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario).agora();
		// duas loca��es
		Locacao l3 = LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario3).agora();
		Locacao l4 = LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario3).agora();

		// Loca��o em dia
		Locacao l2 = LocacaoBuilder.umLocacao().comUsuario(usuario2).agora();

		List<Locacao> locacoes = Arrays.asList(l1, l2, l3, l4);

		// quando a fun��o obterLocacoesPendenter for chamada, retornar� locacoes
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// A��o
		service.notificarAtrasos();

		// Verifica��o

		// Verifica que a fun��o notificar atraso foi chamada
		// e se o usu�rio correto foi passado por par�metro
		// verifica tamb�m quantas vezes o usu�rio foi passado por parametro
		Mockito.verify(email).notificarAtraso(usuario);

		// Usu�rio 3 fez duas loca��es
		// pelo menos 1 email foi enviado
		Mockito.verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		// exatamente 2 emails foram enviados
		// Mockito.verify(email, Mockito.times(2)).notificarAtraso(usuario3);
		// pelo menos 2 emails foram enviados
		// Mockito.verify(email, Mockito.atLeast(2)).notificarAtraso(usuario3);
		// no m�ximo 5 emails foram enviados
		// Mockito.verify(email, Mockito.atMost(5)).notificarAtraso(usuario3);

		// Usuario 2 n�o deve receber e-mail, pois est� em dia
		Mockito.verify(email, Mockito.never()).notificarAtraso(usuario2);

		// Verifica��o gen�rica sem valida��o de usu�rios espec�ficos
		Mockito.verify(email, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));

		// Verificar se n�o houve mais chamadas para a classe email
		// Se houve 3 loca��es enviadas, deve haver 3 chamadas, caso contr�rio
		// ocorrer� uma falha
		Mockito.verifyNoMoreInteractions(email);

		// Verificar se uma classe que n�o � para ser chamada n�o foi chamada
		// exemplificado apenas para fins academicos, pois spc n�o faz parte do escopo
		// desse teste
		// Mockito.verifyZeroInteractions(spc);
	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {

		// Cen�rio
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastr�fica"));

		// Verifica��o
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com SPC, tente novamente");

		// A��o
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void deveProrrogarUmaLocacao() throws FilmeSemEstoqueException, LocadoraException {

		// Cen�rio
		Locacao locacao = LocacaoBuilder.umLocacao().agora();

		// A��o
		service.prorrogarLocacao(locacao, 3);

		// Verifica��o
		// N�o tem como verificar se um objeto dessa fun��o passou pela fun��o
		// dao.salvar,
		// pois na fun��o service.prorrogarLocacao() h� um objeto local que � passado
		// como
		// parametro da fun��o dao.salvar()

		// Para verificarmos esse valor, � necess�rio o usdo do ArgumentCaptor

		// Captura objeto passado pela fun��o dao.salvar
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();

		// Valor retornado -> valor * dias -> 5 * 3
		Assert.assertThat(locacaoRetornada.getValor(), CoreMatchers.is(15.0));
		Assert.assertThat(locacaoRetornada.getDataLocacao(), MatchersProprios.ehHoje());
		Assert.assertThat(locacaoRetornada.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(3));
	}

	@Test
	public void deveAlugarFilme_SemCalcularValor() throws Exception {

		// Cen�rio
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// Definir comportamento da classe privada usando spy do Power Mock
		// nome da fun��o em string pois � uma fun��o privada, est� encapsulada
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);

		// A��o
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verifica��o
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(1.0));

		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);

	}

	@Test
	public void deveCalcularValorLocacao() throws Exception {
		// Cen�rio
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// A��o
		Double valor = (Double)Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

		// Verifica��o
		Assert.assertThat(valor, CoreMatchers.is(5.0));
	}

}
