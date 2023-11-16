package analisadorlexico;

import model.Token;

import java.util.*;

public class AnalisadorLexico {

    private static final int INICIO_STRING = 0;
    private static final String CARACTER_COMENTARIO = "'";
    private static final String CARACTER_ESPACO = " ";
    private static final String CARACTER_STRING = "\"";
    private static final int ESPACO_NAO_ENCONTRADO = -1;
    private static final String ESPACOS_REGEX = "\\s+";
    private static final String TABULACAO_REGEX = "\t";
    private static final String STRING_VAZIA = "";
    private static final String CLASSE_IDENTIFICADOR = "identificador";
    private static final String CLASSE_CADEIA_LITERAL = "cadeia literal";
    private static final String CLASSE_INTEIRO_LITERAL = "inteiro literal";
    private static final String CLASSE_REAL_LITERAL = "real literal";
    private static final String CLASSE_BOOLEANO_LITERAL = "booleano literal";
    private static final String CLASSE_MARCADOR_FINAL = "$";

    private ArrayList<Token> listaTokens = new ArrayList<>();

    private static final HashMap<String, String> classesTokenHashMap = new HashMap<>() {
        {
            put("+", "operador");
            put("-", "operador");
            put("*", "operador");
            put("/", "operador");
            put(">", "operador");
            put("=", "operador");
            put(">=", "operador");
            put("<=", "operador");
            put("==", "operador");
            put("<", "operador");
            put("inteiro", "palavra reservada");
            put("real", "palavra reservada");
            put("enquanto", "palavra reservada");
            put("imprime", "palavra reservada");
            put("le", "palavra reservada");
            put("se", "palavra reservada");
            put("(", "separador");
            put(")", "separador");
        }
    };

    public List<Token> getListaTokens() {
        return Collections.unmodifiableList(listaTokens);
    }

    public void analisaCodigo(String linha) {
        linha = removeComentarios(linha);
        linha = removeTabulacoes(linha);
        armazenaEClassificaTokens(linha);
    }

    private String removeTabulacoes(String linha) {
        return linha.replace(TABULACAO_REGEX, STRING_VAZIA);
    }

    private String removeComentarios(String linha) {
        if (linha.contains(CARACTER_COMENTARIO)) {
            String linhaSemComentario;

            int posicaoInicioComentario = linha.indexOf(CARACTER_COMENTARIO);

            linhaSemComentario = linha.substring(INICIO_STRING, posicaoInicioComentario);

            return linhaSemComentario;
        }
        return linha;
    }

    private void armazenaEClassificaTokens(String linha) {
        if (!linha.isEmpty()) {
            String token;

            int posicaoEspaco = linha.indexOf(CARACTER_ESPACO);

            if (posicaoEspaco != ESPACO_NAO_ENCONTRADO) {

                token = linha.substring(INICIO_STRING, posicaoEspaco);

                adicionaTokenNaLista(token);

                linha = linha.substring(posicaoEspaco);

                linha = linha.replaceFirst(ESPACOS_REGEX, STRING_VAZIA);

                armazenaEClassificaTokens(linha);
            } else {
                linha = linha.replaceAll(ESPACOS_REGEX, STRING_VAZIA);
                adicionaTokenNaLista(linha);
            }
        }
    }

    private void adicionaTokenNaLista(String token) {
        listaTokens.add(new Token(listaTokens.size() + 1, token, classificaClasseToken(token)));
    }

    private String classificaClasseToken(String token) {
        String classe = classesTokenHashMap.get(token);
        if (classe == null) {
            return classificaConstanteLiteral(token);
        }
        return classe;
    }

    private String classificaConstanteLiteral(String token) {
        try {
            Integer.valueOf(token);
            return CLASSE_INTEIRO_LITERAL;
        } catch (NumberFormatException integerException) {
            try {
                Float.valueOf(token);
                return CLASSE_REAL_LITERAL;
            } catch (NumberFormatException floatException) {
                if (token.equals("true") || token.equals("false")) {
                    return CLASSE_BOOLEANO_LITERAL;
                } else {
                    return token.startsWith(CARACTER_STRING) ? CLASSE_CADEIA_LITERAL : CLASSE_IDENTIFICADOR;
                }
            }
        }
    }

    public void adicionaMarcadorFinal() {
        listaTokens.add(new Token(listaTokens.size() + 1, CLASSE_MARCADOR_FINAL, CLASSE_MARCADOR_FINAL));
    }
}
