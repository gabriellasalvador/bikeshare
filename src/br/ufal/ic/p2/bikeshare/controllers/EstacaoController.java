package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.EstacaoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.IdentificacaoEstacaoNulaException;
import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.repositories.Repositorio;

import java.util.Objects;

/**
 * Coordena os casos de uso de estacao: cadastro (US5) e consulta de
 * disponibilidade de vagas e bicicletas (US7).
 */
public class EstacaoController {

    private final Repositorio<Estacao> estacoes;
    private final int bateriaMinima;

    /**
     * @param estacoes      onde as estacoes ficam guardadas
     * @param bateriaMinima carga minima para uma bicicleta eletrica contar como disponivel
     */
    public EstacaoController(Repositorio<Estacao> estacoes, int bateriaMinima) {
        this.estacoes = Objects.requireNonNull(estacoes, "estacoes");
        this.bateriaMinima = bateriaMinima;
    }

    /**
     * Cadastra uma estacao. Um cadastro recusado nao consome id.
     *
     * @return id gerado (ex.: "E1")
     */
    public String criar(String nome, String endereco, String capacidade) throws BikeshareException {
        return estacoes.cadastrar(id -> new Estacao(id, nome, endereco, capacidade)).getId();
    }

    /**
     * Quantidade de vagas livres na estacao, como texto.
     */
    public String getVagasDisponiveis(String idEstacao) throws BikeshareException {
        return String.valueOf(buscar(idEstacao).getVagasLivres());
    }

    /**
     * Quantidade de bicicletas que podem ser retiradas da estacao, como texto.
     * Eletricas abaixo da carga minima ocupam vaga, mas nao contam aqui.
     */
    public String getBicicletasDisponiveis(String idEstacao) throws BikeshareException {
        return String.valueOf(buscar(idEstacao).getBicicletasDisponiveis(bateriaMinima));
    }

    private Estacao buscar(String idEstacao) throws BikeshareException {
        return estacoes.buscar(idEstacao, IdentificacaoEstacaoNulaException::new, EstacaoNaoExisteException::new);
    }
}