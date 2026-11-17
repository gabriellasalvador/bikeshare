package br.ufal.ic.p2.bikeshare.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

/**
 * Salva e carrega o estado do sistema em um arquivo, usando a serializacao
 * nativa do Java (java.io.Serializable), como o projeto exige.
 *
 * A gravacao e atomica: o estado e escrito primeiro em um arquivo temporario
 * e so depois substitui o arquivo oficial. Se algo falhar no meio, o arquivo
 * anterior continua intacto (licao do feedback do WePayU sobre atomicidade).
 *
 * Erros de disco nao sao erros de regra de negocio: indicam problema no
 * ambiente, por isso viram excecoes nao checadas.
 */
public final class ArquivoDeDados {

    private final Path arquivo;

    /**
     * @param caminho caminho do arquivo de dados (ex.: "bikeshare.dat")
     */
    public ArquivoDeDados(String caminho) {
        this.arquivo = Path.of(Objects.requireNonNull(caminho, "caminho"));
    }

    /**
     * Le o estado salvo, se existir.
     *
     * @return o estado salvo, ou vazio se ainda nao houver arquivo
     * @throws IllegalStateException se o arquivo existir mas nao puder ser lido
     */
    public Optional<DadosSistema> carregar() {
        if (!Files.exists(arquivo)) {
            return Optional.empty();
        }
        try (InputStream entrada = Files.newInputStream(arquivo);
             ObjectInputStream leitor = new ObjectInputStream(entrada)) {
            return Optional.of((DadosSistema) leitor.readObject());
        } catch (IOException | ClassNotFoundException | ClassCastException erro) {
            throw new IllegalStateException("Nao foi possivel carregar " + arquivo
                    + ". Apague o arquivo para iniciar um sistema vazio.", erro);
        }
    }

    /**
     * Grava o estado, substituindo o que havia antes.
     *
     * @throws UncheckedIOException se nao for possivel gravar
     */
    public void salvar(DadosSistema dados) {
        Path temporario = arquivo.resolveSibling(arquivo.getFileName() + ".tmp");
        try {
            try (OutputStream saida = Files.newOutputStream(temporario);
                 ObjectOutputStream escritor = new ObjectOutputStream(saida)) {
                escritor.writeObject(dados);
            }
            Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException erro) {
            throw new UncheckedIOException("Nao foi possivel salvar " + arquivo, erro);
        }
    }
}