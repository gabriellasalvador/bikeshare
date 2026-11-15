package br.ufal.ic.p2.bikeshare.config;

/**
 * Catalogo de todos os parametros de negocio configuraveis (requisito nao funcional 4).
 *
 * Cada parametro tem a chave usada no arquivo bikeshare.properties e um valor padrao,
 * usado apenas se o arquivo nao trouxer aquela chave. Assim o sistema nunca fica
 * sem configuracao, e nenhum numero de regra de negocio aparece solto no codigo.
 */
public enum Parametro {

    TARIFA_MINUTO("tarifa.minuto", "0.50"),
    ADICIONAL_MINUTO_ELETRICA("eletrica.adicional.minuto", "0.20"),
    BATERIA_MINIMA_RETIRADA("eletrica.bateria.minima", "15"),

    FRANQUIA_MINUTOS_ASSINANTE("assinante.franquia.minutos", "60"),
    MINUTO_EXCEDENTE_ASSINANTE("assinante.minuto.excedente", "0.30"),
    MENSALIDADE_PADRAO_ASSINANTE("assinante.mensalidade.padrao", "30.00"),

    CARENCIA_MINUTOS("aluguel.carencia.minutos", "2"),
    TEMPO_MAXIMO_MINUTOS("aluguel.tempo.maximo.minutos", "1440"),
    PENALIDADE_EXPIRACAO("aluguel.penalidade.expiracao", "10.00"),

    ARQUIVO_DADOS("persistencia.arquivo", "bikeshare.dat");

    private final String chave;
    private final String valorPadrao;

    Parametro(String chave, String valorPadrao) {
        this.chave = chave;
        this.valorPadrao = valorPadrao;
    }

    public String getChave() {
        return chave;
    }

    public String getValorPadrao() {
        return valorPadrao;
    }
}
