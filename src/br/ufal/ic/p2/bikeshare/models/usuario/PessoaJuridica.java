package br.ufal.ic.p2.bikeshare.models.usuario;

import br.ufal.ic.p2.bikeshare.exceptions.comum.NomeNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.CnpjNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.RazaoSocialNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.models.plano.Plano;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

/**
 * Usuario pessoa juridica (empresa), identificado pelo CNPJ e pela razao social.
 * Cada aluguel continua registrado individualmente, como para pessoa fisica.
 */
public final class PessoaJuridica extends Usuario {

    private static final long serialVersionUID = 1L;

    /** Nome deste tipo de usuario. */
    public static final String TIPO = "pessoaJuridica";

    private final String cnpj;
    private final String razaoSocial;

    /**
     * @param cnpj        CNPJ, guardado como informado
     * @param razaoSocial razao social da empresa
     * @throws CnpjNuloException         se o CNPJ estiver vazio
     * @throws RazaoSocialNulaException  se a razao social estiver vazia
     * @see Usuario#Usuario(String, String, String, String, Plano) demais validacoes
     */
    public PessoaJuridica(String id, String nome, String email, String senha, Plano plano,
                          String cnpj, String razaoSocial) throws NomeNuloException, UsuarioException {
        super(id, nome, email, senha, plano);
        this.cnpj = Validacao.exigirPreenchido(cnpj, CnpjNuloException::new).trim();
        this.razaoSocial = Validacao.exigirPreenchido(razaoSocial, RazaoSocialNulaException::new);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getCnpj() {
        return cnpj;
    }

    @Override
    public String getRazaoSocial() {
        return razaoSocial;
    }
}