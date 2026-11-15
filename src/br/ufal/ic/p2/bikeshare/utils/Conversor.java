package br.ufal.ic.p2.bikeshare.utils;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unico ponto do sistema que transforma texto (vindo da Facade) em numero ou data.
 *
 * Todas as conversoes:
 * - tratam texto so com espacos como vazio;
 * - usam expressao regular estrita, entao "NaN", "Infinity", "1e3" e afins nunca passam
 *   (licao do feedback do WePayU: validacoes numericas aceitavam NaN e infinito);
 * - recebem as fabricas das excecoes, para cada campo manter sua mensagem especifica.
 *
 * Valores monetarios usam BigDecimal, que representa centavos com exatidao
 * (com double, 0,1 + 0,2 nao da exatamente 0,3).
 */
public final class Conversor {

    /** Inteiro com sinal opcional: "5", "-3". Recusa "5.0", "2,5", "1e3". */
    private static final Pattern INTEIRO = Pattern.compile("-?\\d+");

    /** Decimal com sinal opcional e virgula ou ponto: "50", "25,5", "120,50", "-5". */
    private static final Pattern DECIMAL = Pattern.compile("-?\\d+([.,]\\d+)?");

    /** Validade de cartao: mes com 1 ou 2 digitos, barra e ano com 4 digitos. */
    private static final Pattern MES_ANO = Pattern.compile("(\\d{1,2})/(\\d{4})");

    private Conversor() {
        // classe utilitaria: nao deve ser instanciada
    }

    /**
     * Converte um texto em numero inteiro.
     *
     * @param seVazio    fabrica da excecao para texto em branco
     * @param seInvalido fabrica da excecao para texto que nao e inteiro (ou estoura o int)
     */
    public static <V extends BikeshareException, I extends BikeshareException>
    int paraInteiro(String texto, Supplier<V> seVazio, Supplier<I> seInvalido) throws V, I {
        String limpo = Validacao.exigirPreenchido(texto, seVazio).trim();
        if (!INTEIRO.matcher(limpo).matches()) {
            throw seInvalido.get();
        }
        try {
            return Integer.parseInt(limpo);
        } catch (NumberFormatException estouro) {
            throw seInvalido.get();
        }
    }

    /**
     * Converte um texto em valor decimal exato (BigDecimal), aceitando virgula ou ponto.
     *
     * @param seVazio    fabrica da excecao para texto em branco
     * @param seInvalido fabrica da excecao para texto que nao e numero
     */
    public static <V extends BikeshareException, I extends BikeshareException>
    BigDecimal paraDecimal(String texto, Supplier<V> seVazio, Supplier<I> seInvalido) throws V, I {
        String limpo = Validacao.exigirPreenchido(texto, seVazio).trim();
        if (!DECIMAL.matcher(limpo).matches()) {
            throw seInvalido.get();
        }
        return new BigDecimal(limpo.replace(',', '.'));
    }

    /**
     * Converte um texto no formato d/M/aaaa H:mm em data e hora.
     * Datas inexistentes no calendario (31/2, 29/2/2025) e horarios invalidos sao recusados.
     *
     * @param seVazio    fabrica da excecao para texto em branco
     * @param seInvalido fabrica da excecao para formato ou data invalidos
     */
    public static <V extends BikeshareException, I extends BikeshareException>
    LocalDateTime paraDataHora(String texto, Supplier<V> seVazio, Supplier<I> seInvalido) throws V, I {
        String limpo = Validacao.exigirPreenchido(texto, seVazio).trim();
        try {
            return LocalDateTime.parse(limpo, Formatos.DATA_HORA);
        } catch (DateTimeParseException formatoInvalido) {
            throw seInvalido.get();
        }
    }

    /**
     * Converte um texto no formato MM/aaaa (validade de cartao) em mes e ano.
     * Recusa mes fora de 1 a 12 e outros separadores ("12-2028").
     *
     * @param seVazio    fabrica da excecao para texto em branco
     * @param seInvalido fabrica da excecao para formato ou mes invalidos
     */
    public static <V extends BikeshareException, I extends BikeshareException>
    YearMonth paraMesAno(String texto, Supplier<V> seVazio, Supplier<I> seInvalido) throws V, I {
        String limpo = Validacao.exigirPreenchido(texto, seVazio).trim();
        Matcher partes = MES_ANO.matcher(limpo);
        if (!partes.matches()) {
            throw seInvalido.get();
        }
        int mes = Integer.parseInt(partes.group(1));
        int ano = Integer.parseInt(partes.group(2));
        if (mes < 1 || mes > 12) {
            throw seInvalido.get();
        }
        return YearMonth.of(ano, mes);
    }
}
