package br.ufal.ic.p2.bikeshare.exceptions.plano;

/**
 * Lancada quando o plano informado nao existe.
 */
public class PlanoInvalidoException extends PlanoException {

    private static final long serialVersionUID = 1L;

    public PlanoInvalidoException() {
        super("Plano invalido.");
    }
}
