package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando a razao social esta vazia ou so com espacos.
 */
public class RazaoSocialNulaException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public RazaoSocialNulaException() {
        super("Razao social nao pode ser nula.");
    }
}
