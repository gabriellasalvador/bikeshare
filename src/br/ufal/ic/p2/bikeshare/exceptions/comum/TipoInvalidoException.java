package br.ufal.ic.p2.bikeshare.exceptions.comum;

/**
 * Lancada quando o tipo informado (de usuario ou bicicleta) nao existe ou nao combina com os parametros.
 */
public class TipoInvalidoException extends EntradaInvalidaException {

    private static final long serialVersionUID = 1L;

    public TipoInvalidoException() {
        super("Tipo invalido.");
    }
}
