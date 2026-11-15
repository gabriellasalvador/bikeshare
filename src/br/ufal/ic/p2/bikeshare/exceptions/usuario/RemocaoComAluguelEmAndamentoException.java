package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando tentam remover um usuario que esta com uma bicicleta.
 */
public class RemocaoComAluguelEmAndamentoException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public RemocaoComAluguelEmAndamentoException() {
        super("Nao e possivel remover usuario com aluguel em andamento.");
    }
}
