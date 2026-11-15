package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando o metodo nao existe ou veio com os parametros errados.
 */
public class MetodoPagamentoInvalidoException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public MetodoPagamentoInvalidoException() {
        super("Valor de metodo de pagamento invalido.");
    }
}
