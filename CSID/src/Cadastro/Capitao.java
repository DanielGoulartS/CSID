package Cadastro;

import java.awt.event.ActionListener;

/**
 *
 * @author Daniel
 */
public class Capitao extends Usuario{

    public Capitao(int id, String nome, String sobrenome, String usuario, char[] senha, String cargo) {
        super(id, nome, sobrenome, usuario, senha, cargo);
    }


    public void exibir(Janela janela) {
        janela.exibirInterfaceCapitao();
    }

    @Override
    public ActionListener cadastrar(JanelaCadastrarUsuarios janela) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}