package br.ufal.ic.p2.bikeshare.models.aluguel;

import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Corrida desistida: a bicicleta voltou para a mesma estacao dentro do
 * prazo de carencia. Nada e cobrado.
 */
public final class Cancelado extends AluguelEncerrado {

    private static final long serialVersionUID = 1L;

    /** Nome deste estado. */
    public static final String NOME = "cancelado";

    public Cancelado(LocalDateTime fim, Estacao estacaoDestino) {
        super(fim, estacaoDestino, BigDecimal.ZERO);
    }

    @Override
    public String getNome() {
        return NOME;
    }
}