package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando o saldo pre-carregado nao cobre a operacao.
 */
public class SaldoInsuficienteException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public SaldoInsuficienteException() {
        super("Saldo insuficiente.");
    }
}
