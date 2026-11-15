package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando o email nao tem formato valido.
 */
public class EmailInvalidoException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public EmailInvalidoException() {
        super("Email invalido.");
    }
}
