package Cadastro;

import Model.Connection;
import Solicitacoes.JanelaSolicitar;
import Solicitacoes.JanelaTodasAsSolicitacoes;
import java.awt.Component;
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

public abstract class Usuario {

    public int id;
    public String nome, sobrenome, email, usuario, senha, cargo;

    //Métodos da classe usuário
    public Usuario(int id, String nome, String sobrenome, String email, String usuario, char[] senha, String cargo) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.usuario = usuario;
        this.senha = String.valueOf(senha);
        this.cargo = cargo.substring(0, 3);
    }

    public boolean insert(Connection conexao, Usuario usuario) {

        boolean result = conexao.execute("INSERT INTO `usuarios`( `nome`, `sobrenome`, `email`,"
                + " `usuario`, `senha`, `cargo`)"
                + " VALUES ('" + usuario.nome + "','" + usuario.sobrenome + "','"
                + usuario.email + "','" + usuario.usuario + "','"
                + usuario.senha + "','" + usuario.cargo + "');");

        return result;
    }

    public boolean delete(Connection conexao, Component componente, Usuario usuario) {

        boolean result = conexao.execute("DELETE FROM `usuarios` WHERE `nome` = '" + usuario.nome + "'"
                + "AND `sobrenome` = '" + usuario.sobrenome + "'"
                + "AND `usuario` = '" + usuario.usuario + "';");

        return result;
    }

    public ResultSet selectParaPesquisar(Connection conexao, Usuario usuario) {

        ResultSet rs = conexao.executeQuery("SELECT * FROM `usuarios` WHERE "
                + "`id` LIKE '%" + usuario.id + "%' AND"
                + "`nome` LIKE '%" + usuario.nome + "%' AND"
                + " `sobrenome` LIKE '%" + usuario.sobrenome + "%' AND"
                + " `email` LIKE '%" + usuario.email + "%' AND"
                + " `usuario` LIKE '%" + usuario.usuario + "%' ORDER BY `id` ASC;");
        return rs;

    }

    public ResultSet selectPorUsuario(Connection conexao, Usuario usuario) {

        ResultSet rs = conexao.executeQuery("SELECT * FROM `usuarios` WHERE  `usuario` = '" + usuario.usuario + "';");
        return rs;

    }
    
    public ResultSet selectUsuarioPorId(Connection conexao, int usuario) {

        ResultSet rs = conexao.executeQuery("SELECT * FROM `usuarios` WHERE  `id` = '" + usuario + "';");
        return rs;

    }

    public ResultSet selectPorUsuarioESenha(Connection conexao, Usuario usuario) {

        ResultSet rs = conexao.executeQuery("SELECT * FROM `usuarios` WHERE `usuario` = '" + usuario.usuario + "' AND"
                + " `senha` = '" + usuario.senha + "' ORDER BY `id` ASC;");
        return rs;

    }

    public ResultSet selectSolicitadoPorId(Connection conexao, Usuario usuario) {

        ResultSet rs = conexao.executeQuery("SELECT * FROM `solicitacao` WHERE `encarregado` = '" + usuario.id + "' "
                + "OR `solicitante` = '" + usuario.id + "' ORDER BY `id` ASC;");
        return rs;

    }

    //Métodos do Ator Usuario    
    public abstract void exibir(Janela janela);

    public ActionListener logIn(JanelaEntrar jE) {

        return (ActionEvent e) -> {

            jE.conexao.conectar();

            //Cria o painel de Autenticação
            JPanel painelConfirmacao = new JPanel(new GridLayout(0, 1));
            JPanel painelConfirmacao1 = new JPanel(new GridLayout(1, 2));
            JPanel painelConfirmacao2 = new JPanel(new GridLayout(1, 2));
            JLabel lbUsuarioConfirmacao = new JLabel("Usuario: ");
            JLabel lbSenhaConfirmacao = new JLabel("Senha: ");
            JTextField tfUsuarioConfirmacao = new JTextField();
            JPasswordField pfSenhaConfirmacao = new JPasswordField();

            tfUsuarioConfirmacao.addKeyListener(JanelaSolicitar.listener(tfUsuarioConfirmacao, 40));
            pfSenhaConfirmacao.addKeyListener(JanelaSolicitar.listener(pfSenhaConfirmacao, 40));
            painelConfirmacao1.add(lbUsuarioConfirmacao);
            painelConfirmacao1.add(tfUsuarioConfirmacao);
            painelConfirmacao2.add(lbSenhaConfirmacao);
            painelConfirmacao2.add(pfSenhaConfirmacao);
            painelConfirmacao.add(painelConfirmacao1);
            painelConfirmacao.add(painelConfirmacao2);

            //Exibe-o
            JOptionPane.showMessageDialog(jE.painel, painelConfirmacao, "Login",
                    JOptionPane.QUESTION_MESSAGE);

            //Cria o usuário para autenticar e busca-o
            jE.usuario = new Administrador(0, "", "", "", tfUsuarioConfirmacao.getText(),
                    pfSenhaConfirmacao.getPassword(), "000");

            ResultSet rs = jE.usuario.selectPorUsuarioESenha(jE.conexao, jE.usuario);

            try {

                //Se houver exatamente este usuário, filtra seu cargo, e monta-o de acordo
                if (rs.next()) {
                    switch (rs.getString("cargo")) {
                        case "Adm":
                            jE.usuario = new Administrador(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("sobrenome"), rs.getString("email"), rs.getString("usuario"),
                                    rs.getString("senha").toCharArray(), rs.getString("cargo"));

                            jE.usuario.exibir(new JanelaTodasAsSolicitacoes(jE.usuario));

                            break;
                        case "Téc":
                            jE.usuario = new Tecnico(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("sobrenome"), rs.getString("email"), rs.getString("usuario"),
                                    rs.getString("senha").toCharArray(), rs.getString("cargo"));

                            jE.usuario.exibir(new JanelaTodasAsSolicitacoes(jE.usuario));

                            break;
                        case "Com":
                            jE.usuario = new Comandante(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("sobrenome"), rs.getString("email"), rs.getString("usuario"),
                                    rs.getString("senha").toCharArray(), rs.getString("cargo"));

                            jE.usuario.exibir(new JanelaTodasAsSolicitacoes(jE.usuario));

                            break;
                    }

                    //Se não houver, retorna e avisa
                } else {
                    JOptionPane.showMessageDialog(jE.painel, "Falha na autenticação. \nNão encontrado.");
                    return;
                }

            } catch (SQLException ex) {
                System.err.println("\n\n Exceção em Cadastro.Usuario.logIn() \n\n" + ex);
            }

            jE.conexao.desconectar();
            jE.janela.dispose();
        };
    }

}
