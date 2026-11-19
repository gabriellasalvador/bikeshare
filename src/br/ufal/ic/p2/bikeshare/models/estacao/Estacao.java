package br.ufal.ic.p2.bikeshare.models.estacao;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.EnderecoNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.NomeNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.CapacidadeNaoNumericaException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.CapacidadeNaoPositivaException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.CapacidadeNulaException;
import br.ufal.ic.p2.bikeshare.models.Identificavel;
import br.ufal.ic.p2.bikeshare.models.bicicleta.Bicicleta;
import br.ufal.ic.p2.bikeshare.utils.Conversor;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Ponto fixo onde as bicicletas ficam estacionadas, com um numero limitado de vagas.
 *
 * Invariantes: nome e endereco nunca ficam vazios, a capacidade e sempre
 * um numero inteiro positivo, e nunca ha mais bicicletas do que vagas.
 *
 * Nomes repetidos sao permitidos: quem distingue uma estacao da outra e o id.
 *
 * E a estacao (e nao a bicicleta) que sabe quais bicicletas estao nela.
 * Toda bicicleta estacionada ocupa uma vaga, mesmo que nao possa ser retirada
 * (sem carga ou em manutencao).
 */
public class Estacao implements Identificavel, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;
    // tipo concreto (e nao List) porque ArrayList garante ser serializavel
    private final ArrayList<Bicicleta> bicicletas = new ArrayList<>();

    /**
     * Valida os dados na ordem: nome, endereco, capacidade.
     *
     * @param id         identificador gerado pelo sistema (ex.: "E1")
     * @param nome       nome da estacao
     * @param endereco   endereco da estacao
     * @param capacidade texto da quantidade de vagas (so inteiros)
     * @throws NomeNuloException               se o nome estiver vazio
     * @throws EnderecoNuloException           se o endereco estiver vazio
     * @throws CapacidadeNulaException         se a capacidade estiver vazia
     * @throws CapacidadeNaoNumericaException  se a capacidade nao for um inteiro
     * @throws CapacidadeNaoPositivaException  se a capacidade for zero ou negativa
     */
    public Estacao(String id, String nome, String endereco, String capacidade) throws BikeshareException {
        this.id = Objects.requireNonNull(id, "id");
        this.nome = Validacao.exigirPreenchido(nome, NomeNuloException::new);
        this.endereco = Validacao.exigirPreenchido(endereco, EnderecoNuloException::new);
        this.capacidade = validarCapacidade(capacidade);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getCapacidade() {
        return capacidade;
    }

    /**
     * Quantas vagas estao livres (capacidade menos bicicletas estacionadas).
     */
    public int getVagasLivres() {
        return capacidade - bicicletas.size();
    }

    /**
     * Indica se ainda cabe mais uma bicicleta.
     */
    public boolean temVagaLivre() {
        return getVagasLivres() > 0;
    }

    /**
     * Quantas bicicletas estacionadas aqui podem ser retiradas agora.
     *
     * @param bateriaMinima carga minima exigida das bicicletas com bateria
     */
    public int getBicicletasDisponiveis(int bateriaMinima) {
        return (int) bicicletas.stream().filter(b -> b.podeSerRetirada(bateriaMinima)).count();
    }

    /**
     * Indica se a bicicleta esta estacionada nesta estacao.
     */
    public boolean contem(Bicicleta bicicleta) {
        return bicicletas.contains(bicicleta);
    }

    /**
     * Estaciona uma bicicleta, ocupando uma vaga.
     *
     * @param bicicleta bicicleta a estacionar
     * @param seCheia   fabrica do erro para estacao sem vaga (cada comando tem sua mensagem)
     * @throws E se nao houver vaga livre (nada e alterado)
     */
    public <E extends BikeshareException> void estacionar(Bicicleta bicicleta, Supplier<E> seCheia) throws E {
        if (!temVagaLivre()) {
            throw seCheia.get();
        }
        bicicletas.add(Objects.requireNonNull(bicicleta, "bicicleta"));
    }

    /**
     * Tira uma bicicleta da estacao, liberando a vaga.
     */
    public void liberar(Bicicleta bicicleta) {
        bicicletas.remove(bicicleta);
    }

    private static int validarCapacidade(String texto) throws BikeshareException {
        int valor = Conversor.paraInteiro(texto, CapacidadeNulaException::new, CapacidadeNaoNumericaException::new);
        if (valor <= 0) {
            throw new CapacidadeNaoPositivaException();
        }
        return valor;
    }
}