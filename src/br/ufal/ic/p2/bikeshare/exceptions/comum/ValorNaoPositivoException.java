package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando o valor e zero ou negativo.
 */
public class ValorNaoPositivoException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public ValorNaoPositivoException() {
        super("Valor deve ser positivo.");
    }
}
