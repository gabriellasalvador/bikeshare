package br.ufal.ic.p2.bikeshare.models.bicicleta;

import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.NivelBateriaForaDoIntervaloException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.NivelBateriaNaoNumericoException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.NivelBateriaNuloException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

/**
 * Bicicleta com motor eletrico. Tem bateria e so pode ser retirada com carga
 * igual ou acima do minimo configurado.
 *
 * Invariante: o nivel de bateria e sempre um inteiro de 0 a 100.
 */
public final class BicicletaEletrica extends Bicicleta {

    private static final long serialVersionUID = 1L;

    /** Nome deste tipo de bicicleta. */
    public static final String TIPO = "eletrica";

    /** Menor e maior carga possiveis, em porcentagem. */
    private static final int CARGA_MINIMA = 0;
    private static final int CARGA_MAXIMA = 100;

    private final int nivelBateria;

    /**
     * @param id           codigo da bicicleta
     * @param nivelBateria texto do nivel de bateria (inteiro de 0 a 100)
     * @throws NivelBateriaNuloException             se o nivel estiver vazio
     * @throws NivelBateriaNaoNumericoException      se o nivel nao for um inteiro
     * @throws NivelBateriaForaDoIntervaloException  se o nivel estiver fora de 0 a 100
     */
    public BicicletaEletrica(String id, String nivelBateria) throws BicicletaException {
        super(id);
        this.nivelBateria = validarNivel(nivelBateria);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public int getNivelBateria() {
        return nivelBateria;
    }

    /**
     * A carga basta se for igual ou maior que o minimo (15% ja serve).
     */
    @Override
    protected boolean temCargaSuficiente(int bateriaMinima) {
        return nivelBateria >= bateriaMinima;
    }

    private static int validarNivel(String texto) throws BicicletaException {
        int nivel = Conversor.paraInteiro(texto,
                NivelBateriaNuloException::new, NivelBateriaNaoNumericoException::new);
        if (nivel < CARGA_MINIMA || nivel > CARGA_MAXIMA) {
            throw new NivelBateriaForaDoIntervaloException();
        }
        return nivel;
    }
}