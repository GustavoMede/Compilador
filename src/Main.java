import analisadorlexico.AnalisadorLexico;
import analisadorsemantico.AnalisadorSemantico;
import analisadorsintatico.AnalisadorSintaticoGeradorArvore;
import interpretador.Interpretador;
import tabelasimbolos.TabelaSimbolos;

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
        File arquivo = new File("exemplo04.apt");

        BufferedReader leitor = new BufferedReader(new FileReader(arquivo));

        AnalisadorLexico analisadorLexico = new AnalisadorLexico();

        String linha = leitor.readLine();

        int contadorLinhas = 1;

        while (linha != null) {
            analisadorLexico.analisaCodigo(linha, contadorLinhas);

            linha = leitor.readLine();
            contadorLinhas++;
        }

        analisadorLexico.adicionaMarcadorFinal(contadorLinhas);

        System.out.println(analisadorLexico.getListaTokens());

        AnalisadorSintaticoGeradorArvore analisadorSintatico =
                new AnalisadorSintaticoGeradorArvore(analisadorLexico.getListaTokens());
        analisadorSintatico.analisar();
        if (analisadorSintatico.temErros()) {
            analisadorSintatico.printErros();
            return;
        }

        System.out.println("\nMUITO BOM, ANÁLISE LÉXICA E SINTÁTICA OK!");

        analisadorSintatico.mostraArvore(analisadorSintatico.getRaiz(), "", true);

        System.out.println("\n\n");

        AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico(analisadorSintatico.getRaiz());

        analisadorSemantico.analisar();

        if (analisadorSemantico.temErros()) {
            analisadorSemantico.printErros();
        }

        TabelaSimbolos.listaTabela();

        Interpretador interpretador = new Interpretador();

        interpretador.interpretar(analisadorSintatico.getRaiz());
    }
}
