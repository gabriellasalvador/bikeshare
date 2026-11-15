package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando a senha esta vazia ou so com espacos.
 */
public class SenhaNulaException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public SenhaNulaException() {
        super("Senha nao pode ser nula.");
    }
}
