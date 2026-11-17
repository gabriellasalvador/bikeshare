package br.ufal.ic.p2.bikeshare.models.pagamento;

import br.ufal.ic.p2.bikeshare.exceptions.pagamento.ChavePixNulaException;
import br.ufal.ic.p2.bikeshare.utils.Validacao;

/**
 * Pagamento por pix. A cobranca e aprovada na devolucao.
 *
 * Imutavel: trocar de chave e criar um Pix novo.
 */
public final class Pix extends MetodoPagamento {

    private static final long serialVersionUID = 1L;

    /** Nome deste metodo de pagamento. */
    public static final String NOME = "pix";

    private final String chave;

    /**
     * @param chave chave pix, guardada como informada
     * @throws ChavePixNulaException se a chave estiver vazia
     */
    public Pix(String chave) throws ChavePixNulaException {
        this.chave = Validacao.exigirPreenchido(chave, ChavePixNulaException::new).trim();
    }

    @Override
    public String getNome() {
        return NOME;
    }

    @Override
    public String getChavePix() {
        return chave;
    }
}