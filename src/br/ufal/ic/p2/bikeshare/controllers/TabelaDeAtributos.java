package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.AtributoNaoExisteException;

import java.util.HashMap;
import java.util.Map;

/**
 * Liga o nome de cada atributo consultavel ao jeito de le-lo.
 *
 * Substitui o grande "switch (atributo)" do WePayU: cada atributo e
 * registrado uma vez, com uma lambda. Para consultar um atributo novo,
 * basta registrar mais uma linha, sem mexer no resto.
 *
 * Fica na camada de controle (e nao no modelo) porque nomes de atributo
 * e formato de saida sao detalhes de como o sistema conversa com a Facade.
 *
 * @param <T> tipo do objeto consultado
 */
public final class TabelaDeAtributos<T> {

    private final Map<String, LeitorDeAtributo<T>> leitores = new HashMap<>();

    /**
     * Registra um atributo. Devolve a propria tabela, para encadear chamadas.
     *
     * @param nome   nome do atributo (diferencia maiusculas de minusculas)
     * @param leitor como ler o atributo
     */
    public TabelaDeAtributos<T> com(String nome, LeitorDeAtributo<T> leitor) {
        leitores.put(nome, leitor);
        return this;
    }

    /**
     * Le um atributo do objeto.
     *
     * @throws AtributoNaoExisteException se o atributo nao estiver registrado
     * @throws BikeshareException         se o objeto recusar o atributo
     */
    public String ler(T alvo, String atributo) throws BikeshareException {
        LeitorDeAtributo<T> leitor = (atributo == null) ? null : leitores.get(atributo);
        if (leitor == null) {
            throw new AtributoNaoExisteException();
        }
        return leitor.ler(alvo);
    }
}