package View;

import Cadastro.Janela;
import Classes.Solicitacao;
import Cadastro.Usuario;
import Model.Connection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Daniel
 */
public final class JanelaTodasAsSolicitacoes implements Janela{

    //Declaração da Conexão
    Connection conexao;
    //Declaração de Usuário
    Usuario usuario;

    //Declaração da Interface Gráfica
    public JFrame janela = new JFrame("CSID - Controle de Serviço de Infraestrutura de Docagem");
    public JMenuBar barraMenu = new JMenuBar();
    public JMenu menuServicos = new JMenu("Serviços");
    public JMenu menuUsuario = new JMenu("Usuario");
    public JMenuItem menuServicosSolicitar = new JMenuItem("Solicitar Serviço");
    public JMenuItem menuServicosMinhasSolicitacoes = new JMenuItem("Minhas Solicitações");
    public JMenuItem menuServicosMeusServicos = new JMenuItem("Meus Serviços");
    public JMenuItem menuUsuarioCadastrar = new JMenuItem("Cadastrar Usuário");
    public JPanel painelUsuario = new JPanel();
    public TitledBorder painelSolicitacoesBorda = BorderFactory.createTitledBorder("Todos as Solicitações");
    public JPanel painelSolicitacoes = new JPanel();
    public TitledBorder painelMeusServicosBorda = BorderFactory.createTitledBorder("Meus Serviços");
    public JPanel painelMeusServicos = new JPanel();
    public TitledBorder painelMinhasSolicitacoesBorda = BorderFactory.createTitledBorder("Minhas Solicitações");
    public JPanel painelMinhasSolicitacoes = new JPanel();
    public JScrollPane scrollPane = new JScrollPane();
    public JLabel lbNome, lbId, lbUsuario;
    public JPanel painelPaginador = new JPanel();

