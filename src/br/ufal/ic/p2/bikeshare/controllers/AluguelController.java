package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.config.Configuracao;
import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.aluguel.AluguelNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.aluguel.IdentificacaoAluguelNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaForaDaEstacaoException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.IdentificacaoBicicletaNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.DataInvalidaException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.DataNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.EstacaoCheiaException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.EstacaoNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.IdentificacaoEstacaoNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.IdentificacaoUsuarioNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.usuario.UsuarioNaoExisteException;
import br.ufal.ic.p2.bikeshare.models.aluguel.Aluguel;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;
import br.ufal.ic.p2.bikeshare.repositories.Registro;
import br.ufal.ic.p2.bikeshare.repositories.Repositorio;
import br.ufal.ic.p2.bikeshare.utils.Conversor;
import br.ufal.ic.p2.bikeshare.utils.Formatador;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Coordena os casos de uso de aluguel: retirada (US8), devolucao (US9) e consulta.
 *
 * Retirada e devolucao seguem os fluxos das secoes 7.1 e 7.2 do documento:
 * primeiro conferem todas as pre-condicoes e so depois alteram alguma coisa.
 * Se qualquer conferencia falhar, nenhum estado muda.
 */
public class AluguelController {

    /** Atributos que podem ser consultados e como cada um e lido. */
    private static final TabelaDeAtributos<Aluguel> ATRIBUTOS = new TabelaDeAtributos<Aluguel>()
            .com("estado", a -> a.getEstado().getNome())
            .com("usuario", a -> a.getUsuario().getId())
            .com("bicicleta", a -> a.getBicicleta().getId())
            .com("estacaoOrigem", a -> a.getEstacaoOrigem().getId())
            .com("inicio", a -> Formatador.dataHora(a.getInicio()))
            .com("fim", a -> Formatador.dataHora(a.getFim()))
            .com("estacaoDestino", a -> a.getEstacaoDestino().getId())
            .com("valorCobrado", a -> Formatador.moeda(a.getValorCobrado()));

    private final Repositorio<Aluguel> alugueis;
    private final Repositorio<Usuario> usuarios;
    private final Repositorio<Estacao> estacoes;
    private final Registro<Bicicleta> bicicletas;
    private final Configuracao configuracao;

    /**
     * @param alugueis     onde os alugueis ficam guardados
     * @param usuarios     onde os usuarios ficam guardados
     * @param estacoes     onde as estacoes ficam guardadas
     * @param bicicletas   onde as bicicletas ficam guardadas
     * @param configuracao bateria minima, carencia, tempo maximo, penalidade e adicional
     */
    public AluguelController(Repositorio<Aluguel> alugueis, Repositorio<Usuario> usuarios,
                             Repositorio<Estacao> estacoes, Registro<Bicicleta> bicicletas,
                             Configuracao configuracao) {
        this.alugueis = Objects.requireNonNull(alugueis, "alugueis");
        this.usuarios = Objects.requireNonNull(usuarios, "usuarios");
        this.estacoes = Objects.requireNonNull(estacoes, "estacoes");
        this.bicicletas = Objects.requireNonNull(bicicletas, "bicicletas");
        this.configuracao = Objects.requireNonNull(configuracao, "configuracao");
    }

    // ------------------------------------------------------------------ retirada (US8)

    /**
     * Retira uma bicicleta de uma estacao, iniciando um aluguel.
     *
     * Ordem das conferencias: usuario, estacao, bicicleta e data existem;
     * o usuario pode alugar; a bicicleta esta naquela estacao e tem carga;
     * o metodo de pagamento autoriza.
     *
     * @return id do aluguel criado (ex.: "A1")
     */
    public String retirar(String idUsuario, String idEstacao, String idBicicleta, String data)
            throws BikeshareException {
        Usuario usuario = usuarios.buscar(idUsuario,
                IdentificacaoUsuarioNulaException::new, UsuarioNaoExisteException::new);
        Estacao origem = buscarEstacao(idEstacao);
        Bicicleta bicicleta = bicicletas.buscar(idBicicleta,
                IdentificacaoBicicletaNulaException::new, BicicletaNaoExisteException::new);
        LocalDateTime inicio = converterData(data);

        usuario.verificarPodeRetirar();
        if (!origem.contem(bicicleta)) {
            throw new BicicletaForaDaEstacaoException();
        }
        bicicleta.exigirCargaSuficiente(configuracao.bateriaMinimaParaRetirada());
        usuario.getMetodoPagamento().autorizarRetirada();

        Aluguel aluguel = alugueis.cadastrar(id -> new Aluguel(id, usuario, bicicleta, origem, inicio));
        origem.liberar(bicicleta);
        bicicleta.iniciarUso();
        usuario.iniciarAluguel(aluguel.getId());
        return aluguel.getId();
    }

