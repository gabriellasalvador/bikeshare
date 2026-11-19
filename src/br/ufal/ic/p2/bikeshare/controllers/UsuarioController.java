package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.AtributoNaoAlteravelException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.TipoInvalidoException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.EmailJaCadastradoException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.EmailNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.IdentificacaoUsuarioNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioNaoExisteException;
import br.ufal.ic.p2.bikeshare.models.pagamento.FabricaDeMetodoPagamento;
import br.ufal.ic.p2.bikeshare.models.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.models.plano.CatalogoDePlanos;
import br.ufal.ic.p2.bikeshare.models.plano.Plano;
import br.ufal.ic.p2.bikeshare.models.usuario.PessoaFisica;
import br.ufal.ic.p2.bikeshare.models.usuario.PessoaJuridica;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositories.FabricaComId;
import br.ufal.ic.p2.bikeshare.repositories.Repositorio;
import br.ufal.ic.p2.bikeshare.utils.Formatador;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.util.Objects;

/**
 * Coordena os casos de uso de usuario: cadastro (US1), consulta (US2)
 * e alteracao (US3).
 *
 * Nao guarda regra de negocio propria: valida o que envolve VARIOS usuarios
 * (email repetido) e delega o resto aos modelos (cada usuario valida seus
 * dados, cada plano valida sua mensalidade).
 */
public class UsuarioController {

    /** Texto exibido quando o usuario nao tem aluguel em andamento. */
    private static final String SEM_ALUGUEL = "nenhum";

    /** Nome do atributo que guarda o metodo de pagamento. */
    private static final String METODO_PAGAMENTO = "metodoPagamento";

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
            .com("numeroCartao", u -> u.getMetodoPagamento().getNumeroCartao())
            .com("validadeCartao", u -> u.getMetodoPagamento().getValidadeCartao())
            .com("chavePix", u -> u.getMetodoPagamento().getChavePix())
            .com("saldo", u -> Formatador.moeda(u.getMetodoPagamento().getSaldo()))
            .com("aluguelAtivo", u -> u.getIdAluguelAtivo().orElse(SEM_ALUGUEL))
            .com("totalPendencias", u -> Formatador.moeda(u.getTotalPendencias()))
            .com("cpf", Usuario::getCpf)
            .com("cnpj", Usuario::getCnpj)
            .com("razaoSocial", Usuario::getRazaoSocial);

    /**
     * Atributos alteraveis com um unico valor e como cada um e alterado.
     * Os demais (tipo, estado, pendencias...) sao controlados pelo sistema.
     */
    private final TabelaDeAlteracoes<Usuario> alteracoes = new TabelaDeAlteracoes<Usuario>()
            .com("nome", Usuario::setNome)
            .com("email", this::alterarEmail)
            .com("senha", Usuario::setSenha)
            .com("plano", this::alterarPlano)
            .com("saldo", (u, valor) -> u.getMetodoPagamento().recarregar(valor))
            .com(METODO_PAGAMENTO, UsuarioController::usarSaldoPreCarregado);

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

    /**
     * Encontra o usuario dono de um email, sem diferenciar maiusculas de minusculas.
     *
     * @return id do usuario (ex.: "U1")
     * @throws EmailNuloException        se o email estiver vazio
     * @throws UsuarioNaoExisteException se nenhum usuario usar esse email
     */
    public String getIdPorEmail(String email) throws EmailNuloException, UsuarioNaoExisteException {
        Validacao.exigirPreenchido(email, EmailNuloException::new);
        return usuarios.buscarPrimeiro(usuario -> usuario.temEmail(email))
                .orElseThrow(UsuarioNaoExisteException::new)
                .getId();
    }

    // ------------------------------------------------------------------ alteracao (US3)

    /**
     * Altera um atributo que recebe um unico valor (nome, email, senha, plano,
     * recarga de saldo, ou metodo de pagamento saldo pre-carregado).
     * O usuario e conferido antes do atributo. Se o valor for recusado, nada muda.
     */
    public void alterar(String idUsuario, String atributo, String valor) throws BikeshareException {
        Usuario usuario = buscar(idUsuario);
        alteracoes.alterar(usuario, atributo, valor);
    }

    /**
     * Troca o metodo de pagamento por um que usa chave (pix).
     *
     * @throws AtributoNaoAlteravelException se o atributo nao for o metodo de pagamento
     */
    public void alterarMetodoPagamento(String idUsuario, String atributo, String metodo, String chave)
            throws BikeshareException {
        Usuario usuario = buscar(idUsuario);
        exigirMetodoPagamento(atributo);
        usuario.setMetodoPagamento(FabricaDeMetodoPagamento.comChave(metodo, chave));
    }

    /**
     * Troca o metodo de pagamento por um que usa numero e validade (cartao).
     *
     * @throws AtributoNaoAlteravelException se o atributo nao for o metodo de pagamento
     */
    public void alterarMetodoPagamento(String idUsuario, String atributo, String metodo,
                                       String numero, String validade) throws BikeshareException {
        Usuario usuario = buscar(idUsuario);
        exigirMetodoPagamento(atributo);
        usuario.setMetodoPagamento(FabricaDeMetodoPagamento.comNumeroEValidade(metodo, numero, validade));
    }

    // ------------------------------------------------------------------ internos

    /**
     * Troca o email, recusando um email que outro usuario ja use.
     * O proprio email atual pode ser informado de novo.
     */
    private void alterarEmail(Usuario usuario, String email) throws BikeshareException {
        if (usuarios.existe(outro -> outro != usuario && outro.temEmail(email))) {
            throw new EmailJaCadastradoException();
        }
        usuario.setEmail(email);
    }

    /**
     * Troca o plano pelo plano padrao escolhido. Escolher o plano que o
     * usuario ja tem nao muda nada (a mensalidade contratada e mantida).
     */
    private void alterarPlano(Usuario usuario, String nomePlano) throws BikeshareException {
        if (!usuario.getPlano().getNome().equals(nomePlano)) {
            usuario.setPlano(planos.contratarPadrao(nomePlano));
        }
    }

    /**
     * Com um unico valor, o unico metodo de pagamento possivel e o saldo pre-carregado.
     */
    private static void usarSaldoPreCarregado(Usuario usuario, String metodo) throws BikeshareException {
        FabricaDeMetodoPagamento.exigir(metodo, SaldoPreCarregado.NOME);
        usuario.usarSaldoPreCarregado();
    }

    private static void exigirMetodoPagamento(String atributo) throws AtributoNaoAlteravelException {
        if (!METODO_PAGAMENTO.equals(atributo)) {
            throw new AtributoNaoAlteravelException();
        }
    }

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