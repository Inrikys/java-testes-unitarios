package br.ce.wcaquino.servicos;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

// Configura testes em ordem alfabética
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {
	
	public static int contator = 0;

	@Test
	public void inicia() {
		contator = 1;
	}
	
	@Test
	public void verifica() {
		Assert.assertThat(contator, CoreMatchers.is(1));
	}
	

}
