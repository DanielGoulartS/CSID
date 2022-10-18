package Cadastro;
/**
 *
 * @author Daniel
 */
public class Tecnico extends Usuario{
    
    public Tecnico(int id, String nome, String sobrenome, String usuario, char[] senha, String cargo) {
        super(id, nome, sobrenome, usuario, senha, cargo);
    }


    public void exibir(Janela janela) {
        janela.exibirInterfaceTecnico();
    }

   
}
