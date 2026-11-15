package br.ufal.ic.p2.bikeshare.exceptions.estacao;

/**
 * Lancada quando nenhuma estacao tem o id informado.
 */
public class EstacaoNaoExisteException extends EstacaoException {

    private static final long serialVersionUID = 1L;

    public EstacaoNaoExisteException() {
        super("Estacao nao existe.");
    }
}
