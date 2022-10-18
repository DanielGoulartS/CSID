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
            //Monta o objeto para cadastrarUsuario
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

            //Monta o objeto para excluirUsuario
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

            //Monta o objeto para cadastrarUsuario
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

            //Monta o objeto para excluirUsuario
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

            //Monta o objeto para cadastrarUsuario
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

            //Monta o objeto para excluirUsuario
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
            //Pede todos os campos caso não estejam preenchidos
            if (jCEq.tfId.getText().equals("") || jCEq.tfNome.getText().equals("")) {
                JOptionPane.showMessageDialog(jCEq.janela, "Preencha todos os campos devidamente");
                return;
            }

            jCEq.conexao.conectar();

            //Monta o objeto para cadastrarUsuario
            jCEq.equipamento = new Equipamento(jCEq.tfId.getText(), jCEq.tfNome.getText(),
                    String.valueOf(jCEq.cbQuantidade.getSelectedItem()));

            //Busca-o na base de dados
            ResultSet rs = jCEq.equipamento.select(jCEq.conexao, jCEq.equipamento);
            try {

                if (rs.next()) {
                    //Se encontra-lo não cadastra, mas

                    if (rs.isLast()) {
                        //Se for unico resultado, soma a nova quantidade e avisa (Evita acrescimos paralelos)
                        //Soma a quantidade para registrá-la
                        int quantiaDb = Integer.valueOf(rs.getString("quantidade"));
                        int quantiaLocal = Integer.valueOf(jCEq.equipamento.quantidade);
                        jCEq.equipamento.quantidade = String.valueOf(quantiaDb + quantiaLocal);

                        jCEq.equipamento.alter(jCEq.conexao, jCEq.equipamento);
                        JOptionPane.showMessageDialog(jCEq.janela, "Já consta um equipamento com mesmo nome, os itens foram"
                                + " somados.");
                        jCEq.limparFormulario();
                    } else {
                        //Se encontrar muitos itens com estes atributos, avisa e pede correção.
                        JOptionPane.showMessageDialog(jCEq.janela, "Já constam equipamentos com estes dados.");
                    }

                } else {
                    //Se não encontra-lo cadastra-o
                    JOptionPane.showMessageDialog(jCEq.janela, "Novo equipamento cadastrado,"
                            + "\nO id será escolhido pelo Sistema.");
                    jCEq.equipamento.insert(jCEq.conexao, jCEq.equipamento);
                    jCEq.limparFormulario();
                }

            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.cadastrarEquipamentos()\n\n.");
            }
            jCEq.conexao.desconectar();
        };
    }

    static ActionListener excluirEquipamentos(JanelaCadastrarEquipamentos jCEq) {
        return (ActionEvent e) -> {

            //Pede todos os campos caso não estejam preenchidos
            if (jCEq.tfId.getText().equals("") || jCEq.tfNome.getText().equals("")) {
                JOptionPane.showMessageDialog(jCEq.janela, "Preencha todos os campos devidamente");
                return;
            }

            jCEq.conexao.conectar();

            //Monta o objeto para excluirUsuario
            jCEq.equipamento = new Equipamento(jCEq.tfId.getText(), jCEq.tfNome.getText(),
                    String.valueOf(jCEq.cbQuantidade.getSelectedItem()));

            //Busca-o na base de dados
            ResultSet rs = jCEq.equipamento.select(jCEq.conexao, jCEq.equipamento);

            try {
                if (rs.next()) {
                    //Se encontra-lo

                    if (rs.isLast()) {
                        //E for unico resultado

                        //Subtrai a quantia necessária para atualizar a base de dados
                        int quantiaDb = Integer.parseInt(rs.getString("quantidade"));
                        int quantiaLocal = Integer.parseInt(jCEq.equipamento.quantidade);
                        jCEq.equipamento.quantidade = String.valueOf(quantiaDb - quantiaLocal);

                        if (Integer.valueOf(jCEq.equipamento.quantidade) < 0) {
                            //Se a quantia negativar, apaga-o
                            jCEq.equipamento.delete(jCEq.conexao, jCEq.equipamento);
                            JOptionPane.showMessageDialog(jCEq.janela, "Equipamento Completamente Excluido.");
                        } else {
                            //Se não, subtrai-o
                            jCEq.equipamento.alter(jCEq.conexao, jCEq.equipamento);
                            JOptionPane.showMessageDialog(jCEq.janela, "Equipamento Subtraído.");
                        }

                        jCEq.limparFormulario();
                    } else {
                        //Se encontrar muitos, avisa e cancela
                        JOptionPane.showMessageDialog(jCEq.janela, "Confira se as informações inseridas estão idênticas.");
                    }
                } else {
                    //Se não encontra-lo parará e avisará o usuário
                    JOptionPane.showMessageDialog(jCEq.janela, "Confira todas as informações do Equipamento.");
                }
            } catch (SQLException ex) {
                System.err.println("\n\n1-Exceção em Cadastro.Administrador.excluirEquipamentos()\n\n.");
            }
            jCEq.conexao.desconectar();
        };
    }

    public static ActionListener cadastrarUsuario(JanelaCadastrarUsuarios jCU) {
        return (ActionEvent e) -> {

            jCU.conexao.conectar();
            
            String campoSenha = String.valueOf(jCU.pfSenha.getPassword());
            String campoConfirmacao = String.valueOf(jCU.pfConfirmarSenha.getPassword());

            //Se a senha não for igual a confirmação ou faltar algum campo preenchido, retorna
            if (!campoSenha.equals(campoConfirmacao) || campoSenha.equals("") || jCU.tfUsuario.getText().equals("")) {
                JOptionPane.showMessageDialog(jCU.painel, "Preencha corretamente todos os campos, e certifique-se"
                        + " que senha e confirmação estão iguais.");
                return;
            }

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

            JOptionPane.showMessageDialog(jCU.painel, painelConfirmacao, "Você precisa de um Administrador",
                    JOptionPane.QUESTION_MESSAGE);

            //Cria um usuário incompleto com estas informações
            jCU.usuario = new Administrador(0, "", "", tfUsuarioConfirmacao.getText(),
                    pfSenhaConfirmacao.getPassword(), "000");

            //E busca exatamente este usuário e senha
            ResultSet rs = jCU.usuario.selectPorUsuarioESenha(jCU.conexao, jCU.usuario);

            
            
            
            
            try {
                
                
                
                //Se houverem resultados
                if (rs.next()) {

                    //Se for um só
                    if (rs.isLast()) {
                        
                        //Caso seja Administrador monta o usuário para cadastrarUsuario abaixo.
                        switch (rs.getString("cargo")) {
                            case "Adm":                                
                                //Cria um novo usuário para Cadastrar
                                jCU.usuario = new Comandante(0, jCU.tfNome.getText(), jCU.tfSobrenome.getText(),
                                        jCU.tfUsuario.getText(), jCU.pfSenha.getPassword(),
                                        jCU.cbCargo.getSelectedItem().toString());

                                //Mas antes confere se o nome está em uso
                                ResultSet res = jCU.usuario.selectPorUsuario(jCU.conexao, jCU.usuario);

                                    if (res.next()) {
                                        //Caso o nome de usuário esteja usado, não o cadastra
                                        JOptionPane.showMessageDialog(jCU.painel, "Erro. \nNome de Usuário invalido.");
                                    } else {
                                        //Se não, Cadastra
                                        jCU.usuario.insert(jCU.conexao, jCU.usuario);
                                        JOptionPane.showMessageDialog(jCU.painel, "Cadastrado com sucesso.");
                                        jCU.limparCampos();
                                    }
                                break;
                            case "Téc":
                            case "Com":
                                JOptionPane.showMessageDialog(jCU.painel, painelConfirmacao, "Este usuário não "
                                        + "tem essa permissão.", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(jCU.painel, painelConfirmacao, "Autenticação inconclusiva.",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(jCU.painel, painelConfirmacao, "Não autenticado.",
                            JOptionPane.ERROR_MESSAGE);
                }

                
                
            } catch (SQLException ex) {
                System.err.println("\n\n Exceção em Cadastro.Administrador.cadastrar()\n\n" + ex);
            }
            
            jCU.conexao.desconectar();
            
        };
    }

    public static ActionListener excluirUsuario(JanelaCadastrarUsuarios jCU) {
        return (ActionEvent e) -> {
            
            jCU.conexao.conectar();

            //0 = YES, 1 = NO
            int permit = JOptionPane.showConfirmDialog(jCU.janela, "Confirme os dados, e principalmente o ID."
                    + "\nUma vez executada, esta ação não poderá ser desfeita."
                    + "\nDeseja continuar?",
                    "Excluir Usuário", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (permit == 0) {
                //Monta o usuário para Excluir
                jCU.usuario = new Comandante(Integer.parseInt(jCU.tfId.getText()), 
                        jCU.tfNome.getText(), jCU.tfSobrenome.getText(),
                        jCU.tfUsuario.getText(), jCU.pfSenha.getPassword(),
                        jCU.cbCargo.getSelectedItem().toString());

                //Busca o usuário pelo nome de usuário
                ResultSet rs = jCU.usuario.selectPorUsuario(jCU.conexao, jCU.usuario);
                
                
                //Se obtiver o resultado, obviamente unico por conta do cadastro
                try {
                
                    if(rs.next()){
                        jCU.usuario.delete(jCU.conexao, jCU.janela, jCU.usuario);
                    }
                    
                } catch (SQLException ex) {
                    System.err.println("\n\nExceção em Cadastro.Administrador.excluirUsuario();\n\n" + ex);
                } 
            }
            
            jCU.conexao.desconectar();
        };
    }
    
    @Override
    public void exibir(Janela janela) {
        janela.exibirInterfaceAdministrador();

    }

}
