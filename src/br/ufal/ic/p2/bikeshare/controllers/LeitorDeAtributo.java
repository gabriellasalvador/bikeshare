package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Como ler um atributo de um objeto e devolve-lo como texto.
 *
 * Interface funcional (pode ser escrita como lambda), que pode lancar
 * excecoes de negocio, por exemplo "Usuario nao eh pessoa juridica.".
 *
 * @param <T> tipo do objeto lido
 */
@FunctionalInterface
public interface LeitorDeAtributo<T> {

    /**
     * Le o atributo do objeto e devolve no formato de saida do sistema.
     */
    String ler(T alvo) throws BikeshareException;
}