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

    public Usuario select(Connection conexao, Usuario usuario) {

        System.out.println(conexao.isValid());

        conexao.conectar();

        System.out.println("1");
        Usuario adm = new Capitao(usuario.id, usuario.nome, usuario.sobrenome,
                usuario.usuario, usuario.senha.toCharArray(), usuario.cargo);

        System.out.println("2");
        ResultSet rs = conexao.executeQuery("SELECT * FROM `usuarios` WHERE usuario = '" + usuario.usuario + "' "
                + "AND senha = '" + usuario.senha + "' ORDER BY `id` ASC;");

        System.out.println("3");
        try {
            if (rs.next()) {

                System.out.println("4");
                switch (rs.getString("cargo")) {
                    case "Adm":
                        System.out.println("ADM");
                        adm = new Administrador(rs.getInt("id"), rs.getString("nome"), rs.getString("sobrenome"),
                                rs.getString("usuario"), rs.getString("senha").toCharArray(), rs.getString("cargo"));
                        break;
                    case "Téc":
                        System.out.println("TEC");
                        adm = new Tecnico(rs.getInt("id"), rs.getString("nome"), rs.getString("sobrenome"),
                                rs.getString("usuario"), rs.getString("senha").toCharArray(), rs.getString("cargo"));
                        break;
                    case "Cap":
                        System.out.println("CAP");
                        adm = new Capitao(rs.getInt("id"), rs.getString("nome"), rs.getString("sobrenome"),
                                rs.getString("usuario"), rs.getString("senha").toCharArray(), rs.getString("cargo"));
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

    public abstract ActionListener cadastrar(JanelaCadastrarUsuarios janela);

    public abstract ActionListener excluir(JanelaCadastrarUsuarios janela);

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

    public KeyListener pesquisaDinamicaPortos(JanelaCadastrarPortos jCP) {
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
                            cartao.add(new JLabel("Id: " + String.valueOf(novoP.id)));
                            cartao.add(new JLabel("Nome: " + novoP.nome));
                            cartao.add(new JLabel("Endereço: " + novoP.endereco));
                            cartao.add(new JLabel("Telefone: " + novoP.telefone));
                            cartao.add(new JLabel("E-mail: " + novoP.email));
                            jCP.painelLista.add(cartao);
                            jCP.painelLista.add(new JLabel());
                        } while (rs.next());

                    }

                } catch (SQLException ex) {
                    System.err.println(ex);
                    System.err.println("\n\n1-Exceção em Cadastro.Usuario.PesquisaDinamicaPortos()\n\n.");
                }

                jCP.painelLista.setVisible(false);
                jCP.painelLista.setVisible(true);

                //Desonecta a base de dados
                jCP.conexao.desconectar();
            }
        };
    }

    public KeyListener pesquisaDinamicaEmbarcacoes(JanelaCadastrarEmbarcacoes jCE) {
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
                jCE.conexao.conectar();
                jCE.painelLista.removeAll();

                //Monta o objeto para buscar
                jCE.embarcacao = new Embarcacao(jCE.tfId.getText(), jCE.tfNome.getText(), jCE.tfNumero.getText());

                //Busca-o na base de dados
                ResultSet rs = jCE.embarcacao.select(jCE.conexao, jCE.embarcacao);
                try {

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Embarcacao novaE = new Embarcacao(rs.getString("id"), rs.getString("nome"), rs.getString("numero"));

                            //Monta o cartao de cada Embarcação
                            JPanel cartao = new JPanel(new GridLayout(0, 1));
                            cartao.add(new JLabel("Id: " + String.valueOf(novaE.id)));
                            cartao.add(new JLabel("Nome: " + novaE.nome));
                            cartao.add(new JLabel("Número: " + novaE.numero));
                            jCE.painelLista.add(cartao);
                            jCE.painelLista.add(new JLabel());
                        } while (rs.next());

                    }

                } catch (SQLException ex) {
                    System.err.println("\n\n1-Exceção em Cadastro.Usuario.PesquisaDinamicaEmbarcacoes()\n\n.");
                    System.err.println(ex);
                }

                jCE.painelLista.setVisible(false);
                jCE.painelLista.setVisible(true);

                //Desonecta a base de dados
                jCE.conexao.desconectar();
            }
        };
    }
}
