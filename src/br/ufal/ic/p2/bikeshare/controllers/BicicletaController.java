package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaForaDaEstacaoException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaJaExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.IdentificacaoBicicletaNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.EstacaoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.EstacaoSemVagasException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.IdentificacaoEstacaoNulaException;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.models.bicicleta.FabricaDeBicicletas;
import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.repositories.Registro;
import br.ufal.ic.p2.bikeshare.repositories.Repositorio;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.util.Objects;

/**
 * Coordena os casos de uso de bicicleta (US6): adicionar a uma estacao,
 * remover e consultar.
 *
 * Validacoes na ordem cobrada pelos testes: primeiro a estacao, depois a
 * bicicleta (id, repeticao, tipo e bateria) e, por ultimo, a vaga.
 */
public class BicicletaController {

    /** Texto exibido quando a bicicleta nao esta estacionada em nenhuma estacao. */
    private static final String SEM_ESTACAO = "nenhuma";

    /** Atributos que podem ser consultados e como cada um e lido. */
    private final TabelaDeAtributos<Bicicleta> atributos = new TabelaDeAtributos<Bicicleta>()
            .com("tipo", Bicicleta::getTipo)
            .com("estado", b -> b.getEstado().getNome())
            .com("estacao", this::idDaEstacaoOnde)
            .com("nivelBateria", b -> String.valueOf(b.getNivelBateria()));

    private final Registro<Bicicleta> bicicletas;
    private final Repositorio<Estacao> estacoes;

    /**
     * @param bicicletas onde as bicicletas ficam guardadas
     * @param estacoes   onde as estacoes ficam guardadas
     */
    public BicicletaController(Registro<Bicicleta> bicicletas, Repositorio<Estacao> estacoes) {
        this.bicicletas = Objects.requireNonNull(bicicletas, "bicicletas");
        this.estacoes = Objects.requireNonNull(estacoes, "estacoes");
    }

    /**
     * Cadastra uma bicicleta sem bateria (convencional) em uma estacao.
     */
    public void adicionar(String idEstacao, String idBicicleta, String tipo) throws BikeshareException {
        Estacao estacao = buscarEstacao(idEstacao);
        String id = exigirIdNovo(idBicicleta);
        estacionarNova(estacao, FabricaDeBicicletas.criar(id, tipo));
    }

    /**
     * Cadastra uma bicicleta com bateria (eletrica) em uma estacao.
     */
    public void adicionar(String idEstacao, String idBicicleta, String tipo, String nivelBateria)
            throws BikeshareException {
        Estacao estacao = buscarEstacao(idEstacao);
        String id = exigirIdNovo(idBicicleta);
        estacionarNova(estacao, FabricaDeBicicletas.criar(id, tipo, nivelBateria));
    }

    /**
     * Retira a bicicleta do sistema. A vaga e liberada e o mesmo codigo
     * pode ser cadastrado de novo depois.
     *
     * @throws BicicletaForaDaEstacaoException se a bicicleta nao estiver estacionada ali
     */
    public void remover(String idEstacao, String idBicicleta) throws BikeshareException {
        Estacao estacao = buscarEstacao(idEstacao);
        Bicicleta bicicleta = buscar(idBicicleta);
        if (!estacao.contem(bicicleta)) {
            throw new BicicletaForaDaEstacaoException();
        }
        estacao.liberar(bicicleta);
        bicicletas.remover(bicicleta.getId());
    }

    /**
     * Le um atributo de uma bicicleta, ja no formato de saida.
     * A bicicleta e conferida antes do atributo.
     */
    public String getAtributo(String idBicicleta, String atributo) throws BikeshareException {
        return atributos.ler(buscar(idBicicleta), atributo);
    }

    /**
     * Estado da bicicleta (ex.: "disponivel").
     */
    public String getEstado(String idBicicleta) throws BikeshareException {
        return buscar(idBicicleta).getEstado().getNome();
    }

    // ------------------------------------------------------------------ internos

    /**
     * Confere que o codigo foi informado e ainda nao esta em uso.
     *
     * @return o codigo, sem espacos nas pontas
     */
    private String exigirIdNovo(String idBicicleta) throws BikeshareException {
        String id = Validacao.exigirPreenchido(idBicicleta, IdentificacaoBicicletaNulaException::new).trim();
        if (bicicletas.contem(id)) {
            throw new BicicletaJaExisteException();
        }
        return id;
    }

    /**
     * Ocupa a vaga e so entao registra a bicicleta. Se nao houver vaga,
     * nada e alterado.
     */
    private void estacionarNova(Estacao estacao, Bicicleta bicicleta) throws BikeshareException {
        estacao.estacionar(bicicleta, EstacaoSemVagasException::new);
        bicicletas.adicionar(bicicleta, BicicletaJaExisteException::new);
    }

    /**
     * Id da estacao onde a bicicleta esta estacionada, ou "nenhuma".
     */
    private String idDaEstacaoOnde(Bicicleta bicicleta) {
        return estacoes.buscarPrimeiro(estacao -> estacao.contem(bicicleta))
                .map(Estacao::getId)
                .orElse(SEM_ESTACAO);
    }

    private Bicicleta buscar(String idBicicleta) throws BikeshareException {
        return bicicletas.buscar(idBicicleta, IdentificacaoBicicletaNulaException::new,
                BicicletaNaoExisteException::new);
    }

    private Estacao buscarEstacao(String idEstacao) throws BikeshareException {
        return estacoes.buscar(idEstacao, IdentificacaoEstacaoNulaException::new, EstacaoNaoExisteException::new);
    }
}