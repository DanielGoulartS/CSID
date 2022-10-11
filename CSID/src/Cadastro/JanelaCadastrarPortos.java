package Cadastro;

import Model.Connection;
import java.awt.BorderLayout;
import java.awt.Color;
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
    public JPanel painel, painelEsqForm, painelEsqBotoes, painelLista;
    public JScrollPane scrollPanel;
    public JLabel lbId, lbNome, lbEndereco, lbNumero, lbEmail;
    public JTextField tfId, tfNome, tfEndereco, tfTelefone, tfEmail;
    public JButton btCadastrar, btExcluir;

    public JanelaCadastrarPortos(Usuario usuario) {
        //Constrrução do Usuário
        this.usuario = usuario;

        //Construção da Conexão
        conexao = new Connection();

        //Formatação da Interface
        janela = new JFrame();
        janela.setBounds(new Rectangle(720, 500));
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        painel = new JPanel(new BorderLayout());
        painelEsqForm = new JPanel(new GridLayout(0, 2));
        painelEsqBotoes = new JPanel();
        painelLista = new JPanel(new GridLayout(0, 1));
        scrollPanel = new JScrollPane();
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        lbId = new JLabel("Id: ");
        lbNome = new JLabel("Nome: ");
        lbEndereco = new JLabel("Endereço: ");
        lbNumero = new JLabel("Telefone: ");
        lbEmail = new JLabel("E-mail: ");

        tfId = new JTextField();
        tfTelefone = new JTextField();
        tfNome = new JTextField();
        tfEndereco = new JTextField();
        tfEmail = new JTextField();

    }

    //ADD PESQUISA DINAMICA
    @Override
    public boolean exibirInterfaceCapitao() {
        //Adiciona painel de Formulário
        painelEsqForm.add(lbId);
        painelEsqForm.add(tfId);
        painelEsqForm.add(lbNome);
        painelEsqForm.add(tfNome);
        painelEsqForm.add(lbEndereco);
        painelEsqForm.add(tfEndereco);
        painelEsqForm.add(lbNumero);
        painelEsqForm.add(tfTelefone);
        painelEsqForm.add(lbEmail);
        painelEsqForm.add(tfEmail);

        tfId.addKeyListener(usuario.pesquisaDinamica(this));
        tfNome.addKeyListener(usuario.pesquisaDinamica(this));
        tfEndereco.addKeyListener(usuario.pesquisaDinamica(this));
        tfTelefone.addKeyListener(usuario.pesquisaDinamica(this));
        tfEmail.addKeyListener(usuario.pesquisaDinamica(this));

        scrollPanel.setViewportView(painelLista);

        painel.add(painelEsqForm, BorderLayout.WEST);
        painel.add(scrollPanel, BorderLayout.CENTER);


        janela.add(painel);

        janela.setVisible(true);
        return true;

    }

    public void limparFormulario(){
        tfId.setText("");
        tfNome.setText("");
        tfEndereco.setText("");
        tfTelefone.setText("");
        tfEmail.setText("");
    }
    
    @Override
    public boolean exibirInterfaceTecnico() {
        exibirInterfaceCapitao();
        return true;
    }

    @Override
    public boolean exibirInterfaceAdministrador() {
        //Adiciona as opções de cadastro e exclusao
        painel.add(painelEsqBotoes, BorderLayout.SOUTH);

        btCadastrar = new JButton("Cadastrar");
        btExcluir = new JButton("Excluir");

        painelEsqBotoes.add(btCadastrar);
        painelEsqBotoes.add(btExcluir);

        btCadastrar.addActionListener(Administrador.cadastrarPorto(this));
        btExcluir.addActionListener(Administrador.excluirPorto(this));

        exibirInterfaceTecnico();
        return true;
    }

}