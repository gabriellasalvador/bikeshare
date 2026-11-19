package br.ufal.ic.p2.bikeshare;

import br.ufal.ic.p2.bikeshare.controllers.Sistema;
import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Unico ponto de entrada da logica de negocio (requisito nao funcional 1).
 *
 * Padrao Facade: os testes de aceitacao (EasyAccept) chamam estes metodos
 * por reflexao. Cada metodo apenas repassa o pedido ao controller certo,
 * sem nenhuma regra de negocio aqui.
 *
 * Os metodos de criarUsuario e alterarUsuario tem varias versoes
 * (sobrecarga): o EasyAccept escolhe a versao pela quantidade de parametros.
 */
public class Facade {

    private static final String AINDA_NAO_IMPLEMENTADO = "Ainda não implementado.";

    private final Sistema sistema = new Sistema();

    // =====================================================================
    // US1 - Cadastro de usuario
    // =====================================================================

    /** Pessoa fisica, plano avulso. */
    public String criarUsuario(String nome, String email, String senha, String tipo, String cpfOuCnpj)
            throws BikeshareException {
        return sistema.usuarios().criarPessoaFisica(nome, email, senha, tipo, cpfOuCnpj);
    }

    /** Pessoa juridica, plano avulso. */
    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial) throws BikeshareException {
        return sistema.usuarios().criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial);
    }

    /** Pessoa fisica, plano escolhido. */
    public String criarUsuario(String nome, String email, String senha, String tipo, String cpf,
                               String plano, String mensalidade) throws BikeshareException {
        return sistema.usuarios().criarPessoaFisica(nome, email, senha, tipo, cpf, plano, mensalidade);
    }

    /** Pessoa juridica, plano escolhido. */
    public String criarUsuario(String nome, String email, String senha, String tipo, String cnpj,
                               String razaoSocial, String plano, String mensalidade) throws BikeshareException {
        return sistema.usuarios().criarPessoaJuridica(nome, email, senha, tipo, cnpj, razaoSocial,
                plano, mensalidade);
    }

    // =====================================================================
    // US2 - Consulta de usuario
    // =====================================================================

    public String getAtributoUsuario(String usuario, String atributo) throws BikeshareException {
        return sistema.usuarios().getAtributo(usuario, atributo);
    }

    public String getUsuarioPorEmail(String email) throws BikeshareException {
        return sistema.usuarios().getIdPorEmail(email);
    }

    // =====================================================================
    // US3 - Alteracao de usuario (inclui metodo de pagamento)
    // =====================================================================

    /** Atributo simples, recarga de saldo ou metodo saldo pre-carregado. */
    public void alterarUsuario(String usuario, String atributo, String valor) throws BikeshareException {
        sistema.usuarios().alterar(usuario, atributo, valor);
    }

    /** Metodo de pagamento com chave (pix). */
    public void alterarUsuario(String usuario, String atributo, String valor1, String chave)
            throws BikeshareException {
        sistema.usuarios().alterarMetodoPagamento(usuario, atributo, valor1, chave);
    }

    /** Metodo de pagamento com numero e validade (cartao). */
    public void alterarUsuario(String usuario, String atributo, String valor1, String numero, String validade)
            throws BikeshareException {
        sistema.usuarios().alterarMetodoPagamento(usuario, atributo, valor1, numero, validade);
    }

    // =====================================================================
    // US4 - Remocao de usuario
    // =====================================================================

    public void removerUsuario(String usuario) throws BikeshareException {
        sistema.usuarios().remover(usuario);
    }

    // =====================================================================
    // US5 - Cadastro de estacao
    // =====================================================================

    public String criarEstacao(String nome, String endereco, String capacidade) throws BikeshareException {
        return sistema.estacoes().criar(nome, endereco, capacidade);
    }

    // =====================================================================
    // US6 - Gestao de bicicletas na estacao
    // =====================================================================

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void adicionarBicicleta(String estacao, String bicicleta, String tipo, String nivelBateria) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void removerBicicleta(String estacao, String bicicleta) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getAtributoBicicleta(String bicicleta, String atributo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US7 - Consulta de disponibilidade
    // =====================================================================

    public String getBicicletasDisponiveis(String estacao) throws BikeshareException {
        return sistema.estacoes().getBicicletasDisponiveis(estacao);
    }

    public String getVagasDisponiveis(String estacao) throws BikeshareException {
        return sistema.estacoes().getVagasDisponiveis(estacao);
    }

    public String getEstadoBicicleta(String bicicleta) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US8 - Retirada de bicicleta
    // =====================================================================

    public String retirarBicicleta(String usuario, String estacao, String bicicleta, String data) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US9 - Devolucao de bicicleta
    // =====================================================================

    public void devolverBicicleta(String aluguel, String estacao, String data) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US10 - Consulta de historico de alugueis
    // =====================================================================

    public String getHistoricoAlugueis(String usuario) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getHistoricoAlugueis(String usuario, String atributo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public String getAtributoAluguel(String aluguel, String atributo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US11/US12 - Planos e tarifacao / criacao de novos planos
    // =====================================================================

    public String criarPlano(String nome, String franquiaMinutos, String valorMinutoExcedente, String mensalidade) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US13 - Regras de bloqueio / lancamento manual de penalidade
    // =====================================================================

    public void lancaPenalidade(String usuario, String tipo, String valor, String motivo) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US14 - Lancamento e conclusao de manutencao
    // =====================================================================

    public void lancaDefeito(String bicicleta, String descricao) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void concluiManutencao(String bicicleta) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US15 - Relatorios
    // =====================================================================

    public String getTotalFaturado(String dataInicial, String dataFinal) {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // US16 - Undo/redo
    // =====================================================================

    public void undo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    public void redo() {
        throw new UnsupportedOperationException(AINDA_NAO_IMPLEMENTADO);
    }

    // =====================================================================
    // Ciclo de vida do sistema
    // =====================================================================

    public void zerarSistema() {
        sistema.zerar();
    }

    public void encerrarSistema() {
        sistema.encerrar();
    }
}