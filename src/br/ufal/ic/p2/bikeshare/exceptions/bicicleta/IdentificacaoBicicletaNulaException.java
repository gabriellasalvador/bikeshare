package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando o id da bicicleta esta vazio ou so com espacos.
 */
public class IdentificacaoBicicletaNulaException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public IdentificacaoBicicletaNulaException() {
        super("Identificacao da bicicleta nao pode ser nula.");
    }
}
