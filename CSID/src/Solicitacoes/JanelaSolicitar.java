package Solicitacoes;

import Cadastro.Embarcacao;
import Cadastro.Equipamento;
import Cadastro.Janela;
import Cadastro.Porto;
import Cadastro.Servico;
import Cadastro.Usuario;
import Model.Connection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Daniel
 */
public final class JanelaSolicitar implements Janela {

    //Declaração de Conexão
    public Connection conexao;
    //Declaração de Usuario
    public Usuario usuario;
    public Embarcacao embarcacao;
    public Porto porto;
    public Servico servico;
    public Equipamento equipamento;
    
    //Informações usadas
    public ArrayList<Embarcacao> embarcacoes;
    public ArrayList<Porto> portos;
    public ArrayList<String[]> servicos, meusServicos;
    public ArrayList<String[]> equipamentos, meusEquipamentos;

    //Declaração da Interface Gráfica
    public JFrame janela, janelaDeOpcoesServicos, janelaDeOpcoesEquipamentos;
    public JPanel painel, painel2, painel3, painelDeServicos, painelDeServicosDescricao, 
            painelDeEquipamentos, painelDeEquipamentosDescricao;
    public JLabel lbInicio, lbFim, lbEmbarcacao, lbPorto, lbServicos, lbObs, 
            lbEquipamentos;
    public JTextArea taListarEquipamentos, taListarServicos;
    public JFormattedTextField tfInicio, tfFim;
    public JComboBox comboEmbarcacao, comboPorto;
    public JTextField tfObs;
    public JButton btAddServico, enviar;
    public JScrollPane scrollServicos, scrollEquipamentos;
    public JButton btAddEquipamento;
    public MaskFormatter mf, mf2;

    public JanelaSolicitar(Usuario usuario) {

        //Construção de Conexão
        conexao = new Connection();
        conexao.conectar();

        //Construção de Usuario (Já criado na janela anterior)
        this.usuario = usuario;
        embarcacao = new Embarcacao("", "", "");
        porto = new Porto("", "", "","","");
        servico = new Servico("", "", "");
        equipamento = new Equipamento("", "", "");
        
        embarcacoes = new ArrayList<>();
        portos = new ArrayList<>();
        servicos = new ArrayList<>();
        equipamentos = new ArrayList<>();
        meusServicos = new ArrayList<>();
        meusEquipamentos = new ArrayList<>();


        //Inicia Janela de Serviços
        janelaDeOpcoesServicos = new JFrame("Adicionar Serviços");
        scrollServicos = new JScrollPane();
        painelDeServicos = new JPanel(new GridLayout(0,1));
        painelDeServicosDescricao = new JPanel( new GridLayout(0,1));
        scrollServicos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //Inicia Janela de Equipamentos
        janelaDeOpcoesEquipamentos = new JFrame("Adicionar Equipamentos");
        scrollEquipamentos = new JScrollPane();
        painelDeEquipamentos = new JPanel(new GridLayout(0,1));
        painelDeEquipamentosDescricao = new JPanel( new GridLayout(0,1));
        scrollEquipamentos.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        //Inicia Janela Solicitar
        janela = new JFrame("Solicitar Novo Serviço");

        painel = new JPanel();
        painel2 = new JPanel();
        painel3 = new JPanel();

        painel = new JPanel();

        painel2 = new JPanel(new GridLayout(0, 2));

        painel3 = new JPanel(new GridLayout(0, 1));

        lbInicio = new JLabel("Inicio de Docagem");
        tfInicio = new JFormattedTextField();
        tfInicio.addKeyListener(dataListener(tfInicio));

        lbFim = new JLabel("Fim de Docagem");
        tfFim = new JFormattedTextField();
        tfFim.addKeyListener(dataListener(tfFim));
        
        try {
            mf = new MaskFormatter("##/##/####");
            mf2 = new MaskFormatter("##/##/####");
            mf.install(tfInicio);
            mf2.install(tfFim);
        } catch (ParseException ex) {
            System.err.println("Exceção em JanelaSolicitar.JanelaSolicitar(). MaskFormatter de datas. " + ex);
        }

        
        lbEmbarcacao = new JLabel("Embarcação:");
        comboEmbarcacao = new JComboBox();
        buscarEmbarcacoes();

        lbPorto = new JLabel("Porto:");
        comboPorto = new JComboBox();
        buscarPortos();

        buscarServicos();
        criarJanelaServicos();
        lbServicos = new JLabel("Serviços:");
        btAddServico = new JButton("Adicionar Serviço");
        btAddServico.addActionListener((e) -> { janelaDeOpcoesServicos.setVisible(true); });
        taListarServicos = new JTextArea();
        taListarServicos.setLineWrap(true);
        taListarServicos.setEditable(false);

        buscarEquipamentos();
        criarJanelaEquipamentos();
        lbEquipamentos = new JLabel("Equipamentos:");
        btAddEquipamento = new JButton("Adicionar Equipamentos");
        btAddEquipamento.addActionListener((e) -> { janelaDeOpcoesEquipamentos.setVisible(true); });
        taListarEquipamentos = new JTextArea();
        taListarEquipamentos.setLineWrap(true);
        taListarEquipamentos.setEditable(false);

        
        lbObs = new JLabel("Observações:");
        tfObs = new JTextField();
        tfObs.addKeyListener(listener(tfObs, 200));
        tfObs.setBorder(new BasicBorders.FieldBorder(Color.gray, Color.white, Color.gray, Color.white));


        enviar = new JButton("Solicitar");
        enviar.addActionListener(solicitar());


        conexao.desconectar();
    }

    
    public void buscarEmbarcacoes() {
        //Busca Embarcações
        ResultSet rs = embarcacao.select(conexao, embarcacao);
        try {
            //Se houver ao menos um adiciona cada item ao ArrayList e ComboBox correspondente
            if (rs.next()) {
                do {
                    embarcacoes.add(new Embarcacao(rs.getString("id"), rs.getString("nome"), rs.getString("numero")) );
                    comboEmbarcacao.addItem("ID: " + rs.getString("id") + " | " + embarcacoes.get(rs.getRow()-1).nome);
                } while (rs.next());
            }
        } catch (SQLException ex) {
            System.err.println("Exceção em JanelaSolicitar.buscarEmbarcacoes()" + ex);
        }
    }

