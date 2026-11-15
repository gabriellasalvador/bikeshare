package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando se consulta ou informa bateria para uma bicicleta que nao tem.
 */
public class BicicletaSemBateriaException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public BicicletaSemBateriaException() {
        super("Bicicleta nao possui bateria.");
    }
}
