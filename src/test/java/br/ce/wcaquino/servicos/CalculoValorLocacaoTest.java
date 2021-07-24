package br.ce.wcaquino.servicos;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

// Testes Data Driven (Orientados a Dados)
@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

	// Variáveis de cenário no escopo da classe
	public LocacaoService service;

	@Parameter
	public List<Filme> filmes;

	@Parameter(value = 1)
	public Double valorLocacao;

	@Parameter(value = 2)
	public String descricao;

	private static Filme f1 = new Filme("Filme 1", 2, 4.0);
	private static Filme f2 = new Filme("Filme 2", 2, 4.0);
	private static Filme f3 = new Filme("Filme 3", 2, 4.0);
	private static Filme f4 = new Filme("Filme 4", 2, 4.0);
	private static Filme f5 = new Filme("Filme 5", 2, 4.0);
	private static Filme f6 = new Filme("Filme 6", 2, 4.0);
	private static Filme f7 = new Filme("Filme 6", 2, 4.0);

	@Before
	public void setup() {
		service = new LocacaoService();
	}

	// Parametrização de ações
	// Lista com filme é o primeiro parâmetro
	// Valor locação é o segundo
	// Para cada linha, será feito um teste
	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { { Arrays.asList(f1, f2), 8.0, "2 filmes: Sem desconto" },
				{ Arrays.asList(f1, f2, f3), 11.0, "3 filmes: 25%" },
				{ Arrays.asList(f1, f2, f3, f4), 13.0, "4 filmes: 50%" },
				{ Arrays.asList(f1, f2, f3, f4, f5), 14.0, "5 filmes: 75%" },
				{ Arrays.asList(f1, f2, f3, f4, f5, f6), 14.0, "5 filmes: 100%" },
				{ Arrays.asList(f1, f2, f3, f4, f5, f6, f7), 18.0, "7 filmes: Sem descontos" } });
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {

		// Cenário
		Usuario usuario = new Usuario("Usuario 1");

		// Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// Verificação
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(valorLocacao));
	}
}
