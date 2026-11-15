package br.ufal.ic.p2.bikeshare.exceptions.plano;

/**
 * Lancada quando a mensalidade nao e um numero.
 */
public class MensalidadeNaoNumericaException extends PlanoException {

    private static final long serialVersionUID = 1L;

    public MensalidadeNaoNumericaException() {
        super("Mensalidade deve ser numerica.");
    }
}
