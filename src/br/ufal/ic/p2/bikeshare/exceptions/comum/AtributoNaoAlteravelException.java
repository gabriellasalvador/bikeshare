package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando o atributo nao existe ou e controlado pelo sistema.
 */
public class AtributoNaoAlteravelException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public AtributoNaoAlteravelException() {
        super("Atributo nao pode ser alterado.");
    }
}
