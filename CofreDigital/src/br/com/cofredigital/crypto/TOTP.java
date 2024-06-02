package br.com.cofredigital.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

public class TOTP {
    private byte[] key = null;
    private long timeStepInSeconds = 30;

    // Construtor da classe.
    public TOTP(String base32EncodedSecret, long timeStepInSeconds) throws Exception {
        this.key = decodeBase32(base32EncodedSecret);
        this.timeStepInSeconds = timeStepInSeconds;
    }

    // Método auxiliar para decodificar a chave secreta codificada em base32.
    private byte[] decodeBase32(String base32EncodedSecret) {
        return Base64.getDecoder().decode(base32EncodedSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Recebe o HASH HMAC-SHA1 e determina o código TOTP de 6 algarismos decimais.
    private String getTOTPCodeFromHash(byte[] hash) {
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        int otp = binary % 1000000;
        return String.format("%06d", otp);
    }

    // Recebe o contador e a chave secreta para produzir o hash HMAC-SHA1.
    private byte[] HMAC_SHA1(byte[] counter, byte[] keyByteArray) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(keyByteArray, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(keySpec);
        return mac.doFinal(counter);
    }

    // Recebe o intervalo de tempo e executa o algoritmo TOTP para produzir o código TOTP.
    private String TOTPCode(long timeInterval) throws NoSuchAlgorithmException, InvalidKeyException {
        long counter = timeInterval / this.timeStepInSeconds;
        byte[] counterBytes = ByteBuffer.allocate(8).putLong(counter).array();
        byte[] hash = HMAC_SHA1(counterBytes, key);
        return getTOTPCodeFromHash(hash);
    }

    // Método que é utilizado para solicitar a geração do código TOTP.
    public String generateCode() throws NoSuchAlgorithmException, InvalidKeyException {
        return TOTPCode(new Date().getTime() / 1000);
    }

    // Método que é utilizado para validar um código TOTP (inputTOTP).
    public boolean validateCode(String inputTOTP) throws NoSuchAlgorithmException, InvalidKeyException {
        long currentTime = new Date().getTime() / 1000;
        for (int i = -1; i <= 1; i++) {
            String totp = TOTPCode((currentTime + i * this.timeStepInSeconds));
            if (inputTOTP.equals(totp)) {
                return true;
            }
        }
        return false;
    }
}
