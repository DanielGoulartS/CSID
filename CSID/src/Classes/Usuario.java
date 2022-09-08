package Classes;

public class Usuario {

    private static Usuario novoUsuario;
    public int id;
    public String nome, sobrenome, usuario, senha, cargo;

    
    private Usuario(int id, String nome, String sobrenome, String usuario, String senha, String cargo) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.usuario = usuario;
        this.senha = senha;
        this.cargo = cargo;
    }

    public static Usuario GetInstance(int id, String nome, String sobrenome, String usuario, String senha, String cargo) {
        if (novoUsuario == null) {
            novoUsuario = new Usuario(id, nome, sobrenome, usuario, senha, cargo);
        }
        return novoUsuario;
    }

    public static void ClearInstance() {
            novoUsuario = null;
    }

}
