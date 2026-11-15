package br.ufal.ic.p2.bikeshare.exceptions.plano;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Base dos erros de plano de tarifacao e mensalidade.
 * Permite tratar todos os erros deste assunto de uma vez, se necessario.
 */
public abstract class PlanoException extends BikeshareException {

    private static final long serialVersionUID = 1L;

    protected PlanoException(String mensagem) {
        super(mensagem);
    }
}
