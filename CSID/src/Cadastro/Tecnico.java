package Cadastro;

import java.awt.event.ActionListener;

/**
 *
 * @author Daniel
 */
public class Tecnico extends Usuario{
    
    public Tecnico(int id, String nome, String sobrenome, String usuario, char[] senha, String cargo) {
        super(id, nome, sobrenome, usuario, senha, cargo);
    }
    
    public Tecnico(int id, String nome, String sobrenome, String usuario, String senha, String cargo) {
        super(id, nome, sobrenome, usuario, senha, cargo);
    }

    public void exibir(Janela janela) {
        janela.exibirInterfaceTecnico();
    }

    @Override
    public ActionListener cadastrar(JanelaCadastrarUsuario janela) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ActionListener excluir(JanelaCadastrarUsuario janela) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
   
}