    // ------------------------------------------------------------------ devolucao (US9)

    /**
     * Devolve a bicicleta de um aluguel em uma estacao, encerrando o aluguel.
     *
     * Ordem das conferencias: aluguel, estacao e data existem; o aluguel esta
     * em andamento; a data nao e anterior a retirada; a estacao tem vaga.
     * Depois, um de tres desfechos:
     * - mesma estacao dentro da carencia: cancelado, sem cobranca;
     * - acima do tempo maximo: expirado, sem tarifa, com penalidade e a
     *   bicicleta vai para manutencao;
     * - nos demais casos: finalizado, com a tarifa do plano mais o adicional
     *   da bicicleta. Se o pagamento for recusado, nada muda.
     */
    public void devolver(String idAluguel, String idEstacao, String data) throws BikeshareException {
        Aluguel aluguel = buscar(idAluguel);
        Estacao destino = buscarEstacao(idEstacao);
        LocalDateTime fim = converterData(data);

        aluguel.exigirEmAndamento();
        Duration duracao = aluguel.duracaoAte(fim);
        if (!destino.temVagaLivre()) {
            throw new EstacaoCheiaException();
        }

        if (dentroDaCarencia(aluguel, destino, duracao)) {
            cancelar(aluguel, destino, fim);
        } else if (passouDoTempoMaximo(duracao)) {
            expirar(aluguel, destino, fim);
        } else {
            finalizar(aluguel, destino, fim, duracao);
        }
        aluguel.getUsuario().encerrarAluguel();
    }

    // ------------------------------------------------------------------ consulta

    /**
     * Le um atributo de um aluguel, ja no formato de saida.
     * O aluguel e conferido antes do atributo.
     */
    public String getAtributo(String idAluguel, String atributo) throws BikeshareException {
        return ATRIBUTOS.ler(buscar(idAluguel), atributo);
    }

    // ------------------------------------------------------------------ desfechos da devolucao

    private boolean dentroDaCarencia(Aluguel aluguel, Estacao destino, Duration duracao) {
        return aluguel.voltouParaOrigem(destino) && duracao.compareTo(configuracao.carencia()) <= 0;
    }

    private boolean passouDoTempoMaximo(Duration duracao) {
        return duracao.compareTo(configuracao.tempoMaximoAluguel()) > 0;
    }

    /**
     * Desistencia: a bicicleta volta disponivel e nada e cobrado.
     */
    private void cancelar(Aluguel aluguel, Estacao destino, LocalDateTime fim) throws BikeshareException {
        destino.estacionar(aluguel.getBicicleta(), EstacaoCheiaException::new);
        aluguel.getBicicleta().encerrarUso();
        aluguel.cancelar(fim, destino);
    }

    /**
     * Atraso: sem tarifa, com penalidade, e a bicicleta vai para manutencao.
     */
    private void expirar(Aluguel aluguel, Estacao destino, LocalDateTime fim) throws BikeshareException {
        destino.estacionar(aluguel.getBicicleta(), EstacaoCheiaException::new);
        aluguel.getBicicleta().enviarParaManutencao();
        aluguel.expirar(fim, destino);
        aluguel.getUsuario().registrarPenalidade(configuracao.penalidadeExpiracao());
    }

    /**
     * Corrida normal: cobra primeiro (se o pagamento for recusado, nada muda)
     * e so depois devolve a bicicleta e encerra o aluguel.
     */
    private void finalizar(Aluguel aluguel, Estacao destino, LocalDateTime fim, Duration duracao)
            throws BikeshareException {
        Usuario usuario = aluguel.getUsuario();
        Bicicleta bicicleta = aluguel.getBicicleta();
        BigDecimal valor = usuario.getPlano().calcularTarifa(duracao)
                .add(bicicleta.calcularAdicional(duracao, configuracao.adicionalPorMinutoEletrica()));

        usuario.getMetodoPagamento().cobrar(valor);
        destino.estacionar(bicicleta, EstacaoCheiaException::new);
        bicicleta.encerrarUso();
        aluguel.finalizar(fim, destino, valor);
    }

    // ------------------------------------------------------------------ internos

    private Aluguel buscar(String idAluguel) throws BikeshareException {
        return alugueis.buscar(idAluguel, IdentificacaoAluguelNulaException::new, AluguelNaoExisteException::new);
    }

    private Estacao buscarEstacao(String idEstacao) throws BikeshareException {
        return estacoes.buscar(idEstacao, IdentificacaoEstacaoNulaException::new, EstacaoNaoExisteException::new);
    }

    private static LocalDateTime converterData(String data) throws BikeshareException {
        return Conversor.paraDataHora(data, DataNulaException::new, DataInvalidaException::new);
    }
}