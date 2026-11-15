package br.ufal.ic.p2.bikeshare.config;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

/**
 * Parametros de negocio do sistema, lidos do arquivo bikeshare.properties.
 *
 * E imutavel: todos os valores sao lidos e validados uma unica vez, na criacao.
 * Se o arquivo tiver um valor invalido, o sistema falha logo ao iniciar
 * (fail fast), em vez de calcular uma tarifa errada no meio de uma devolucao.
 *
 * Uma instancia e criada pelo sistema e entregue a quem precisa (injecao de
 * dependencia), em vez de ser acessada como variavel global.
 */
public final class Configuracao {

    private static final String ARQUIVO = "/bikeshare.properties";

    private final Map<Parametro, String> valores;

    private Configuracao(Map<Parametro, String> valores) {
        this.valores = valores;
        validar();
    }

    /**
     * Le o bikeshare.properties do classpath. Chaves ausentes (ou o arquivo inteiro
     * ausente) assumem o valor padrao declarado em {@link Parametro}.
     */
    public static Configuracao carregar() {
        Properties arquivo = new Properties();
        try (InputStream entrada = Configuracao.class.getResourceAsStream(ARQUIVO)) {
            if (entrada != null) {
                arquivo.load(entrada);
            }
        } catch (IOException erro) {
            throw new IllegalStateException("Nao foi possivel ler " + ARQUIVO, erro);
        }

        Map<Parametro, String> valores = new EnumMap<>(Parametro.class);
        for (Parametro parametro : Parametro.values()) {
            valores.put(parametro, arquivo.getProperty(parametro.getChave(), parametro.getValorPadrao()).trim());
        }
        return new Configuracao(valores);
    }

    // ------------------------------------------------------------------ tarifacao

    public BigDecimal tarifaPorMinuto() {
        return decimal(Parametro.TARIFA_MINUTO);
    }

    public BigDecimal adicionalPorMinutoEletrica() {
        return decimal(Parametro.ADICIONAL_MINUTO_ELETRICA);
    }

    public int bateriaMinimaParaRetirada() {
        return inteiro(Parametro.BATERIA_MINIMA_RETIRADA);
    }

    // ------------------------------------------------------------------ assinante

    public Duration franquiaAssinante() {
        return Duration.ofMinutes(inteiro(Parametro.FRANQUIA_MINUTOS_ASSINANTE));
    }

    public BigDecimal minutoExcedenteAssinante() {
        return decimal(Parametro.MINUTO_EXCEDENTE_ASSINANTE);
    }

    public BigDecimal mensalidadePadraoAssinante() {
        return decimal(Parametro.MENSALIDADE_PADRAO_ASSINANTE);
    }

    // ------------------------------------------------------------------ aluguel

    public Duration carencia() {
        return Duration.ofMinutes(inteiro(Parametro.CARENCIA_MINUTOS));
    }

    public Duration tempoMaximoAluguel() {
        return Duration.ofMinutes(inteiro(Parametro.TEMPO_MAXIMO_MINUTOS));
    }

    public BigDecimal penalidadeExpiracao() {
        return decimal(Parametro.PENALIDADE_EXPIRACAO);
    }

    // ------------------------------------------------------------------ persistencia

    public String arquivoDados() {
        return valores.get(Parametro.ARQUIVO_DADOS);
    }

    // ------------------------------------------------------------------ internos

    private BigDecimal decimal(Parametro parametro) {
        return new BigDecimal(valores.get(parametro));
    }

    private int inteiro(Parametro parametro) {
        return Integer.parseInt(valores.get(parametro));
    }

    /**
     * Confere todos os valores na criacao: numeros validos e nunca negativos,
     * bateria minima entre 0 e 100 e nome de arquivo preenchido.
     */
    private void validar() {
        Parametro[] decimais = {
                Parametro.TARIFA_MINUTO, Parametro.ADICIONAL_MINUTO_ELETRICA,
                Parametro.MINUTO_EXCEDENTE_ASSINANTE, Parametro.MENSALIDADE_PADRAO_ASSINANTE,
                Parametro.PENALIDADE_EXPIRACAO
        };
        Parametro[] inteiros = {
                Parametro.BATERIA_MINIMA_RETIRADA, Parametro.FRANQUIA_MINUTOS_ASSINANTE,
                Parametro.CARENCIA_MINUTOS, Parametro.TEMPO_MAXIMO_MINUTOS
        };
        try {
            for (Parametro parametro : decimais) {
                exigirNaoNegativo(parametro, decimal(parametro).signum());
            }
            for (Parametro parametro : inteiros) {
                exigirNaoNegativo(parametro, Integer.signum(inteiro(parametro)));
            }
        } catch (NumberFormatException erro) {
            throw new IllegalStateException("Parametro com valor nao numerico em " + ARQUIVO, erro);
        }
        if (bateriaMinimaParaRetirada() > 100) {
            throw invalido(Parametro.BATERIA_MINIMA_RETIRADA);
        }
        if (arquivoDados().isEmpty()) {
            throw invalido(Parametro.ARQUIVO_DADOS);
        }
    }

    private void exigirNaoNegativo(Parametro parametro, int sinal) {
        if (sinal < 0) {
            throw invalido(parametro);
        }
    }

    private IllegalStateException invalido(Parametro parametro) {
        return new IllegalStateException("Valor invalido para " + parametro.getChave()
                + ": " + valores.get(parametro));
    }
}
