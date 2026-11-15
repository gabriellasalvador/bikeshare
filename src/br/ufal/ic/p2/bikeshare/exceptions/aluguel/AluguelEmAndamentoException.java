package br.ufal.ic.p2.bikeshare.exceptions.aluguel;

/**
 * Lancada quando se consulta um dado que so existe depois da devolucao.
 */
public class AluguelEmAndamentoException extends AluguelException {

    private static final long serialVersionUID = 1L;

    public AluguelEmAndamentoException() {
        super("Aluguel ainda esta em andamento.");
    }
}
