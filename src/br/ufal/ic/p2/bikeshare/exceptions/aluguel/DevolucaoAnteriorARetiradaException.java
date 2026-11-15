package br.ufal.ic.p2.bikeshare.exceptions.aluguel;

/**
 * Lancada quando a data de devolucao e anterior a de retirada.
 */
public class DevolucaoAnteriorARetiradaException extends AluguelException {

    private static final long serialVersionUID = 1L;

    public DevolucaoAnteriorARetiradaException() {
        super("Data de devolucao anterior a data de retirada.");
    }
}
