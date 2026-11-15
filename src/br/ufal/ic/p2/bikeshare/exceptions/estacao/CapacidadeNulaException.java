package br.ufal.ic.p2.bikeshare.exceptions.estacao;

/**
 * Lancada quando a capacidade esta vazia ou so com espacos.
 */
public class CapacidadeNulaException extends EstacaoException {

    private static final long serialVersionUID = 1L;

    public CapacidadeNulaException() {
        super("Capacidade nao pode ser nula.");
    }
}
