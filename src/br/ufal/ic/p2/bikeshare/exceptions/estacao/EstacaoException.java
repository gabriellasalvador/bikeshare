package br.ufal.ic.p2.bikeshare.exceptions.estacao;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Base dos erros de cadastro e ocupacao de estacoes.
 * Permite tratar todos os erros deste assunto de uma vez, se necessario.
 */
public abstract class EstacaoException extends BikeshareException {

    private static final long serialVersionUID = 1L;

    protected EstacaoException(String mensagem) {
        super(mensagem);
    }
}
