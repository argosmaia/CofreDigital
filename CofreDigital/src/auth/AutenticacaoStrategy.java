/**
 * 
 */
package auth;

import entidades.usuario.Usuario;

public interface AutenticacaoStrategy {
    boolean verificarSenha(Usuario user, String senha);
    boolean verificarToken(Usuario user, String token);
}
