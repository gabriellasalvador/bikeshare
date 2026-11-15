package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando um dado de cartao e pedido a quem nao paga com cartao.
 */
public class UsuarioNaoUsaCartaoException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoUsaCartaoException() {
        super("Usuario nao usa cartao.");
    }
}
