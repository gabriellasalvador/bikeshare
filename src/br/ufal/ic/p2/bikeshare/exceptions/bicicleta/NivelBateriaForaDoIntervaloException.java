package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando o nivel de bateria esta fora de 0 a 100.
 */
public class NivelBateriaForaDoIntervaloException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public NivelBateriaForaDoIntervaloException() {
        super("Nivel de bateria deve estar entre 0 e 100.");
    }
}
