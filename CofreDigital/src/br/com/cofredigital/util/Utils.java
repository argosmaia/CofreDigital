package br.com.cofredigital.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import br.com.cofredigital.crypto.Base32;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

public class Utils {

    // Geração de nome de código aleatório
    public static String gerarNomeCodigoAleatorio() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Geração de chave simétrica
    public static byte[] gerarChaveSimetrica() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Chave de 128 bits
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }
    
    public static String hashSenha(String senha) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(senha.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    // Criptografar conteúdo com chave simétrica
    public static byte[] criptografarConteudo(byte[] conteudo, byte[] chaveSimetrica) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(chaveSimetrica, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(conteudo);
    }

    // Descriptografar conteúdo com chave simétrica
    public static byte[] descriptografarConteudo(byte[] conteudoCriptografado, byte[] chaveSimetrica) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(chaveSimetrica, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(conteudoCriptografado);
    }

    // Assinar conteúdo com chave privada
    public static byte[] assinarConteudo(byte[] conteudo, PrivateKey chavePrivada) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(chavePrivada);
        signature.update(conteudo);
        return signature.sign();
    }

    // Verificar assinatura com certificado
    public static boolean verificarAssinatura(byte[] conteudo, byte[] assinatura, Certificate certificado) throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initVerify(certificado);
        signature.update(conteudo);
        return signature.verify(assinatura);
    }

    // Criar envelope digital com certificado
    public static byte[] criarEnvelopeDigital(byte[] chaveSimetrica, Certificate certificado) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, certificado);
        return cipher.doFinal(chaveSimetrica);
    }

    // Abrir envelope digital com chave privada
    public static byte[] abrirEnvelopeDigital(byte[] envelopeDigital, PrivateKey chavePrivada) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, chavePrivada);
        return cipher.doFinal(envelopeDigital);
    }

    // Salvar arquivo
    public static void salvarArquivo(byte[] conteudo, String caminhoArquivo) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            fos.write(conteudo);
        }
    }

    // Carregar conteúdo do arquivo
    public static byte[] carregarConteudoArquivo(String caminhoArquivo) throws IOException {
        File file = new File(caminhoArquivo);
        return Files.readAllBytes(file.toPath());
    }

    // Carregar chave privada de um arquivo
    public static PrivateKey carregarChavePrivada(String caminhoArquivo, String senha) throws Exception {
        byte[] keyBytes = carregarConteudoArquivo(caminhoArquivo);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    // Carregar certificado de um arquivo
    public static Certificate carregarCertificado(String caminhoArquivo) throws Exception {
        try (FileInputStream fis = new FileInputStream(caminhoArquivo)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertificate(fis);
        }
    }

    // Geração de token TOTP
    public static String gerarTotpToken(String chaveSecreta, int intervaloDeTempo) {
        byte[] chaveBytes = Base64.getDecoder().decode(chaveSecreta);
        long intervalo = (System.currentTimeMillis() / 1000) / intervaloDeTempo;
        byte[] intervaloBytes = ByteBuffer.allocate(8).putLong(intervalo).array();
        
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(chaveBytes, "HmacSHA1");
            mac.init(keySpec);
            byte[] hash = mac.doFinal(intervaloBytes);
            int offset = hash[hash.length - 1] & 0xf;
            int binary = (hash[offset] & 0x7f) << 24
                    | (hash[offset + 1] & 0xff) << 16
                    | (hash[offset + 2] & 0xff) << 8
                    | (hash[offset + 3] & 0xff);
            int token = binary % 1000000;
            return String.format("%06d", token);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Erro ao gerar token TOTP", e);
        }
    }

    // Geração de chave secreta para TOTP
    public static String gerarChaveSecretaTotp() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20]; // 160 bits
        random.nextBytes(bytes);
        return Base32.encodeToString(bytes);
    }
}
