package br.ufal.ic.p2.bikeshare.models.plano;

import br.ufal.ic.p2.bikeshare.exceptions.plano.PlanoSemMensalidadeException;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

/**
 * Plano sem mensalidade: o usuario paga uma tarifa por cada minuto de corrida.
 * E o plano padrao de quem se cadastra sem escolher plano.
 */
public final class PlanoAvulso extends Plano {

    private static final long serialVersionUID = 1L;

    private final BigDecimal tarifaPorMinuto;

    /**
     * @param nome            nome do plano (ex.: "avulso")
     * @param tarifaPorMinuto valor cobrado por minuto, vindo da configuracao
     */
    public PlanoAvulso(String nome, BigDecimal tarifaPorMinuto) {
        super(nome);
        this.tarifaPorMinuto = Objects.requireNonNull(tarifaPorMinuto, "tarifa por minuto");
    }

    /**
     * Valor = minutos da corrida x tarifa por minuto.
     */
    @Override
    public BigDecimal calcularTarifa(Duration duracao) {
        return tarifaPorMinuto.multiply(BigDecimal.valueOf(minutos(duracao)));
    }

    /**
     * O plano avulso nao tem mensalidade, entao so aceita o campo vazio.
     * Como o plano e imutavel, o proprio modelo serve para todos os usuarios.
     *
     * @throws PlanoSemMensalidadeException se alguma mensalidade for informada
     */
    @Override
    public Plano contratar(String mensalidade) throws PlanoSemMensalidadeException {
        if (!Validacao.estaEmBranco(mensalidade)) {
            throw new PlanoSemMensalidadeException();
        }
        return this;
    }
}