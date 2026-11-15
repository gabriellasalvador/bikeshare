package br.ufal.ic.p2.bikeshare.exceptions.estacao;

/**
 * Lancada quando o ciclista tenta devolver uma bicicleta numa estacao lotada.
 */
public class EstacaoCheiaException extends EstacaoException {

    private static final long serialVersionUID = 1L;

    public EstacaoCheiaException() {
        super("Estacao cheia.");
    }
}
