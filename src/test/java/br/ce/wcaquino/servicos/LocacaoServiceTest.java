package br.ce.wcaquino.servicos;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Test
	public void testeLocacao() {

		// Cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);

		// A��o
		Locacao locacao = service.alugarFilme(usuario, filme);

		// Verifica��o
		// Assert.assertEquals(5.0, locacao.getValor(), 0.01);
		// Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new
		// Date()));
		// Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(),
		// DataUtils.obterDataComDiferencaDias(1)));

		// AssertThat
		// Verifique que o valor da loca��o � igual a 5.0
		// Assert.assertThat(locacao.getValor(),
		// CoreMatchers.is(CoreMatchers.equalTo(5.0)));
		// Assert.assertThat(locacao.getValor(),
		// CoreMatchers.is(CoreMatchers.not(5.0)));
		// Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new
		// Date()), CoreMatchers.is(true));
		// Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(),
		// DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));

		// Error collector
		error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
	}
}
