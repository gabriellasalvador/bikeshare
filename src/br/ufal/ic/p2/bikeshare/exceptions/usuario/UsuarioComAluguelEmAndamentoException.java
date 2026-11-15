package br.ufal.ic.p2.bikeshare.exceptions.usuario;

/**
 * Lancada quando o usuario tenta uma nova retirada sem ter devolvido a anterior.
 */
public class UsuarioComAluguelEmAndamentoException extends UsuarioException {

    private static final long serialVersionUID = 1L;

    public UsuarioComAluguelEmAndamentoException() {
        super("Usuario ja possui um aluguel em andamento.");
    }
}
