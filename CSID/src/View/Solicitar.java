package View;

import Classes.Solicitacao;
import Cadastro.Usuario;
import Model.Connection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Daniel
 */
public final class Solicitar {

    //Declaração de Conexão
    Connection conexao;
    //Declaração de Usuario
    Usuario usuario;

    //Declaração da Interface Gráfica
    JFrame frame = new JFrame("Solicitar Novo Serviço");
    JPanel painel = new JPanel();
    JPanel painel2 = new JPanel();
    JPanel painel3 = new JPanel();
    JLabel title = new JLabel("Agendameno de Docagem");
    JLabel lbInicio = new JLabel("Inicio de Docagem");
    JFormattedTextField tfInicio = new JFormattedTextField();
    JLabel lbFim = new JLabel("Fim de Docagem");
    JFormattedTextField tfFim = new JFormattedTextField();
    JLabel lbEmbarcacao = new JLabel("Embarcação:");
    JComboBox comboEmbarcacao = new JComboBox();
    JLabel lbPorto = new JLabel("Local:");
    JComboBox comboPorto = new JComboBox();
    JSeparator separador = new JSeparator();
    JLabel lbServicos = new JLabel("Serviços:");
    JLabel lbCamera = new JLabel("Câmeras:");
    JCheckBox cbCamera1 = new JCheckBox("Checagem");
    JCheckBox cbCamera2 = new JCheckBox("Alocação");
    JTextField tfCamera = new JTextField("Locais");
    JLabel lbInternet = new JLabel("Internet:");
    JCheckBox cbInternet1 = new JCheckBox("Checagem de Pontos");
    JCheckBox cbInternet2 = new JCheckBox("Checagem de Rede");
    JCheckBox cbInternet3 = new JCheckBox("Checagem Antena de Rádio");
    JLabel lbTelefone = new JLabel("Telefonia:");
    JCheckBox cbTelefone1 = new JCheckBox("Checagem de Telefone");
    JCheckBox cbTelefone2 = new JCheckBox("Solicitação Telefone");
    JTextField tfTelefone = new JTextField("Locais");
    JLabel lbSolicitacao1 = new JLabel("Solicite itens SEM urgência:");
    JTextArea taSolicitacao1 = new JTextArea();
    JLabel lbSolicitacao2 = new JLabel("Solicite itens COM urgência:");
    JTextArea taSolicitacao2 = new JTextArea();
    JButton enviar = new JButton("Solicitar");

