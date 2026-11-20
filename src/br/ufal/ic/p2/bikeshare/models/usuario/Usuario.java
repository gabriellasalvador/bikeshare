package br.ufal.ic.p2.bikeshare.models.usuario;

import br.ufal.ic.p2.bikeshare.exceptions.comum.NomeNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.EmailInvalidoException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.EmailNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.SenhaNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioComAluguelEmAndamentoException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioInadimplenteException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioNaoEhPessoaFisicaException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioNaoEhPessoaJuridicaException;
import br.ufal.ic.p2.bikeshare.models.Identificavel;
import br.ufal.ic.p2.bikeshare.models.pagamento.Carteira;
import br.ufal.ic.p2.bikeshare.models.pagamento.MetodoPagamento;
import br.ufal.ic.p2.bikeshare.models.pagamento.SaldoPreCarregado;
import br.ufal.ic.p2.bikeshare.models.pagamento.SemMetodoPagamento;
import br.ufal.ic.p2.bikeshare.models.plano.Plano;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Pessoa ou empresa cadastrada para usar as bicicletas.
 *
 * Classe abstrata: todo usuario e uma {@link PessoaFisica} ou uma
 * {@link PessoaJuridica}. Aqui ficam os dados e regras comuns aos dois.
 *
 * Invariantes (sempre verdadeiras, protegidas pelo construtor e pelos setters):
 * nome, email e senha nunca ficam vazios, e o email sempre tem formato valido.
 * Os setters usam as mesmas validacoes do construtor, entao nao existe forma
 * de deixar um usuario em estado invalido.
 *
 * Dados exclusivos de um tipo (CPF, CNPJ, razao social) tem aqui uma versao
 * padrao que recusa o pedido; cada subclasse sobrescreve apenas o que possui.
 *
 * A carteira (saldo pre-carregado) fica escondida aqui dentro: so e usada
 * atraves do metodo de pagamento, nunca entregue para fora.
 */
public abstract class Usuario implements Identificavel, Serializable {

    private static final long serialVersionUID = 1L;

    /** Algo antes do @, algo depois, e um ponto no dominio. Sem espacos. */
    private static final Pattern FORMATO_EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");

    private final String id;
    private String nome;
    private String email;
    private String senha;
    private Plano plano;
    private final Carteira carteira = new Carteira();
    private MetodoPagamento metodoPagamento = new SemMetodoPagamento();
    private EstadoUsuario estado = EstadoUsuario.ATIVO;
    private String idAluguelAtivo;
    private BigDecimal totalPendencias = BigDecimal.ZERO;

    /**
     * Cria um usuario ativo, sem metodo de pagamento.
     *
     * @param id    identificador gerado pelo sistema (ex.: "U1")
     * @param nome  nome do usuario
     * @param email email do usuario
     * @param senha senha do usuario
     * @param plano plano contratado
     * @throws NomeNuloException      se o nome estiver vazio
     * @throws EmailNuloException     se o email estiver vazio
     * @throws EmailInvalidoException se o email nao tiver formato valido
     * @throws SenhaNulaException     se a senha estiver vazia
     */
    protected Usuario(String id, String nome, String email, String senha, Plano plano)
            throws NomeNuloException, UsuarioException {
        this.id = Objects.requireNonNull(id, "id");
        this.nome = validarNome(nome);
        this.email = validarEmail(email);
        this.senha = validarSenha(senha);
        this.plano = Objects.requireNonNull(plano, "plano");
    }

    // ------------------------------------------------------------------ tipo

    /**
     * Tipo do usuario, como aparece para o usuario (ex.: "pessoaFisica").
     */
    public abstract String getTipo();

    // ------------------------------------------------------------------ dados comuns

    @Override
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    /**
     * @throws NomeNuloException se o nome estiver vazio (nada e alterado)
     */
    public void setNome(String nome) throws NomeNuloException {
        this.nome = validarNome(nome);
    }

    public String getEmail() {
        return email;
    }

    /**
     * Altera o email. A unicidade entre usuarios e conferida por quem guarda
     * todos os usuarios; aqui so se garante o formato.
     *
     * @throws EmailNuloException     se o email estiver vazio (nada e alterado)
     * @throws EmailInvalidoException se o email nao tiver formato valido (nada e alterado)
     */
    public void setEmail(String email) throws EmailNuloException, EmailInvalidoException {
        this.email = validarEmail(email);
    }

