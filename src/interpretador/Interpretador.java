package interpretador;

import model.No;
import model.Token;

import java.util.*;

public class Interpretador {

    private final List<NoVariavel> variaveis = new ArrayList<>();
    private static final String OPERADOR_SOMA = "+";
    private static final String OPERADOR_SUBTRACAO = "-";
    private static final String OPERADOR_DIVISAO = "/";
    private static final String OPERADOR_MULTIPLICACAO = "*";
    private static final String OPERADOR_LOGICO_MAIOR_QUE = ">";
    private static final String OPERADOR_LOGICO_MENOR_QUE = "<";
    private static final String OPERADOR_LOGICO_MAIOR_IGUAL_QUE = ">=";
    private static final String OPERADOR_LOGICO_MENOR_IGUAL_QUE = "<=";
    private static final String OPERADOR_LOGICO_IGUAL = "==";
    private static final String OPERADOR_LOGICO_DIFERENTE = "!=";
    private static final String TIPO_INTEIRO = "inteiro";
    private static final String TIPO_REAL = "real";
    private static final String TIPO_CADEIA = "cadeia";
    private static final String TIPO_LOGICO = "logico";

    public Interpretador() {
    }

    public Object interpretar(No no) {
        switch (no.getTipo()) {
            case comando: {
                return comando(no);
            }
            case comandos: {
                return comandos(no);
            }
            case comando_interno: {
                return comandoInterno(no);
            }
            case decl: {
                return decl(no);
            }
            case tipo: {
                return tipo(no);
            }
            case ids: {
                return ids(no);
            }
            case ids2: {
                return ids2(no);
            }
            case atrib: {
                return atrib(no);
            }
            case expr_arit: {
                return exprArit(no);
            }
            case operan: {
                return operan(no);
            }
            case op_arit: {
                return opArit(no);
            }
            case escrita: {
                return escrita(no);
            }
            case imprim: {
                return imprim(no);
            }
            case leitura: {
                return leitura(no);
            }
            case laco: {
                return laco(no);
            }
            case expr_rel: {
                return exprRel(no);
            }
            case op_rel: {
                return opRel(no);
            }
			case cond: {
				return cond(no);
			}
            case cond_true: {
                return condTrue(no);
            }
            case cond_false: {
                return condFalse(no);
            }
            default:
                return null;
        }
    }

    private Object comando(No no) {
        interpretar(no.getFilho(1));
        return null;
    }

    private Object comandos(No no) {
        if (no.getFilhos().size() == 2) {
            interpretar(no.getFilho(0));
            interpretar(no.getFilho(1));
        }
        return null;
    }

    private Object comandoInterno(No no) {
        return interpretar(no.getFilho(0));
    }

    private Object decl(No no) {
        String tipo = (String) interpretar(no.getFilho(0));
        List<Token> ids = new ArrayList<>();
        if (no.getFilhos().size() >= 2) {
            ids = (List<Token>) interpretar(no.getFilho(1));
        } else {
            ids.add(no.getFilho(0).getToken());
        }
        adicionaVariaveis(tipo, ids);
        return null;
    }

    private Object tipo(No no) {
        return no.getFilho(0).getToken().getImagem();
    }

    private Object ids(No no) {
        List<Token> ids2 = new ArrayList<>();
        if (no.getFilhos().size() == 2) {
            ids2 = (List<Token>) interpretar(no.getFilho(1));
        }
        if (!no.getFilhos().isEmpty()) {
            ids2.add(no.getFilho(0).getToken());
        }
        return ids2;
    }

    private Object ids2(No no) {
        if (no.getFilhos().isEmpty()) {
            return new ArrayList<Token>();
        } else {
            List<Token> ids2 = (List<Token>) interpretar(no.getFilho(1));
            ids2.add(no.getFilho(0).getToken());
            return ids2;
        }
    }

