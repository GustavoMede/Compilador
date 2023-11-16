package model;

import java.util.ArrayList;
import java.util.List;

public class No {

	private Token token;
	private final TipoNo tipo;
	private final List<No> filhos = new ArrayList<>();

	public void addFilho(No filho) {
		filhos.add(filho);
	}
	
	public No(TipoNo tipo) {
		this.tipo = tipo;
	}

	public No(Token token) {
		this.token = token;
		this.tipo = TipoNo.token;
	}

	public List<No> getFilhos() {
		return filhos;
	}

	@Override
	public String toString() {
		if(tipo == TipoNo.token) {
			return token.getImagem();
		}
		return tipo.name();
	}

}
