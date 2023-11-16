package analisadorsintatico;

import model.No;
import model.TipoNo;
import model.Token;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSintaticoGeradorArvore {

	private List<Token> tokens = null;
	private List<String> erros = new ArrayList<String>();
	private Token token = null;
	private int pToken = 0;
	private No raiz = null;

	public AnalisadorSintaticoGeradorArvore(List<Token> tokens) {
		this.tokens = tokens;
	}

	public void analisar() {
		leToken();
		raiz = comandos();
		if(!token.getClasse().equals("$")) {
			erros.add("Esperado EOF ($), token: " + token);
		}
	}

	/**
	 * <comandos> ::= <comando> <comandos> |
	 */
	private No comandos() {
		No no = new No(TipoNo.comandos);
		if(token.getImagem().equals("(")) {
			no.addFilho(comando());
			no.addFilho(comandos());
		}
		return no;
	}

	/**
	 * <comando> ::= '(' <comando_interno> ')'
	 */
	private No comando() {
		No no = new No(TipoNo.comando);
		if(token.getImagem().equals("(")) {
			no.addFilho(new No(token));
			leToken();
			no.addFilho(comandoInterno());
			if(token.getImagem().equals(")")) {
				no.addFilho(new No(token));
				leToken();
			} else {
				erros.add("Esperado ')', token: " + token);
			}
		} else {
			erros.add("Esperado '(', token: " + token);
		}
		return no;
	}

	/**
	 * <comando_interno> ::= <decl> | <escrita> | <leitura> | <atrib> | <laco> | <cond>
	 */
	private No comandoInterno() {
		No noRetorno = new No(TipoNo.comando_interno);
		switch (token.getImagem()) {
			case "inteiro", "real", "cadeia", "logico" -> noRetorno.addFilho(decl());
			case "imprime" -> noRetorno.addFilho(escrita());
			case "le" -> noRetorno.addFilho(leitura());
			case "=" -> noRetorno.addFilho(atrib());
			case "enquanto" -> noRetorno.addFilho(laco());
			case "se" -> noRetorno.addFilho(cond());
			default ->
					erros.add("Esperado 'inteiro' ou 'real' ou 'cadeia' ou 'logico' ou 'imprime' ou 'le' ou '=' ou " +
							"'enquanto' ou 'se', token: " + token);
		}
		return noRetorno;
	}

	/**
	 * <decl> ::= <tipo> <ids>
	 */
	private No decl() {
		No novoNo = new No(TipoNo.decl);
		novoNo.addFilho(tipo());
		novoNo.addFilho(ids());
		return novoNo;
	}

	/**
	 * <tipo> ::= 'inteiro' | 'real' | 'cadeia' | 'logico'
	 */
	private No tipo() {
		No novoNo = new No(TipoNo.tipo);

		if(token.getImagem().equals("inteiro")
				|| token.getImagem().equals("real")
				|| token.getImagem().equals("cadeia")
				|| token.getImagem().equals("logico")) {
			novoNo.addFilho(new No(token));
			leToken();
		} else {
			erros.add("Esperado 'inteiro' ou 'real' ou 'cadeia' ou 'logico', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <ids> ::= id <ids2>
	 */
	private No ids() {
		No novoNo = new No(TipoNo.ids);

		if(token.getClasse().equals("identificador")) {
			novoNo.addFilho(new No(token));
			leToken();
			novoNo.addFilho(ids2());
		} else {
			erros.add("Esperado um identificador, token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <ids2> ::= id <ids2> |
	 */
	private No ids2() {
		No novoNo = new No(TipoNo.ids);

		if(token.getClasse().equals("identificador")) {
			novoNo.addFilho(new No(token));
			leToken();
			novoNo.addFilho(ids2());
		}
		return novoNo;
	}

	/**
	 * <escrita> ::= 'imprime' <imprim>
	 */
	private No escrita() {
		No novoNo = new No(TipoNo.escrita);
		if(token.getImagem().equals("imprime")) {
			novoNo.addFilho(new No(token));
			leToken();
			novoNo.addFilho(imprim());
		} else {
			erros.add("Esperado 'imprime', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <imprim> ::= id | stringLiteral
	 */
	private No imprim() {
		No novoNo = new No(TipoNo.imprim);

		if(token.getClasse().equals("identificador")
				|| token.getClasse().equals("cadeia literal")) {
			novoNo.addFilho(new No(token));
			leToken();
		} else {
			erros.add("Esperado um identificador ou string literal, token: "+ token);
		}

		return novoNo;
	}

	/**
	 * <leitura> ::= 'le' id
	 */
	private No leitura() {
		No novoNo = new No(TipoNo.leitura);

		if(token.getImagem().equals("le")) {
			novoNo.addFilho(new No(token));
			leToken();
			if(token.getClasse().equals("identificador")) {
				novoNo.addFilho(new No(token));
				leToken();
			} else {
				erros.add("Esperado um identificador, token: "+ token);
			}
		} else {
			erros.add("Esperado 'le', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <atrib> 	::= '=' id <expr_arit>
	 */
	private No atrib() {
		No novoNo = new No(TipoNo.atrib);

		if(token.getImagem().equals("=")) {
			novoNo.addFilho(new No(token));
			leToken();
			if(token.getClasse().equals("identificador")) {
				novoNo.addFilho(new No(token));
				leToken();
			} else {
				erros.add("Esperado um identificador, token: "+ token);
			}
			novoNo.addFilho(exprArit());
		} else {
			erros.add("Esperado '=', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <expr_arit> ::= <operan> | '(' <op_arit> <expr_arit> <expr_arit> ')'
	 */
	private No exprArit() {
		No novoNo = new No(TipoNo.expr_arit);

		if(token.getClasse().equals("identificador")
				|| token.getClasse().equals("cadeia literal")
				|| token.getClasse().equals("inteiro literal")
				|| token.getClasse().equals("real literal")
				|| token.getClasse().equals("booleano literal")
		) {
			novoNo.addFilho(operan());

		} else if(token.getImagem().equals("(")) {
			novoNo.addFilho(new No(token));
			leToken();
			novoNo.addFilho(opArit());
			novoNo.addFilho(exprArit());
			novoNo.addFilho(exprArit());
			if(token.getImagem().equals(")")) {
				leToken();
			} else {
				erros.add("Esperado ')', token: "+ token);
			}
		} else {
			erros.add("Esperado um '(' ou identificador ou uma string literal ou um inteiro literal ou um real literal ou um logico literal, token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <op_arit> ::= '+' | '-' | '*' | '/' | '.'
	 */
	private No opArit() {
		No novoNo = new No(TipoNo.op_arit);

		if(token.getImagem().equals("+")
				|| token.getImagem().equals("-")
				|| token.getImagem().equals("*")
				|| token.getImagem().equals("/")
				|| token.getImagem().equals(".")) {
			novoNo.addFilho(new No(token));
			leToken();
		} else {
			erros.add("Esperado '+' ou '-' ou '*' ou '/' ou '.', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <operan> ::= id | stringLiteral | intLiteral | realLiteral | logicoLiteral
	 */
	private No operan() {
		No novoNo = new No(TipoNo.operan);

		if(token.getClasse().equals("identificador")
				|| token.getClasse().equals("cadeia literal")
				|| token.getClasse().equals("inteiro literal")
				|| token.getClasse().equals("real literal")
				|| token.getClasse().equals("booleano literal")
		) {
			novoNo.addFilho(new No(token));
			leToken();
		} else {
			erros.add("Esperado um identificador ou uma string literal ou um inteiro literal ou um real literal ou um logico literal, token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <laco> ::= 'enquanto' '(' <expr_rel> ')' <comando> <comandos>
	 */
	private No laco() {
		No novoNo = new No(TipoNo.laco);

		if(token.getImagem().equals("enquanto")) {
			novoNo.addFilho(new No(token));
			leToken();
			if(token.getImagem().equals("(")) {
				novoNo.addFilho(new No(token));
				leToken();
				novoNo.addFilho(exprRel());
				if(token.getImagem().equals(")")) {
					novoNo.addFilho(new No(token));
					leToken();
					novoNo.addFilho(comando());
					novoNo.addFilho(comandos());
				} else {
					erros.add("Esperado ')', token: "+ token);
				}
			} else {
				erros.add("Esperado '(', token: "+ token);
			}
		} else {
			erros.add("Esperado 'enquanto', token: "+ token);
		}

		return novoNo;
	}

	/**
	 * <expr_rel> ::= <operan> | <op_rel> <expr_rel> <expr_rel>
	 */
	private No exprRel() {
		No novoNo = new No(TipoNo.expr_rel);

		if(token.getClasse().equals("identificador")
				|| token.getClasse().equals("cadeia literal")
				|| token.getClasse().equals("inteiro literal")
				|| token.getClasse().equals("real literal")
				|| token.getClasse().equals("booleano literal")
		) {
			novoNo.addFilho(operan());
		} else if(token.getImagem().equals(">")
				|| token.getImagem().equals(">=")
				|| token.getImagem().equals("<")
				|| token.getImagem().equals("<=")
				|| token.getImagem().equals("==")
				|| token.getImagem().equals("!=")) {
			novoNo.addFilho(opRel());
			novoNo.addFilho(exprRel());
			novoNo.addFilho(exprRel());
		} else {
			erros.add("Esperado um identificador ou uma string literal ou um inteiro literal ou um real literal ou um logico literal "
					+ "ou '>' ou '>=' ou '<' ou '<=' ou '==' ou '!=', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <op_rel> ::= '>' | '>=' | '<' | '<=' | '==' | '!='
	 */
	private No opRel() {
		No novoNo = new No(TipoNo.op_rel);

		if(token.getImagem().equals(">")
				|| token.getImagem().equals(">=")
				|| token.getImagem().equals("<")
				|| token.getImagem().equals("<=")
				|| token.getImagem().equals("==")
				|| token.getImagem().equals("!=")) {
			novoNo.addFilho(new No(token));
			leToken();
		} else {
			erros.add("Esperado '>' ou '>=' ou '<' ou '<=' ou '==' ou '!=', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <cond> ::= 'se' '(' <expr_rel> ')' <cond_true> <cond_false>
	 */
	private No cond() {
		No novoNo = new No(TipoNo.cond);

		if(token.getImagem().equals("se")) {
			novoNo.addFilho(new No(token));
			leToken();
			if(token.getImagem().equals("(")) {
				novoNo.addFilho(new No(token));
				leToken();
				novoNo.addFilho(exprRel());
				if(token.getImagem().equals(")")) {
					novoNo.addFilho(new No(token));
					leToken();
					novoNo.addFilho(condTrue());
					novoNo.addFilho(condFalse());
				} else {
					erros.add("Esperado ')', token: "+ token);
				}
			} else {
				erros.add("Esperado '(', token: "+ token);
			}
		} else {
			erros.add("Esperado 'se', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <cond_true> 	::= '(' <comando> <comandos> ')'
	 */
	private No condTrue() {
		No novoNo = new No(TipoNo.cond_true);

		if(token.getImagem().equals("(")) {
			novoNo.addFilho(new No(token));
			leToken();
			novoNo.addFilho(comando());
			novoNo.addFilho(comandos());
			if(token.getImagem().equals(")")) {
				leToken();
			} else {
				erros.add("Esperado ')', token: "+ token);
			}
		} else {
			erros.add("Esperado '(', token: "+ token);
		}
		return novoNo;
	}

	/**
	 * <cond_false> ::= '(' <comando> <comandos> ')' |
	 */
	private No condFalse() {
		No novoNo = new No(TipoNo.cond_false);

		if(token.getImagem().equals("(")) {
			novoNo.addFilho(new No(token));
			leToken();
			novoNo.addFilho(comando());
			novoNo.addFilho(comandos());
			if(token.getImagem().equals(")")) {
				novoNo.addFilho(new No(token));
				leToken();
			} else {
				erros.add("Esperado ')', token: "+ token);
			}
		}
		return novoNo;
	}

	private Token leToken() {
		token = tokens.get(pToken);
		pToken++;
		return token;
	}

	public boolean temErros() {
		return !erros.isEmpty();
	}

	public void printErros() {
		System.out.println("Erros sintáticos:");
		erros.forEach(e -> System.out.println(e));
	}

	public No getRaiz() {
		return raiz;
	}

	public void mostraArvore(No raiz, String prefixo, boolean rabo) {
		System.out.println(prefixo + (rabo ? "└── " : "├── ") + raiz.toString());

		List<No> children = raiz.getFilhos();
		for (int i = 0; i < children.size() - 1; i++) {
			mostraArvore(children.get(i), prefixo + (rabo ? "    " : "│   "), false);
		}
		if (!children.isEmpty()) {
			mostraArvore(children.get(children.size() - 1), prefixo + (rabo ?"    " : "│   "), true);
		}
	}

}