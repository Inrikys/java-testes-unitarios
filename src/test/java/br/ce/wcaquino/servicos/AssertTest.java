package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {

	@Test
	public void test() {
		// Verifica booleans
		Assert.assertTrue(true);
		Assert.assertFalse(false);

		// Verifica números
		Assert.assertEquals("Erro de comparação", 1, 1);
		// Terceiro parâmetro -> Margem de erro
		Assert.assertEquals(0.51234, 0.51, 0.01);
		Assert.assertEquals(Math.PI, 3.14, 0.01);

		int i = 5;
		Integer i2 = 5;
		// Incorreto
		// Assert.assertEquals(i, i2);
		// Correto
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());

		// Verifica String
		Assert.assertEquals("Batata", "Batata");
		Assert.assertNotEquals("Batata", "Beterraba");
		Assert.assertTrue("batata".equalsIgnoreCase("Batata"));
		Assert.assertTrue("batata".startsWith("ba"));

		// Verifica Objetos
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;

		// Verifica através de equals e hashcode do próprio objeto
		Assert.assertEquals(u1, u2);

		// Verifica se são da mesma instancia
		Assert.assertSame(u2, u2);
		Assert.assertNotSame(u2, u1);

		// Verifica se objeto é null
		Assert.assertNull(u3);
		Assert.assertNotNull(u2);

	}

}
