package br.com.cofredigital.auth;

import br.com.cofredigital.db.GerenciadorBancoDados;
import br.com.cofredigital.model.Usuario;
import br.com.cofredigital.util.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

public class Autenticador {

    private static final int INTERVALO_TEMPO = 30; // 30 segundos
    private static final int DIGITOS_TOKEN = 6; // 6 dígitos
    private static final String HMAC_ALGORITMO = "HmacSHA1";

    private GerenciadorBancoDados dbManager;

    public Autenticador(GerenciadorBancoDados dbManager) {
        this.dbManager = dbManager;
    }

    public boolean autenticarUsuario(String login, String senha, String token) throws SQLException {
        try {
            dbManager.conectar();
            String hashSenha = Utils.hashSenha(senha);
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(
                "SELECT * FROM usuarios WHERE login = ? AND senha = ?"
            );
            stmt.setString(1, login);
            stmt.setString(2, hashSenha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String chaveSecreta = rs.getString("chave_secreta");
                return verificarToken(chaveSecreta, token);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            dbManager.desconectar();
        }
    }

    private boolean verificarToken(String chaveSecreta, String token) throws GeneralSecurityException {
        long tempoAtual = System.currentTimeMillis() / 1000L;
        long[] intervalosTempo = {tempoAtual, tempoAtual - INTERVALO_TEMPO, tempoAtual + INTERVALO_TEMPO};

        for (long intervalo : intervalosTempo) {
            String totp = gerarTOTP(chaveSecreta, intervalo);
            if (totp.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private String gerarTOTP(String chaveSecreta, long intervaloTempo) throws GeneralSecurityException {
        byte[] key = Base64.getDecoder().decode(chaveSecreta);
        byte[] data = new byte[8];
        for (int i = 7; i >= 0; i--) {
            data[i] = (byte) (intervaloTempo & 0xFF);
            intervaloTempo >>= 8;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, HMAC_ALGORITMO);
        Mac mac = Mac.getInstance(HMAC_ALGORITMO);
        mac.init(signKey);

        byte[] hash = mac.doFinal(data);
        int offset = hash[hash.length - 1] & 0xF;

        int binary =
                ((hash[offset] & 0x7F) << 24) |
                ((hash[offset + 1] & 0xFF) << 16) |
                ((hash[offset + 2] & 0xFF) << 8) |
                (hash[offset + 3] & 0xFF);

        int otp = binary % (int) Math.pow(10, DIGITOS_TOKEN);
        return String.format("%06d", otp);
    }
}
