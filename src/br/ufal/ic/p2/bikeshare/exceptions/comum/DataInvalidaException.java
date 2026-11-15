package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando a data nao esta no formato d/M/aaaa H:mm ou nao existe no calendario.
 */
public class DataInvalidaException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public DataInvalidaException() {
        super("Data invalida.");
    }
}