    public void buscarPortos() {
        //Busca Portos
        ResultSet rs = porto.select(conexao, porto);
        try {
            //Se houver ao menos um adiciona cada item ao ArrayList e ComboBox correspondente
            if (rs.next()) {
                do {
                    portos.add(new Porto(rs.getString("id"), rs.getString("telefone"),
                            rs.getString("nome"), rs.getString("endereco"), rs.getString("email")) );
                    comboPorto.addItem("ID: " + rs.getString("id") + " | " + portos.get(rs.getRow()-1).nome );
                } while (rs.next());
            }
        } catch (SQLException ex) {
            System.err.println("Exceção em JanelaSolicitar.buscarPortos()" + ex);
        }
    }
    
    public void buscarServicos() {
        //Busca Serviços
        ResultSet rs = servico.select(conexao, servico);
        try {
            //Se houver ao menos um adiciona cada item ao ArrayList
            if (rs.next()) {
                do {
                    String[] i = {rs.getString("id"), rs.getString("nome"), rs.getString("descricao")};
                    servicos.add(i);
                } while (rs.next());
            }
        } catch (SQLException ex) {
            System.err.println("Exceção em JanelaSolicitar.buscarPortos()" + ex);
        }
    }

    private void buscarEquipamentos() {
        //Busca Serviços
        ResultSet rs = equipamento.select(conexao, equipamento);
        try {
            //Se houver ao menos um adiciona cada item ao ArrayList
            if (rs.next()) {
                do {
                    String[] s = {rs.getString("id"), rs.getString("nome"), rs.getString("quantidade")};
                    equipamentos.add(s);
                } while (rs.next());
            }
        } catch (SQLException ex) {
            System.err.println("Exceção em JanelaSolicitar.buscarEquipamentos()" + ex);
        }
    }
    
    public void criarJanelaServicos(){
        janelaDeOpcoesServicos.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        janelaDeOpcoesServicos.setLayout(new GridLayout(0,1));
        janelaDeOpcoesServicos.setSize(520, 500);
        janelaDeOpcoesServicos.setLocationRelativeTo(null);
        janelaDeOpcoesServicos.setResizable(false);
        
        for (String[] service : servicos) {
            JButton btServico = new JButton(service[1]);//nome
            JButton btAdicionarServico = new JButton("Adicionar");
            painelDeServicos.add(btServico);
            btServico.addActionListener((e) -> {
                painelDeServicosDescricao.setVisible(false);

                painelDeServicosDescricao.removeAll();
                painelDeServicosDescricao.add(new JLabel("id: " + service[0])); //id
                painelDeServicosDescricao.add(new JLabel(service[1]));//nome
                painelDeServicosDescricao.add(new JLabel(service[2]));//descricao
                painelDeServicosDescricao.add(btAdicionarServico);
                
                painelDeServicosDescricao.setVisible(true);
            });
               btAdicionarServico.addActionListener((ev) -> {
                adicionarRemoverItem(service, meusServicos, btAdicionarServico, taListarServicos.getText());
                
                String s = "";
                for(String[] servico : meusServicos){
                    s += "id:-"+servico[0] + "- " + servico[1] + "_ ";
                }
                taListarServicos.setText( s );
            });

            scrollServicos.setViewportView(painelDeServicos);
            
            janelaDeOpcoesServicos.add(scrollServicos);
            janelaDeOpcoesServicos.add(painelDeServicosDescricao);
        }
    }
    
