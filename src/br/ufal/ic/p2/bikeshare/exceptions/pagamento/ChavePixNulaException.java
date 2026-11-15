package br.ufal.ic.p2.bikeshare.exceptions.pagamento;

/**
 * Lancada quando a chave pix esta vazia ou so com espacos.
 */
public class ChavePixNulaException extends PagamentoException {

    private static final long serialVersionUID = 1L;

    public ChavePixNulaException() {
        super("Chave pix nao pode ser nula.");
    }
}
