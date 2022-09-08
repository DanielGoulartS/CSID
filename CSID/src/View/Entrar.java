package View;

import Classes.Usuario;
import Model.Connection;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Daniel
 */
public class Entrar extends JFrame {

    //Declaração da Conexão
    Connection conexao;
    //Declaração de Usuário
    Usuario usuario;

    //Declaração da Interface Gráfica
    JFrame janela = new JFrame("CSID - Controle de Serviço de Infraestrutura Docagem");
    JPanel painel = new JPanel();
    JPanel painel2 = new JPanel();
    JLabel titulo = new JLabel("Controle de Serviço de Infraestrutura Docagem");
    JLabel lbUsuario = new JLabel("Usuário:");
    JTextField tfUsuario = new JTextField();
    JLabel senha = new JLabel("Senha:");
    JPasswordField tfSenha = new JPasswordField();
    JButton btAcessar = new JButton("Acessar");
    JButton btCadastrar = new JButton("Sou novo aqui!");

    public Entrar() {
        //Construção da Conexão
        conexao = new Connection();

        //Formatação da Interface
        janela = new JFrame("CSID - Controle de Serviço de Infraestrutura Docagem");
        janela.setBounds(new Rectangle(1024, 720));
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(200, 200, 200, 200));

        painel2 = new JPanel();
        painel2.setLayout(new GridLayout(0, 2));

        titulo = new JLabel("Controle de Serviço de Infraestrutura Docagem");

        lbUsuario = new JLabel("Usuário:");
        tfUsuario = new JTextField();

        senha = new JLabel("Senha:");
        tfSenha = new JPasswordField();

        btAcessar = new JButton("Acessar!");

        btCadastrar = new JButton("Sou novo aqui!");

        //Funcionalidades
        btAcessar.addActionListener(acessar(this));

        //Exibição da Interface Gráfica
        painel.add(painel2, BorderLayout.CENTER);

        painel2.add(titulo);
        painel2.add(new JLabel());
        painel2.add(lbUsuario);
        painel2.add(tfUsuario);
        painel2.add(senha);
        painel2.add(tfSenha);
        painel2.add(btAcessar);
        painel2.add(btCadastrar);

        janela.add(painel);
        janela.setVisible(true);
    }

    public ActionListener acessar(Entrar tela) {
        return (ActionEvent e) -> {
            String pass = String.valueOf(tela.tfSenha.getPassword());

            //tela.usuario = new Usuario(tela.tfUsuario.getText(), pass);
            tela.conexao.conectar();

            ResultSet rs = tela.conexao.executeQuery("Select * from usuarios where"
                    + " usuario = '" + tela.tfUsuario.getText()
                    + "' and senha ='" + pass
                    + "';");
            try {
                if (rs.next()) {
                    //Passando parâmetros pois é a unica janela que cria um usuário
                    tela.usuario = Usuario.GetInstance(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("sobrenome"),
                            rs.getString("usuario"),
                            rs.getString("senha"),
                            rs.getString("cargo"));
                    
                    new Solicitar();
                    
                    System.out.println("Acessado com Sucesso!!");
                    
                    pass = null;
                    tela.conexao.desconectar();
                    tela.janela.dispose();
                
                } else {
                    System.err.println("Acesso negado");

                }
            } catch (SQLException ex) {
                System.err.println("Falha no Login, View.Entrar.acessar()");
            }
            pass = null;
            tela.conexao.desconectar();
        };
    }
}
