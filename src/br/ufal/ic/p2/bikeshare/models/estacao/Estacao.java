package br.ufal.ic.p2.bikeshare.models.estacao;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.EnderecoNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.comum.NomeNuloException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.CapacidadeNaoNumericaException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.CapacidadeNaoPositivaException;
import br.ufal.ic.p2.bikeshare.exceptions.estacao.CapacidadeNulaException;
import br.ufal.ic.p2.bikeshare.models.Identificavel;
import br.ufal.ic.p2.bikeshare.utils.Conversor;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.io.Serializable;
import java.util.Objects;

/**
 * Ponto fixo onde as bicicletas ficam estacionadas, com um numero limitado de vagas.
 *
 * Invariantes: nome e endereco nunca ficam vazios e a capacidade e sempre
 * um numero inteiro positivo. Nada disso muda depois do cadastro.
 *
 * Nomes repetidos sao permitidos: quem distingue uma estacao da outra e o id.
 */
public class Estacao implements Identificavel, Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String nome;
    private final String endereco;
    private final int capacidade;

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
     * Quantas vagas estao livres. Enquanto a estacao nao recebe bicicletas
     * (o que chega na US6), todas as vagas estao livres.
     */
    public int getVagasLivres() {
        return capacidade;
    }

    /**
     * Quantas bicicletas podem ser retiradas agora. Enquanto a estacao nao
     * recebe bicicletas (o que chega na US6), nao ha nenhuma.
     */
    public int getBicicletasDisponiveis() {
        return 0;
    }

    private static int validarCapacidade(String texto) throws BikeshareException {
        int valor = Conversor.paraInteiro(texto, CapacidadeNulaException::new, CapacidadeNaoNumericaException::new);
        if (valor <= 0) {
            throw new CapacidadeNaoPositivaException();
        }
        return valor;
    }
}