package model;

public class Token {

    private final int id;
    private final String imagem;

    private final String classe;

    public String getImagem() {
        return imagem;
    }

    public String getClasse() {
        return classe;
    }

    public Token(int id, String imagem, String classe) {
        this.id = id;
        this.imagem = imagem;
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "\nToken{" + id + ", " + imagem + ", " + classe + '}';
    }
}
