package br.ufal.ic.p2.bikeshare.repositories;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.models.Identificavel;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Guarda os itens de um tipo (usuarios, estacoes, alugueis) e gera seus ids.
 *
 * Padrao Repository: o resto do sistema nao sabe como os itens sao
 * guardados (aqui, um mapa em memoria); so pede para cadastrar, buscar e
 * remover. E generico ({@code <T>}), entao a mesma classe serve para todos.
 *
 * Os ids sao sequenciais com prefixo (U1, U2, ...) e seguem estas regras:
 * - um cadastro recusado nao consome id;
 * - um id removido nunca e reaproveitado;
 * - o contador e salvo junto com os itens, entao sobrevive ao reinicio.
 *
 * @param <T> tipo dos itens guardados
 */
public class Repositorio<T extends Identificavel & Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String prefixo;
    private int ultimoNumero = 0;
    // tipo concreto (e nao Map) porque LinkedHashMap garante ser serializavel;
    // tambem mantem a ordem de cadastro
    private final LinkedHashMap<String, T> itens = new LinkedHashMap<>();

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
        itens.put(id, item);
        ultimoNumero++;
        return item;
    }

    /**
     * Busca um item pelo id.
     *
     * @param id          id procurado (espacos nas pontas sao ignorados)
     * @param seVazio     fabrica do erro para id em branco
     * @param seNaoExiste fabrica do erro para id inexistente
     */
    public <V extends BikeshareException, N extends BikeshareException>
    T buscar(String id, Supplier<V> seVazio, Supplier<N> seNaoExiste) throws V, N {
        String chave = Validacao.exigirPreenchido(id, seVazio).trim();
        T item = itens.get(chave);
        if (item == null) {
            throw seNaoExiste.get();
        }
        return item;
    }

    /**
     * Primeiro item, em ordem de cadastro, que atende a condicao.
     */
    public Optional<T> buscarPrimeiro(Predicate<T> condicao) {
        return itens.values().stream().filter(condicao).findFirst();
    }

    /**
     * Indica se algum item atende a condicao.
     */
    public boolean existe(Predicate<T> condicao) {
        return itens.values().stream().anyMatch(condicao);
    }

    /**
     * Remove o item com o id informado (se nao existir, nada acontece).
     * O id removido nao volta a ser usado.
     */
    public void remover(String id) {
        itens.remove(id);
    }

    /**
     * Todos os itens, em ordem de cadastro. A colecao devolvida e somente
     * leitura: so o repositorio adiciona ou remove itens.
     */
    public Collection<T> todos() {
        return Collections.unmodifiableCollection(itens.values());
    }
}