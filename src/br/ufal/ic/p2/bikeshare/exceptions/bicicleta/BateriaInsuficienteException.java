package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando a bateria esta abaixo do minimo configurado para retirada.
 */
public class BateriaInsuficienteException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public BateriaInsuficienteException() {
        super("Nivel de bateria insuficiente.");
    }
}
