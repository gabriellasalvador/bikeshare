package br.ufal.ic.p2.bikeshare.exceptions.aluguel;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Base dos erros de retirada, devolucao e consulta de alugueis.
 * Permite tratar todos os erros deste assunto de uma vez, se necessario.
 */
public abstract class AluguelException extends BikeshareException {

    private static final long serialVersionUID = 1L;

    protected AluguelException(String mensagem) {
        super(mensagem);
    }
}
