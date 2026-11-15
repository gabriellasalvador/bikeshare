package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando a bicicleta nao esta estacionada na estacao informada.
 */
public class BicicletaForaDaEstacaoException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public BicicletaForaDaEstacaoException() {
        super("Bicicleta nao esta nesta estacao.");
    }
}
