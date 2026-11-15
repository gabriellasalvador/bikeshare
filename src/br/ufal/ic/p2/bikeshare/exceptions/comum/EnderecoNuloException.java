package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando o endereco esta vazio ou so com espacos.
 */
public class EnderecoNuloException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public EnderecoNuloException() {
        super("Endereco nao pode ser nulo.");
    }
}
