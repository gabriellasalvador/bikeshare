package br.ufal.ic.p2.bikeshare.exceptions.aluguel;

/**
 * Lancada quando nenhum aluguel tem o id informado.
 */
public class AluguelNaoExisteException extends AluguelException {

    private static final long serialVersionUID = 1L;

    public AluguelNaoExisteException() {
        super("Aluguel nao existe.");
    }
}
