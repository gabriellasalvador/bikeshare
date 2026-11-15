package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando um atributo exclusivo de pessoa juridica e pedido a outro tipo de usuario.
 */
public class UsuarioNaoEhPessoaJuridicaException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoEhPessoaJuridicaException() {
        super("Usuario nao eh pessoa juridica.");
    }
}
