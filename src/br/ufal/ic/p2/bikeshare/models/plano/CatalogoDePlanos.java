package br.ufal.ic.p2.bikeshare.models.plano;

import br.ufal.ic.p2.bikeshare.config.Configuracao;
import br.ufal.ic.p2.bikeshare.exceptions.plano.MensalidadeNegativaException;
import br.ufal.ic.p2.bikeshare.exceptions.plano.PlanoException;
import br.ufal.ic.p2.bikeshare.exceptions.plano.PlanoInvalidoException;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Catalogo dos planos que o sistema oferece, cada um guardado como um modelo.
 *
 * Padrao Prototype: para contratar um plano, o catalogo pega o modelo pelo nome
 * e pede para ele gerar o plano do usuario ({@link Plano#contratar(String)}).
 * Assim o catalogo nunca precisa de "if" por tipo de plano: cada modelo sabe
 * se aceita mensalidade e como.
 *
 * Os modelos sao montados com os valores da configuracao (nada fixo no codigo).
 * Na US12, os planos customizados entram aqui como mais modelos.
 */
public class CatalogoDePlanos implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Nome do plano padrao, usado quando o usuario nao escolhe plano. */
    public static final String AVULSO = "avulso";

    /** Nome do plano com mensalidade e franquia oferecido desde o inicio. */
    public static final String ASSINANTE = "assinante";

    // tipo concreto (e nao Map) porque LinkedHashMap garante ser serializavel
    private final LinkedHashMap<String, Plano> modelos = new LinkedHashMap<>();

    /**
     * Monta o catalogo inicial com os planos avulso e assinante.
     *
     * @param configuracao fonte da tarifa, franquia, valor excedente e mensalidade padrao
     */
    public CatalogoDePlanos(Configuracao configuracao) {
        modelos.put(AVULSO, new PlanoAvulso(AVULSO, configuracao.tarifaPorMinuto()));
        try {
            modelos.put(ASSINANTE, new PlanoAssinatura(ASSINANTE,
                    configuracao.franquiaAssinante(),
                    configuracao.minutoExcedenteAssinante(),
                    configuracao.mensalidadePadraoAssinante()));
        } catch (MensalidadeNegativaException impossivel) {
            // a Configuracao ja recusa valores negativos ao ser carregada
            throw new IllegalStateException("Mensalidade padrao invalida na configuracao", impossivel);
        }
    }

    /**
     * Plano padrao de quem se cadastra sem escolher plano.
     */
    public Plano planoPadrao() {
        return modelos.get(AVULSO);
    }

    /**
     * Contrata um plano informando a mensalidade (cadastro com plano escolhido).
     *
     * @param nome        nome do plano (diferencia maiusculas de minusculas)
     * @param mensalidade texto da mensalidade (vazio para planos sem mensalidade)
     * @throws PlanoInvalidoException se o plano nao existir
     * @throws PlanoException         se a mensalidade nao combinar com o plano
     */
    public Plano contratar(String nome, String mensalidade) throws PlanoException {
        return buscarModelo(nome).contratar(mensalidade);
    }

    /**
     * Contrata um plano com os valores padrao do modelo (troca de plano sem
     * informar mensalidade). Como planos sao imutaveis, o proprio modelo serve.
     *
     * @param nome nome do plano (diferencia maiusculas de minusculas)
     * @throws PlanoInvalidoException se o plano nao existir
     */
    public Plano contratarPadrao(String nome) throws PlanoInvalidoException {
        return buscarModelo(nome);
    }

    private Plano buscarModelo(String nome) throws PlanoInvalidoException {
        if (Validacao.estaEmBranco(nome) || !modelos.containsKey(nome)) {
            throw new PlanoInvalidoException();
        }
        return modelos.get(nome);
    }
}
