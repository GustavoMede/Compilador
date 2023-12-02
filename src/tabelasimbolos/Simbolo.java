package tabelasimbolos;

import model.Token;

public class Simbolo {

	private Token token;
	private String tipo;
	
	public Simbolo() {
		super();
	}

	public Simbolo(Token token) {
		super();
		this.token = token;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public String toString(){
		return token+" : "+tipo;
	}
}
