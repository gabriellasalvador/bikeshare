package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando o valor esta vazio ou so com espacos.
 */
public class ValorNuloException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public ValorNuloException() {
        super("Valor nao pode ser nulo.");
    }
}
