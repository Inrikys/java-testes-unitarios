package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {
	
	// exemplo de como funciona o thenReturn do Mockito para simular qualquer classe
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		Mockito.when(calc.somar(1, 2)).thenReturn(5);

		System.out.println(calc.somar(1, 2));
	}

}
