package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando o valor nao e um numero.
 */
public class ValorNaoNumericoException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public ValorNaoNumericoException() {
        super("Valor deve ser numerico.");
    }
}
