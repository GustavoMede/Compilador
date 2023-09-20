import java.io.*;

public class Main {

    /*
        Gustavo Medeiros Coelho: 202012359,
        Rafael Lizardo Pontes Borges: 202112347
        Rodrigo José Modesto Costa: 202111464
        Gustavo Silva Fernandes: 202110213
        Matheus Gigliotti Kassagas: ??
     */
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