    private Object atrib(No no) {
        String variavelAtrib = no.getFilho(1).getToken().getImagem();

        Optional<NoVariavel> variavelArmazenada = variaveis.stream()
                .filter(noVariavel -> noVariavel.getNome().equals(variavelAtrib))
                .findFirst();

        Object resultadoExprArit = interpretar(no.getFilho(2));

        switch (variavelArmazenada.get().getTipo()) {
            case TIPO_INTEIRO -> variavelArmazenada.get().setValor(Math.round(Float.parseFloat(resultadoExprArit.toString())));
            case TIPO_REAL -> variavelArmazenada.get().setValor(resultadoExprArit);
            case TIPO_CADEIA -> variavelArmazenada.get().setValor(resultadoExprArit.toString());
            case TIPO_LOGICO -> variavelArmazenada.get().setValor(Boolean.parseBoolean(resultadoExprArit.toString()));
        }
        return null;
    }

    private Object exprArit(No no) {
        if (no.getFilhos().size() >= 2) {
            String operador = (String) interpretar(no.getFilho(1));
            List<String> operandos = new ArrayList<>();
            int contador = 2;
            while (contador < no.getFilhos().size()) {
                operandos.add(exprArit2(no.getFilho(contador)));
                contador++;
            }
            /**
             * Caso o No expr_arit tenha mais de dois filhos, significa que temos uma operação aritmetica, sendo assim
             * precisamos resolvê-la
             **/
            return switch (operador) {
                case OPERADOR_SOMA -> somaOperandos(operandos);
                case OPERADOR_SUBTRACAO -> subtraiOperandos(operandos);
                case OPERADOR_DIVISAO -> divideOperandos(operandos);
                case OPERADOR_MULTIPLICACAO -> multiplicaOperandos(operandos);
                default -> 0;
            };
        } else {
            /**
             * Caso contrário, temos uma atribuição simples, portando, podemos apenas retornar o operando.
             **/
            return interpretar(no.getFilho(0));
        }
    }

    private String operan(No no) {
        if (no.getFilho(0).getToken().getImagem().substring(0, 1).matches("^[a-zA-Z]+$")) {
            Optional<NoVariavel> variavel = variaveis.stream()
                    .filter(noVariavel -> noVariavel.getNome().equals(no.getFilho(0).getToken().getImagem()))
                    .findFirst();
            if (variavel.isPresent()) {
                return variavel.get().getValor().toString();
            } else {
                return "0";
            }
        } else {
            return no.getFilho(0).getToken().getImagem();
        }
    }

    private String opArit(No no) {
        return no.getFilho(0).getToken().getImagem();
    }


    private Object escrita(No no) {
        String body = interpretar(no.getFilho(1)).toString();
        System.out.println(body);
        return null;
    }

    private String imprim(No no) {
        Optional<NoVariavel> variavel = variaveis.stream()
                .filter(noVariavel -> noVariavel.getNome().equals(no.getFilho(0).getToken().getImagem()))
                .findFirst();

        if (variavel.isPresent()) {
            return variavel.get().getValor().toString();
        } else {
            return no.getFilho(0).getToken().getImagem();
        }
    }

    private Object leitura(No no) {
        Scanner scanner = new Scanner(System.in);
        NoVariavel variavelLeitura = variaveis.stream()
                .filter(noVariavel -> noVariavel.getNome().equals(no.getFilho(1).getToken().getImagem()))
                .findFirst()
                .get();

        String entrada = scanner.next();

        switch (variavelLeitura.getTipo()) {
            case TIPO_INTEIRO -> variavelLeitura.setValor(Integer.parseInt(entrada));
            case TIPO_REAL -> variavelLeitura.setValor(Float.parseFloat(entrada));
            case TIPO_CADEIA -> variavelLeitura.setValor(entrada);
            case TIPO_LOGICO -> variavelLeitura.setValor(Boolean.parseBoolean(entrada));
        }
        return null;
    }

    private Object laco(No no) {
        boolean resultadoValidacao = (boolean) interpretar(no.getFilho(2));

        while (resultadoValidacao) {
            interpretar(no.getFilho(4));
            interpretar(no.getFilho(5));
            resultadoValidacao = (boolean) interpretar(no.getFilho(2));
        }

        return null;
    }

    private boolean exprRel(No no) {
        String operadorRelacional = (String) interpretar(no.getFilho(0));
        int operando1 = Integer.parseInt(exprRel2(no.getFilho(1)));
        int operando2 = Integer.parseInt(exprRel2(no.getFilho(2)));

        return validaExpressao(operadorRelacional, operando1, operando2);
    }

    private String opRel(No no) {
        return no.getFilho(0).getToken().getImagem();
    }

