package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando a data esta vazia ou so com espacos.
 */
public class DataNulaException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public DataNulaException() {
        super("Data nao pode ser nula.");
    }
}
