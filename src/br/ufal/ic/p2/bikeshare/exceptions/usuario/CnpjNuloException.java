package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando o CNPJ esta vazio ou so com espacos.
 */
public class CnpjNuloException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public CnpjNuloException() {
        super("CNPJ nao pode ser nulo.");
    }
}
