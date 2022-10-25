package Solicitacoes;

import Cadastro.Administrador;
import Cadastro.Embarcacao;
import Cadastro.Janela;
import Cadastro.JanelaCadastrarEmbarcacoes;
import Cadastro.JanelaCadastrarEquipamentos;
import Cadastro.JanelaCadastrarPortos;
import Cadastro.JanelaCadastrarServicos;
import Cadastro.JanelaCadastrarUsuarios;
import Cadastro.Porto;
import Cadastro.Tecnico;
import Cadastro.Usuario;
import Model.Connection;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Daniel
 */
public final class JanelaTodasAsSolicitacoes implements Janela {

    //Declaração da Conexão
    public Connection conexao;
    //Declaração de Usuário
    public Usuario usuario;
    
    //Declaracao de Auxiliar
    public Solicitacao solicitacao;

    //Declaração da Interface Gráfica
    public JFrame janela;
    public JMenuBar barraMenu;
    public JMenu menuServicos, menuCadastrar;
    public JMenuItem menuServicosSolicitar, menuServicosTodasAsSolicitacoes, menuServicosMinhasSolicitacoes,
            menuServicosMeusServicos, menuCadastrarUsuario, menuCadastrarEmbarcacao, menuCadastrarPorto, menuCadastrarServico, 
            menuCadastrarEquipamento;
    public JPanel painelUsuario, painelSolicitacoes, painelMeusServicos, painelMinhasSolicitacoes, painelPaginador;
    public TitledBorder painelSolicitacoesBorda, painelMeusServicosBorda, painelMinhasSolicitacoesBorda;
    public JScrollPane scrollPane;
    public JLabel lbNome, lbId, lbUsuario, lbCargo;
    public JButton btAtualizar;

    public JanelaTodasAsSolicitacoes(Usuario usuario) {
        //Construção de Conexão
        conexao = new Connection();

        //Construção de Usuario (Já criado na janela anterior)
        this.usuario = usuario;
        
        //Construção de Auxiliar;
        this.solicitacao = new Solicitacao(0, 0, "22/01/2022", "23/02/2023", 0, "0", "", "", "", "");

        //Formatação da Interface Gráfica
        janela = new JFrame("CSID - Controle de Serviço de Infraestrutura de Docagem");

        //Menu
        barraMenu = new JMenuBar();
        menuServicos = new JMenu("Serviços");
        menuCadastrar = new JMenu("Cadastros");
        menuServicosSolicitar = new JMenuItem("Nova Solicitação");
        menuServicosTodasAsSolicitacoes = new JMenuItem("Todas as Solicitações");
        menuServicosMinhasSolicitacoes = new JMenuItem("Minhas Solicitações");
        menuServicosMeusServicos = new JMenuItem("Meus Serviços");
        menuCadastrarUsuario = new JMenuItem("Cadastrar Usuário");
        menuCadastrarEmbarcacao = new JMenuItem("Cadastrar Embarcação");
        menuCadastrarPorto = new JMenuItem("Cadastrar Porto");
        menuCadastrarServico = new JMenuItem("Cadastrar Serviço");
        menuCadastrarEquipamento = new JMenuItem("Cadastrar Equipamento");

        //Painel Usuario
        scrollPane = new JScrollPane();
        scrollPane.getVerticalScrollBar().setUnitIncrement(13);
        painelUsuario = new JPanel();
        painelSolicitacoes = new JPanel(new GridLayout(0,1));
        painelMinhasSolicitacoes = new JPanel(new GridLayout(0, 1));
        painelMeusServicos = new JPanel(new GridLayout(0, 1));
        painelPaginador = new JPanel();
        painelSolicitacoesBorda = BorderFactory.createTitledBorder("Todos as Solicitações");
        painelMinhasSolicitacoesBorda = BorderFactory.createTitledBorder("Minhas Solicitações");
        painelMeusServicosBorda = BorderFactory.createTitledBorder("Meus Serviços");

        lbId = new JLabel();
        lbNome = new JLabel();
        lbUsuario = new JLabel();
        lbCargo = new JLabel();
        btAtualizar = new JButton("Atualizar TUDO");
    }

