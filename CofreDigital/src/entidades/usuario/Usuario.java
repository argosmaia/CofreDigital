/**
 * 
 */
package entidades.usuario;

/**
 * 
 */
public class Usuario {
	private String id;
    private String nome;
    private String email;
    private String senhaHash;
    private String chavePrivadaCriptografada;
    private String certificadoDigitalPEM;
    
    public Usuario() {
    	
    }

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the senhaHash
	 */
	public String getSenhaHash() {
		return senhaHash;
	}

	/**
	 * @param senhaHash the senhaHash to set
	 */
	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}

	/**
	 * @return the chavePrivadaCriptografada
	 */
	public String getChavePrivadaCriptografada() {
		return chavePrivadaCriptografada;
	}

	/**
	 * @param chavePrivadaCriptografada the chavePrivadaCriptografada to set
	 */
	public void setChavePrivadaCriptografada(String chavePrivadaCriptografada) {
		this.chavePrivadaCriptografada = chavePrivadaCriptografada;
	}

	/**
	 * @return the certificadoDigitalPEM
	 */
	public String getCertificadoDigitalPEM() {
		return certificadoDigitalPEM;
	}

	/**
	 * @param certificadoDigitalPEM the certificadoDigitalPEM to set
	 */
	public void setCertificadoDigitalPEM(String certificadoDigitalPEM) {
		this.certificadoDigitalPEM = certificadoDigitalPEM;
	}
    
    
}
