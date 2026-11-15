package br.ufal.ic.p2.bikeshare.exceptions.comum;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Base dos erros de entrada compartilhados por varios comandos (campos vazios, tipos, atributos, datas e valores).
 * Permite tratar todos os erros deste assunto de uma vez, se necessario.
 */
public abstract class EntradaInvalidaException extends BikeshareException {

    private static final long serialVersionUID = 1L;

    protected EntradaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
