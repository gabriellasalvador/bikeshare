package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.comum.EntradaInvalidaException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.PagamentoException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.UsuarioNaoUsaCartaoException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.UsuarioNaoUsaPixException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.UsuarioNaoUsaSaldoException;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Forma como o usuario paga pelas corridas.
 *
 * Padrao Strategy: cada subclasse sabe autorizar uma retirada e cobrar uma
 * corrida do seu jeito. Quem cobra so chama {@link #cobrar(BigDecimal)}, sem
 * saber se e cartao, pix ou saldo.
 *
 * Os dados especificos de cada forma (numero do cartao, chave pix, saldo)
 * tem aqui uma versao padrao que recusa o pedido. Cada subclasse sobrescreve
 * apenas os dados que realmente possui. Assim ninguem precisa perguntar
 * "esse metodo e cartao?" (sem instanceof): cada um responde por si.
 */
public abstract class MetodoPagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Nome do metodo, como aparece para o usuario (ex.: "cartao", "pix").
     */
    public abstract String getNome();

    /**
     * Confere se o usuario pode iniciar um aluguel com este metodo.
     * Por padrao aprova: cartao e pix sao cobrados so na devolucao.
     *
     * @throws PagamentoException se este metodo nao permitir a retirada
     */
    public void autorizarRetirada() throws PagamentoException {
        // aprovado por padrao
    }

    /**
     * Cobra o valor de uma corrida finalizada.
     * Por padrao aprova: cartao e pix sao cobrados fora do sistema
     * (nao ha integracao com gateway de pagamento real).
     *
     * @param valor valor da corrida, nunca negativo
     * @throws PagamentoException se a cobranca nao puder ser feita
     */
    public void cobrar(BigDecimal valor) throws PagamentoException {
        // aprovado por padrao
    }

    /**
     * @throws UsuarioNaoUsaCartaoException se o metodo nao for cartao
     */
    public String getNumeroCartao() throws UsuarioNaoUsaCartaoException {
        throw new UsuarioNaoUsaCartaoException();
    }

    /**
     * @throws UsuarioNaoUsaCartaoException se o metodo nao for cartao
     */
    public String getValidadeCartao() throws UsuarioNaoUsaCartaoException {
        throw new UsuarioNaoUsaCartaoException();
    }

    /**
     * @throws UsuarioNaoUsaPixException se o metodo nao for pix
     */
    public String getChavePix() throws UsuarioNaoUsaPixException {
        throw new UsuarioNaoUsaPixException();
    }

    /**
     * @throws UsuarioNaoUsaSaldoException se o metodo nao for saldo pre-carregado
     */
    public BigDecimal getSaldo() throws UsuarioNaoUsaSaldoException {
        throw new UsuarioNaoUsaSaldoException();
    }

    /**
     * Adiciona credito ao saldo pre-carregado.
     *
     * @param valor texto do valor a recarregar
     * @throws UsuarioNaoUsaSaldoException se o metodo nao for saldo pre-carregado
     * @throws EntradaInvalidaException    se o valor for vazio, nao numerico ou nao positivo
     */
    public void recarregar(String valor) throws UsuarioNaoUsaSaldoException, EntradaInvalidaException {
        throw new UsuarioNaoUsaSaldoException();
    }
}