    public Solicitar() {

        //Construção de Conexão
        conexao = new Connection();

        //Construção de Usuario (Já criado na janela anterior)
        usuario = Usuario.GetInstance(0, "", "", "", "", "");

        //Formatação da Interface Gráfica
        frame = new JFrame("CSD - Controle de Serviço de Docagem");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(100, 25, 500, 700);
        frame.setResizable(false);

        painel = new JPanel();
        painel.setLayout(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painel2 = new JPanel();
        painel2.setLayout(new GridLayout(0, 2));

        painel3 = new JPanel();
        painel3.setLayout(new GridLayout(0, 1));

        title = new JLabel("<html><h2>Agendameno de Serviço</h2>");

        lbInicio = new JLabel("Inicio de Docagem");
        tfInicio = new JFormattedTextField();

        lbFim = new JLabel("Fim de Docagem");
        tfFim = new JFormattedTextField();

        lbEmbarcacao = new JLabel("Embarcação:");
        String[] barcos = {"Barco1", "Barco2", "Barco3"};
        comboEmbarcacao = new JComboBox(barcos);

        lbPorto = new JLabel("Local:");
        String[] portos = {"Porto Mauá", "Porto Niteroi 1", "Porto Rio 1"};
        comboPorto = new JComboBox(portos);

        separador = new JSeparator();

        lbServicos = new JLabel("Serviços:");

        lbCamera = new JLabel("Câmeras:");
        cbCamera1 = new JCheckBox("Checagem");
        cbCamera2 = new JCheckBox("Alocação");
        tfCamera = new JTextField("Locais");

        lbInternet = new JLabel("Internet:");
        cbInternet1 = new JCheckBox("Checagem de Pontos");
        cbInternet2 = new JCheckBox("Checagem de Rede");
        cbInternet3 = new JCheckBox("Checagem Antena de Rádio");

        lbTelefone = new JLabel("Telefonia:");
        cbTelefone1 = new JCheckBox("Checagem de Telefone");
        cbTelefone2 = new JCheckBox("Solicitação Telefone");
        tfTelefone = new JTextField("Locais");

        lbSolicitacao1 = new JLabel("Solicite itens SEM urgência:");
        taSolicitacao1 = new JTextArea();
        taSolicitacao1.setBorder(new BasicBorders.FieldBorder(Color.gray, Color.white, Color.gray, Color.white));

        lbSolicitacao2 = new JLabel("Solicite itens COM urgência:");
        taSolicitacao2 = new JTextArea();
        taSolicitacao2.setBounds(0, 0, 20, 50);
        taSolicitacao2.setBorder(new BasicBorders.FieldBorder(Color.gray, Color.white, Color.gray, Color.white));

        enviar = new JButton("Solicitar");

        //Funcionalidades
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            MaskFormatter mf2 = new MaskFormatter("##/##/####");
            mf.install(tfInicio);
            mf2.install(tfFim);
        } catch (ParseException ex) {
            System.err.println("Exceção ao formatar campo de data. Solicitar.Solicitar() /Funcionalidades > try catch");
            Logger.getLogger(Solicitar.class.getName()).log(Level.SEVERE, null, ex);
        }

        tfCamera.setVisible(false);
        cbCamera2.addActionListener(ocultar(tfCamera));

        tfTelefone.setVisible(false);
        cbTelefone2.addActionListener(ocultar(tfTelefone));

        enviar.addActionListener(solicitar(frame,
                conexao,
                cbCamera1, cbCamera2,
                cbInternet1, cbInternet2,
                cbInternet3, cbTelefone1, cbTelefone2,
                tfCamera, tfTelefone,
                tfInicio, tfFim,
                comboEmbarcacao,
                comboPorto,
                taSolicitacao1,
                taSolicitacao2));

        //Limite de tamanho da JTextField/Area
        tfCamera.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tfCamera.getText().length() >= 80) {
                    e.consume();
                    String texto = tfCamera.getText().substring(0, 80);
                    tfCamera.setText(texto);
                }
            }
        });
        tfTelefone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (tfTelefone.getText().length() >= 80) {
                    e.consume();
                    String texto = tfTelefone.getText().substring(0, 80);
                    tfTelefone.setText(texto);
                }
            }
        });
        taSolicitacao1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (taSolicitacao1.getText().length() >= 200) {
                    e.consume();
                    String texto = taSolicitacao1.getText().substring(0, 200);
                    taSolicitacao1.setText(texto);
                }
            }
        });
        taSolicitacao2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (taSolicitacao2.getText().length() >= 200) {
                    e.consume();
                    String texto = taSolicitacao2.getText().substring(0, 200);
                    taSolicitacao2.setText(texto);
                }
            }
        });

        //Exibição da Interface Gráfica
        painel.add(painel2, BorderLayout.NORTH);
        painel.add(painel3, BorderLayout.CENTER);

        painel2.add(title);
        painel2.add(new JLabel());

        painel2.add(lbInicio);
        painel2.add(tfInicio);

        painel2.add(lbFim);
        painel2.add(tfFim);

        painel2.add(lbEmbarcacao);
        painel2.add(comboEmbarcacao);

        painel2.add(lbPorto);
        painel2.add(comboPorto);

        //painel.add(separador);
        painel3.add(lbServicos);

        painel3.add(lbCamera);
        painel3.add(cbCamera1);
        painel3.add(cbCamera2);
        painel3.add(tfCamera);

        painel3.add(lbInternet);
        painel3.add(cbInternet1);
        painel3.add(cbInternet2);
        painel3.add(cbInternet3);

        painel3.add(lbTelefone);
        painel3.add(cbTelefone1);
        painel3.add(cbTelefone2);
        painel3.add(tfTelefone);

        painel3.add(lbSolicitacao1);
        painel3.add(taSolicitacao1);

        painel3.add(lbSolicitacao2);
        painel3.add(taSolicitacao2);

        painel.add(enviar, BorderLayout.SOUTH);

        frame.add(painel);
    }

    public void setVisible(boolean valor) {
        frame.setVisible(true);
    }

    //monta o array de checkboxes
    public String montarArray(JCheckBox[] componentes) {
        int i = 0;
        String vetor = "";

        for (JCheckBox component : componentes) {
            if (component.isSelected()) {
                vetor += "/ " + component.getText();
                i++;
            }
        }

        return vetor;
    }

    //Insere o campo local no fim do array de checkboxes
    public String[] push(String[] array, String texto) {
        if (!(texto == null) && !(texto.trim().isEmpty()) && !(texto.trim().equals("Locais"))) {
            array[(Array.getLength(array) - 1)] = texto;
        }
        return array;
    }

    //Oculta o componente de local que não convem quando a checkbox está desmarcada
    public ActionListener ocultar(JTextField campo) {
        return (ActionEvent e) -> {
            campo.setVisible(!campo.isVisible());
        };
    }

    //Cria o objeto solicitacao e tomará ações com ele
    public ActionListener solicitar(
            JFrame frame,
            Connection conexao,
            JCheckBox cbCamera1, JCheckBox cbCamera2,
            JCheckBox cbInternet1, JCheckBox cbInternet2,
            JCheckBox cbInternet3, JCheckBox cbTelefone1, JCheckBox cbTelefone2,
            JTextField tfCamera, JTextField tfTelefone,
            JTextField tfInicio, JTextField tfFim,
            JComboBox comboEmbarcacao,
            JComboBox comboPorto,
            JTextArea taSolicitacao1,
            JTextArea taSolicitacao2) {
        return (ActionEvent e) -> {
            try {
                conexao.conectar();

                //Recupera Componentes para criar os arrays
                JCheckBox[] boxes1 = {cbCamera1, cbCamera2};
                JCheckBox[] boxes2 = {cbInternet1, cbInternet2, cbInternet3};
                JCheckBox[] boxes3 = {cbTelefone1, cbTelefone2};

                //Monta os arrays String[]
                String cameras = montarArray(boxes1);
                String internet = montarArray(boxes2);
                String telefonia = montarArray(boxes3);

                //Adiciona os textArea
                cameras += "/ " + tfCamera.getText();
                telefonia += "/ " + tfTelefone.getText();

                //Cria o objeto Solicitacao
                Solicitacao solicitacao = new Solicitacao(
                        tfInicio.getText(),
                        tfFim.getText(),
                        usuario.id,
                        (String) comboEmbarcacao.getSelectedItem(),
                        (String) comboPorto.getSelectedItem(),
                        cameras,
                        internet,
                        telefonia,
                        taSolicitacao1.getText(),
                        taSolicitacao2.getText());

                //Insere a solicitação na base de dados para ser aceita ou recusada
                conexao.execute("INSERT INTO `solicitacao`("
                        + "`inicio`,"
                        + " `fim`,"
                        + " `encarregado`,"
                        + " `solicitante`,"
                        + " `navio`,"
                        + " `porto`,"
                        + " `cameras`,"
                        + " `internet`,"
                        + " `telefonia`,"
                        + " `itemSimples`,"
                        + " `itemUrgente`"
                        + ")"
                        + " VALUES ("
                        + "'" + solicitacao.dateToString(solicitacao.getInicio()) + "',"
                        + "'" + solicitacao.dateToString(solicitacao.getFim()) + "',"
                        + "'" + solicitacao.getEncarregado() + "',"
                        + "'" + solicitacao.getSolicitante()+ "',"
                        + "'" + solicitacao.getEmbarcacao() + "',"
                        + "'" + solicitacao.getLocal() + "',"
                        + "'" + solicitacao.getCameras() + "',"
                        + "'" + solicitacao.getInternet() + "',"
                        + "'" + solicitacao.getTelefonia() + "',"
                        + "'" + solicitacao.getItemSimples() + "',"
                        + "'" + solicitacao.getItemUrgente() + "');"
                );

                conexao.desconectar();

                System.out.println(solicitacao.toString());
                JOptionPane.showMessageDialog(frame, "Solicitação enviada. "
                        + "\nMonitore a aba 'Serviços > Minhas Solicitações', "
                        + "para acompanhar a resposta.", "Solicitação Enviada", JOptionPane.PLAIN_MESSAGE);
                frame.dispose();

            } catch (HeadlessException ex) {

                JOptionPane.showMessageDialog(frame, "Sua solicitação não pôde "
                        + "ser enviada apropriadamente, entre em contato com "
                        + "a gerência.", "Falha no Envio", JOptionPane.ERROR_MESSAGE);
                System.err.println("Exceção ao manter solicitação. View.Solicitar.solicitar()");
            }
        };

    }
}
