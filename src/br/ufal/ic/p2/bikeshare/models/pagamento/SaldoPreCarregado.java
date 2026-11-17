package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.comum.EntradaInvalidaException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.SaldoInsuficienteException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Pagamento com dinheiro pre-carregado na carteira do usuario.
 *
 * Diferente de cartao e pix, aqui o sistema controla o dinheiro de verdade:
 * a retirada exige saldo e a devolucao debita o valor da corrida.
 *
 * O saldo nao fica guardado aqui, e sim na Carteira do usuario, que este
 * objeto apenas usa. Por isso trocar de metodo nao apaga o saldo.
 */
public final class SaldoPreCarregado extends MetodoPagamento {

    private static final long serialVersionUID = 1L;

    /** Nome deste metodo de pagamento. */
    public static final String NOME = "saldoPreCarregado";

    private final Carteira carteira;

    /**
     * @param carteira carteira do proprio usuario
     */
    public SaldoPreCarregado(Carteira carteira) {
        this.carteira = Objects.requireNonNull(carteira, "carteira");
    }

    @Override
    public String getNome() {
        return NOME;
    }

    /**
     * @throws SaldoInsuficienteException se a carteira estiver zerada
     */
    @Override
    public void autorizarRetirada() throws SaldoInsuficienteException {
        if (!carteira.temSaldo()) {
            throw new SaldoInsuficienteException();
        }
    }

    /**
     * @throws SaldoInsuficienteException se o valor for maior que o saldo (nada e debitado)
     */
    @Override
    public void cobrar(BigDecimal valor) throws SaldoInsuficienteException {
        carteira.debitar(valor);
    }

    @Override
    public BigDecimal getSaldo() {
        return carteira.getSaldo();
    }

    @Override
    public void recarregar(String valor) throws EntradaInvalidaException {
        carteira.recarregar(valor);
    }
}