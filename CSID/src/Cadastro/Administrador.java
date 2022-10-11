package Cadastro;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Daniel
 */
public class Administrador extends Usuario {

    static ActionListener cadastrarPorto(JanelaCadastrarPortos jCP) {
        return (ActionEvent e) -> {
            jCP.conexao.conectar();
            //Monta o objeto para cadastrar
            jCP.porto = new Porto("", jCP.tfTelefone.getText(), jCP.tfNome.getText(), jCP.tfEndereco.getText(),
                    jCP.tfEmail.getText());

            //Busca-o na base de dados
            ResultSet rs = jCP.porto.select(jCP.conexao, jCP.porto);
            try {

                if (rs.next()) {
                    //Se encontra-lo não cadastra, e avisa
                    JOptionPane.showMessageDialog(jCP.janela, "Já consta um porto com mesmo nome, confira-o, ou tente outro nome.");
                } else {
                    //Se não encontra-lo cadastra-o
                    JOptionPane.showMessageDialog(jCP.janela, "O id será escolhido pelo Sistema.");
                    jCP.porto.insert(jCP.conexao, jCP.porto);
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarPorto()\n\n.");
            }
            jCP.conexao.desconectar();
        };
    }

    static ActionListener excluirPorto(JanelaCadastrarPortos jCP) {
        return (ActionEvent e) -> {
            jCP.conexao.conectar();

            //Monta o objeto para excluir
            jCP.porto = new Porto("", jCP.tfTelefone.getText(), jCP.tfNome.getText(), jCP.tfEndereco.getText(),
                    jCP.tfEmail.getText());

            //Busca-o na base de dados
            ResultSet rs = jCP.porto.select(jCP.conexao, jCP.porto);
            try {

                if (rs.next()) {
                    //Se for primeiro e ultimo resultado (evita exclusão em massa)
                    if (rs.isLast()) {
                        //Ao encontra-lo excluirá
                        JOptionPane.showMessageDialog(jCP.janela, "Porto Excluído.");
                        jCP.porto.delete(jCP.conexao, jCP.porto);
                        jCP.limparFormulario();
                    } else {
                        JOptionPane.showMessageDialog(jCP.janela, "Confira se as informações inseridas estão idênticas.");
                    }
                } else {
                    //Se não encontra-lo parará e avisará o usuário
                    JOptionPane.showMessageDialog(jCP.janela, "Confira todas as informações do Porto.");
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarPorto()\n\n.");
            }
            jCP.conexao.desconectar();
        };
    }

    public Administrador(int id, String nome, String sobrenome, String usuario, char[] senha, String cargo) {
        super(id, nome, sobrenome, usuario, senha, cargo);
    }

    public Administrador(int id, String nome, String sobrenome, String usuario, String senha, String cargo) {
        super(id, nome, sobrenome, usuario, senha, cargo);
    }

    @Override
    public void exibir(Janela janela) {
        janela.exibirInterfaceAdministrador();

    }

    @Override
    public ActionListener cadastrar(JanelaCadastrarUsuario janela) {
        return (ActionEvent e) -> {

            String campoSenha = String.valueOf(janela.pfSenha.getPassword());
            String campoConfirmacao = String.valueOf(janela.pfConfirmarSenha.getPassword());

            //Se a senha for igual a confirmação
            if (campoSenha.equals(campoConfirmacao) && !campoSenha.equals("") && !janela.tfUsuario.getText().equals("")) {

                Usuario novoUsuario = new Capitao(0, janela.tfNome.getText(), janela.tfSobrenome.getText(),
                        janela.tfUsuario.getText(), janela.pfSenha.getPassword(),
                        janela.cbCargo.getSelectedItem().toString());

                //Cria o painel de Autenticação
                JPanel painelConfirmacao = new JPanel(new GridLayout(0, 1));
                JPanel painelConfirmacao1 = new JPanel(new GridLayout(1, 2));
                JPanel painelConfirmacao2 = new JPanel(new GridLayout(1, 2));
                JLabel lbUsuarioConfirmacao = new JLabel("Usuario: ");
                JLabel lbSenhaConfirmacao = new JLabel("Senha: ");
                JTextField tfUsuarioConfirmacao = new JTextField();
                JPasswordField pfSenhaConfirmacao = new JPasswordField();

                painelConfirmacao1.add(lbUsuarioConfirmacao);
                painelConfirmacao1.add(tfUsuarioConfirmacao);
                painelConfirmacao2.add(lbSenhaConfirmacao);
                painelConfirmacao2.add(pfSenhaConfirmacao);
                painelConfirmacao.add(painelConfirmacao1);
                painelConfirmacao.add(painelConfirmacao2);

                JOptionPane.showMessageDialog(janela.painel, painelConfirmacao, "Você precisa de um Administrador",
                        JOptionPane.QUESTION_MESSAGE);

                //E busca o usuario na base
                Usuario autenticador = new Administrador(0, "", "", tfUsuarioConfirmacao.getText(),
                        pfSenhaConfirmacao.getPassword(), "000");

                //sempre retorna
                autenticador = novoUsuario.select(janela.conexao, autenticador);

                //Se for ADM
                if (autenticador instanceof Administrador) {
                    //Executará o novo cadastro
                    if (novoUsuario.insert(janela.conexao, novoUsuario)) {
                        JOptionPane.showMessageDialog(janela.painel, "Cadastrado com sucesso.");
                        janela.limparCampos();
                        //Ou falhará com o nome repetido
                    } else {
                        JOptionPane.showMessageDialog(janela.painel, "Falha no Cadastro, Nome de Usuário invalido.");
                    }

                    //Ou o usuario não é ADM
                } else {
                    JOptionPane.showMessageDialog(janela.painel, "Administrador não autenticado.");
                }

            } else {
                JOptionPane.showMessageDialog(janela.painel, "Verifique o nome de usuário, senha e confirmação, e tente"
                        + "novamente.");
            }
        };
    }

    @Override
    public ActionListener excluir(JanelaCadastrarUsuario janela) {
        return (ActionEvent e) -> {

            //0 = YES, 1 = NO
            int permit = JOptionPane.showConfirmDialog(janela.janela, "Confirme o Nome, Sobrenome, e o Nome de usuário."
                    + "\nUma vez executada, esta ação não poderá ser desfeita."
                    + "\nDeseja continuar?",
                    "Excluir Usuário", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (permit == 0) {
                Usuario novoUsuario = new Capitao(0, janela.tfNome.getText(), janela.tfSobrenome.getText(),
                        janela.tfUsuario.getText(), janela.pfSenha.getPassword(),
                        janela.cbCargo.getSelectedItem().toString());

                novoUsuario.delete(janela.conexao, janela.janela, novoUsuario);
            }
        };
    }

}
