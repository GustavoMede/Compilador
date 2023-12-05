package interpretador;

public class NoVariavel {

    private String tipo;

    private String nome;

    private Object valor;

    public NoVariavel(String tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
        this.valor = 0;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public Object getValor() {
        return valor;
    }
}
