package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando o saldo e consultado ou recarregado por quem nao usa saldo pre-carregado.
 */
public class UsuarioNaoUsaSaldoException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoUsaSaldoException() {
        super("Usuario nao usa saldo pre-carregado.");
    }
}
