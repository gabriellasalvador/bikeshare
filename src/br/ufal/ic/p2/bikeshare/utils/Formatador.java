package br.ufal.ic.p2.bikeshare.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Unico ponto do sistema que transforma valores em texto para devolver a Facade.
 * Garante o mesmo formato de saida em todos os comandos.
 */
public final class Formatador {

    private Formatador() {
        // classe utilitaria: nao deve ser instanciada
    }

    /**
     * Valor monetario com duas casas e virgula decimal. Ex.: 175.5 vira "175,50".
     */
    public static String moeda(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_EVEN).toPlainString().replace('.', ',');
    }

    /**
     * Data e hora no formato d/M/aaaa H:mm, sem zeros a esquerda. Ex.: "29/2/2024 7:05".
     */
    public static String dataHora(LocalDateTime dataHora) {
        return dataHora.format(Formatos.DATA_HORA);
    }

    /**
     * Mes e ano no formato MM/aaaa. Ex.: "02/2031".
     */
    public static String mesAno(YearMonth mesAno) {
        return mesAno.format(Formatos.MES_ANO);
    }
}
