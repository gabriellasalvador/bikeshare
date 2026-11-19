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
 * Guarda itens identificados por id, para buscar, listar e remover.
 *
 * Padrao Repository: o resto do sistema nao sabe como os itens sao guardados
 * (aqui, um mapa em memoria na ordem de cadastro). E generico ({@code <T>}),
 * entao serve para qualquer tipo de item.
 *
 * Nesta classe o id vem de fora (como o codigo das bicicletas, escolhido
 * pelo operador). Para ids gerados pelo sistema, veja {@link Repositorio}.
 *
 * @param <T> tipo dos itens guardados
 */
public class Registro<T extends Identificavel & Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    // tipo concreto (e nao Map) porque LinkedHashMap garante ser serializavel;
    // tambem mantem a ordem de cadastro
    private final LinkedHashMap<String, T> itens = new LinkedHashMap<>();

    /**
     * Guarda um item cujo id ja foi escolhido.
     *
     * @param item        item a guardar
     * @param seJaExiste  fabrica do erro para id repetido
     * @throws E se ja houver um item com o mesmo id (nada e alterado)
     */
    public <E extends BikeshareException> void adicionar(T item, Supplier<E> seJaExiste) throws E {
        if (contem(item.getId())) {
            throw seJaExiste.get();
        }
        guardar(item);
    }

    /**
     * Indica se ha um item com o id informado.
     */
    public boolean contem(String id) {
        return id != null && itens.containsKey(id.trim());
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
     */
    public void remover(String id) {
        itens.remove(id);
    }

    /**
     * Todos os itens, em ordem de cadastro. A colecao devolvida e somente
     * leitura: so o registro adiciona ou remove itens.
     */
    public Collection<T> todos() {
        return Collections.unmodifiableCollection(itens.values());
    }

    /**
     * Guarda o item sem conferir repeticao. So as subclasses usam, quando
     * ja garantiram que o id e novo.
     */
    protected void guardar(T item) {
        itens.put(item.getId(), item);
    }
}