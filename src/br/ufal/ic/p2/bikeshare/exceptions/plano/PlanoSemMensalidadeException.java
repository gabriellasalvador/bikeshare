package br.ufal.ic.p2.bikeshare.exceptions.plano;

/**
 * Lancada quando se consulta ou informa mensalidade para um plano que nao a cobra.
 */
public class PlanoSemMensalidadeException extends PlanoException {

    private static final long serialVersionUID = 1L;

    public PlanoSemMensalidadeException() {
        super("Plano nao possui mensalidade.");
    }
}
