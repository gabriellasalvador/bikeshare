package br.ufal.ic.p2.bikeshare.persistence;

import br.ufal.ic.p2.bikeshare.config.Configuracao;
import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.models.plano.CatalogoDePlanos;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositories.Repositorio;

import java.io.Serializable;

/**
 * Todo o estado do sistema, reunido em um unico objeto.
 *
 * E este objeto que e salvo no disco ao encerrar e lido de volta ao iniciar.
 * Como os repositorios guardam os itens E os contadores de id, salvar este
 * objeto preserva tudo, inclusive a sequencia dos proximos ids.
 *
 * A cada nova User Story com dados novos (estacoes, alugueis...), basta
 * acrescentar o repositorio correspondente aqui.
 */
public final class DadosSistema implements Serializable {

    private static final long serialVersionUID = 1L;

    private final CatalogoDePlanos planos;
    private final Repositorio<Usuario> usuarios = new Repositorio<>("U");
    private final Repositorio<Estacao> estacoes = new Repositorio<>("E");

    /**
     * Cria um estado vazio, com os planos iniciais da configuracao.
     */
    public DadosSistema(Configuracao configuracao) {
        this.planos = new CatalogoDePlanos(configuracao);
    }

    public CatalogoDePlanos getPlanos() {
        return planos;
    }

    public Repositorio<Usuario> getUsuarios() {
        return usuarios;
    }

    public Repositorio<Estacao> getEstacoes() {
        return estacoes;
    }
}