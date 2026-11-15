package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando o numero do cartao esta vazio ou so com espacos.
 */
public class NumeroCartaoNuloException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public NumeroCartaoNuloException() {
        super("Numero do cartao nao pode ser nulo.");
    }
}
