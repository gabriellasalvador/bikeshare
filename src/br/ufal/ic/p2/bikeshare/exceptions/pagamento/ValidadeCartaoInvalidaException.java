package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando a validade nao esta no formato MM/aaaa ou tem mes inexistente.
 */
public class ValidadeCartaoInvalidaException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public ValidadeCartaoInvalidaException() {
        super("Validade do cartao invalida.");
    }
}
