package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.comum.EntradaInvalidaException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.ValorNaoNumericoException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.ValorNaoPositivoException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.ValorNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.SaldoInsuficienteException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Dinheiro pre-carregado de um usuario.
 *
 * Cada usuario tem a sua carteira desde o cadastro, mesmo que nao use saldo.
 * Ela fica no usuario, e nao no metodo de pagamento, para que trocar de
 * metodo (ex.: de saldo para pix e de volta) nunca faca o saldo sumir.
 *
 * O saldo nunca fica negativo: so entra valor positivo e so sai o que existe.
 */
public final class Carteira implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal saldo = BigDecimal.ZERO;

    public BigDecimal getSaldo() {
        return saldo;
    }

    /**
     * Indica se ha algum dinheiro na carteira.
     */
    public boolean temSaldo() {
        return saldo.signum() > 0;
    }

    /**
     * Soma um valor ao saldo (a recarga e acumulativa).
     *
     * @param valor texto do valor a recarregar (aceita virgula ou ponto)
     * @throws ValorNuloException         se o valor estiver vazio
     * @throws ValorNaoNumericoException  se o valor nao for numero
     * @throws ValorNaoPositivoException  se o valor for zero ou negativo
     */
    public void recarregar(String valor) throws EntradaInvalidaException {
        BigDecimal credito = Conversor.paraDecimal(valor, ValorNuloException::new, ValorNaoNumericoException::new);
        if (credito.signum() <= 0) {
            throw new ValorNaoPositivoException();
        }
        saldo = saldo.add(credito);
    }

    /**
     * Retira um valor do saldo. Se nao houver saldo suficiente, nada muda.
     *
     * @param valor valor a debitar, nunca negativo
     * @throws SaldoInsuficienteException se o valor for maior que o saldo
     */
    public void debitar(BigDecimal valor) throws SaldoInsuficienteException {
        if (valor.compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException();
        }
        saldo = saldo.subtract(valor);
    }
}