package br.ufal.ic.p2.bikeshare.exceptions.plano;

/**
 * Lancada quando a mensalidade esta vazia ou so com espacos.
 */
public class MensalidadeNulaException extends PlanoException {

    private static final long serialVersionUID = 1L;

    public MensalidadeNulaException() {
        super("Mensalidade nao pode ser nula.");
    }
}
