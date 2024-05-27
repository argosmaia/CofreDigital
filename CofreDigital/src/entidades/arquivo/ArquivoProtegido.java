package entidades.arquivo;

public class ArquivoProtegido {
    private String nomeCodigo;
    private String nomeSecreto;
    private String dono;
    private String grupo;

    public ArquivoProtegido() {
    }

    public ArquivoProtegido(String nomeCodigo, String nomeSecreto, String dono, String grupo) {
        this.nomeCodigo = nomeCodigo;
        this.nomeSecreto = nomeSecreto;
        this.dono = dono;
        this.grupo = grupo;
    }

    public String getNomeCodigo() {
        return nomeCodigo;
    }

    public void setNomeCodigo(String nomeCodigo) {
        this.nomeCodigo = nomeCodigo;
    }

    public String getNomeSecreto() {
        return nomeSecreto;
    }

    public void setNomeSecreto(String nomeSecreto) {
        this.nomeSecreto = nomeSecreto;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
}