    private Object cond(No no) {
        boolean resultadoValidacao = (boolean) interpretar(no.getFilho(2));

        if (resultadoValidacao) {
            interpretar(no.getFilho(4));
        } else {
            interpretar(no.getFilho(5));
        }
        return null;
    }

    private Object condTrue(No no) {
        interpretar(no.getFilho(1));
        interpretar(no.getFilho(2));
        return null;
    }

    private Object condFalse(No no) {
        interpretar(no.getFilho(1));
        interpretar(no.getFilho(2));
        return null;
    }

    /**
     * Funções auxiliares.
     **/
    private void adicionaVariaveis(String tipoIds, List<Token> ids) {
        int contador = 0;

        while (contador < ids.size()) {
            variaveis.add(new NoVariavel(tipoIds, ids.get(contador).getImagem()));
            contador++;
        }
    }

    private String exprArit2(No no) {
        return (String) interpretar(no.getFilho(0));
    }

    private String exprRel2(No no) {
        return (String) interpretar(no.getFilho(0));
    }

    private boolean validaExpressao(String operadorRelacional, int operando1, int operando2) {
        switch (operadorRelacional) {
            case OPERADOR_LOGICO_MAIOR_QUE -> {
                return operando1 > operando2;
            }
            case OPERADOR_LOGICO_MENOR_QUE -> {
                return operando1 < operando2;
            }
            case OPERADOR_LOGICO_MAIOR_IGUAL_QUE -> {
                return operando1 >= operando2;
            }
            case OPERADOR_LOGICO_MENOR_IGUAL_QUE -> {
                return operando1 <= operando2;
            }
            case OPERADOR_LOGICO_IGUAL -> {
                return operando1 == operando2;
            }
            case OPERADOR_LOGICO_DIFERENTE -> {
                return operando1 != operando2;
            }
        }
        return false;
    }

    private Object somaOperandos(List<String> operandos) {
        try {
            float resultado = 0;
            int contador = 0;
            while (contador < operandos.size()) {
                resultado += Float.parseFloat(operandos.get(contador));
                contador++;
            }
            return resultado;
        } catch (NumberFormatException e) {
            int resultado = 0;
            int contador = 0;
            while (contador < operandos.size()) {
                resultado += Integer.parseInt(operandos.get(contador));
                contador++;
            }
            return resultado;
        }
    }

    private Object subtraiOperandos(List<String> operandos) {
        try {
            float resultado = 0;
            int contador = 0;
            while (contador < operandos.size()) {
                resultado = resultado - Float.parseFloat(operandos.get(contador));
                contador++;
            }
            return resultado;
        } catch (NumberFormatException e) {
            int resultado = 0;
            int contador = 0;
            while (contador < operandos.size()) {
                resultado = resultado - Integer.parseInt(operandos.get(contador));
                contador++;
            }
            return resultado;
        }
    }

    private Object divideOperandos(List<String> operandos) {
        try {
            float resultado;
            int contador = 2;
            resultado = Float.parseFloat(operandos.get(0)) / Float.parseFloat(operandos.get(1));
            while (contador < operandos.size()) {
                resultado = resultado / Float.parseFloat(operandos.get(contador));
                contador++;
            }
            return resultado;
        } catch (NumberFormatException e) {
            int resultado;
            int contador = 2;
            resultado = Integer.parseInt(operandos.get(0)) / Integer.parseInt(operandos.get(1));
            while (contador < operandos.size()) {
                resultado = resultado / Integer.parseInt(operandos.get(contador));
                contador++;
            }
            return resultado;
        }
    }

    private Object multiplicaOperandos(List<String> operandos) {
        try {
            float resultado = 0;
            int contador = 2;
            resultado = Float.parseFloat(operandos.get(0)) * Float.parseFloat(operandos.get(1));
            while (contador < operandos.size()) {
                resultado = resultado * Float.parseFloat(operandos.get(contador));
                contador++;
            }
            return resultado;
        } catch (NumberFormatException e) {
            int resultado;
            int contador = 2;
            resultado = Integer.parseInt(operandos.get(0)) * Integer.parseInt(operandos.get(1));
            while (contador < operandos.size()) {
                resultado = resultado * Integer.parseInt(operandos.get(contador));
                contador++;
            }
            return resultado;
        }
    }
}
