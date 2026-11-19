package br.ufal.ic.p2.bikeshare.models.bicicleta;

/**
 * Situacao de uma bicicleta, seguindo o diagrama de estados do documento
 * (secao 7): Disponivel, Em uso e Em manutencao.
 *
 * Uma bicicleta removida deixa de existir no sistema, por isso nao ha
 * um estado "removida" aqui.
 */
public enum EstadoBicicleta {

    /** Estacionada e pronta para ser retirada. */
    DISPONIVEL("disponivel"),

    /** Retirada por um usuario, em uma corrida. */
    EM_USO("emUso"),

    /** Com defeito ou recolhida, aguardando manutencao. */
    EM_MANUTENCAO("emManutencao");

    private final String nome;

    EstadoBicicleta(String nome) {
        this.nome = nome;
    }

    /**
     * Nome do estado, como aparece para o usuario (ex.: "emUso").
     */
    public String getNome() {
        return nome;
    }
}