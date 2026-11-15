package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando nenhum usuario tem o id ou email informado.
 */
public class UsuarioNaoExisteException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoExisteException() {
        super("Usuario nao existe.");
    }
}