    public void criarJanelaEquipamentos(){
        janelaDeOpcoesEquipamentos.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        janelaDeOpcoesEquipamentos.setLayout(new GridLayout(0,1));
        janelaDeOpcoesEquipamentos.setSize(520, 500);
        janelaDeOpcoesEquipamentos.setLocationRelativeTo(null);
        janelaDeOpcoesEquipamentos.setResizable(false);
        
        for (String[] equipment : equipamentos) {
            JButton btEquipamento = new JButton(equipment[1]);//nome
            JButton btAdicionarEquipamento = new JButton("Adicionar");
            JComboBox cbQuantidade = new JComboBox();
            
            for (int i = 1; i <= Integer.parseInt(equipment[2]); i++) {
                cbQuantidade.addItem(i);
            }
            
            painelDeEquipamentos.add(btEquipamento);
            btEquipamento.addActionListener((e) -> {
                painelDeEquipamentosDescricao.setVisible(false);

                painelDeEquipamentosDescricao.removeAll();
                painelDeEquipamentosDescricao.add(new JLabel("id: " + equipment[0]));//id
                painelDeEquipamentosDescricao.add(new JLabel(equipment[1]));//nome
                painelDeEquipamentosDescricao.add(cbQuantidade);//quantidade
                painelDeEquipamentosDescricao.add(btAdicionarEquipamento);
                
                painelDeEquipamentosDescricao.setVisible(true);
            });
               btAdicionarEquipamento.addActionListener((ev) -> {
                   equipment[2] = String.valueOf(cbQuantidade.getSelectedItem());
                adicionarRemoverItem(equipment, meusEquipamentos, btAdicionarEquipamento, taListarEquipamentos.getText());
                
                String s = "";
                for(String[] equipamento : meusEquipamentos){
                    s += "id:-" + equipamento[0] + "-" + equipamento[1] + "-" + equipamento[2] + "_ ";
                }
                taListarEquipamentos.setText( s );
            });

            scrollEquipamentos.setViewportView(painelDeEquipamentos);
            
            janelaDeOpcoesEquipamentos.add(scrollEquipamentos);
            janelaDeOpcoesEquipamentos.add(painelDeEquipamentosDescricao);
        }
    }

    public void adicionarRemoverItem(Object item, ArrayList array, JButton botao, String texto) {
            
        if(texto.length() < 150){
            if (array.contains(item)) {
                array.remove(item);
                botao.setText("Adicionar");
            } else {
                array.add(item);
                botao.setText("Remover");
            }
        } else{
            JOptionPane.showMessageDialog(painel, "Limite Atingido.\nRemova outros adicionados para acrescentar novos.");
        }

    }