    public JanelaTodasAsSolicitacoes(Usuario usuario) {
        //Construção de Conexão
        conexao = new Connection();
        conexao.conectar();

        //Construção de Usuario (Já criado na janela anterior)
        this.usuario = usuario;

        //Formatação da Interface Gráfica
        janela.setBounds(0, 0, 1024, 720);
        janela.setLocationRelativeTo(null);
        janela.setLayout(new BorderLayout());
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        //Menu
        barraMenu.add(menuServicos);
        menuServicos.add(menuServicosSolicitar);
        menuServicos.add(menuServicosMinhasSolicitacoes);
        menuServicos.add(menuServicosMeusServicos);

        barraMenu.add(menuUsuario);
        menuUsuario.add(menuUsuarioCadastrar);

        //Painel Usuario
        lbId = new JLabel();
        lbNome = new JLabel();
        lbUsuario = new JLabel();

        lbId.setText("Meu ID: " + String.valueOf(usuario.id));
        lbNome.setText("Nome: " + usuario.nome + " " + usuario.sobrenome);
        lbUsuario.setText("Nome de Usuário: " + usuario.usuario);

        painelUsuario.setBackground(Color.LIGHT_GRAY);
        painelUsuario.setLayout(new GridLayout(0, 4));
        painelUsuario.add(lbId);
        painelUsuario.add(lbNome);
        painelUsuario.add(lbUsuario);

        //Painel Solicitações
        painelSolicitacoesBorda.setTitleJustification(TitledBorder.CENTER);
        painelSolicitacoes.setBorder(painelSolicitacoesBorda);

        //Painel Meus Serviços
        painelMeusServicosBorda.setTitleColor(Color.white);
        painelMeusServicosBorda.setTitleJustification(TitledBorder.CENTER);
        painelMeusServicos.setBorder(painelMeusServicosBorda);
        painelMeusServicos.setBackground(Color.black);

        //Painel Minhas Solicitações
        painelMinhasSolicitacoesBorda.setTitleColor(Color.white);
        painelMinhasSolicitacoesBorda.setTitleJustification(TitledBorder.CENTER);
        painelMinhasSolicitacoes.setBorder(painelMinhasSolicitacoesBorda);
        painelMinhasSolicitacoes.setBackground(Color.gray);

        //Painel do Paginador
        painelPaginador.add(new JLabel("Paginar excesso de Solicitações."));

        //Funcionalidades
        //Menu
        menuServicosSolicitar.addActionListener(abrirSolicitar());
        menuServicosMinhasSolicitacoes.addActionListener(abrirMinhasSolicitacoes());
        menuServicosMeusServicos.addActionListener(abrirMeusServicos());

        //Paineis
        alimentarPainel(painelSolicitacoes, "SELECT * FROM solicitacao;");

        //Exibição
        scrollPane.setViewportView(painelSolicitacoes);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        janela.setJMenuBar(barraMenu);
        janela.add(painelUsuario, BorderLayout.NORTH);
        janela.add(scrollPane, BorderLayout.CENTER);
        janela.add(painelPaginador, BorderLayout.SOUTH);

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

    public ActionListener aceitarSolicitacao(JLabel labelEncarregado, Solicitacao solicitacao, JButton este) {
        return (ActionEvent e) -> {

            conexao.conectar();

            try {

                if (solicitacao.getEncarregado() == 0) { //Se a solicitacao não tiver Encarregado, atribua-se
                    atribuir(labelEncarregado, solicitacao, este);
                } else { //Caso seja você, desatribua-se
                    desatribuir(labelEncarregado, solicitacao, este);
                }

            } catch (SQLException ex) {
                System.err.println("Erro ao interagir com a solicitação, View.Principal.aceitarSolicitacao().");
                System.err.println(ex);

                JOptionPane.showMessageDialog(janela, "A Solicitação não pôde "
                        + "ser apropriadamente atualizada, talvez ja tenha sido atribuida a outro usuário."
                        + "\nAtualize o feed, ou procure pela Solicitação usando o ID da mesma.",
                        "Falha na Interação", JOptionPane.ERROR_MESSAGE);
            }

            conexao.desconectar();
        };
    }

    public void atribuir(JLabel labelEncarregado, Solicitacao solicitacao, JButton este) throws SQLException {
        //0 = YES, 1 = NO
        int permit = JOptionPane.showConfirmDialog(janela, "Uma vez atribuida a solicitação,"
                + " o cliente (solicitante) "
                + "\nreceberá uma notificação desta atividade. "
                + "\nDeseja continuar?",
                "Atribuir Solicitação", JOptionPane.YES_NO_OPTION);

        if (permit == 0) {

            //Atualiza o encarregado na base de dados usando o id do usuario logado e da solicitacao
            conexao.execute("UPDATE solicitacao SET encarregado = " + usuario.id
                    + " WHERE id = " + solicitacao.getId() + " AND encarregado = 0;");

            //Busca o usuario pelo id, e atribui seu nome na solicitacao e na interface gráfica
            ResultSet rs = conexao.executeQuery("SELECT usuario FROM usuarios WHERE id = " + usuario.id + ";");
            rs.next();
            String nomeUsuario = rs.getString("usuario");
            solicitacao.setEncarregado(usuario.id);
            labelEncarregado.setText("Encarregado: " + nomeUsuario);
            este.setText("Desatribuir");

            JOptionPane.showMessageDialog(janela, "Solicitação Atribuida. "
                    + "\nEsta solicitação ainda pode ser vista por todos nesta listagem,"
                    + " mas agora você terá acesso a ela através do menu Serviços > Meus Serviços.",
                    "Solicitação Atribuida", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void desatribuir(JLabel labelEncarregado, Solicitacao solicitacao, JButton este) throws SQLException {

        //0 = YES, 1 = NO
        int permit = JOptionPane.showConfirmDialog(janela, "Uma vez desatribuida a solicitação,"
                + " o cliente (solicitante) "
                + "\nNÃO RECEBERÁ uma notificação desta atividade. "
                + "\nDeseja continuar?",
                "Desatribuir Solicitação", JOptionPane.YES_NO_OPTION);

        if (permit == 0) {
            //Atualiza o encarregado para 0 na base de dados.
            conexao.execute("UPDATE solicitacao SET encarregado = 0 WHERE id = '" + solicitacao.getId() + "'"
                    + " AND encarregado = '" + solicitacao.getEncarregado() + "';");

            solicitacao.setEncarregado(0);
            labelEncarregado.setText("Encarregado: ");
            este.setText("Atribuir");

            JOptionPane.showMessageDialog(janela, "Solicitação Desatribuida. "
                    + "\nVocê não verá mais esta solicitação em seus Serviços.",
                    "Solicitação Desatribuida", JOptionPane.PLAIN_MESSAGE);
        }

    }

    public JPanel novoPainel(Solicitacao solicitacao) throws SQLException {

        //Recupera o nome de usuario do encarregado
        String encarregadoNome = "Encarregado: ";

        if (solicitacao.getEncarregado() != 0) {
            ResultSet rsUser = conexao.executeQuery("SELECT usuario FROM usuarios WHERE id = "
                    + solicitacao.getEncarregado() + ";");
            rsUser.next();
            String nomeDeUsuario = rsUser.getString("usuario");

            encarregadoNome = "Encarregado: " + nomeDeUsuario;
        }

        //Recupera o nome de usuario do solicitante
        String solicitanteNome = "Solicitante: ";

        if (solicitacao.getSolicitante() != 0) {
            ResultSet rsUser = conexao.executeQuery("SELECT usuario FROM usuarios WHERE id = "
                    + solicitacao.getSolicitante() + ";");
            rsUser.next();
            String nomeDeUsuario = rsUser.getString("usuario");

            solicitanteNome = "Solicitante: " + nomeDeUsuario;
        }

        //Instanciação
        JPanel painel0 = new JPanel(new BorderLayout(10, 2));

        JPanel painel = new JPanel(new GridLayout(0, 4));
        JLabel lbIdent = new JLabel("Identificação: " + solicitacao.getId());
        JLabel lbEncarregado = new JLabel(encarregadoNome);
        JLabel lbInicio = new JLabel("Inicio: " + String.valueOf(solicitacao.dateToString(solicitacao.getInicio())));
        JLabel lbFim = new JLabel("Fim: " + String.valueOf(solicitacao.dateToString(solicitacao.getFim())));
        JLabel lbSolicitante = new JLabel(solicitanteNome);
        JLabel lbEmbarcacao = new JLabel("Embarcação: " + solicitacao.getEmbarcacao());
        JLabel lbPorto = new JLabel("Porto: " + solicitacao.getLocal());

        JPanel painel2 = new JPanel(new GridLayout(0, 1));

        JPanel painelBt = new JPanel(new FlowLayout());
        JButton btAceitar = new JButton("Atribuir");
        JButton btRecusar = new JButton("Recusar");

        //Formatação
        painel0.setBackground(Color.GRAY);
        painel0.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Funcionalidade
        btAceitar.addActionListener(aceitarSolicitacao(lbEncarregado, solicitacao, btAceitar));

        //Exibição
        painel0.add(painel, BorderLayout.NORTH);
        painel.add(lbIdent);
        painel.add(lbEncarregado);
        painel.add(lbInicio);
        painel.add(lbFim);
        painel.add(lbSolicitante);
        painel.add(lbEmbarcacao);
        painel.add(lbPorto);

        painel2.add(new JLabel("<html><p style=\"width:" + 700 + "px\">Câmeras: "
                + solicitacao.getCameras()
                + "</p></html>"));
        painel2.add(new JLabel("<html><p style=\"width:" + 700 + "px\">Internet: "
                + solicitacao.getInternet()
                + "</p></html>"));
        painel2.add(new JLabel("<html><p style=\"width:" + 700 + "px\">Telefonia: "
                + solicitacao.getTelefonia()
                + "</p></html>"));
        painel2.add(new JLabel("<html><p style=\"width:" + 700 + "px\">Itens: "
                + solicitacao.getItemSimples()
                + "</p></html>"));
        painel2.add(new JLabel("<html><p style=\"width:" + 700 + "px\">Urgência: "
                + solicitacao.getItemUrgente()
                + "</p></html>"));

        painelBt.add(btAceitar);
        painelBt.add(btRecusar);

        painel0.add(painel2, BorderLayout.CENTER);
        painel0.add(painelBt, BorderLayout.SOUTH);

        //Desativa os botoes caso já tenha encarregado, e  não seja o usuário
        if ((solicitacao.getEncarregado() != usuario.id) && (solicitacao.getEncarregado() != 0)) {
            btAceitar.setEnabled(false);
            btRecusar.setEnabled(false);
        }
        //Caso a solicitação ja seja do usuário, o botão diz para desatribui-la
        if (solicitacao.getEncarregado() == usuario.id) {
            btAceitar.setText("Desatribuir");
        }

        return painel0;
    }

    private void alimentarPainel(JPanel painel, String query) {
        //Painel Meus Serviços
        conexao.conectar();

        painel.setLayout(new GridLayout(0, 1));

        ResultSet rs = conexao.executeQuery(query);

        try {
            while (rs.next()) {
                Solicitacao novaSolicitacao = new Solicitacao(
                        rs.getInt("id"),
                        rs.getInt("encarregado"),
                        rs.getString("inicio"),
                        rs.getString("fim"),
                        rs.getInt("solicitante"),
                        rs.getString("navio"),
                        rs.getString("porto"),
                        rs.getString("cameras"),
                        rs.getString("internet"),
                        rs.getString("telefonia"),
                        rs.getString("itemSimples"),
                        rs.getString("itemUrgente"));
                painel.add(novoPainel(novaSolicitacao));
            }

        } catch (SQLException e) {
            System.err.println("Exceção em Principal.Principal(), while resultset. 2");
        }

        conexao.desconectar();
    }

    private ActionListener abrirMeusServicos() {
        return (ActionEvent e) -> {
            if (menuServicosMeusServicos.getText().equals("Meus Serviços")) {
                //Limpa o painel
                painelMeusServicos.removeAll();

                //Busca as Solicitações correspondentes para alimentar o painel
                alimentarPainel(painelMeusServicos, "SELECT * FROM solicitacao WHERE encarregado = " + usuario.id + ";");

                scrollPane.setViewportView(painelMeusServicos);
                menuServicosMeusServicos.setText("Todas as Solicitações");
                menuServicosMinhasSolicitacoes.setText("Minhas Solicitações");
            } else {
                scrollPane.setViewportView(painelSolicitacoes);
                menuServicosMeusServicos.setText("Meus Serviços");
            }
        };
    }

    private ActionListener abrirMinhasSolicitacoes() {
        return (ActionEvent e) -> {
            if (menuServicosMinhasSolicitacoes.getText().equals("Minhas Solicitações")) {
                //Limpa o painel
                painelMinhasSolicitacoes.removeAll();

                //Busca as Solicitações correspondentes para alimentar o painel
                alimentarPainel(painelMinhasSolicitacoes, "SELECT * FROM solicitacao WHERE solicitante = " + usuario.id + ";");
                scrollPane.setViewportView(painelMinhasSolicitacoes);
                menuServicosMinhasSolicitacoes.setText("Todas as Solicitações");
                menuServicosMeusServicos.setText("Meus Serviços");
            } else {
                scrollPane.setViewportView(painelSolicitacoes);
                menuServicosMinhasSolicitacoes.setText("Minhas Solicitações");
            }
        };
    }

}
