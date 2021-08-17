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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

// Configuração do PowerMock
// Runner para PowerMock
@RunWith(PowerMockRunner.class)
// Preparar classe para utilizar PowerMock
@PrepareForTest({ LocacaoService.class })
public class LocacaoServiceTest_PowerMock {

	// MOCKS
	@Mock
	private SPCService spc;

	@Mock
	private LocacaoDAO dao;

	@Mock
	private EmailService email;

	// Classe onde os Mocks serão injetados
	@InjectMocks
	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	// Executa antes dos testes
	@Before
	public void setup() {
		// Configurando injeção de dependência de Mocks na classe Locacao Service
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

		// Cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		// Filme filme = new Filme("Filme 1", 2, 5.0);
		// Utilizando Data Builder
		Filme filme = FilmeBuilder.umFilme().comValor(4.0).agora();

		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));

		// Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação

		// Error collector
		error.checkThat(locacao.getValor(), CoreMatchers.is(4.0));

		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)),
				CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)),
				CoreMatchers.is(true));
	}

	@Test
	public void naoDeveDevolverFilmeNoDomingoAoAlugarNoSabado() throws Exception {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");
		Filme f1 = new Filme("Filme 1", 2, 4.0);
		List<Filme> filmes = Arrays.asList(f1);

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verificação
		Assert.assertThat(resultado.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(resultado.getDataRetorno(), MatchersProprios.caiNumaSegunda());
	}


	@Test
	public void deveAlugarFilme_SemCalcularValor() throws Exception {

		// Cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		// Definir comportamento da classe privada usando spy do Power Mock
		// nome da função em string pois é uma função privada, está encapsulada
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);

		// Ação
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// Verificação
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(1.0));

		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);

	}

	@Test
	public void deveCalcularValorLocacao() throws Exception {
		// Cenário
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// Ação
		Double valor = (Double)Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

		// Verificação
		Assert.assertThat(valor, CoreMatchers.is(5.0));
	}

}
