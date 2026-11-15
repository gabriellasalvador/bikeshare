package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando o CPF esta vazio ou so com espacos.
 */
public class CpfNuloException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public CpfNuloException() {
        super("CPF nao pode ser nulo.");
    }
}
