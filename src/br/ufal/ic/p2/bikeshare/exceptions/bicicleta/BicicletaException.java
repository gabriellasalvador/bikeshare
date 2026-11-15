package br.ufal.ic.p2.bikeshare.exceptions.bicicleta;

import br.ufal.ic.p2.bikeshare.exceptions.BikeshareException;

/**
 * Base dos erros de cadastro, consulta e uso de bicicletas.
 * Permite tratar todos os erros deste assunto de uma vez, se necessario.
 */
public abstract class BicicletaException extends BikeshareException {

    private static final long serialVersionUID = 1L;

    protected BicicletaException(String mensagem) {
        super(mensagem);
    }
}
