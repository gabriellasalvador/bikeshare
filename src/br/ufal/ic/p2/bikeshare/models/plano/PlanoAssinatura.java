package br.ufal.ic.p2.bikeshare.models.plano;

import br.ufal.ic.p2.bikeshare.exceptions.plano.MensalidadeNaoNumericaException;
import br.ufal.ic.p2.bikeshare.exceptions.plano.MensalidadeNegativaException;
import br.ufal.ic.p2.bikeshare.exceptions.plano.MensalidadeNulaException;
import br.ufal.ic.p2.bikeshare.exceptions.plano.PlanoException;
import br.ufal.ic.p2.bikeshare.utils.Conversor;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

/**
 * Plano com mensalidade: cada corrida tem uma franquia de minutos gratis e
 * o usuario paga apenas os minutos que passarem da franquia.
 *
 * O plano "assinante" e uma instancia desta classe. Os planos customizados
 * da US12 (com franquia e valor proprios) tambem serao, sem precisar de
 * nenhuma classe nova.
 */
public final class PlanoAssinatura extends Plano {

    private static final long serialVersionUID = 1L;

    private final Duration franquia;
    private final BigDecimal valorMinutoExcedente;
    private final BigDecimal mensalidade;

    /**
     * @param nome                 nome do plano (ex.: "assinante")
     * @param franquia             tempo gratis por corrida
     * @param valorMinutoExcedente valor de cada minuto alem da franquia
     * @param mensalidade          valor mensal contratado
     * @throws MensalidadeNegativaException se a mensalidade for negativa
     */
    public PlanoAssinatura(String nome, Duration franquia, BigDecimal valorMinutoExcedente,
                           BigDecimal mensalidade) throws MensalidadeNegativaException {
        super(nome);
        this.franquia = Objects.requireNonNull(franquia, "franquia");
        this.valorMinutoExcedente = Objects.requireNonNull(valorMinutoExcedente, "valor do minuto excedente");
        Objects.requireNonNull(mensalidade, "mensalidade");
        if (mensalidade.signum() < 0) {
            throw new MensalidadeNegativaException();
        }
        this.mensalidade = mensalidade;
    }

    /**
     * Valor = minutos que passaram da franquia x valor do minuto excedente.
     * Corridas dentro da franquia custam zero.
     */
    @Override
    public BigDecimal calcularTarifa(Duration duracao) {
        long excedente = Math.max(0, minutos(duracao) - franquia.toMinutes());
        return valorMinutoExcedente.multiply(BigDecimal.valueOf(excedente));
    }

    /**
     * Cria uma copia deste plano com a mensalidade combinada com o usuario.
     * A franquia e o valor excedente continuam os do modelo.
     *
     * @throws MensalidadeNulaException        se a mensalidade estiver vazia
     * @throws MensalidadeNaoNumericaException se a mensalidade nao for numero
     * @throws MensalidadeNegativaException    se a mensalidade for negativa
     */
    @Override
    public Plano contratar(String mensalidade) throws PlanoException {
        BigDecimal valor = Conversor.paraDecimal(mensalidade,
                MensalidadeNulaException::new, MensalidadeNaoNumericaException::new);
        return new PlanoAssinatura(getNome(), franquia, valorMinutoExcedente, valor);
    }

    @Override
    public BigDecimal getMensalidade() {
        return mensalidade;
    }
}