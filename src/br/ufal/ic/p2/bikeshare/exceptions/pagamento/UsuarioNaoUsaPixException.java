package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando um dado de pix e pedido a quem nao paga com pix.
 */
public class UsuarioNaoUsaPixException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoUsaPixException() {
        super("Usuario nao usa pix.");
    }
}
