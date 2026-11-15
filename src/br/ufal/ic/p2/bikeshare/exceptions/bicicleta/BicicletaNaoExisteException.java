package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando nenhuma bicicleta ativa tem o id informado.
 */
public class BicicletaNaoExisteException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public BicicletaNaoExisteException() {
        super("Bicicleta nao existe.");
    }
}
