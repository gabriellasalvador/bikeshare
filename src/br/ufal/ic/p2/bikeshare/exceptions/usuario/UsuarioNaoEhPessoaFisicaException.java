package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando um atributo exclusivo de pessoa fisica e pedido a outro tipo de usuario.
 */
public class UsuarioNaoEhPessoaFisicaException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoEhPessoaFisicaException() {
        super("Usuario nao eh pessoa fisica.");
    }
}
