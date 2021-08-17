package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.runners.ParallelRunner;

@RunWith(ParallelRunner.class)
public class CalculadoraTest {

	private Calculadora calc;

	@Before
	public void setup() {
		calc = new Calculadora();
	}

	@Test
	public void deveSomarDoisValores() {

		// Cenário
		int a = 5;
		int b = 3;

		Calculadora calc = new Calculadora();

		// Ação
		int resultado = calc.somar(a, b);

		// Verificação
		Assert.assertEquals(8, resultado);
	}

	@Test
	public void deveSubstrairDoisValores() {

		// Cenário
		int a = 8;
		int b = 5;

		// Ação
		int resultado = calc.substrair(a, b);

		// Verificação
		Assert.assertEquals(3, resultado);
	}

	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {

		// Cenário
		int a = 6;
		int b = 3;

		// Ação
		int resultado = calc.divide(a, b);

		// Verificação
		Assert.assertEquals(2, resultado);
	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		// Cenário
		int a = 6;
		int b = 0;

		// Ação
		int resultado = calc.divide(a, b);

		// Verificação
		Assert.assertEquals(2, resultado);
	}
}
