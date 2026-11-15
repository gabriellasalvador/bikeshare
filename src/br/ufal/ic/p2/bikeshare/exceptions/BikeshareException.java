package br.ufal.ic.p2.bikeshare.exceptions;

/**
 * Raiz de todas as excecoes de regra de negocio do Bikeshare.
 *
 * E uma excecao checada (extends Exception): quem chama um metodo que pode
 * violar uma regra de negocio e obrigado a tratar ou declarar o erro.
 *
 * Cada subclasse concreta representa UMA situacao de erro e carrega uma mensagem
 * fixa. Assim, a mesma situacao sempre gera a mesma mensagem, em qualquer comando
 * (requisito nao funcional 5 do projeto).
 */
public abstract class BikeshareException extends Exception {

    private static final long serialVersionUID = 1L;

    protected BikeshareException(String mensagem) {
        super(mensagem);
    }
}
