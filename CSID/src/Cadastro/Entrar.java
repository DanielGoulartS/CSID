package Cadastro;

import Cadastro.Administrador;
import Cadastro.Usuario;
import Model.Connection;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Daniel
 */
public final class Entrar implements Janela {

    //Declaração da Conexão
    public Connection conexao = new Connection();
    //Declaração de Usuário
    public Usuario usuario;

    //Declaração da Interface Gráfica
    public JFrame janela = new JFrame("CSID - Controle de Serviço de Infraestrutura de Docagem");
    public JPanel painel = new JPanel();
    public JPanel painel2 = new JPanel();
    public JLabel titulo = new JLabel("Controle de Serviço de Infraestrutura Docagem");
    public JButton btAcessar = new JButton("Acessar");
    public JButton btCadastrar = new JButton("Sou novo aqui!");

    public Entrar() {
        //Construção do Usuario
        usuario = new Administrador(0, "", "", "", "", "000");

        //Construção da Conexão
        conexao = new Connection();

        //Formatação da Interface
        janela = new JFrame("CSID - Controle de Serviço de Infraestrutura de Docagem");
        janela.setBounds(new Rectangle(720, 250));
        janela.setLocationRelativeTo(null);
        janela.setLayout(new FlowLayout());
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        painel = new JPanel();
        painel.setLayout(new BorderLayout());

        painel2 = new JPanel();
        painel2.setLayout(new GridLayout(0, 2));

        titulo = new JLabel("<HTML><h2>Controle de Serviço de Infraestrutura de Docagem</h2>");

        btAcessar = new JButton("Acessar!");

        btCadastrar = new JButton("Sou novo aqui!");

        //Exibição da Interface Gráfica
        painel2.add(btAcessar);
        painel2.add(btCadastrar);

        painel.add(titulo, BorderLayout.NORTH);
        painel.add(painel2, BorderLayout.CENTER);

        janela.add(painel);
    }

    @Override
    public boolean exibirInterfaceCapitao() {
        btAcessar.addActionListener(usuario.acessar(this));
        btCadastrar.addActionListener(abrirJanelaCadastrarUsuario());
        janela.setVisible(true);
        return true;
    }

    public ActionListener abrirJanelaCadastrarUsuario() {
        return (ActionEvent e) -> {
            new JanelaCadastrarUsuario(usuario).exibirInterfaceExterna();
        };
    }

    @Override
    public boolean exibirInterfaceTecnico() {
        exibirInterfaceCapitao();
        return true;
    }

    @Override
    public boolean exibirInterfaceAdministrador() {
        exibirInterfaceTecnico();
        return true;
    }

}
