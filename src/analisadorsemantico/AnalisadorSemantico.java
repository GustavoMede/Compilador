package analisadorsemantico;

import model.No;
import model.Token;
import tabelasimbolos.TabelaSimbolos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.TipoNo.operan;

@SuppressWarnings("unchecked")
public class AnalisadorSemantico {

	private No raiz = null;
	private List<String> erros = new ArrayList<>();

	private Token identificadorEmAnalise = null;

	public AnalisadorSemantico(No raiz) {
		this.raiz = raiz;
	}

	public void analisar() {
		analisar(raiz);
	}

	private Object analisar(No no) {
		switch (no.getTipo()) {
			case comandos: {
				return comandos(no);
			}
			case comando: {
				return comando(no);
			}
			case comando_interno: {
				return comandoIntern(no);
			}
			case decl: {
				return decl(no);
			}
//			case escrita: {
//				return escrita(no);
//			}
//			case leitura: {
//				return leitura(no);
//			}
			case atrib: {
				return atrib(no);
			}
//			case laco: {
//				return laco(no);
//			}
//			case cond: {
//				return cond(no);
//			}
			case expr_arit: {
				return exprArit(no);
			}
			case operan: {
				return operan(no);
			}
			case tipo: {
				return tipo(no);
			}
			case ids: {
				return ids(no);
			}
			case ids2: {
				return ids2(no);
			}
		default:
			return null;
		}

	}

	/**
	 * <operan> ::= id | stringLiteral | intLiteral | realLiteral | logicoLiteral
	 */
	private Object operan(No no) {
		String tipoIdentificadorEmAnalise = TabelaSimbolos.getTipo(identificadorEmAnalise);
		Token tokenOperan = no.getFilho(0).getToken();
		String nomeIdentificador = identificadorEmAnalise.getImagem();

		if (tipoIdentificadorEmAnalise == null) {
			return erros.add("A variável " + nomeIdentificador + " não foi previamente declarada!!!");
		}
		if (tokenOperan.getClasse().equals("identificador")) {
			if (tipoIdentificadorEmAnalise.equals(TabelaSimbolos.getTipo(tokenOperan))) {
				return tokenOperan;
			} else {
				return erros.add("Não é possível realizar a operação!!! A variável " + identificadorEmAnalise.getImagem() + " não é do tipo " + TabelaSimbolos.getTipo(tokenOperan));
			}
		} else if (tokenOperan.getClasse().contains(tipoIdentificadorEmAnalise)) {
			return tokenOperan;
		} else {
			return erros.add("Não é possível realizar a operação!!! A variável " + identificadorEmAnalise.getImagem() + " não é do tipo " + tokenOperan.getClasse());
		}
	}

	/**
	 * <expr_arit> ::= <operan> | '(' <op_arit> <expr_arit> <expr_arit> ')'
	 */
	private Object exprArit(No no) {
		// Valida se primeiro No filho é um delimitador
		if (no.getFilho(0).getTipo() != operan) {
			if (no.getFilho(0).getToken().getImagem().equals("(")) {
				analisar(no.getFilho(2));
				analisar(no.getFilho(3));
			}
			//Caso positivo, pegar o próximo filho e validar suas expressões aritméticas
		} else {
			analisar(no.getFilho(0));
		}
		analisar(no.getFilho(0));
		return null;
	}

	/**
	 * <comandos> ::= <comando> <comandos> |
	 */
	private Object comandos(No no) {
		if(no.getFilhos().size() == 2) {
			analisar(no.getFilho(0));
			analisar(no.getFilho(1));
		}
		return null;
	}

	/**
	 * <comando> ::= '(' <comando_interno> ')'
	 */
	private Object comando(No no) {
		analisar(no.getFilho(1));
		return null;
	}

	/**
	 * <comando_interno> ::= <decl> | <escrita> | <leitura> | <atrib> | <laco> | <cond>
	 */
	private Object comandoIntern(No no) {
		return analisar(no.getFilho(0));
	}

	/**
	 * <decl> ::= <tipo> <ids>
	 */
	private Object decl(No no) {
		String tipo = (String) analisar(no.getFilho(0));
		if (no.getFilhos().size() >= 2) {
			List<Token> ids = (List<Token>) analisar(no.getFilho(1));
			Collections.reverse(ids);
			for(Token id: ids) {
				String tipoId = TabelaSimbolos.getTipo(id);
				if (tipoId != null) {
					erros.add("Token redeclarado: " + id);
				} else {
					TabelaSimbolos.setTipo(id, tipo);
				}
			}
		}
		return null;
	}

	/**
	 * <atrib> 	::= '=' id <expr_arit>
	 */
	private No atrib(No no) {
		Token tokenAtrib = no.getFilho(0).getToken();
		Token tokenIdentificador = no.getFilho(1).getToken();
		No noExprArit = no.getFilho(2);

		if (tokenAtrib.getImagem().equals("=")) {
			if (tokenIdentificador.getClasse().equals("identificador")) {

				int posicaoTokenListaSimbolos = TabelaSimbolos.contains(tokenIdentificador);
				if (posicaoTokenListaSimbolos != -1) {
					identificadorEmAnalise = tokenIdentificador;
					analisar(noExprArit);
				}
			} else {
				erros.add("Esperado um identificador, token: " + tokenIdentificador);
			}
		} else {
			erros.add("Esperado '=', token: " + tokenAtrib);
		}
		return no;
	}

	/**
	 * <tipo> ::= 'inteiro' | 'real' | 'cadeia' | 'logico'
	 */
	private Object tipo(No no) {
		return no.getFilho(0).getToken().getImagem();
	}

	/**
	 * <ids> ::= id <ids2>
	 */
	private Object ids(No no) {
		List<Token> ids2 = new ArrayList<>();
		if (no.getFilhos().size() == 2) {
			ids2 = (List<Token>) analisar(no.getFilho(1));
		}
		if (!no.getFilhos().isEmpty()) {
			ids2.add(no.getFilho(0).getToken());
		}
		return ids2;
	}

	/**
	 * <ids2> ::= id <ids2> |
	 */
	private Object ids2(No no) {
		if(no.getFilhos().isEmpty()) {
			return new ArrayList<Token>();
		} else {
			List<Token> ids2 = (List<Token>) analisar(no.getFilho(1));
			ids2.add(no.getFilho(0).getToken());
			return ids2;
		}
	}

	public boolean temErros() {
		return !erros.isEmpty();
	}

	public void printErros() {
		erros.forEach(erro -> System.out.println(erro));
	}

}
