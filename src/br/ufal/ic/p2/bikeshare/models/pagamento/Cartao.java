package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.pagamento.NumeroCartaoNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.ValidadeCartaoInvalidaException;
import br.ufal.ic.p2.bikeshare.exceptions.pagamento.ValidadeCartaoNulaException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;
import br.ufal.ic.p2.bikeshare.utils.Formatador;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.time.YearMonth;

/**
 * Pagamento com cartao de credito. A cobranca e aprovada na devolucao.
 *
 * Imutavel: trocar de cartao e criar um Cartao novo. Como o construtor
 * valida tudo, nao existe Cartao com numero vazio ou validade invalida.
 */
public final class Cartao extends MetodoPagamento {

    private static final long serialVersionUID = 1L;

    /** Nome deste metodo de pagamento. */
    public static final String NOME = "cartao";

    private final String numero;
    private final YearMonth validade;

    /**
     * @param numero   numero do cartao, guardado como informado
     * @param validade validade no formato MM/aaaa
     * @throws NumeroCartaoNuloException       se o numero estiver vazio
     * @throws ValidadeCartaoNulaException     se a validade estiver vazia
     * @throws ValidadeCartaoInvalidaException se a validade nao for um MM/aaaa valido
     */
    public Cartao(String numero, String validade)
            throws NumeroCartaoNuloException, ValidadeCartaoNulaException, ValidadeCartaoInvalidaException {
        this.numero = Validacao.exigirPreenchido(numero, NumeroCartaoNuloException::new).trim();
        this.validade = Conversor.paraMesAno(validade,
                ValidadeCartaoNulaException::new, ValidadeCartaoInvalidaException::new);
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getNumeroCartao() {
        return numero;
    }

    /**
     * Validade no formato MM/aaaa (ex.: "02/2031").
     */
    @Override
    public String getValidadeCartao() {
        return Formatador.mesAno(validade);
    }
}