    //Cria o objeto solicitacao e tomará ações com ele
    public ActionListener solicitar() {
        return (ActionEvent e) -> {
            
            conexao.conectar();
            
            //Solicita preenchimento caso as datas estejam vazias
            if(tfInicio.getText().equals("") || tfFim.getText().equals("") || tfInicio.getText().equals("  /  /    ") || 
                    tfFim.getText().equals("  /  /    ")){
                JOptionPane.showMessageDialog(janela, "Preencha devidamente ao menos as datas!", "Atenção",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            //Recupera a embarcação selecionada
                Embarcacao embarcacaoFinal = embarcacoes.get(comboEmbarcacao.getSelectedIndex());
                
            //Recupera o Porto selecionado
                Porto portoFinal = portos.get(comboPorto.getSelectedIndex());
                
                
            //Recupera os Servicos
            String str = taListarServicos.getText();
            
            int i = str.split("_").length;
            String[][] listaDeServicos = new String[i][3];
            i = 0;
            for (String str2 : str.split("_")) {//separa objetos
                int j = 0;
                String[] objeto = new String[3];
                for (String str3 : str2.split("-")) {//separa os campos
                    objeto[j] = str3;
                    j++;
                }
                listaDeServicos[i] = objeto;
                i++;
            }
            
            for(String[] s : listaDeServicos){
            System.err.println(s[1] + " X " + s[2]);
            }

            
            //Recupera os equipamentos
            String strEq = taListarEquipamentos.getText();
            
            int iEq = strEq.split("_").length;
            String[][] listaDeEquipamentos = new String[iEq][5];//Lista de Equipamentos é uma matriz de X por 5
            iEq = 0;//primeiro objeto
            for (String str2 : strEq.split("_")) {//separa objetos
                int j = 0;//primeiro campo
                String[] objeto = new String[5];//Cada objeto da matriz terá 5 campos
                for (String str3 : str2.split("-")) {//separa os campos
                    objeto[j] = str3;//preenche os campos
                    j++;//próximo campo
                }
                listaDeEquipamentos[iEq] = objeto;//acopla o objeto à matriz
                iEq++;//Próximo objeto
            }
            
            
            //Faz a remoção dos Equipamentos
            for(String[] s : listaDeEquipamentos){//Para cada objeto na lista de equipaments
            Equipamento equipamentoSelecionado = new Equipamento(s[1], s[2], s[3]);//instancia-se o equipamento
            
                try {
                    //Busca exatamente este na base de dados
                    ResultSet rs = equipamentoSelecionado.selectUnico(conexao, equipamentoSelecionado);
                    if (rs.next()) {//Se houver
                        do {
                            //diminui a quantidade pedida da disponível e diminui, ou exclui o item, se conveniente
                            int novaQuantidade = Integer.valueOf(rs.getString("quantidade")) - Integer.valueOf(s[3]);
                            
                            Equipamento novoEquipamento = new Equipamento(rs.getString("id"), rs.getString("nome"),
                                    String.valueOf(novaQuantidade));
                            
                            if (novaQuantidade <= 0) {
                                novoEquipamento.delete(conexao, novoEquipamento);
                            } else {
                                novoEquipamento.alter(conexao, novoEquipamento);
                            }
                            
                            
                        } while (rs.next());
                    }
                } catch (SQLException ex) {
                    System.err.println("JanelaSolicitar.enviar() " + ex);
                }
                
            }
            
            
            
            //Cria e Insere-a na base
            Solicitacao solicitacao = new Solicitacao(0, 0, tfInicio.getText(), tfFim.getText(), 
                    usuario.id, embarcacaoFinal.id, portoFinal.id, taListarServicos.getText(),
                    taListarEquipamentos.getText(), tfObs.getText());
            
            try{
                solicitacao.insert(conexao, solicitacao);
            } catch(Exception ex) {
                System.err.println("FALHA NO JANELASOLICITAR.SOLICITAR()" + ex);
                return;
            }
            
            JOptionPane.showMessageDialog(painel, "Solicitação de Serviço criada.\nAcompanhe-a no menu Solicitação > "
                    + "Minhas Solicitações, para saber seu status.", "Sucesso", JOptionPane.PLAIN_MESSAGE);
            this.janela.dispose();
            
            conexao.desconectar();
        };
    }

    public static KeyListener listener(JTextField tf, int tamanho) {
        return new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                
                if (Character.valueOf(e.getKeyChar()).toString().equals("-")
                        || Character.valueOf(e.getKeyChar()).toString().equals("_")) {
                    e.consume();
                }
                if (tf.getText().length() >= tamanho) {
                    e.consume();
                    String texto = tf.getText().substring(0, tamanho);
                    tf.setText(texto);
                }
                
            }
        };
    }

    public static KeyAdapter dataListener(JTextField tf){
        return new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode()<96||e.getKeyCode()>105) {
                    e.consume();
                }
            }
        };
    }
    
    @Override
    public boolean exibirInterfaceComandante() {
        janela.setVisible(false);
        //Exibe todo mundo
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janela.setSize(500, 700);
        janela.setLocationRelativeTo(null);
        janela.setResizable(false);

        painel.setLayout(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Exibição da Interface Gráfica
        painel.add(painel2, BorderLayout.NORTH);
        painel.add(painel3, BorderLayout.CENTER);
        

        painel2.add(lbInicio);
        painel2.add(tfInicio);

        painel2.add(lbFim);
        painel2.add(tfFim);
        

        painel2.add(lbEmbarcacao);
        painel2.add(comboEmbarcacao);

        painel2.add(lbPorto);
        painel2.add(comboPorto);

        painel3.add(lbServicos);
        painel3.add(btAddServico);
        painel3.add(taListarServicos);

        painel3.add(lbEquipamentos);
        painel3.add(btAddEquipamento);
        painel3.add(taListarEquipamentos);

        painel3.add(lbObs);
        painel3.add(tfObs);

        painel.add(enviar, BorderLayout.SOUTH);

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
        return true;
    }

}
