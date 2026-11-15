package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando o usuario tenta retirar uma bicicleta sem metodo de pagamento.
 */
public class MetodoPagamentoNaoCadastradoException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public MetodoPagamentoNaoCadastradoException() {
        super("Metodo de pagamento nao cadastrado.");
    }
}
