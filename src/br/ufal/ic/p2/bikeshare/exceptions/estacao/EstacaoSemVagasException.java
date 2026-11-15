package br.ufal.ic.p2.bikeshare.exceptions.estacao;

/**
 * Lancada quando o operador tenta cadastrar uma bicicleta numa estacao lotada.
 */
public class EstacaoSemVagasException extends EstacaoException {

    private static final long serialVersionUID = 1L;

    public EstacaoSemVagasException() {
        super("Estacao sem vagas disponiveis.");
    }
}
