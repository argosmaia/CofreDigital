package br.com.cofredigital.arquivo;

import br.com.cofredigital.model.Usuario;
import br.com.cofredigital.util.CriptografiaUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

public class PastaSegura {

    private File pastaSegura;

    public PastaSegura(File pastaSegura) {
        this.pastaSegura = pastaSegura;
        if (!pastaSegura.exists()) {
            pastaSegura.mkdirs();
        }
    }

    public void addProtectedFile(Usuario usuario, String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("Arquivo não encontrado.");
        }

        String fileCode = gerarNomeCodigoAleatorio();
        byte[] fileContent = Files.readAllBytes(file.toPath());

        // Criptografar o conteúdo do arquivo
        byte[] chaveSimetrica = CriptografiaUtils.gerarChaveSimetrica();
        byte[] conteudoCriptografado = CriptografiaUtils.criptografarConteudo(fileContent, chaveSimetrica);

        // Assinar o conteúdo criptografado
        byte[] assinatura = CriptografiaUtils.assinarConteudo(conteudoCriptografado, usuario.getChavePrivada());

        // Criar envelope digital
        byte[] envelopeDigital = CriptografiaUtils.criarEnvelopeDigital(chaveSimetrica, usuario.getCertificado());

        // Salvar os arquivos
        salvarArquivo(conteudoCriptografado, fileCode + ".enc");
        salvarArquivo(envelopeDigital, fileCode + ".env");
        salvarArquivo(assinatura, fileCode + ".asd");
    }

    public void removeProtectedFile(String fileCode) {
        File encFile = new File(pastaSegura, fileCode + ".enc");
        File envFile = new File(pastaSegura, fileCode + ".env");
        File asdFile = new File(pastaSegura, fileCode + ".asd");

        encFile.delete();
        envFile.delete();
        asdFile.delete();
    }

    public File getProtectedFile(String fileCode, PrivateKey chavePrivada, Certificate certificado) throws Exception {
        File encFile = new File(pastaSegura, fileCode + ".enc");
        File envFile = new File(pastaSegura, fileCode + ".env");
        File asdFile = new File(pastaSegura, fileCode + ".asd");

        byte[] conteudoCriptografado = Files.readAllBytes(encFile.toPath());
        byte[] envelopeDigital = Files.readAllBytes(envFile.toPath());
        byte[] assinatura = Files.readAllBytes(asdFile.toPath());

        // Verificar assinatura
        if (!CriptografiaUtils.verificarAssinatura(conteudoCriptografado, assinatura, certificado)) {
            throw new SecurityException("Assinatura inválida.");
        }

        // Abrir envelope digital
        byte[] chaveSimetrica = CriptografiaUtils.abrirEnvelopeDigital(envelopeDigital, chavePrivada);

        // Descriptografar conteúdo
        byte[] conteudoOriginal = CriptografiaUtils.descriptografarConteudo(conteudoCriptografado, chaveSimetrica);

        // Salvar arquivo temporário
        File tempFile = File.createTempFile(fileCode, ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(conteudoOriginal);
        }
        return tempFile;
    }

    private void salvarArquivo(byte[] conteudo, String nomeArquivo) throws IOException {
        File file = new File(pastaSegura, nomeArquivo);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(conteudo);
        }
    }

    private String gerarNomeCodigoAleatorio() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
