package br.ufal.ic.p2.bikeshare.controllers;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.aluguel.AluguelNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.aluguel.IdentificacaoAluguelNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaForaDaEstacaoException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.BicicletaNaoExisteException;
import br.ufal.ic.p2.bikeshare.exceptions.bicicleta.IdentificacaoBicicletaNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.DataInvalidaException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.DataNulaException;
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

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Coordena os casos de uso de aluguel: retirada de bicicleta (US8) e consulta.
 *
 * A retirada segue o fluxo da secao 7.1 do documento: primeiro confere todas
 * as pre-condicoes e so depois altera alguma coisa. Se qualquer conferencia
 * falhar, nenhum estado muda e nenhum id de aluguel e consumido.
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
    private final int bateriaMinima;

    /**
     * @param alugueis      onde os alugueis ficam guardados
     * @param usuarios      onde os usuarios ficam guardados
     * @param estacoes      onde as estacoes ficam guardadas
     * @param bicicletas    onde as bicicletas ficam guardadas
     * @param bateriaMinima carga minima para retirar uma bicicleta eletrica
     */
    public AluguelController(Repositorio<Aluguel> alugueis, Repositorio<Usuario> usuarios,
                             Repositorio<Estacao> estacoes, Registro<Bicicleta> bicicletas, int bateriaMinima) {
        this.alugueis = Objects.requireNonNull(alugueis, "alugueis");
        this.usuarios = Objects.requireNonNull(usuarios, "usuarios");
        this.estacoes = Objects.requireNonNull(estacoes, "estacoes");
        this.bicicletas = Objects.requireNonNull(bicicletas, "bicicletas");
        this.bateriaMinima = bateriaMinima;
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
        Estacao origem = estacoes.buscar(idEstacao,
                IdentificacaoEstacaoNulaException::new, EstacaoNaoExisteException::new);
        Bicicleta bicicleta = bicicletas.buscar(idBicicleta,
                IdentificacaoBicicletaNulaException::new, BicicletaNaoExisteException::new);
        LocalDateTime inicio = Conversor.paraDataHora(data, DataNulaException::new, DataInvalidaException::new);

        usuario.verificarPodeRetirar();
        if (!origem.contem(bicicleta)) {
            throw new BicicletaForaDaEstacaoException();
        }
        bicicleta.exigirCargaSuficiente(bateriaMinima);
        usuario.getMetodoPagamento().autorizarRetirada();

        Aluguel aluguel = alugueis.cadastrar(id -> new Aluguel(id, usuario, bicicleta, origem, inicio));
        origem.liberar(bicicleta);
        bicicleta.iniciarUso();
        usuario.iniciarAluguel(aluguel.getId());
        return aluguel.getId();
    }

    // ------------------------------------------------------------------ consulta

    /**
     * Le um atributo de um aluguel, ja no formato de saida.
     * O aluguel e conferido antes do atributo.
     */
    public String getAtributo(String idAluguel, String atributo) throws BikeshareException {
        return ATRIBUTOS.ler(buscar(idAluguel), atributo);
    }

    private Aluguel buscar(String idAluguel) throws BikeshareException {
        return alugueis.buscar(idAluguel, IdentificacaoAluguelNulaException::new, AluguelNaoExisteException::new);
    }
}