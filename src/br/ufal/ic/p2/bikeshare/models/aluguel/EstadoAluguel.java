package br.ufal.ic.p2.bikeshare.models.aluguel;

import br.ufal.ic.p2.bikeshare.exceptions.aluguel.AluguelException;
import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Fase em que um aluguel se encontra, seguindo o diagrama de estados do
 * documento (secao 7): Em andamento, e depois Finalizado, Cancelado ou Expirado.
 *
 * Padrao State: o aluguel guarda um objeto de estado e repassa para ele as
 * perguntas cuja resposta depende da fase. Por exemplo, "qual o valor
 * cobrado?" nao tem resposta enquanto a corrida nao termina. Assim o
 * aluguel nunca precisa de "if (estado == ...)".
 */
public abstract class EstadoAluguel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Nome do estado, como aparece para o usuario (ex.: "emAndamento").
     */
    public abstract String getNome();

    /**
     * Indica se a corrida ainda esta acontecendo.
     */
    public abstract boolean emAndamento();

    /**
     * Momento da devolucao.
     *
     * @throws AluguelException se o aluguel ainda nao terminou
     */
    public abstract LocalDateTime getFim() throws AluguelException;

    /**
     * Estacao onde a bicicleta foi devolvida.
     *
     * @throws AluguelException se o aluguel ainda nao terminou
     */
    public abstract Estacao getEstacaoDestino() throws AluguelException;

    /**
     * Valor cobrado pela corrida.
     *
     * @throws AluguelException se o aluguel ainda nao terminou
     */
    public abstract BigDecimal getValorCobrado() throws AluguelException;
}