package br.ufal.ic.p2.bikeshare.exceptions.estacao;

/**
 * Lancada quando a capacidade e zero ou negativa.
 */
public class CapacidadeNaoPositivaException extends EstacaoException {

    private static final long serialVersionUID = 1L;

    public CapacidadeNaoPositivaException() {
        super("Capacidade deve ser positiva.");
    }
}
