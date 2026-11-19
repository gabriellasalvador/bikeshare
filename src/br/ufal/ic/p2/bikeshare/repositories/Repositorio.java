package br.ufal.ic.p2.bikeshare.repositories;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.models.Identificavel;

import java.io.Serializable;

/**
 * Registro que tambem gera os ids dos itens (usuarios, estacoes, alugueis).
 *
 * Herda de {@link Registro} o jeito de buscar, listar e remover, e acrescenta
 * os ids sequenciais com prefixo (U1, U2, ...), que seguem estas regras:
 * - um cadastro recusado nao consome id;
 * - um id removido nunca e reaproveitado;
 * - o contador e salvo junto com os itens, entao sobrevive ao reinicio.
 *
 * @param <T> tipo dos itens guardados
 */
public class Repositorio<T extends Identificavel & Serializable> extends Registro<T> {

    private static final long serialVersionUID = 1L;

    private final String prefixo;
    private int ultimoNumero = 0;

    /**
     * @param prefixo letra que antecede o numero do id (ex.: "U" gera U1, U2...)
     */
    public Repositorio(String prefixo) {
        this.prefixo = prefixo;
    }

    /**
     * Reserva o proximo id, cria o item com ele e o guarda.
     *
     * O contador so avanca se a criacao der certo: se a fabrica lancar erro,
     * nada e guardado e o mesmo id sera oferecido no proximo cadastro.
     *
     * @param fabrica receita que cria o item a partir do id
     * @return o item criado e guardado
     * @throws E o erro lancado pela fabrica, se os dados forem invalidos
     */
    public <E extends BikeshareException> T cadastrar(FabricaComId<T, E> fabrica) throws E {
        String id = prefixo + (ultimoNumero + 1);
        T item = fabrica.criar(id);
        guardar(item);
        ultimoNumero++;
        return item;
    }
}