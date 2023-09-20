import java.util.*;

public class AnalisadorLexico {

    private static final int INICIO_STRING = 0;
    private static final String CARACTER_COMENTARIO = "'";
    private static final String CARACTER_ESPACO = " ";
    private static final int ESPACO_NAO_ENCONTRADO = -1;
    private static final String ESPACOS_REGEX = "\\s+";
    private static final String TABULACAO_REGEX = "\t";
    private static final String STRING_VAZIA = "";

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
            return Character.isLetter(token.charAt(INICIO_STRING)) ? "identificador" : "constante literal";
        }
        return classe;
    }

    public void adicionaMarcadorFinal() {
        listaTokens.add(new Token(listaTokens.size() + 1, "$", "$"));
    }
}
