import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnalisadorLexico {

    private static final int INICIO_STRING = 0;
    private static final String CARACTER_COMENTARIO = "'";
    private static final String CARACTER_ESPACO = " ";
    private static final int ESPACO_NAO_ENCONTRADO = -1;
    private static final String STRING_VAZIA = "";

    private ArrayList<Token> listaTokens = new ArrayList<>();

    public List<Token> getListaTokens() {
        return Collections.unmodifiableList(listaTokens);
    }

    public void analisaCodigo(String linha) {
        linha = removeComentarios(linha);
        linha = removeTabulacoes(linha);
        separaComandosPorEspaco(linha);
    }

    private String removeTabulacoes(String linha) {
        return linha.replace("\t", STRING_VAZIA);
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

    private void separaComandosPorEspaco(String linha) {
        if (!linha.isEmpty()) {
            String token;

            int posicaoEspaco = linha.indexOf(CARACTER_ESPACO);

            if (posicaoEspaco != ESPACO_NAO_ENCONTRADO) {

                token = linha.substring(INICIO_STRING, posicaoEspaco);

                listaTokens.add(new Token(token, 1, 1));

                linha = linha.substring(posicaoEspaco);

                linha = linha.replaceFirst("\\s+", STRING_VAZIA);

                separaComandosPorEspaco(linha);
            } else {
                linha = linha.replaceAll("\\s+", STRING_VAZIA);
                listaTokens.add(new Token(linha, 1, 1));
            }
        }
    }
}
