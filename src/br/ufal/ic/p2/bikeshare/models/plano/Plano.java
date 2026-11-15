package br.ufal.ic.p2.bikeshare.models.plano;

import br.ufal.ic.p2.bikeshare.exceptions.plano.PlanoException;
import br.ufal.ic.p2.bikeshare.exceptions.plano.PlanoSemMensalidadeException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;

/**
 * Plano de tarifacao contratado por um usuario.
 *
 * Padrao Strategy: cada subclasse sabe calcular o proprio valor de uma corrida.
 * Quem cobra (o aluguel) so chama {@link #calcularTarifa(Duration)}, sem precisar
 * saber qual plano o usuario tem. Para criar um plano novo, basta uma subclasse
 * nova, sem mexer em quem usa.
 *
 * Planos sao imutaveis: depois de criados, nada muda. Por isso o mesmo objeto
 * pode ser compartilhado com seguranca, e trocar de plano e trocar o objeto.
 */
public abstract class Plano implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nome;

    protected Plano(String nome) {
        this.nome = Objects.requireNonNull(nome, "nome do plano");
    }

    /**
     * Nome do plano, como aparece para o usuario (ex.: "avulso", "assinante").
     */
    public String getNome() {
        return nome;
    }

    /**
     * Calcula quanto o usuario paga pelo tempo de uma corrida, segundo este plano.
     * Adicionais da bicicleta (como o da eletrica) nao entram aqui: sao da bicicleta.
     *
     * @param duracao tempo total da corrida
     * @return valor da tarifa, nunca negativo
     */
    public abstract BigDecimal calcularTarifa(Duration duracao);

    /**
     * Cria o plano que o usuario efetivamente contrata, a partir deste modelo.
     * Cada subclasse decide o que fazer com a mensalidade informada.
     *
     * @param mensalidade texto da mensalidade informada no cadastro (pode estar vazio)
     * @return plano pronto para ser associado ao usuario
     * @throws PlanoException se a mensalidade nao combinar com este tipo de plano
     */
    public abstract Plano contratar(String mensalidade) throws PlanoException;

    /**
     * Mensalidade cobrada por este plano.
     * Por padrao, planos nao tem mensalidade; quem tiver sobrescreve este metodo.
     *
     * @throws PlanoSemMensalidadeException se o plano nao cobra mensalidade
     */
    public BigDecimal getMensalidade() throws PlanoSemMensalidadeException {
        throw new PlanoSemMensalidadeException();
    }

    /**
     * Minutos inteiros de uma duracao, usados em todos os calculos de tarifa.
     */
    protected static long minutos(Duration duracao) {
        return duracao.toMinutes();
    }
}