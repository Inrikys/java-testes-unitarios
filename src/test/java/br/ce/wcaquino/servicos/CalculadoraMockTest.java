package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CalculadoraMockTest {

	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Mock
	private EmailService email;
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devoMostrarDiferencaEntreMockSpy() {
		
		// Comportamento de um Mock
		// Define um retorno de acordo com uma entrada
		// Caso a soma for 1 + 2 -> retorna 8
		// Se mudar o valor da soma para inesperado pelo Mock, retorna 0
		Mockito.when(calcMock.somar(1, 2)).thenReturn(8);
		System.out.println("Mock: " + calcMock.somar(1,5));
		
		
		// Comportamento de um Spy
		// Define um retorno de acordo com uma entrada
		// Caso a soma for 1 + 2 -> retorna 8
		// Se mudar o valor da soma para inesperado pelo Spy, retorna
		// a execução da função.
		// O Spy não funciona com interfaces, apenas com classes concretas
		
		// OBS: A execução pode ocorrer no momento da definição da expectativa
		// Perigoso, pois pode gerar uma exception
		// Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
		
		// Evita executar a função antes de definir a expectativa
		Mockito.doReturn(5).when(calcSpy).somar(1,2);
		System.out.println("Spy: " + calcSpy.somar(1,5));

		
		
		// Mock chama a execução real do método caso valor esperado seja
		// verdadeiro
		Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
		System.out.println("Mock: " + calcMock.somar(1,2));
		
		
		// Mock em procedimento que retorna void
		// Não executa o procedimento, apenas simula o retorno
		System.out.println("Mock:");
		calcMock.imprime();
		
		
		// Para evitar que Spy execute um método.
		// Mockito.doNothing().when(calcSpy).imprime();
		
		// Spy em procedimento que retorna void
		// Comportamento padrão -> executa o método
		System.out.println("Spy:");
		calcSpy.imprime();
	}
	
	
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
