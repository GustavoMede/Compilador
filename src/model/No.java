package model;

import model.Token;

import java.util.ArrayList;
import java.util.List;

public class No {

	private No pai;
	private Token token;
	private TipoNo tipo;
	private List<No> filhos = new ArrayList<No>();

	public void addFilho(No filho) {
		filhos.add(filho);
		filho.setPai(this);
	}
	
	public No(TipoNo tipo) {
		this.tipo = tipo;
	}

	public No(Token token) {
		this.token = token;
		this.tipo = TipoNo.token;
	}

	public No getPai() {
		return pai;
	}

	public void setPai(No pai) {
		this.pai = pai;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public TipoNo getTipo() {
		return tipo;
	}

	public void setTipo(TipoNo tipo) {
		this.tipo = tipo;
	}

	public List<No> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<No> filhos) {
		this.filhos = filhos;
	}

	@Override
	public String toString() {
		if(tipo == TipoNo.token) {
			return token.getImagem();
		}
		return tipo.name();
	}

}
