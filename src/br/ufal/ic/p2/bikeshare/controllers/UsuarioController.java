package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.TipoInvalidoException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.EmailJaCadastradoException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.IdentificacaoUsuarioNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioNaoExisteException;
import br.ufal.ic.p2.bikeshare.models.plano.CatalogoDePlanos;
import br.ufal.ic.p2.bikeshare.models.plano.Plano;
import br.ufal.ic.p2.bikeshare.models.usuario.PessoaFisica;
import br.ufal.ic.p2.bikeshare.models.usuario.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositories.FabricaComId;
import br.ufal.ic.p2.bikeshare.repositories.Repositorio;
import br.ufal.ic.p2.bikeshare.utils.Formatador;

import java.util.Objects;

/**
 * Coordena os casos de uso de usuario: cadastro e consulta.
 *
 * Nao guarda regra de negocio propria: valida o que envolve VARIOS usuarios
 * (email repetido) e delega o resto aos modelos (cada usuario valida seus
 * dados, cada plano valida sua mensalidade).
 */
public class UsuarioController {

    /** Atributos que podem ser consultados e como cada um e lido. */
    private static final TabelaDeAtributos<Usuario> ATRIBUTOS = new TabelaDeAtributos<Usuario>()
            .com("nome", Usuario::getNome)
            .com("email", Usuario::getEmail)
            .com("senha", Usuario::getSenha)
            .com("tipo", Usuario::getTipo)
            .com("estado", u -> u.getEstado().getNome())
            .com("plano", u -> u.getPlano().getNome())
            .com("mensalidade", u -> Formatador.moeda(u.getPlano().getMensalidade()))
            .com("metodoPagamento", u -> u.getMetodoPagamento().getNome())
            .com("cpf", Usuario::getCpf)
            .com("cnpj", Usuario::getCnpj)
            .com("razaoSocial", Usuario::getRazaoSocial);

    private final Repositorio<Usuario> usuarios;
    private final CatalogoDePlanos planos;

    /**
     * @param usuarios onde os usuarios ficam guardados
     * @param planos   planos que podem ser contratados
     */
    public UsuarioController(Repositorio<Usuario> usuarios, CatalogoDePlanos planos) {
        this.usuarios = Objects.requireNonNull(usuarios, "usuarios");
        this.planos = Objects.requireNonNull(planos, "planos");
    }

    // ------------------------------------------------------------------ cadastro (US1)

    /**
     * Cadastra uma pessoa fisica no plano padrao (avulso).
     *
     * @return id gerado (ex.: "U1")
     */
    public String criarPessoaFisica(String nome, String email, String senha, String tipo, String cpf)
            throws BikeshareException {
        exigirTipo(tipo, PessoaFisica.TIPO);
        Plano plano = planos.planoPadrao();
        return cadastrar(id -> new PessoaFisica(id, nome, email, senha, plano, cpf));
    }

    /**
     * Cadastra uma pessoa fisica no plano escolhido.
     *
     * @return id gerado (ex.: "U1")
     */
    public String criarPessoaFisica(String nome, String email, String senha, String tipo, String cpf,
                                    String plano, String mensalidade) throws BikeshareException {
        exigirTipo(tipo, PessoaFisica.TIPO);
        Plano contratado = planos.contratar(plano, mensalidade);
        return cadastrar(id -> new PessoaFisica(id, nome, email, senha, contratado, cpf));
    }

    /**
     * Cadastra uma pessoa juridica no plano padrao (avulso).
     *
     * @return id gerado (ex.: "U1")
     */
    public String criarPessoaJuridica(String nome, String email, String senha, String tipo,
                                      String cnpj, String razaoSocial) throws BikeshareException {
        exigirTipo(tipo, PessoaJuridica.TIPO);
        Plano plano = planos.planoPadrao();
        return cadastrar(id -> new PessoaJuridica(id, nome, email, senha, plano, cnpj, razaoSocial));
    }

    /**
     * Cadastra uma pessoa juridica no plano escolhido.
     *
     * @return id gerado (ex.: "U1")
     */
    public String criarPessoaJuridica(String nome, String email, String senha, String tipo,
                                      String cnpj, String razaoSocial, String plano, String mensalidade)
            throws BikeshareException {
        exigirTipo(tipo, PessoaJuridica.TIPO);
        Plano contratado = planos.contratar(plano, mensalidade);
        return cadastrar(id -> new PessoaJuridica(id, nome, email, senha, contratado, cnpj, razaoSocial));
    }

    // ------------------------------------------------------------------ consulta (US1, US2)

    /**
     * Le um atributo de um usuario, ja no formato de saida.
     * O usuario e conferido antes do atributo.
     */
    public String getAtributo(String idUsuario, String atributo) throws BikeshareException {
        Usuario usuario = buscar(idUsuario);
        return ATRIBUTOS.ler(usuario, atributo);
    }

    // ------------------------------------------------------------------ internos

    /**
     * Cria o usuario com o proximo id e so o guarda se o email estiver livre.
     * Qualquer erro cancela o cadastro inteiro, sem consumir o id.
     */
    private String cadastrar(FabricaComId<Usuario, BikeshareException> fabrica)
            throws BikeshareException {
        Usuario novo = usuarios.cadastrar(id -> {
            Usuario usuario = fabrica.criar(id);
            exigirEmailLivre(usuario.getEmail());
            return usuario;
        });
        return novo.getId();
    }

    private Usuario buscar(String idUsuario) throws BikeshareException {
        return usuarios.buscar(idUsuario, IdentificacaoUsuarioNulaException::new, UsuarioNaoExisteException::new);
    }

    /**
     * Cada metodo de cadastro atende a um tipo; o tipo informado precisa ser esse.
     */
    private static void exigirTipo(String informado, String esperado) throws TipoInvalidoException {
        if (!esperado.equals(informado)) {
            throw new TipoInvalidoException();
        }
    }

    private void exigirEmailLivre(String email) throws EmailJaCadastradoException {
        if (usuarios.existe(outro -> outro.temEmail(email))) {
            throw new EmailJaCadastradoException();
        }
    }
}