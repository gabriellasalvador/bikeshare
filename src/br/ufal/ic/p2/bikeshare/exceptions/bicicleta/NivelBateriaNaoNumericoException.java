package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

/**
 * Lancada quando o nivel de bateria nao e um numero inteiro.
 */
public class NivelBateriaNaoNumericoException extends BicicletaException {

    private static final long serialVersionUID = 1L;

    public NivelBateriaNaoNumericoException() {
        super("Nivel de bateria deve ser numerico.");
    }
}
