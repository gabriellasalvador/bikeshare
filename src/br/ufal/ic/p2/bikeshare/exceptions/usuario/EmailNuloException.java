package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando o email esta vazio ou so com espacos.
 */
public class EmailNuloException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public EmailNuloException() {
        super("Email nao pode ser nulo.");
    }
}
