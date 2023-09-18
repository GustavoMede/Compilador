public class Token {

    private String token;

    private int linha;

    private int coluna;

    public Token(String token, int linha, int coluna) {
        this.token = token;
        this.linha = linha;
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return "\nToken{" + token + ", " + linha + ", " + coluna + '}';
    }
}
