package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.config.Configuracao;
import br.ufal.ic.p2.bikeshare.persistence.ArquivoDeDados;
import br.ufal.ic.p2.bikeshare.persistence.DadosSistema;

/**
 * Raiz do sistema: monta os controllers e os liga aos dados.
 *
 * A Facade conversa so com esta classe. Aqui fica o ciclo de vida:
 * carregar o estado salvo ao iniciar, zerar e salvar ao encerrar.
 */
public final class Sistema {

    private final Configuracao configuracao;
    private final ArquivoDeDados arquivo;
    private DadosSistema dados;
    private UsuarioController usuarios;
    private EstacaoController estacoes;

    /**
     * Inicia o sistema com os parametros do bikeshare.properties e
     * recupera o estado salvo pelo ultimo encerramento, se houver.
     */
    public Sistema() {
        this.configuracao = Configuracao.carregar();
        this.arquivo = new ArquivoDeDados(configuracao.arquivoDados());
        usar(arquivo.carregar().orElseGet(() -> new DadosSistema(configuracao)));
    }

    /**
     * Apaga todos os dados e reinicia os contadores de id.
     */
    public void zerar() {
        usar(new DadosSistema(configuracao));
    }

    /**
     * Salva o estado atual, para ser recuperado na proxima execucao.
     */
    public void encerrar() {
        arquivo.salvar(dados);
    }

    public UsuarioController usuarios() {
        return usuarios;
    }

    public EstacaoController estacoes() {
        return estacoes;
    }

    /**
     * Passa a trabalhar com o estado informado, religando os controllers a ele.
     */
    private void usar(DadosSistema novosDados) {
        this.dados = novosDados;
        this.usuarios = new UsuarioController(dados.getUsuarios(), dados.getPlanos());
        this.estacoes = new EstacaoController(dados.getEstacoes());
    }
}