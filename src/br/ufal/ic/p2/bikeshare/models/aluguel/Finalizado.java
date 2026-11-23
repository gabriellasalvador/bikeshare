package br.ufal.ic.p2.bikeshare.models.aluguel;

import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Corrida normal: a bicicleta foi devolvida dentro do tempo maximo e o
 * valor da tarifa foi cobrado.
 */
public final class Finalizado extends AluguelEncerrado {

    private static final long serialVersionUID = 1L;

    /** Nome deste estado. */
    public static final String NOME = "finalizado";

    /**
     * @param valorCobrado valor da tarifa cobrada
     */
    public Finalizado(LocalDateTime fim, Estacao estacaoDestino, BigDecimal valorCobrado) {
        super(fim, estacaoDestino, valorCobrado);
    }

    @Override
    public String getNome() {
        return NOME;
    }
}