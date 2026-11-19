package br.ufal.ic.p2.bikeshare.models.bicicleta;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaSemBateriaException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.NivelBateriaNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.TipoInvalidoException;

/**
 * Cria a bicicleta certa a partir do tipo informado pelo operador.
 *
 * Padrao Factory: quem cadastra bicicletas nao precisa saber qual classe
 * criar, nem conferir se o nivel de bateria combina com o tipo.
 * O tipo diferencia maiusculas de minusculas ("Eletrica" e invalido).
 */
public final class FabricaDeBicicletas {

    private FabricaDeBicicletas() {
        // classe utilitaria: nao deve ser instanciada
    }

    /**
     * Cria uma bicicleta sem informar bateria (so a convencional pode).
     *
     * @throws NivelBateriaNuloException se o tipo for eletrica (ela exige bateria)
     * @throws TipoInvalidoException     se o tipo nao existir
     */
    public static Bicicleta criar(String id, String tipo) throws BikeshareException {
        if (BicicletaConvencional.TIPO.equals(tipo)) {
            return new BicicletaConvencional(id);
        }
        if (BicicletaEletrica.TIPO.equals(tipo)) {
            throw new NivelBateriaNuloException();
        }
        throw new TipoInvalidoException();
    }

    /**
     * Cria uma bicicleta informando a bateria (so a eletrica pode).
     *
     * @throws BicicletaSemBateriaException se o tipo for convencional (ela nao tem bateria)
     * @throws TipoInvalidoException        se o tipo nao existir
     * @throws BikeshareException           se o nivel de bateria for invalido
     */
    public static Bicicleta criar(String id, String tipo, String nivelBateria) throws BikeshareException {
        if (BicicletaEletrica.TIPO.equals(tipo)) {
            return new BicicletaEletrica(id, nivelBateria);
        }
        if (BicicletaConvencional.TIPO.equals(tipo)) {
            throw new BicicletaSemBateriaException();
        }
        throw new TipoInvalidoException();
    }
}