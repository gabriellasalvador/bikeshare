package br.ufal.ic.p2.bikeshare.utils;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Formatos de data compartilhados entre a leitura (Conversor) e a escrita (Formatador),
 * para que entrada e saida nunca fiquem desencontradas.
 */
final class Formatos {

    /**
     * Data e hora no formato d/M/aaaa H:mm.
     * STRICT recusa datas inexistentes (31/2, 29/2 de ano nao bissexto, 25:00, 10:60).
     * Na leitura aceita zero a esquerda ("07:05"); na escrita nao gera ("7:05").
     */
    static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("d/M/uuuu H:mm").withResolverStyle(ResolverStyle.STRICT);

    /** Mes e ano da validade de cartao, sempre com dois digitos no mes (ex.: 02/2031). */
    static final DateTimeFormatter MES_ANO =
            DateTimeFormatter.ofPattern("MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private Formatos() {
    }
}
