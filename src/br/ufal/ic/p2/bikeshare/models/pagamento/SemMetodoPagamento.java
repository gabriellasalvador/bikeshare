package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.pagamento.MetodoPagamentoNaoCadastradoException;

import java.math.BigDecimal;

/**
 * Representa o usuario que ainda nao cadastrou forma de pagamento.
 *
 * Padrao Null Object: em vez de guardar null no usuario (e espalhar
 * "if (metodo == null)" pelo sistema), o usuario guarda este objeto, que
 * se comporta como um metodo de pagamento que recusa tudo.
 */
public final class SemMetodoPagamento extends MetodoPagamento {

    private static final long serialVersionUID = 1L;

    /** Nome exibido quando o usuario nao tem metodo de pagamento. */
    public static final String NOME = "nenhum";

    @Override
    public String getNome() {
        return NOME;
    }

    /**
     * @throws MetodoPagamentoNaoCadastradoException sempre: sem metodo nao ha retirada
     */
    @Override
    public void autorizarRetirada() throws MetodoPagamentoNaoCadastradoException {
        throw new MetodoPagamentoNaoCadastradoException();
    }

    /**
     * @throws MetodoPagamentoNaoCadastradoException sempre: sem metodo nao ha cobranca
     */
    @Override
    public void cobrar(BigDecimal valor) throws MetodoPagamentoNaoCadastradoException {
        throw new MetodoPagamentoNaoCadastradoException();
    }
}