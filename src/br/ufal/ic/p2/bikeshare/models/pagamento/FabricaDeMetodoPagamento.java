package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.pagamento.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.PagamentoException;

/**
 * Cria o metodo de pagamento certo a partir do nome e dos dados informados.
 *
 * Padrao Factory: quem pede um metodo nao precisa saber qual classe criar,
 * nem conferir se os dados combinam com o metodo. Cada forma de pedir
 * (so a chave, ou numero e validade) aceita apenas o metodo que usa
 * esses dados; qualquer outra combinacao e recusada.
 */
public final class FabricaDeMetodoPagamento {

    private FabricaDeMetodoPagamento() {
        // classe utilitaria: nao deve ser instanciada
    }

    /**
     * Cria um metodo que usa apenas uma chave (hoje, so o pix).
     *
     * @param nome  nome do metodo pedido
     * @param chave chave informada
     * @throws MetodoPagamentoInvalidoException se o metodo nao for pix
     * @throws PagamentoException               se a chave for invalida
     */
    public static MetodoPagamento comChave(String nome, String chave) throws PagamentoException {
        exigir(nome, Pix.NOME);
        return new Pix(chave);
    }

    /**
     * Cria um metodo que usa numero e validade (hoje, so o cartao).
     *
     * @param nome     nome do metodo pedido
     * @param numero   numero informado
     * @param validade validade informada (MM/aaaa)
     * @throws MetodoPagamentoInvalidoException se o metodo nao for cartao
     * @throws PagamentoException               se o numero ou a validade forem invalidos
     */
    public static MetodoPagamento comNumeroEValidade(String nome, String numero, String validade)
            throws PagamentoException {
        exigir(nome, Cartao.NOME);
        return new Cartao(numero, validade);
    }

    /**
     * Confere que o metodo pedido e exatamente o que esta forma de pedir aceita.
     *
     * @throws MetodoPagamentoInvalidoException se nao for
     */
    public static void exigir(String pedido, String aceito) throws MetodoPagamentoInvalidoException {
        if (!aceito.equals(pedido)) {
            throw new MetodoPagamentoInvalidoException();
        }
    }
}