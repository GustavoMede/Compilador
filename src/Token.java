public class Token {

    private int id;
    private String token;

    private String classe;

    public Token(int id, String token, String classe) {
        this.id = id;
        this.token = token;
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "\nToken{" + id + ", " + token + ", " + classe + '}';
    }
}
