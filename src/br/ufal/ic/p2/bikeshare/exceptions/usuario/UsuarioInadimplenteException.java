package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando um usuario com pendencias tenta retirar uma bicicleta.
 */
public class UsuarioInadimplenteException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public UsuarioInadimplenteException() {
        super("Usuario inadimplente.");
    }
}
