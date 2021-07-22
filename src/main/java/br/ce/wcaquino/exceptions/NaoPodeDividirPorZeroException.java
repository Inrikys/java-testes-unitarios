package br.ce.wcaquino.exceptions;

public class NaoPodeDividirPorZeroException extends Exception {
	private static final long serialVersionUID = 1L;

	public NaoPodeDividirPorZeroException(String message) {
		super(message);
	}

}
