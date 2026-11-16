package br.ufal.ic.p2.bikeshare.repositories;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Receita para criar um item a partir do id que o repositorio reservou.
 *
 * E uma interface funcional: pode ser escrita como lambda, por exemplo
 * {@code id -> new PessoaFisica(id, nome, email, senha, plano, cpf)}.
 * Diferente das interfaces funcionais prontas do Java, esta pode lancar
 * excecoes de negocio (o tipo E), o que permite validar durante a criacao.
 *
 * @param <T> tipo do item criado
 * @param <E> tipo do erro que a criacao pode lancar
 */
@FunctionalInterface
public interface FabricaComId<T, E extends BikeshareException> {

    /**
     * Cria o item com o id informado, ou lanca erro se os dados forem invalidos.
     */
    T criar(String id) throws E;
}