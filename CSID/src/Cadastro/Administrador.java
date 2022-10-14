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

    public Administrador(int id, String nome, String sobrenome, String usuario, char[] senha, String cargo) {
        super(id, nome, sobrenome, usuario, senha, cargo);
    }

    
    
    static ActionListener cadastrarPortos(JanelaCadastrarPortos jCP) {
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

    static ActionListener excluirPortos(JanelaCadastrarPortos jCP) {
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

    static ActionListener cadastrarEmbarcacoes(JanelaCadastrarEmbarcacoes jCE) {
        return (ActionEvent e) -> {
            jCE.conexao.conectar();

            //Monta o objeto para cadastrar
            jCE.embarcacao = new Embarcacao(jCE.tfId.getText(), jCE.tfNome.getText(), jCE.tfNumero.getText());

            //Busca-o na base de dados
            ResultSet rs = jCE.embarcacao.select(jCE.conexao, jCE.embarcacao);
            try {

                if (rs.next()) {
                    //Se encontra-lo não cadastra, e avisa
                    JOptionPane.showMessageDialog(jCE.janela, "Já consta uma embarcação com mesmo nome."
                            + " \nConfira, ou tente outro nome.");
                } else {
                    //Se não encontra-lo cadastra-o
                    JOptionPane.showMessageDialog(jCE.janela, "O id será escolhido pelo Sistema.");
                    jCE.embarcacao.insert(jCE.conexao, jCE.embarcacao);
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarEmbarcacoes()\n\n.");
            }
            jCE.conexao.desconectar();
        };
    }

    static ActionListener excluirEmbarcacoes(JanelaCadastrarEmbarcacoes jCE) {
        return (ActionEvent e) -> {
            jCE.conexao.conectar();

            //Monta o objeto para excluir
            jCE.embarcacao = new Embarcacao(jCE.tfId.getText(), jCE.tfNome.getText(), jCE.tfNumero.getText());

            //Busca-o na base de dados
            ResultSet rs = jCE.embarcacao.select(jCE.conexao, jCE.embarcacao);
            try {

                if (rs.next()) {
                    //Se for primeiro e ultimo resultado (evita exclusão em massa)
                    if (rs.isLast()) {
                        //Ao encontra-lo excluirá
                        JOptionPane.showMessageDialog(jCE.janela, "Embarcação Excluída.");
                        jCE.embarcacao.delete(jCE.conexao, jCE.embarcacao);
                        jCE.limparFormulario();
                    } else {
                        JOptionPane.showMessageDialog(jCE.janela, "Confira se as informações inseridas estão idênticas.");
                    }
                } else {
                    //Se não encontra-lo parará e avisará o usuário
                    JOptionPane.showMessageDialog(jCE.janela, "Confira todas as informações da Embarcação.");
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarEmbarcacoes()\n\n.");
            }
            jCE.conexao.desconectar();
        };
    }

    static ActionListener cadastrarServicos(JanelaCadastrarServicos jCS) {
        return (ActionEvent e) -> {
            jCS.conexao.conectar();

            //Monta o objeto para cadastrar
            jCS.servico = new Servico("", jCS.tfNome.getText(), jCS.tfDescricao.getText());

            //Busca-o na base de dados
            ResultSet rs = jCS.servico.select(jCS.conexao, jCS.servico);
            try {

                if (rs.next()) {
                    //Se encontra-lo não cadastra, e avisa
                    JOptionPane.showMessageDialog(jCS.janela, "Já consta um serviço com mesmo nome, confira-o,"
                            + " ou tente outro nome.");
                } else {
                    //Se não encontra-lo cadastra-o
                    JOptionPane.showMessageDialog(jCS.janela, "O id será escolhido pelo Sistema.");
                    jCS.servico.insert(jCS.conexao, jCS.servico);
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarServico()\n\n.");
            }
            jCS.conexao.desconectar();
        };
    }

    static ActionListener excluirServicos(JanelaCadastrarServicos jCS) {
        return (ActionEvent e) -> {
            jCS.conexao.conectar();

            //Monta o objeto para excluir
            jCS.servico = new Servico(jCS.tfId.getText(), jCS.tfNome.getText(), jCS.tfDescricao.getText());

            //Busca-o na base de dados
            ResultSet rs = jCS.servico.select(jCS.conexao, jCS.servico);
            try {

                if (rs.next()) {
                    //Se for primeiro e ultimo resultado (evita exclusão em massa)
                    if (rs.isLast()) {
                        //Ao encontra-lo excluirá
                        JOptionPane.showMessageDialog(jCS.janela, "Serviço Excluído.");
                        jCS.servico.delete(jCS.conexao, jCS.servico);
                        jCS.limparFormulario();
                    } else {
                        JOptionPane.showMessageDialog(jCS.janela, "Confira se as informações inseridas estão idênticas.");
                    }
                } else {
                    //Se não encontra-lo parará e avisará o usuário
                    JOptionPane.showMessageDialog(jCS.janela, "Confira todas as informações do Serviço.");
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarServico()\n\n.");
            }
            jCS.conexao.desconectar();
        };
    }

    static ActionListener cadastrarEquipamentos(JanelaCadastrarEquipamentos jCEq) {
        return (ActionEvent e) -> {
            jCEq.conexao.conectar();

            //Monta o objeto para cadastrar
            jCEq.equipamento = new Equipamento("", jCEq.tfNome.getText(), jCEq.tfQuantidade.getText());

            //Busca-o na base de dados
            ResultSet rs = jCEq.equipamento.select(jCEq.conexao, jCEq.equipamento);
            try {

                if (rs.next()) {
                    //Se encontra-lo não cadastra, mas soma, e avisa
                    jCEq.alter(jCEq.conexao, jCEq.equipamento);
                    JOptionPane.showMessageDialog(jCEq.janela, "Já consta um equipamento com mesmo nome, os itens foram"
                            + "somados.");
                } else {
                    //Se não encontra-lo cadastra-o
                    JOptionPane.showMessageDialog(jCEq.janela, "Novo equipamento cadastrado,"
                            + "\nO id será escolhido pelo Sistema.");
                    jCEq.equipamento.insert(jCEq.conexao, jCEq.equipamento);
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarServico()\n\n.");
            }
            jCE.conexao.desconectar();
        };
    }

    static ActionListener excluirEquipamentos(JanelaCadastrarEquipamentos jCEq) {
        return (ActionEvent e) -> {
            jCS.conexao.conectar();

            //Monta o objeto para excluir
            jCS.servico = new Servico(jCS.tfId.getText(), jCS.tfNome.getText(), jCS.tfDescricao.getText());

            //Busca-o na base de dados
            ResultSet rs = jCS.servico.select(jCS.conexao, jCS.servico);
            try {

                if (rs.next()) {
                    //Se for primeiro e ultimo resultado (evita exclusão em massa)
                    if (rs.isLast()) {
                        //Ao encontra-lo excluirá
                        JOptionPane.showMessageDialog(jCS.janela, "Serviço Excluído.");
                        jCS.servico.delete(jCS.conexao, jCS.servico);
                        jCS.limparFormulario();
                    } else {
                        JOptionPane.showMessageDialog(jCS.janela, "Confira se as informações inseridas estão idênticas.");
                    }
                } else {
                    //Se não encontra-lo parará e avisará o usuário
                    JOptionPane.showMessageDialog(jCS.janela, "Confira todas as informações do Serviço.");
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarServico()\n\n.");
            }
            jCS.conexao.desconectar();
        };
    }
    
    
    
    @Override
    public void exibir(Janela janela) {
        janela.exibirInterfaceAdministrador();

    }

    @Override
    public ActionListener cadastrar(JanelaCadastrarUsuarios janela) {
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

    public static ActionListener excluir(JanelaCadastrarUsuarios janela) {
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
