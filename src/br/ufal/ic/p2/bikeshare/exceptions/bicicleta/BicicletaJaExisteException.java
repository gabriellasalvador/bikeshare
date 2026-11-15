package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando ja existe uma bicicleta ativa com o id informado.
 */
public class BicicletaJaExisteException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public BicicletaJaExisteException() {
        super("Bicicleta ja existe.");
    }
}
