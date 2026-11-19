package br.ufal.ic.p2.bikeshare.models.bicicleta;

import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaSemBateriaException;
import br.ufal.ic.p2.bikeshare.models.Identificavel;

import java.io.Serializable;
import java.util.Objects;

/**
 * Bicicleta do sistema, identificada por um codigo informado pelo operador.
 *
 * Classe abstrata: toda bicicleta e uma {@link BicicletaConvencional} ou
 * uma {@link BicicletaEletrica}. As diferencas entre elas (ter bateria,
 * poder ser retirada com pouca carga) sao resolvidas por polimorfismo:
 * cada subclasse sobrescreve so o que tem de diferente, sem instanceof.
 *
 * A bicicleta nao guarda em qual estacao esta: quem sabe disso e a propria
 * estacao. Assim existe uma unica fonte da verdade, e as duas informacoes
 * nunca ficam desencontradas.
 */
public abstract class Bicicleta implements Identificavel, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private EstadoBicicleta estado = EstadoBicicleta.DISPONIVEL;

    /**
     * Cria uma bicicleta disponivel.
     *
     * @param id codigo da bicicleta, ja validado por quem a cria
     */
    protected Bicicleta(String id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    /**
     * Tipo da bicicleta, como aparece para o usuario (ex.: "eletrica").
     */
    public abstract String getTipo();

    @Override
    public String getId() {
        return id;
    }

    public EstadoBicicleta getEstado() {
        return estado;
    }

    /**
     * Nivel de bateria, de 0 a 100.
     * Por padrao, bicicletas nao tem bateria; quem tiver sobrescreve este metodo.
     *
     * @throws BicicletaSemBateriaException se a bicicleta nao tiver bateria
     */
    public int getNivelBateria() throws BicicletaSemBateriaException {
        throw new BicicletaSemBateriaException();
    }

    /**
     * Indica se a bicicleta pode ser retirada agora: precisa estar disponivel
     * e, se depender de bateria, ter carga suficiente.
     *
     * @param bateriaMinima carga minima exigida das bicicletas com bateria
     */
    public boolean podeSerRetirada(int bateriaMinima) {
        return estado == EstadoBicicleta.DISPONIVEL && temCargaSuficiente(bateriaMinima);
    }

    /**
     * Indica se a carga basta para uma corrida. Por padrao, sim: bicicletas
     * sem bateria nao dependem de carga. Quem tiver bateria sobrescreve.
     */
    protected boolean temCargaSuficiente(int bateriaMinima) {
        return true;
    }
}