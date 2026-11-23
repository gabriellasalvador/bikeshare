package br.ufal.ic.p2.bikeshare.models.aluguel;

import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Corrida que passou do tempo maximo. A tarifa nao e cobrada: no lugar dela
 * o usuario recebe uma penalidade, e a bicicleta vai para manutencao.
 */
public final class Expirado extends AluguelEncerrado {

    private static final long serialVersionUID = 1L;

    /** Nome deste estado. */
    public static final String NOME = "expirado";

    public Expirado(LocalDateTime fim, Estacao estacaoDestino) {
        super(fim, estacaoDestino, BigDecimal.ZERO);
    }

    @Override
    public String getNome() {
        return NOME;
    }
}