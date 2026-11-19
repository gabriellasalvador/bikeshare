package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Como alterar um atributo de um objeto a partir de um texto.
 *
 * E o "par" do {@link LeitorDeAtributo}: um le, o outro escreve.
 * Interface funcional (pode ser escrita como lambda), que pode lancar
 * excecoes de negocio, por exemplo "Nome nao pode ser nulo.".
 *
 * @param <T> tipo do objeto alterado
 */
@FunctionalInterface
public interface EditorDeAtributo<T> {

    /**
     * Altera o atributo do objeto com o valor informado.
     */
    void alterar(T alvo, String valor) throws BikeshareException;
}