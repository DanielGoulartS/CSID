package View.Cadastro;

import Classes.Cadastro.Usuario;
import Model.Connection;
import View.Principal;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public final class Entrar {

    //Declaração da Conexão
    Connection conexao;
    //Declaração de Usuário
    Usuario usuario;

    //Declaração da Interface Gráfica
    JFrame janela = new JFrame("CSID - Controle de Serviço de Infraestrutura de Docagem");
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
        janela.setBounds(new Rectangle(720, 250));
        janela.setLocationRelativeTo(null);
        janela.setLayout(new FlowLayout());
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        painel = new JPanel();
        painel.setLayout(new GridLayout(0, 1));

        painel2 = new JPanel();
        painel2.setLayout(new GridLayout(0, 2));

        titulo = new JLabel("<HTML><h2>Controle de Serviço de Infraestrutura de Docagem</h2>");

        lbUsuario = new JLabel("Usuário:");
        tfUsuario = new JTextField();

        senha = new JLabel("Senha:");
        tfSenha = new JPasswordField();

        btAcessar = new JButton("Acessar!");

        btCadastrar = new JButton("Sou novo aqui!");

        //Funcionalidades
        tfSenha.addKeyListener(interagirEnter(btAcessar));
        btAcessar.addActionListener(acessar(conexao, janela, tfUsuario, tfSenha, usuario));

        //Exibição da Interface Gráfica
        painel2.add(lbUsuario);
        painel2.add(tfUsuario);
        painel2.add(senha);
        painel2.add(tfSenha);
        painel2.add(btAcessar);
        painel2.add(btCadastrar);

        painel.add(titulo);
        painel.add(painel2);

        janela.add(painel);
    }

    public void setVisible(boolean valor) {
        janela.setVisible(true);
    }

    //Ocasiona Login ao apertar Enter
    public KeyListener interagirEnter(JButton btAcessar) {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    btAcessar.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
    }

    //Logica para comunicar com a Base de Dados, gerar Usuário, fazer Login, e abrir próxima Tela.
    public ActionListener acessar(Connection conexao,
            JFrame janela,
            JTextField tfUsuario, JPasswordField tfSenha,
            Usuario usuario) {
        return (ActionEvent e) -> {
            String pass = String.valueOf(tfSenha.getPassword());

            //tela.usuario = new Usuario(tela.tfUsuario.getText(), pass);
            conexao.conectar();

            ResultSet rs = conexao.executeQuery("Select * from usuarios where"
                    + " usuario = '" + tfUsuario.getText()
                    + "' and senha ='" + pass
                    + "';");
            try {
                if (rs.next()) {
                    //Passando parâmetros pois é a unica janela que cria um usuário
                    Usuario.GetInstance(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("sobrenome"),
                            rs.getString("usuario"),
                            rs.getString("senha"),
                            rs.getString("cargo"));

                    new Principal().setVisible(true);

                    System.out.println("Acessado com Sucesso!!");

                    System.gc();

                    conexao.desconectar();
                    janela.dispose();

                } else {
                    System.err.println("Acesso negado");

                }
            } catch (SQLException ex) {
                System.err.println("Falha no Login, View.Entrar.acessar()");
            }

            System.gc();

            conexao.desconectar();
        };
    }
}