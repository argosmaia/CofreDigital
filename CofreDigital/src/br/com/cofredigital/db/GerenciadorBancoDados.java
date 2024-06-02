package br.com.cofredigital.db;

import br.com.cofredigital.model.Usuario;

import java.sql.*;

public class GerenciadorBancoDados {

    private Connection connection;

    public void conectar() throws SQLException {
        String url = "jdbc:sqlite:cofre_digital.db";
        connection = DriverManager.getConnection(url);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "login TEXT," +
                "senha TEXT," +
                "grupo TEXT," +
                "certificado TEXT," +
                "chave_privada TEXT," +
                "chave_secreta TEXT" +
                ")");
    }

    public void desconectar() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void salvarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (login, senha, grupo, certificado, chave_privada, chave_secreta) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, usuario.getLogin());
        pstmt.setString(2, usuario.getSenhaPessoal());
        pstmt.setString(3, usuario.getGrupo());
        pstmt.setString(4, usuario.getCaminhoCertificado());
        pstmt.setString(5, usuario.getCaminhoChavePrivada());
        pstmt.setString(6, usuario.getChaveSecreta());
        pstmt.executeUpdate();
    }
}
