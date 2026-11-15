package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando o nivel de bateria esta vazio ou nao foi informado.
 */
public class NivelBateriaNuloException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public NivelBateriaNuloException() {
        super("Nivel de bateria nao pode ser nulo.");
    }
}