    public ActionListener aceitarSolicitacao(JLabel labelEncarregado, Solicitacao solicitacao, JButton este) {
        return (ActionEvent e) -> {

            conexao.conectar();

            try {

                if (solicitacao.encarregado == 0) { //Se a solicitacao não tiver Encarregado, atribua-se
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
                + "\nNÃO RECEBERÁ uma notificação desta atividade. "
                + "\nDeseja continuar?",
                "Atribuir Solicitação", JOptionPane.YES_NO_OPTION);

        if (permit == 0) {

            //Atualiza o encarregado na base de dados usando o id do usuario logado e da solicitacao
            conexao.execute("UPDATE solicitacao SET encarregado = " + usuario.id
                    + " WHERE id = " + solicitacao.id + " AND encarregado = 0;");

            //Busca o usuario pelo id, e atribui seu nome na solicitacao e na interface gráfica
            ResultSet rs = conexao.executeQuery("SELECT usuario FROM usuarios WHERE id = " + usuario.id + ";");
            rs.next();
            String nomeUsuario = rs.getString("usuario");
            solicitacao.encarregado = usuario.id;
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
            conexao.execute("UPDATE solicitacao SET encarregado = 0 WHERE id = '" + solicitacao.id + "'"
                    + " AND encarregado = '" + solicitacao.encarregado + "';");

            solicitacao.encarregado = 0;
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

        if (solicitacao.encarregado != 0) {
            ResultSet rsUser = usuario.selectUsuarioPorId(conexao, solicitacao.encarregado);
            rsUser.next();
            String nomeDeUsuario = rsUser.getString("usuario") + "(ID: " + rsUser.getString("id") +")";

            encarregadoNome += nomeDeUsuario;
        }

        //Recupera o nome de usuario do solicitante
        String solicitanteNome = "Solicitante: ";

        if (solicitacao.solicitante != 0) {
            ResultSet rsUser = usuario.selectUsuarioPorId(conexao, solicitacao.solicitante);
            rsUser.next();
            String nomeDeUsuario = rsUser.getString("usuario") + "(ID: " + rsUser.getString("id") +")";

            solicitanteNome += nomeDeUsuario;
        }

        //Recupera o nome da Embarcação
        String embarcacaoNome = "Embarcação: ";

        if (!solicitacao.embarcacao.equals("")) {
            Embarcacao boat = new Embarcacao(solicitacao.embarcacao, "", "");
            ResultSet rsBoat = boat.selectPorId(conexao, boat);
            if(rsBoat.next()){
            embarcacaoNome += rsBoat.getString("nome") + "(ID: " + rsBoat.getString("id") +")";
            }

        }
             
        //Recupera o nome da Porto
        String portoNome = "Porto: ";

        if (!solicitacao.porto.equals("")) {
            Porto port = new Porto(solicitacao.porto, "", "", "", "");
            ResultSet rsPort = port.selectPorId(conexao, port);
            if (rsPort.next()) {
                portoNome += rsPort.getString("nome") + "(ID: " + rsPort.getString("id") + ")";
            }

        }


        //Instanciação
        JPanel painel0 = new JPanel(new BorderLayout(10, 2));

        JPanel painel = new JPanel(new GridLayout(0, 4));
        JLabel lbIdent = new JLabel("Identificação: " + solicitacao.id);
        JLabel lbEncarregado = new JLabel(encarregadoNome);
        JLabel lbInicio = new JLabel("Inicio: " + String.valueOf(solicitacao.dateToString(solicitacao.inicio)) );
        JLabel lbFim = new JLabel("Fim: " + String.valueOf(solicitacao.dateToString(solicitacao.fim)) );
        JLabel lbSolicitante = new JLabel(solicitanteNome);
        JLabel lbEmbarcacao = new JLabel(embarcacaoNome);
        JLabel lbPorto = new JLabel(portoNome);
        JTextArea taServicos = new JTextArea();
        JTextArea taEquipamentos = new JTextArea();
        JTextArea taObs = new JTextArea();
        JPanel painel2 = new JPanel(new GridLayout(0, 1));

        JPanel painelBt = new JPanel();
        JButton btAceitar = new JButton("Atribuir");
        JButton btRecusar = new JButton("Excluir");

        //Formatação
        taServicos.setLineWrap(true);
        taServicos.setEditable(false);
        taEquipamentos.setLineWrap(true);
        taEquipamentos.setEditable(false);
        taObs.setLineWrap(true);
        taObs.setEditable(false);
        
        painel0.setBackground(Color.GRAY);
        painel0.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        //Funcionalidade
        btAceitar.addActionListener(aceitarSolicitacao(lbEncarregado, solicitacao, btAceitar));
        btRecusar.addActionListener((e) -> {
            if(usuario.excluirSolicitacao(solicitacao, this)){
                painel0.setVisible(false);
                painel0.add(new JLabel("Solicitacao Excluída."));
            }
                
        });

        //Exibição
        painel0.add(painel, BorderLayout.NORTH);
        painel.add(lbIdent);
        painel.add(lbEncarregado);
        painel.add(lbInicio);
        painel.add(lbFim);
        painel.add(lbSolicitante);
        painel.add(lbEmbarcacao);
        painel.add(lbPorto);

        taServicos.setText(solicitacao.servicos);
        painel2.add(taServicos);
        taEquipamentos.setText(solicitacao.equipamentos);
        painel2.add(taEquipamentos);
        taObs.setText(solicitacao.obs);
        painel2.add(taObs);

        painelBt.add(btAceitar);
        painelBt.add(btRecusar);

        painel0.add(painel2, BorderLayout.CENTER);
        painel0.add(painelBt, BorderLayout.SOUTH);

        //Desativa os botoes caso já tenha encarregado, e  não seja o usuário
        if ((solicitacao.encarregado != usuario.id) && (solicitacao.encarregado != 0)) {
            btAceitar.setEnabled(false);
            btRecusar.setEnabled(false);
        }
        //Caso a solicitação ja seja do usuário, o botão diz para desatribui-la
        if (solicitacao.encarregado == usuario.id) {
            btAceitar.setText("Desatribuir");
        }
        //Se usuários encarregados forem excluidos a solicitação estará disponível novamente
        if(encarregadoNome.equals("Encarregado: ")){
            btAceitar.setEnabled(true);
            btRecusar.setEnabled(true);
        }
        //Se o usuário for Comandante,não poderá aceitar.
        if(usuario.cargo.equals("Com")){
            btAceitar.setEnabled(false);
            //E se não for dono da solicitacao, não poderá exclui-la também.
            if(solicitacao.solicitante != usuario.id){
                btRecusar.setEnabled(false);
            }
        }

        return painel0;
    }

    
    @Override
    public boolean exibirInterfaceComandante() {
        //Formatação de interface gráfica
        janela.setSize(1024, 720);
        janela.setLocationRelativeTo(null);
        janela.setLayout(new BorderLayout());
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        //Barra de Menu
        //Menu Serviços
        barraMenu.add(menuServicos);
        //Item Todas as Solicitacoes
        menuServicos.add(menuServicosTodasAsSolicitacoes);
        menuServicosTodasAsSolicitacoes.addActionListener((e) -> {
                usuario.verSolicitacoes(this);
        });
        //Item Minhas Solicitacoes
        menuServicos.add(menuServicosMinhasSolicitacoes);
        menuServicosMinhasSolicitacoes.addActionListener((e) -> {
                usuario.verMinhasSolicitacoes(this);
        });
        //Item Nova Solicitacao
        menuServicos.add(menuServicosSolicitar);
        menuServicosSolicitar.addActionListener((ActionEvent e) -> {
            usuario.exibir(new JanelaSolicitar(usuario));
        });
        //Menu Cadastrar 
        barraMenu.add(menuCadastrar);
        //Item Cadastrar Usuario
        menuCadastrar.add(menuCadastrarUsuario);
        menuCadastrarUsuario.addActionListener((ActionEvent e) -> {
            usuario.exibir(new JanelaCadastrarUsuarios(usuario));
        });
        //Item Cadastrar Embarcacao
        menuCadastrar.add(menuCadastrarEmbarcacao);
        menuCadastrarEmbarcacao.addActionListener((ActionEvent e) -> {
            usuario.exibir(new JanelaCadastrarEmbarcacoes(usuario));
        });
        //Item Cadastrar Porto
        menuCadastrar.add(menuCadastrarPorto);
        menuCadastrarPorto.addActionListener((ActionEvent e) -> {
            usuario.exibir(new JanelaCadastrarPortos(usuario));
        });
        //Item Cadastrar Servico
        menuCadastrar.add(menuCadastrarServico);
        menuCadastrarServico.addActionListener((ActionEvent e) -> {
            usuario.exibir(new JanelaCadastrarServicos(usuario));
        });
        //Item Cadastrar Equipamento
        menuCadastrar.add(menuCadastrarEquipamento);
        menuCadastrarEquipamento.addActionListener((ActionEvent e) -> {
            usuario.exibir(new JanelaCadastrarEquipamentos(usuario));
        });

        lbId.setText("Meu ID: " + String.valueOf(usuario.id));
        lbNome.setText("Nome: " + usuario.nome + " " + usuario.sobrenome);
        lbUsuario.setText("Nome de Usuário: " + usuario.usuario);
        lbCargo.setText("Cargo: " + usuario.cargo);

        painelUsuario.setBackground(Color.LIGHT_GRAY);
        painelUsuario.setLayout(new GridLayout(0, 5));
        painelUsuario.add(lbId);
        painelUsuario.add(lbNome);
        painelUsuario.add(lbUsuario);
        painelUsuario.add(lbCargo);
        painelUsuario.add(btAtualizar);
        
        btAtualizar.addActionListener((e) -> {
            usuario.verSolicitacoes(this);
        });
        
        

        //Painel Solicitações
        painelSolicitacoesBorda.setTitleJustification(TitledBorder.CENTER);
        painelSolicitacoes.setBorder(painelSolicitacoesBorda);

        scrollPane.setViewportView(painelSolicitacoes);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

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

        //Exibição
        janela.setJMenuBar(barraMenu);
        janela.add(painelUsuario, BorderLayout.NORTH);
        janela.add(scrollPane, BorderLayout.CENTER);
        janela.add(painelPaginador, BorderLayout.SOUTH);

        janela.setVisible(true);
        return true;
    }

    @Override
    public boolean exibirInterfaceTecnico() {
        //Barra de Menu
        //Menu Serviços
        menuServicos.add(menuServicosMeusServicos);
        menuServicosMeusServicos.addActionListener((e) -> {
                Tecnico.verMeusServicos(this);
        });
        exibirInterfaceComandante();
        return true;
    }

    @Override
    public boolean exibirInterfaceAdministrador() {

        exibirInterfaceTecnico();
        return true;
    }

}
