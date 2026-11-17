package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.config.Configuracao;
import br.ufal.ic.p2.bikeshare.models.plano.CatalogoDePlanos;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositories.Repositorio;

/**
 * Raiz do sistema: monta os controllers e os liga aos dados.
 *
 * A Facade conversa so com esta classe. Aqui fica o ciclo de vida
 * (zerar e, nas proximas etapas, salvar e carregar o estado).
 */
public final class Sistema {

    private final Configuracao configuracao;
    private UsuarioController usuarios;

    /**
     * Cria o sistema vazio, com os parametros do bikeshare.properties.
     */
    public Sistema() {
        this.configuracao = Configuracao.carregar();
        zerar();
    }

    /**
     * Apaga todos os dados e reinicia os contadores de id.
     */
    public void zerar() {
        CatalogoDePlanos planos = new CatalogoDePlanos(configuracao);
        Repositorio<Usuario> repositorioUsuarios = new Repositorio<>("U");
        this.usuarios = new UsuarioController(repositorioUsuarios, planos);
    }

    public UsuarioController usuarios() {
        return usuarios;
    }
}