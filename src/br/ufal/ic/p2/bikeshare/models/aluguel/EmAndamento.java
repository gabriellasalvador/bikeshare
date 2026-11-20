package br.ufal.ic.p2.bikeshare.models.aluguel;

import br.ufal.ic.p2.bikeshare.exceptions.aluguel.AluguelEmAndamentoException;
import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Estado de um aluguel cuja bicicleta ainda nao foi devolvida.
 *
 * Os dados do fim da corrida (momento, estacao de destino e valor)
 * ainda nao existem, entao pedi-los e recusado.
 */
public final class EmAndamento extends EstadoAluguel {

    private static final long serialVersionUID = 1L;

    /** Nome deste estado. */
    public static final String NOME = "emAndamento";

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public boolean emAndamento() {
        return true;
    }

    /**
     * @throws AluguelEmAndamentoException sempre: a corrida ainda nao terminou
     */
    @Override
    public LocalDateTime getFim() throws AluguelEmAndamentoException {
        throw new AluguelEmAndamentoException();
    }

    /**
     * @throws AluguelEmAndamentoException sempre: a corrida ainda nao terminou
     */
    @Override
    public Estacao getEstacaoDestino() throws AluguelEmAndamentoException {
        throw new AluguelEmAndamentoException();
    }

    /**
     * @throws AluguelEmAndamentoException sempre: a corrida ainda nao terminou
     */
    @Override
    public BigDecimal getValorCobrado() throws AluguelEmAndamentoException {
        throw new AluguelEmAndamentoException();
    }
}