package br.ufal.ic.p2.bikeshare.models;

/**
 * Algo que o sistema identifica por um id unico (ex.: "U1", "E2", "A3").
 *
 * E uma interface (Aula 08): define um contrato que classes de hierarquias
 * diferentes (usuarios, estacoes, alugueis) podem cumprir, sem ter um pai
 * em comum. Assim o mesmo repositorio serve para guardar todas elas.
 */
public interface Identificavel {

    /**
     * Identificador unico, que nunca muda depois de gerado.
     */
    String getId();
}