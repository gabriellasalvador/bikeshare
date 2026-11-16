package br.ufal.ic.p2.bikeshare.models.usuario;

import br.ufal.ic.p2.bikeshare.exceptions.comum.NomeNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.CpfNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.models.plano.Plano;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

/**
 * Usuario pessoa fisica, identificado pelo CPF.
 */
public final class PessoaFisica extends Usuario {

    private static final long serialVersionUID = 1L;

    /** Nome deste tipo de usuario. */
    public static final String TIPO = "pessoaFisica";

    private final String cpf;

    /**
     * @param cpf CPF, guardado como informado
     * @throws CpfNuloException se o CPF estiver vazio
     * @see Usuario#Usuario(String, String, String, String, Plano) demais validacoes
     */
    public PessoaFisica(String id, String nome, String email, String senha, Plano plano, String cpf)
            throws NomeNuloException, UsuarioException {
        super(id, nome, email, senha, plano);
        this.cpf = Validacao.exigirPreenchido(cpf, CpfNuloException::new).trim();
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getCpf() {
        return cpf;
    }
}