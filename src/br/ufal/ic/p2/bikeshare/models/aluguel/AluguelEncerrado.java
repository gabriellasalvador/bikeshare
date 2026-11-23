package br.ufal.ic.p2.bikeshare.models.aluguel;

import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base dos estados de um aluguel que ja terminou (finalizado, cancelado ou
 * expirado). Todos guardam o momento da devolucao, a estacao de destino e o
 * valor cobrado; cada subclasse define seu nome.
 *
 * Imutavel: depois de encerrado, o aluguel nao muda mais.
 */
public abstract class AluguelEncerrado extends EstadoAluguel {

    private static final long serialVersionUID = 1L;

    private final LocalDateTime fim;
    private final Estacao estacaoDestino;
    private final BigDecimal valorCobrado;

    /**
     * @param fim            momento da devolucao
     * @param estacaoDestino estacao onde a bicicleta foi devolvida
     * @param valorCobrado   valor cobrado pela corrida (zero se nada foi cobrado)
     */
    protected AluguelEncerrado(LocalDateTime fim, Estacao estacaoDestino, BigDecimal valorCobrado) {
        this.fim = Objects.requireNonNull(fim, "fim");
        this.estacaoDestino = Objects.requireNonNull(estacaoDestino, "estacao de destino");
        this.valorCobrado = Objects.requireNonNull(valorCobrado, "valor cobrado");
    }

    @Override
    public boolean emAndamento() {
        return false;
    }

    @Override
    public LocalDateTime getFim() {
        return fim;
    }

    @Override
    public Estacao getEstacaoDestino() {
        return estacaoDestino;
    }

    @Override
    public BigDecimal getValorCobrado() {
        return valorCobrado;
    }
}