    /**
     * Indica se este usuario usa o email informado, sem diferenciar
     * maiusculas de minusculas (ANA@exemplo.com e ana@exemplo.com sao o mesmo).
     */
    public boolean temEmail(String outroEmail) {
        return outroEmail != null && normalizarEmail(email).equals(normalizarEmail(outroEmail));
    }

    public String getSenha() {
        return senha;
    }

    /**
     * @throws SenhaNulaException se a senha estiver vazia (nada e alterado)
     */
    public void setSenha(String senha) throws SenhaNulaException {
        this.senha = validarSenha(senha);
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = Objects.requireNonNull(plano, "plano");
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    /**
     * Troca o metodo de pagamento (cartao ou pix). O saldo da carteira
     * continua guardado, caso o usuario volte a usar saldo pre-carregado.
     */
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = Objects.requireNonNull(metodoPagamento, "metodo de pagamento");
    }

    /**
     * Passa a pagar com o saldo da propria carteira. O saldo que ja existia
     * e mantido (a carteira e sempre a mesma, desde o cadastro).
     */
    public void usarSaldoPreCarregado() {
        this.metodoPagamento = new SaldoPreCarregado(carteira);
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    /**
     * Id do aluguel que o usuario esta fazendo agora, se houver.
     *
     * @return o id do aluguel em andamento, ou vazio se nao houver nenhum
     */
    public Optional<String> getIdAluguelAtivo() {
        return Optional.ofNullable(idAluguelAtivo);
    }

    /**
     * Soma das penalidades que o usuario ainda nao pagou. Comeca em zero.
     */
    public BigDecimal getTotalPendencias() {
        return totalPendencias;
    }

    // ------------------------------------------------------------------ aluguel

    /**
     * Confere se o usuario, por sua propria situacao, pode iniciar uma corrida.
     * O metodo de pagamento e conferido a parte, depois da bicicleta.
     *
     * @throws UsuarioInadimplenteException           se tiver penalidades nao pagas
     * @throws UsuarioComAluguelEmAndamentoException  se ja estiver com uma bicicleta
     */
    public void verificarPodeRetirar() throws UsuarioException {
        if (estado == EstadoUsuario.INADIMPLENTE) {
            throw new UsuarioInadimplenteException();
        }
        if (idAluguelAtivo != null) {
            throw new UsuarioComAluguelEmAndamentoException();
        }
    }

    /**
     * Registra que o usuario comecou uma corrida.
     *
     * @param idAluguel id do aluguel que acabou de ser criado
     */
    public void iniciarAluguel(String idAluguel) {
        this.idAluguelAtivo = Objects.requireNonNull(idAluguel, "id do aluguel");
    }

    // ------------------------------------------------------------------ dados exclusivos

    /**
     * @throws UsuarioNaoEhPessoaFisicaException se o usuario nao for pessoa fisica
     */
    public String getCpf() throws UsuarioNaoEhPessoaFisicaException {
        throw new UsuarioNaoEhPessoaFisicaException();
    }

    /**
     * @throws UsuarioNaoEhPessoaJuridicaException se o usuario nao for pessoa juridica
     */
    public String getCnpj() throws UsuarioNaoEhPessoaJuridicaException {
        throw new UsuarioNaoEhPessoaJuridicaException();
    }

    /**
     * @throws UsuarioNaoEhPessoaJuridicaException se o usuario nao for pessoa juridica
     */
    public String getRazaoSocial() throws UsuarioNaoEhPessoaJuridicaException {
        throw new UsuarioNaoEhPessoaJuridicaException();
    }

    // ------------------------------------------------------------------ validacoes

    private static String validarNome(String nome) throws NomeNuloException {
        return Validacao.exigirPreenchido(nome, NomeNuloException::new);
    }

    private static String validarEmail(String email) throws EmailNuloException, EmailInvalidoException {
        String preenchido = Validacao.exigirPreenchido(email, EmailNuloException::new).trim();
        if (!FORMATO_EMAIL.matcher(preenchido).matches()) {
            throw new EmailInvalidoException();
        }
        return preenchido;
    }

    private static String validarSenha(String senha) throws SenhaNulaException {
        return Validacao.exigirPreenchido(senha, SenhaNulaException::new);
    }

    private static String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}