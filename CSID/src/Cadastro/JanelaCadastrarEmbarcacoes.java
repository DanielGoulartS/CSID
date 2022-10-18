package Cadastro;

import Model.Connection;
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
public class JanelaCadastrarEmbarcacoes implements Janela {

    public Usuario usuario;
    public Embarcacao embarcacao;
    public Connection conexao;

    public JFrame janela;
    public JPanel painel, painelEsqForm, painelEsqBotoes, painelLista;
    public JScrollPane scrollPanel;
    public JLabel lbId, lbNome, lbNumero;
    public JTextField tfId, tfNome, tfNumero;
    public JButton btCadastrar, btExcluir;

    public JanelaCadastrarEmbarcacoes(Usuario usuario) {

        //Iniciação de variáveis necessárias
        this.conexao = new Connection();
        this.usuario = usuario;

        //Iniciação da Interface Gráfica
        janela = new JFrame("Cadastrar Embarcações");
        janela.setBounds(new Rectangle(720, 500));
        janela.setLocationRelativeTo(null);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janela.setResizable(false);

        painel = new JPanel(new BorderLayout());
        painelEsqForm = new JPanel(new GridLayout(0, 2));
        painelEsqBotoes = new JPanel();
        painelLista = new JPanel(new GridLayout(0, 1));
        scrollPanel = new JScrollPane();
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        lbId = new JLabel("Id: ");
        lbNome = new JLabel("Nome: ");
        lbNumero = new JLabel("Número: ");

        tfId = new JTextField();
        tfNome = new JTextField();
        tfNumero = new JTextField();

    }

    public void limparFormulario() {
        tfId.setText("");
        tfNome.setText("");
        tfNumero.setText("");
    }

    @Override
    public boolean exibirInterfaceComandante() {
        //Adiciona painel de Formulário
        painelEsqForm.add(lbId);
        painelEsqForm.add(tfId);
        painelEsqForm.add(lbNome);
        painelEsqForm.add(tfNome);
        painelEsqForm.add(lbNumero);
        painelEsqForm.add(tfNumero);

        tfId.addKeyListener(usuario.pesquisaDinamicaEmbarcacoes(this));
        tfNome.addKeyListener(usuario.pesquisaDinamicaEmbarcacoes(this));
        tfNumero.addKeyListener(usuario.pesquisaDinamicaEmbarcacoes(this));

        scrollPanel.setViewportView(painelLista);

        painel.add(painelEsqForm, BorderLayout.WEST);
        painel.add(scrollPanel, BorderLayout.CENTER);

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
        //Adiciona as opções de cadastro e exclusao
        painel.add(painelEsqBotoes, BorderLayout.SOUTH);

        btCadastrar = new JButton("Cadastrar");
        btExcluir = new JButton("Excluir");

        painelEsqBotoes.add(btCadastrar);
        painelEsqBotoes.add(btExcluir);

        btCadastrar.addActionListener(Administrador.cadastrarEmbarcacoes(this));
        btExcluir.addActionListener(Administrador.excluirEmbarcacoes(this));

        exibirInterfaceTecnico();
        return true;
    }
}