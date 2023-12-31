import analisadorlexico.AnalisadorLexico;
import analisadorsemantico.AnalisadorSemantico;
import analisadorsintatico.AnalisadorSintaticoGeradorArvore;
import interpretador.Interpretador;
import tabelasimbolos.TabelaSimbolos;

import java.io.*;

public class Main {
    
    public static void main(String[] args) throws IOException {
        File arquivo = new File("calculofatorial.apt");

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

        System.out.println("\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= EXECUÇÃO DO PROGRAMA =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");

        Interpretador interpretador = new Interpretador();

        interpretador.interpretar(analisadorSintatico.getRaiz());
    }
}
