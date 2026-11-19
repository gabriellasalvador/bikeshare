package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.AtributoNaoAlteravelException;

import java.util.HashMap;
import java.util.Map;

/**
 * Liga o nome de cada atributo alteravel ao jeito de altera-lo.
 *
 * Mesma ideia da {@link TabelaDeAtributos}, para escrita: so os atributos
 * registrados aqui podem ser alterados. Os controlados pelo sistema
 * (tipo, estado, pendencias...) simplesmente nao sao registrados, e
 * pedir para altera-los e recusado.
 *
 * @param <T> tipo do objeto alterado
 */
public final class TabelaDeAlteracoes<T> {

    private final Map<String, EditorDeAtributo<T>> editores = new HashMap<>();

    /**
     * Registra um atributo alteravel. Devolve a propria tabela, para encadear chamadas.
     *
     * @param nome   nome do atributo (diferencia maiusculas de minusculas)
     * @param editor como alterar o atributo
     */
    public TabelaDeAlteracoes<T> com(String nome, EditorDeAtributo<T> editor) {
        editores.put(nome, editor);
        return this;
    }

    /**
     * Altera um atributo do objeto.
     *
     * @throws AtributoNaoAlteravelException se o atributo nao estiver registrado
     * @throws BikeshareException            se o valor for recusado
     */
    public void alterar(T alvo, String atributo, String valor) throws BikeshareException {
        EditorDeAtributo<T> editor = (atributo == null) ? null : editores.get(atributo);
        if (editor == null) {
            throw new AtributoNaoAlteravelException();
        }
        editor.alterar(alvo, valor);
    }
}