package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando a validade do cartao esta vazia ou so com espacos.
 */
public class ValidadeCartaoNulaException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public ValidadeCartaoNulaException() {
        super("Validade do cartao nao pode ser nula.");
    }
}
