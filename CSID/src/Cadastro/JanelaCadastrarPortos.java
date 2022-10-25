package Cadastro;

import Model.Connection;
import Solicitacoes.JanelaSolicitar;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Daniel
 */
public class JanelaCadastrarPortos implements Janela {

    public Usuario usuario;
    public Porto porto;
    public Connection conexao;

    public JFrame janela;
    public JPanel painel, painelEsq1, painelEsqForm, painelEsqBotoes, painelLista;
    public JScrollPane scrollPanel;
    public JLabel lbId, lbNome, lbEndereco, lbTelefone, lbEmail;
    public JTextField tfId, tfNome, tfEndereco, tfTelefone, tfEmail;
    public JButton btCadastrar, btExcluir;

    public JanelaCadastrarPortos(Usuario usuario) {
        //Constrrução do Usuário
        this.usuario = usuario;

        //Construção da Conexão
        conexao = new Connection();

        //Formatação da Interface
        janela = new JFrame();
        janela.setTitle("Cadastrar Portos");
        janela.setBounds(new Rectangle(720, 500));
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janela.setResizable(false);

        painel = new JPanel(new GridLayout(0,2));
        painelEsq1 = new JPanel(new BorderLayout());
        painelEsqForm = new JPanel(new GridLayout(0, 2));
        painelEsqBotoes = new JPanel();
        painelLista = new JPanel(new GridLayout(0, 1));
        scrollPanel = new JScrollPane();
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        lbId = new JLabel("Id: ");
        lbNome = new JLabel("Nome: ");
        lbEndereco = new JLabel("Endereço: ");
        lbTelefone = new JLabel("Telefone: ");
        lbEmail = new JLabel("E-mail: ");

        tfId = new JTextField();
        tfTelefone = new JTextField();
        tfNome = new JTextField();
        tfEndereco = new JTextField();
        tfEmail = new JTextField();

    }

    public void limparFormulario() {
        tfId.setText("");
        tfNome.setText("");
        tfEndereco.setText("");
        tfTelefone.setText("");
        tfEmail.setText("");
    }

    @Override
    public boolean exibirInterfaceComandante() {
        //Adiciona painel de Formulário
        painelEsqForm.add(lbId);
        painelEsqForm.add(tfId);
        painelEsqForm.add(lbNome);
        painelEsqForm.add(tfNome);
        painelEsqForm.add(lbEndereco);
        painelEsqForm.add(tfEndereco);
        painelEsqForm.add(lbTelefone);
        painelEsqForm.add(tfTelefone);
        painelEsqForm.add(lbEmail);
        painelEsqForm.add(tfEmail);

        tfId.addKeyListener(usuario.pesquisaDinamicaPortos(this));
        tfNome.addKeyListener(usuario.pesquisaDinamicaPortos(this));
        tfEndereco.addKeyListener(usuario.pesquisaDinamicaPortos(this));
        tfTelefone.addKeyListener(usuario.pesquisaDinamicaPortos(this));
        tfEmail.addKeyListener(usuario.pesquisaDinamicaPortos(this));
        tfNome.addKeyListener(JanelaSolicitar.listener(tfNome, 40));
        tfEndereco.addKeyListener(JanelaSolicitar.listener(tfEndereco, 100));
        tfTelefone.addKeyListener(JanelaSolicitar.listener(tfTelefone, 40));
        tfEmail.addKeyListener(JanelaSolicitar.listener(tfEmail, 100));
        

        scrollPanel.setViewportView(painelLista);

        painelEsq1.add(painelEsqForm, BorderLayout.CENTER);
        painel.add(painelEsq1);
        painel.add(scrollPanel);

        janela.add(painel);

        janela.setVisible(true);
        return true;

    }

    @Override
    public boolean exibirInterfaceTecnico() {
        exibirInterfaceComandante();
        return true;
    }

    @Override
    public boolean exibirInterfaceAdministrador() {
        exibirInterfaceTecnico();
        
        //Adiciona as opções de cadastro e exclusao
        painelEsq1.add(painelEsqBotoes, BorderLayout.SOUTH);

        btCadastrar = new JButton("Cadastrar");
        btExcluir = new JButton("Excluir");

        
        painelEsqBotoes.add(btCadastrar);
        painelEsqBotoes.add(btExcluir);

        btCadastrar.addActionListener(Administrador.cadastrarPortos(this));
        btExcluir.addActionListener(Administrador.excluirPortos(this));

        return true;
    }

}
