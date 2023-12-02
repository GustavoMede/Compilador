package model;

public class Token {

    private String imagem;
    private Integer linha;
    private Integer coluna;
    private String classe;
    private Integer indiceTabSimb;

    public Token(String imagem, String classe, Integer linha, Integer coluna, Integer indiceTabSimb) {
        super();
        this.imagem = imagem;
        this.linha = linha;
        this.coluna = coluna;
        this.classe = classe;
        this.indiceTabSimb = indiceTabSimb;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Integer getLinha() {
        return linha;
    }

    public void setLinha(Integer linha) {
        this.linha = linha;
    }

    public Integer getColuna() {
        return coluna;
    }

    public void setColuna(Integer coluna) {
        this.coluna = coluna;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Integer getIndiceTabSimb() {
        return indiceTabSimb;
    }

    public void setIndiceTabSimb(Integer indiceTabSimb) {
        this.indiceTabSimb = indiceTabSimb;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imagem == null) ? 0 : imagem.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Token other = (Token) obj;
        if (imagem == null) {
            if (other.imagem != null)
                return false;
        } else if (!imagem.equals(other.imagem))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Token [imagem=" + imagem + ", linha=" + linha + ", coluna=" + coluna + ", classe=" + classe
                + ", indiceTabSimb=" + indiceTabSimb + "]\n";
    }
}
