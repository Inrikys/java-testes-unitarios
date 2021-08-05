package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import br.ce.wcaquino.entidades.Locacao;

public class CalculadoraMockTest {

	// exemplo de como funciona o thenReturn do Mockito para simular qualquer classe
	@Test
	public void teste() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		// insere o ArgumentCaptor na função onde deseja capturar os parametros
		// e possivelmente utilizá-lo no teste.
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);
	
		
		Assert.assertEquals(5, calc.somar(1, 2));
		
		// Captura valores que foram passados em calc.somar
		System.out.println(argCapt.getAllValues());
	}

}
