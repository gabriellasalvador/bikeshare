package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando o atributo consultado nao existe.
 */
public class AtributoNaoExisteException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public AtributoNaoExisteException() {
        super("Atributo nao existe.");
    }
}
