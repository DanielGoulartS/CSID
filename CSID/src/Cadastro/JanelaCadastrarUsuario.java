package Cadastro;

import Model.Connection;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Daniel
 */
public final class JanelaCadastrarUsuario implements Janela {

    //Instanciação de Váriáveis
    public Connection conexao = new Connection();
    public Usuario usuario;
    public JFrame janela = new JFrame("Gerenciar Usuários");
    public JPanel painel = new JPanel(new GridLayout(0, 2));
    public JPanel painelEsquerdo = new JPanel(new BorderLayout());
    public JPanel painelEsquerdo1 = new JPanel(new GridLayout(0, 2));
    public JPanel painelEsquerdo2 = new JPanel(new FlowLayout());
    public JPanel painelDireito = new JPanel(new GridLayout(0, 1));
    public JScrollPane scrollPane = new JScrollPane();
    public JLabel lbId = new JLabel("ID: ");
    public JLabel lbNome = new JLabel("Nome: ");
    public JLabel lbSobrenome = new JLabel("Sobrenome: ");
    public JLabel lbUsuario = new JLabel("Usuário: ");
    public JLabel lbSenha = new JLabel("Senha: ");
    public JLabel lbConfirmarSenha = new JLabel("Confirmar Senha: ");
    public JLabel lbCargo = new JLabel("Cargo: ");
    public JTextField tfId = new JTextField();
    public JTextField tfNome = new JTextField();
    public JTextField tfSobrenome = new JTextField();
    public JTextField tfUsuario = new JTextField();
    public JPasswordField pfSenha = new JPasswordField();
    public JPasswordField pfConfirmarSenha = new JPasswordField();
    public JComboBox cbCargo = new JComboBox();
    public JButton btCadastrar = new JButton("Cadastrar");
    public JButton btExcluir = new JButton("Excluir");

    public JanelaCadastrarUsuario(Usuario usuario) {
        //Constrrução do Usuário
        this.usuario = usuario;
        
        //Construção da Conexão
        conexao = new Connection();

        //Formatação da Interface
        janela.setBounds(new Rectangle(720, 500));
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        //Painel Formulário de Usuário
        painelEsquerdo1.add(lbId);
        painelEsquerdo1.add(tfId);
        painelEsquerdo1.add(lbNome);
        painelEsquerdo1.add(tfNome);
        painelEsquerdo1.add(lbSobrenome);
        painelEsquerdo1.add(tfSobrenome);
        painelEsquerdo1.add(lbUsuario);
        painelEsquerdo1.add(tfUsuario);

        painelEsquerdo.add(painelEsquerdo1, BorderLayout.CENTER);
        painelEsquerdo.add(painelEsquerdo2, BorderLayout.SOUTH);

        cbCargo.addItem("Administrador");
        cbCargo.addItem("Técnico");
        cbCargo.addItem("Capitão de Embarcação");

        //Painel Usuários
        scrollPane.setViewportView(painelDireito);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //Exibição
        painel.add(painelEsquerdo);

        janela.add(painel);

    }

    
    
    public KeyListener pesquisaDinamica(JPanel painel) {
        return new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                painel.removeAll();

                if (!conexao.isValid()) {
                    conexao.conectar();
                }

                ResultSet rs;
                rs = conexao.executeQuery("SELECT * FROM usuarios WHERE usuario LIKE '%" + tfUsuario.getText() + "%'"
                        + " AND id LIKE '%" + tfId.getText() + "%'"
                        + " AND nome LIKE '%" + tfNome.getText() + "%'"
                        + " AND sobrenome LIKE '%" + tfSobrenome.getText() + "%';");

                try {
                    while (rs.next()) {
                        painel.add(novoPainel(rs.getString("id"), rs.getString("usuario"),
                                rs.getString("nome"), rs.getString("sobrenome"), rs.getString("cargo")));
                    }
                } catch (SQLException ex) {
                    System.err.println("Erro na pesquisa ou alimentação do painel."
                            + "View.Cadastro.GerenciarUsuario.pesquisaDinamica().");
                }
                painel.add(new JLabel());
                painel.add(new JLabel());
                painel.add(new JLabel());

                painel.setVisible(false);
                painel.setVisible(true);

            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        };
    }

    public JPanel novoPainel(String id, String usuario, String nome, String sobrenome, String cargo) {
        //Formata Elemento
        JPanel novo = new JPanel(new BorderLayout(2, 2));
        novo.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));//)Border(5, 2, 5, 2));
        JPanel novo2 = new JPanel(new GridLayout(2, 3));
        JLabel lbIdPesquisa = new JLabel(id);
        JLabel lbUsuarioPesquisa = new JLabel(usuario);
        JLabel lbNomePesquisa = new JLabel(nome);
        JLabel lbSobrenomePesquisa = new JLabel(sobrenome);
        JLabel lbCargoPesquisa = new JLabel(cargo);

        novo2.add(lbIdPesquisa);
        novo2.add(lbUsuarioPesquisa);
        novo2.add(new JLabel());
        novo2.add(lbNomePesquisa);
        novo2.add(lbSobrenomePesquisa);
        novo2.add(lbCargoPesquisa);

        novo.add(novo2);

        novo.setPreferredSize(new Dimension(10, 10));

        return novo;
    }

    public void limparCampos() {
        tfId.setText("");
        tfNome.setText("");
        tfSobrenome.setText("");
        tfUsuario.setText("");
        pfSenha.setText("");
        pfConfirmarSenha.setText("");
    }

    public boolean exibirInterfaceExterna() {

        //Completar formulário de usuário
        painelEsquerdo1.add(lbSenha);
        painelEsquerdo1.add(pfSenha);
        painelEsquerdo1.add(lbConfirmarSenha);
        painelEsquerdo1.add(pfConfirmarSenha);
        painelEsquerdo1.add(lbCargo);
        painelEsquerdo1.add(cbCargo);

        //Botões de cadastro
        btCadastrar.addActionListener(usuario.cadastrar(this));
        painelEsquerdo2.add(btCadastrar);
        painelEsquerdo.add(painelEsquerdo2, BorderLayout.SOUTH);

        //Geral
        janela.setVisible(true);
        return true;
    }

    @Override
    public boolean exibirInterfaceCapitao() {

        //Funcionalidades
        tfId.addKeyListener(pesquisaDinamica(painelDireito));
        tfUsuario.addKeyListener(pesquisaDinamica(painelDireito));
        tfNome.addKeyListener(pesquisaDinamica(painelDireito));
        tfSobrenome.addKeyListener(pesquisaDinamica(painelDireito));

        //Painel de pesquisa
        painel.add(scrollPane);

        //Geral
        janela.setVisible(true);
        return true;
    }

    @Override
    public boolean exibirInterfaceTecnico() {
        exibirInterfaceCapitao();
        return true;
    }

    @Override
    public boolean exibirInterfaceAdministrador() {
        //Completar formulário de usuário
        painelEsquerdo1.add(lbSenha);
        painelEsquerdo1.add(pfSenha);
        painelEsquerdo1.add(lbConfirmarSenha);
        painelEsquerdo1.add(pfConfirmarSenha);
        painelEsquerdo1.add(lbCargo);
        painelEsquerdo1.add(cbCargo);

        //Botões de cadastro
        btCadastrar.addActionListener(usuario.cadastrar(this));
        btExcluir.addActionListener(usuario.excluir(this));
        painelEsquerdo2.add(btCadastrar);
        painelEsquerdo2.add(btExcluir);

        //Geral
        exibirInterfaceTecnico();
        return true;
    }

}
