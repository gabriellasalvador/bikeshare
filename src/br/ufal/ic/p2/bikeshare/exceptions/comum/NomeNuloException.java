package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando o nome (de usuario ou estacao) esta vazio ou so com espacos.
 */
public class NomeNuloException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public NomeNuloException() {
        super("Nome nao pode ser nulo.");
    }
}
