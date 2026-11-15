package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando outro usuario ja usa o email (sem diferenciar maiusculas).
 */
public class EmailJaCadastradoException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public EmailJaCadastradoException() {
        super("Email ja cadastrado.");
    }
}
