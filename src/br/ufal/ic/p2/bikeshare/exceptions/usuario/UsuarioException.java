package br.ufal.ic.p2.bikeshare.exceptions.usuario;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Base dos erros de cadastro, consulta e situacao de usuarios.
 * Permite tratar todos os erros deste assunto de uma vez, se necessario.
 */
public abstract class UsuarioException extends BikeshareException {

    private static final long serialVersionUID = 1L;

    protected UsuarioException(String mensagem) {
        super(mensagem);
    }
}
