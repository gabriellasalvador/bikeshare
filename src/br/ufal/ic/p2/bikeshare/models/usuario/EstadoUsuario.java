package br.ufal.ic.p2.bikeshare.models.usuario;

/**
 * Situacao de um usuario no sistema.
 *
 * Um enum (e nao um texto solto) garante que o usuario so pode estar
 * em um dos estados previstos, e guarda o nome exibido de cada um.
 */
public enum EstadoUsuario {

    /** Pode retirar bicicletas normalmente. */
    ATIVO("ativo"),

    /** Tem penalidades nao pagas e esta impedido de retirar bicicletas. */
    INADIMPLENTE("inadimplente");

    private final String nome;

    EstadoUsuario(String nome) {
        this.nome = nome;
    }

    /**
     * Nome do estado, como aparece para o usuario (ex.: "ativo").
     */
    public String getNome() {
        return nome;
    }
}