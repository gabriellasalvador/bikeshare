package br.ufal.ic.p2.bikeshare.exceptions.estacao;

/**
 * Lancada quando a capacidade nao e um numero inteiro.
 */
public class CapacidadeNaoNumericaException extends EstacaoException {

    private static final long serialVersionUID = 1L;

    public CapacidadeNaoNumericaException() {
        super("Capacidade deve ser numerica.");
    }
}
