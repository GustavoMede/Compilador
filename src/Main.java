import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        File arquivo = new File("codigoaportugueseido.apt");

        BufferedReader leitor = new BufferedReader(new FileReader(arquivo));

        AnalisadorLexico analisadorLexico = new AnalisadorLexico();

        String linha = leitor.readLine();

        while (linha != null) {
            analisadorLexico.analisaCodigo(linha);

            linha = leitor.readLine();
        }

        analisadorLexico.adicionaMarcadorFinal();

        System.out.println(analisadorLexico.getListaTokens());
    }
}
