package View;

import Classes.Solicitacao;
import Classes.Cadastro.Usuario;
import Model.Connection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Daniel
 */
public final class Principal {

    //Declaração da Conexão
    Connection conexao;
    //Declaração de Usuário
    Usuario usuario;

    //Declaração da Interface Gráfica
    JFrame janela = new JFrame("CSID - Controle de Serviço de Infraestrutura de Docagem");
    JMenuBar barraMenu = new JMenuBar();
    JMenu menuServicos = new JMenu("Serviços");
    JMenu menuUsuario = new JMenu("Usuario");
    JMenuItem menuServicosSolicitar = new JMenuItem("Solicitar Serviço");
    JMenuItem menuServicosMinhasSolicitacoes = new JMenuItem("Minhas Solicitações");
    JMenuItem menuServicosMeusServicos = new JMenuItem("Meus Serviços");
    JMenuItem menuUsuarioCadastrar = new JMenuItem("Cadastrar Usuário");
    JPanel painelusuario = new JPanel();
    JPanel painelSolicitacoes = new JPanel();
    JScrollPane scrollPane = new JScrollPane();
    JLabel lbNome, lbId, lbUsuario;
    JTable tabela = new JTable(0, 11);

    public Principal() {
        //Construção de Conexão
        conexao = new Connection();
        conexao.conectar();

        //Construção de Usuario (Já criado na janela anterior)
        usuario = Usuario.GetInstance(0, "", "", "", "", "");

        //Formatação da Interface Gráfica
        //Menu
        barraMenu.add(menuServicos);
        menuServicos.add(menuServicosSolicitar);
        menuServicosSolicitar.addActionListener(abrirSolicitar());
        menuServicos.add(menuServicosMeusServicos);
        menuServicos.add(menuServicosMinhasSolicitacoes);

        barraMenu.add(menuUsuario);
        menuUsuario.add(menuUsuarioCadastrar);

        //Painel Usuario
        lbId = new JLabel();
        lbNome = new JLabel();
        lbUsuario = new JLabel();

        lbId.setText("Meu ID: " + String.valueOf(usuario.id));
        lbNome.setText("Nome: " + usuario.nome + " " + usuario.sobrenome);
        lbUsuario.setText("Nome de Usuário: " + usuario.usuario);

        painelusuario = new JPanel();
        painelusuario.setBackground(Color.LIGHT_GRAY);
        painelusuario.setLayout(new GridLayout(0, 4));
        painelusuario.add(lbId);
        painelusuario.add(lbNome);
        painelusuario.add(lbUsuario);

        //Painel Solicitações
        painelSolicitacoes.setLayout(new GridLayout(0, 1));
        ResultSet rs = conexao.executeQuery("SELECT * FROM solicitacao;");

        try {
            while (rs.next()) {
                Solicitacao novaSolicitacao = new Solicitacao(
                        rs.getString("inicio"),
                        rs.getString("fim"),
                        rs.getString("navio"),
                        rs.getString("porto"),
                        rs.getString("cameras"),
                        rs.getString("internet"),
                        rs.getString("telefonia"),
                        rs.getString("itemSimples"),
                        rs.getString("itemUrgente"));
                painelSolicitacoes.add(novoPainel(novaSolicitacao));
            }

        } catch (SQLException e) {
            System.err.println("Exceção em Principal.Principal() linha 95, while resultset.");
        }

        //Exibição
        janela.setBounds(125, 25, 1024, 720);
        janela.setLayout(new BorderLayout());
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        scrollPane.setViewportView(painelSolicitacoes);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        janela.setJMenuBar(barraMenu);
        janela.add(painelusuario, BorderLayout.NORTH);
        janela.add(scrollPane, BorderLayout.CENTER);
        janela.add(new JLabel(" "), BorderLayout.SOUTH);

        conexao.desconectar();
    }

    public void setVisible(boolean valor) {
        janela.setVisible(valor);
    }

    public ActionListener abrirSolicitar() {
        return (ActionEvent e) -> {
            new Solicitar().setVisible(true);
        };
    }

    public JPanel novoPainel(Solicitacao solicitacao) {
        JPanel painel = new JPanel(new GridLayout(0, 2));
        painel.setSize(300, 300);
        JButton btAceitar = new JButton("Aceitar");
        JButton btRecusar = new JButton("Recusar");

        painel.add(new JLabel("Identificação: " + "?"));
        painel.add(new JLabel("Encarregado: " + "Encarregado"));
        painel.add(new JLabel("Inicio: " + String.valueOf(solicitacao.dateToString(solicitacao.getInicio()))));
        painel.add(new JLabel("Fim: " + String.valueOf(solicitacao.dateToString(solicitacao.getFim()))));
        painel.add(new JLabel("Solicitante: " + "Solicitante"));
        painel.add(new JLabel("Embarcação: " + solicitacao.getEmbarcacao()));
        painel.add(new JLabel("Porto: " + solicitacao.getLocal()));
        painel.add(new JLabel("<html><p style=\"width:" + 370 + "px\">Câmeras: "
                + solicitacao.getCameras()
                + "</p></html>"));
        painel.add(new JLabel("<html><p style=\"width:" + 370 + "px\">Internet: "
                + solicitacao.getInternet()
                + "</p></html>"));
        painel.add(new JLabel("<html><p style=\"width:" + 370 + "px\">Telefonia: "
                + solicitacao.getTelefonia()
                + "</p></html>"));
        painel.add(new JLabel("<html><p style=\"width:" + 370 + "px\">Itens: "
                + solicitacao.getItemSimples()
                + "</p></html>"));
        painel.add(new JLabel("<html><p style=\"width:" + 370 + "px\">Urgência: "
                + solicitacao.getItemUrgente()
                + "</p></html>"));
        painel.add(btAceitar);
        painel.add(btRecusar);

        return painel;
    }

}
