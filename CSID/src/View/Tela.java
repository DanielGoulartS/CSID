package View;

import Classes.Solicitacao;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
public class Tela {

    //Declaração Global da Interface
    JFrame frame = new JFrame("CSD - Controle de Serviço de Docagem");
    JPanel painel = new JPanel();
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

    public Tela() {
        //Declaração e Construção     
        frame = new JFrame("CSD - Controle de Serviço de Docagem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 25, 500, 600);
        frame.setResizable(false);

        painel = new JPanel();
        painel.setLayout(new GridLayout(0, 1));

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
        separador.setOrientation(JSeparator.VERTICAL);

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
        enviar.addActionListener(solicitar(this));

        //Formatação
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            MaskFormatter mf2 = new MaskFormatter("##/##/####");
            mf.install(tfInicio);
            mf2.install(tfFim);
        } catch (ParseException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tfCamera.setVisible(false);
        cbCamera2.addActionListener(ocultar(tfCamera));
        
        tfTelefone.setVisible(false);
        cbTelefone2.addActionListener(ocultar(tfTelefone));
        

        //Exibição
        painel.add(title);
        painel.add(separador);

        painel.add(lbInicio);
        painel.add(tfInicio);

        painel.add(lbFim);
        painel.add(tfFim);

        painel.add(lbEmbarcacao);
        painel.add(comboEmbarcacao);

        painel.add(lbPorto);
        painel.add(comboPorto);

        painel.add(lbServicos);

        painel.add(lbCamera);
        painel.add(cbCamera1);
        painel.add(cbCamera2);
        painel.add(tfCamera);

        painel.add(lbInternet);
        painel.add(cbInternet1);
        painel.add(cbInternet2);
        painel.add(cbInternet3);

        painel.add(lbTelefone);
        painel.add(cbTelefone1);
        painel.add(cbTelefone2);
        painel.add(tfTelefone);

        painel.add(lbSolicitacao1);
        painel.add(taSolicitacao1);

        painel.add(lbSolicitacao2);
        painel.add(taSolicitacao2);

        painel.add(enviar);

        frame.add(painel);

        frame.setVisible(true);

    }

    //monta o array de checkboxes
    public String[] montarArray(JCheckBox[] componentes) {
        int i = 0;
        String[] vetor = new String[3];

        for (JCheckBox component : componentes) {
            if (component.isSelected()) {
                vetor[i] = component.getText();
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
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                campo.setVisible(!campo.isVisible());
                }
        };
    }

    //Cria o objeto solicitacao e tomará ações com ele
    public ActionListener solicitar(Tela tela) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Recupera Componentes para criar os arrays
                JCheckBox[] boxes1 = {tela.cbCamera1, tela.cbCamera2};
                JCheckBox[] boxes2 = {tela.cbInternet1, tela.cbInternet2, tela.cbInternet3};
                JCheckBox[] boxes3 = {tela.cbTelefone1, tela.cbTelefone2};

                //Monta os arrays String[]
                String[] cameras = tela.montarArray(boxes1);
                String[] internet = tela.montarArray(boxes2);
                String[] telefonia = tela.montarArray(boxes3);

                //Adiciona os textArea
                tela.push(cameras, tela.tfCamera.getText());
                tela.push(telefonia, tela.tfTelefone.getText());

                //Cria o objeto Solicitacao
                Solicitacao solicitacao = new Solicitacao(
                        tela.tfInicio.getText(),
                        tela.tfFim.getText(),
                        (String) tela.comboEmbarcacao.getSelectedItem(),
                        (String) tela.comboPorto.getSelectedItem(),
                        cameras,
                        internet,
                        telefonia,
                        tela.taSolicitacao1.getText(),
                        tela.taSolicitacao2.getText());

                System.out.println(solicitacao.toString());
            }
        };
    }

}
