package br.com.cofredigital.model;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class Usuario {
    private String login;
    private String senhaPessoal;
    private String grupo;
    private String caminhoCertificado;
    private String caminhoChavePrivada;
    private String chaveSecreta;
    private PrivateKey chavePrivada;
    private Certificate certificado;

    public Usuario(String login, String senhaPessoal, String grupo, String caminhoCertificado, String caminhoChavePrivada, String chaveSecreta) throws Exception {
        this.login = login;
        this.senhaPessoal = senhaPessoal;
        this.grupo = grupo;
        this.caminhoCertificado = caminhoCertificado;
        this.caminhoChavePrivada = caminhoChavePrivada;
        this.chaveSecreta = chaveSecreta;
        carregarChavePrivadaECertificado();
    }

    private void carregarChavePrivadaECertificado() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(caminhoCertificado)) {
            keyStore.load(fis, chaveSecreta.toCharArray());
        }
        this.chavePrivada = (PrivateKey) keyStore.getKey("alias", chaveSecreta.toCharArray());
        this.certificado = keyStore.getCertificate("alias");
    }

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the senhaPessoal
	 */
	public String getSenhaPessoal() {
		return senhaPessoal;
	}

	/**
	 * @param senhaPessoal the senhaPessoal to set
	 */
	public void setSenhaPessoal(String senhaPessoal) {
		this.senhaPessoal = senhaPessoal;
	}

	/**
	 * @return the grupo
	 */
	public String getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	/**
	 * @return the caminhoCertificado
	 */
	public String getCaminhoCertificado() {
		return caminhoCertificado;
	}

	/**
	 * @param caminhoCertificado the caminhoCertificado to set
	 */
	public void setCaminhoCertificado(String caminhoCertificado) {
		this.caminhoCertificado = caminhoCertificado;
	}

	/**
	 * @return the caminhoChavePrivada
	 */
	public String getCaminhoChavePrivada() {
		return caminhoChavePrivada;
	}

	/**
	 * @param caminhoChavePrivada the caminhoChavePrivada to set
	 */
	public void setCaminhoChavePrivada(String caminhoChavePrivada) {
		this.caminhoChavePrivada = caminhoChavePrivada;
	}

	/**
	 * @return the chaveSecreta
	 */
	public String getChaveSecreta() {
		return chaveSecreta;
	}

	/**
	 * @param chaveSecreta the chaveSecreta to set
	 */
	public void setChaveSecreta(String chaveSecreta) {
		this.chaveSecreta = chaveSecreta;
	}

	/**
	 * @return the chavePrivada
	 */
	public PrivateKey getChavePrivada() {
		return chavePrivada;
	}

	/**
	 * @param chavePrivada the chavePrivada to set
	 */
	public void setChavePrivada(PrivateKey chavePrivada) {
		this.chavePrivada = chavePrivada;
	}

	/**
	 * @return the certificado
	 */
	public Certificate getCertificado() {
		return certificado;
	}

	/**
	 * @param certificado the certificado to set
	 */
	public void setCertificado(Certificate certificado) {
		this.certificado = certificado;
	}

}
