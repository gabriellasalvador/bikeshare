package br.ufal.ic.p2.bikeshare.exceptions.aluguel;

/**
 * Lancada quando o id do aluguel esta vazio ou so com espacos.
 */
public class IdentificacaoAluguelNulaException extends AluguelException {

    private static final long serialVersionUID = 1L;

    public IdentificacaoAluguelNulaException() {
        super("Identificacao do aluguel nao pode ser nula.");
    }
}
