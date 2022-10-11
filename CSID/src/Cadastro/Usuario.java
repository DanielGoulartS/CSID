package Cadastro;

import Model.Connection;
import View.JanelaTodasAsSolicitacoes;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public abstract class Usuario {

    public int id;
    public String nome, sobrenome, usuario, senha, cargo;

    public Usuario(int id, String nome, String sobrenome, String usuario, char[] senha, String cargo) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.usuario = usuario;
        this.senha = String.valueOf(senha);
        this.cargo = cargo.substring(0, 3);
    }

    public Usuario(int id, String nome, String sobrenome, String usuario, String senha, String cargo) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.usuario = usuario;
        this.senha = senha;
        this.cargo = cargo.substring(0, 3);
    }

    public Usuario select(Connection conexao, Usuario usuario) {

        System.out.println(conexao.isValid());

        conexao.conectar();

        System.out.println("1");
        Usuario adm = new Capitao(usuario.id, usuario.nome, usuario.sobrenome,
                usuario.usuario, usuario.senha, usuario.cargo);

        System.out.println("2");
        ResultSet rs = conexao.executeQuery("SELECT * FROM `usuarios` WHERE usuario = '" + usuario.usuario + "' "
                + "AND senha = '" + usuario.senha + "';");

        System.out.println("3");
        try {
            if (rs.next()) {

                System.out.println("4");
                switch (rs.getString("cargo")) {
                    case "Adm":
                        System.out.println("ADM");
                        adm = new Administrador(rs.getInt("id"), rs.getString("nome"), rs.getString("sobrenome"),
                                rs.getString("usuario"), rs.getString("senha"), rs.getString("cargo"));
                        break;
                    case "Téc":
                        System.out.println("TEC");
                        adm = new Tecnico(rs.getInt("id"), rs.getString("nome"), rs.getString("sobrenome"),
                                rs.getString("usuario"), rs.getString("senha"), rs.getString("cargo"));
                        break;
                    case "Cap":
                        System.out.println("CAP");
                        adm = new Capitao(rs.getInt("id"), rs.getString("nome"), rs.getString("sobrenome"),
                                rs.getString("usuario"), rs.getString("senha"), rs.getString("cargo"));
                        break;
                }

            }
        } catch (SQLException ex) {
            System.err.println("Exceção em Usuario.select()");
        }
        conexao.desconectar();
        return adm;

    }

    public boolean insert(Connection conexao, Usuario usuario) {

        conexao.conectar();
        boolean result = false;

        ResultSet rs = conexao.executeQuery("SELECT * FROM usuarios WHERE usuario = '" + usuario.usuario + "';");

        try {//Se ja houver este usuário, nao cadastrará

            if (rs.next()) {
            } else {
                //Se não houver cadastrará normalmente
                conexao.execute("INSERT INTO `usuarios`( `nome`, `sobrenome`, `usuario`, `senha`, `cargo`)"
                        + " VALUES ('" + usuario.nome + "','" + usuario.sobrenome + "','" + usuario.usuario + "','"
                        + usuario.senha + "','" + usuario.cargo + "');");
                result = true;
            }
        } catch (SQLException ex) {
            System.err.println("Erro em Classes.Cadastro.Usuario.insert()");
        }

        conexao.desconectar();

        return result;
    }

    public boolean delete(Connection conexao, Component componente, Usuario usuario) {

        conexao.conectar();
        boolean result = false;

        ResultSet rs = conexao.executeQuery("SELECT * FROM usuarios WHERE usuario = '" + usuario.usuario + "';");
        try {
            if (!rs.next()) {
                JOptionPane.showMessageDialog(componente, "Nome de Usuário invalido.");
                return result;
            }
        } catch (SQLException ex) {
            System.err.println("Erro em Classes.Cadastro.Usuario.delete()");
        }

        if (conexao.execute("DELETE FROM `usuarios` WHERE `nome` = '" + usuario.nome + "'"
                + "AND `sobrenome` = '" + usuario.sobrenome + "'"
                + "AND `usuario` = '" + usuario.usuario + "';")) {
            result = true;
        }

        conexao.desconectar();

        return result;
    }


    public ActionListener abrirJanela(Janela janela) {
        return (ActionEvent e) -> {
            this.exibir(janela);
        };
    }

    public abstract void exibir(Janela janela);

    public abstract ActionListener cadastrar(JanelaCadastrarUsuario janela);

    public abstract ActionListener excluir(JanelaCadastrarUsuario janela);

    public ActionListener acessar(Entrar entrar) {

        return (ActionEvent e) -> {

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

            //Exibe-o
            JOptionPane.showMessageDialog(entrar.painel, painelConfirmacao, "Login",
                    JOptionPane.QUESTION_MESSAGE);

            Usuario autenticador = new Administrador(0, "", "", tfUsuarioConfirmacao.getText(),
                    pfSenhaConfirmacao.getPassword(), "000");

            Usuario novoUsuario = autenticador.select(entrar.conexao, autenticador);

            //Se mudar as informações incompativeis, acesse
            if (!novoUsuario.cargo.equals(autenticador.cargo)) {

                novoUsuario.exibir(new JanelaTodasAsSolicitacoes(novoUsuario));

                //se manter cancele
            } else {
                System.err.println("Exceção em Usuario.acessar()");
                JOptionPane.showMessageDialog(entrar.painel, "Falha na autenticação.");
            }
        };
    }

    public KeyListener pesquisaDinamica(JanelaCadastrarPortos jCP) {
        return new KeyListener() {

            //Faz Nada
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            //Quando solta uma tecla, atualiza o painel com os resultados possíveis
            @Override
            public void keyReleased(KeyEvent e) {
                //Conecta a base de dados
                jCP.conexao.conectar();
                jCP.painelLista.removeAll();

                //Monta o objeto para buscar
                jCP.porto = new Porto(jCP.tfId.getText(), jCP.tfTelefone.getText(), 
                        jCP.tfNome.getText(), jCP.tfEndereco.getText(), jCP.tfEmail.getText());

                //Busca-o na base de dados
                ResultSet rs = jCP.porto.select(jCP.conexao, jCP.porto);
                try {

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Porto novoP = new Porto(rs.getString("id"), rs.getString("telefone"), rs.getString("nome"),
                                    rs.getString("endereco"), rs.getString("email"));

                            //Monta o cartao de cada Porto
                            JPanel cartao = new JPanel(new GridLayout(0, 1));
                            cartao.add(new JLabel(String.valueOf(novoP.id)));
                            cartao.add(new JLabel(novoP.nome));
                            cartao.add(new JLabel(novoP.endereco));
                            cartao.add(new JLabel(novoP.telefone));
                            cartao.add(new JLabel(novoP.email));
                            jCP.painelLista.add(cartao);
                        } while (rs.next());

                    }

                } catch (SQLException ex) {
                    System.err.println(ex);
                    System.err.println("\n\n1-Exceção em Cadastro.Usuario.PesquisaDinamica()\n\n.");
                }


                jCP.painelLista.add(new JLabel());
                jCP.painelLista.add(new JLabel());
                jCP.painelLista.add(new JLabel());
                jCP.painelLista.setVisible(false);
                jCP.painelLista.setVisible(true);

                //Desonecta a base de dados
                jCP.conexao.desconectar();
            }
        };
    }
}