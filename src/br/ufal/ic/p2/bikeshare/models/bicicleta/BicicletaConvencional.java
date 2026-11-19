package br.ufal.ic.p2.bikeshare.models.bicicleta;

/**
 * Bicicleta comum, a pedal. Nao tem bateria e nao tem adicional na tarifa.
 */
public final class BicicletaConvencional extends Bicicleta {

    private static final long serialVersionUID = 1L;

    /** Nome deste tipo de bicicleta. */
    public static final String TIPO = "convencional";

    /**
     * @param id codigo da bicicleta
     */
    public BicicletaConvencional(String id) {
        super(id);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }
}