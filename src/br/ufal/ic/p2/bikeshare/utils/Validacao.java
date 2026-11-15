package br.ufal.ic.p2.bikeshare.utils;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

import java.util.function.Supplier;

/**
 * Checagens de texto reaproveitadas por todo o sistema.
 *
 * O erro a lancar e recebido como fabrica (ex.: NomeNuloException::new), entao a
 * mesma regra serve para qualquer campo sem perder a mensagem especifica de cada um.
 */
public final class Validacao {

    private Validacao() {
        // classe utilitaria: nao deve ser instanciada
    }

    /**
     * Um texto conta como vazio quando e nulo, vazio ou formado so por espacos.
     */
    public static boolean estaEmBranco(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /**
     * Exige que o texto esteja preenchido e devolve o proprio texto, sem alteracao.
     *
     * @param texto    valor recebido
     * @param seVazio  fabrica da excecao a lancar se o texto estiver em branco
     */
    public static <E extends BikeshareException> String exigirPreenchido(String texto, Supplier<E> seVazio) throws E {
        if (estaEmBranco(texto)) {
            throw seVazio.get();
        }
        return texto;
    }
}
