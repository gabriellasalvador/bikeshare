package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Base dos erros de metodo de pagamento e cobranca.
 * Permite tratar todos os erros deste assunto de uma vez, se necessario.
 */
public abstract class PagamentoException extends BikeshareException {

    private static final long serialVersionUID = 1L;

    protected PagamentoException(String mensagem) {
        super(mensagem);
    }
}
