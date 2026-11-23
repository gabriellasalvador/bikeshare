package br.ufal.ic.p2.bikeshare.models.aluguel;

import br.ufal.ic.p2.bikeshare.exceptions.aluguel.AluguelException;
import br.ufal.ic.p2.bikeshare.exceptions.aluguel.AluguelNaoEstaEmAndamentoException;
import br.ufal.ic.p2.bikeshare.exceptions.aluguel.DevolucaoAnteriorARetiradaException;
import br.ufal.ic.p2.bikeshare.models.Identificavel;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.models.estacao.Estacao;
import br.ufal.ic.p2.bikeshare.models.usuario.Usuario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Uma corrida: quem retirou, qual bicicleta, de onde e quando.
 *
 * O aluguel continua existindo depois de terminado (e mesmo depois que o
 * usuario e removido), para manter o historico.
 *
 * Tudo que depende da fase da corrida (fim, destino, valor) e respondido
 * pelo estado atual, no padrao State (veja {@link EstadoAluguel}).
 */
public class Aluguel implements Identificavel, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final Usuario usuario;
    private final Bicicleta bicicleta;
    private final Estacao estacaoOrigem;
    private final LocalDateTime inicio;
    private EstadoAluguel estado = new EmAndamento();

    /**
     * Cria um aluguel em andamento. Quem cria ja conferiu todas as regras
     * da retirada.
     *
     * @param id            identificador gerado pelo sistema (ex.: "A1")
     * @param usuario       quem retirou
     * @param bicicleta     bicicleta retirada
     * @param estacaoOrigem estacao de onde saiu
     * @param inicio        momento da retirada
     */
    public Aluguel(String id, Usuario usuario, Bicicleta bicicleta, Estacao estacaoOrigem, LocalDateTime inicio) {
        this.id = Objects.requireNonNull(id, "id");
        this.usuario = Objects.requireNonNull(usuario, "usuario");
        this.bicicleta = Objects.requireNonNull(bicicleta, "bicicleta");
        this.estacaoOrigem = Objects.requireNonNull(estacaoOrigem, "estacao de origem");
        this.inicio = Objects.requireNonNull(inicio, "inicio");
    }

    @Override
    public String getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Bicicleta getBicicleta() {
        return bicicleta;
    }

    public Estacao getEstacaoOrigem() {
        return estacaoOrigem;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public EstadoAluguel getEstado() {
        return estado;
    }

    /**
     * @throws AluguelException se o aluguel ainda nao terminou
     */
    public LocalDateTime getFim() throws AluguelException {
        return estado.getFim();
    }

    /**
     * @throws AluguelException se o aluguel ainda nao terminou
     */
    public Estacao getEstacaoDestino() throws AluguelException {
        return estado.getEstacaoDestino();
    }

    /**
     * @throws AluguelException se o aluguel ainda nao terminou
     */
    public BigDecimal getValorCobrado() throws AluguelException {
        return estado.getValorCobrado();
    }

    // ------------------------------------------------------------------ devolucao (US9)

    /**
     * @throws AluguelNaoEstaEmAndamentoException se o aluguel ja terminou
     */
    public void exigirEmAndamento() throws AluguelNaoEstaEmAndamentoException {
        if (!estado.emAndamento()) {
            throw new AluguelNaoEstaEmAndamentoException();
        }
    }

    /**
     * Tempo de corrida ate o momento informado.
     *
     * @throws DevolucaoAnteriorARetiradaException se o momento for anterior a retirada
     */
    public Duration duracaoAte(LocalDateTime fim) throws DevolucaoAnteriorARetiradaException {
        if (fim.isBefore(inicio)) {
            throw new DevolucaoAnteriorARetiradaException();
        }
        return Duration.between(inicio, fim);
    }

    /**
     * Indica se a bicicleta esta sendo devolvida na mesma estacao de onde saiu.
     */
    public boolean voltouParaOrigem(Estacao destino) {
        return estacaoOrigem == destino;
    }

    /**
     * Encerra como corrida normal, registrando o valor cobrado.
     */
    public void finalizar(LocalDateTime fim, Estacao destino, BigDecimal valor)
            throws AluguelNaoEstaEmAndamentoException {
        encerrar(new Finalizado(fim, destino, valor));
    }

    /**
     * Encerra como desistencia (dentro da carencia), sem cobranca.
     */
    public void cancelar(LocalDateTime fim, Estacao destino) throws AluguelNaoEstaEmAndamentoException {
        encerrar(new Cancelado(fim, destino));
    }

    /**
     * Encerra por passar do tempo maximo, sem cobranca de tarifa.
     */
    public void expirar(LocalDateTime fim, Estacao destino) throws AluguelNaoEstaEmAndamentoException {
        encerrar(new Expirado(fim, destino));
    }

    /**
     * Troca o estado. So um aluguel em andamento pode ser encerrado, e uma vez so.
     */
    private void encerrar(AluguelEncerrado novoEstado) throws AluguelNaoEstaEmAndamentoException {
        exigirEmAndamento();
        this.estado = novoEstado;
    }
}