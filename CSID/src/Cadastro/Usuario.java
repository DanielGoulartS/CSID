package Cadastro;

import Model.Connection;
import Solicitacoes.JanelaTodasAsSolicitacoes;
import Solicitacoes.Solicitacao;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public abstract class Usuario {

    public int id;
    public String nome, sobrenome, usuario, senha, cargo;

    //Métodos da classe usuário
    public Usuario(int id, String nome, String sobrenome, String usuario, char[] senha, String cargo) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.usuario = usuario;
        this.senha = String.valueOf(senha);
        this.cargo = cargo.substring(0, 3);
    }

    public boolean insert(Connection conexao, Usuario usuario) {

        boolean result = conexao.execute("INSERT INTO `usuarios`( `nome`, `sobrenome`, `usuario`, `senha`, `cargo`)"
                + " VALUES ('" + usuario.nome + "','" + usuario.sobrenome + "','" + usuario.usuario + "','"
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
            jE.usuario = new Administrador(0, "", "", tfUsuarioConfirmacao.getText(),
                    pfSenhaConfirmacao.getPassword(), "000");

            ResultSet rs = jE.usuario.selectPorUsuarioESenha(jE.conexao, jE.usuario);

            try {

                //Se houver exatamente este usuário, filtra seu cargo, e monta-o de acordo
                if (rs.next()) {
                    switch (rs.getString("cargo")) {
                        case "Adm":
                            jE.usuario = new Administrador(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("sobrenome"), rs.getString("usuario"),
                                    rs.getString("senha").toCharArray(), rs.getString("cargo"));

                            jE.usuario.exibir(new JanelaTodasAsSolicitacoes(jE.usuario));

                            break;
                        case "Téc":
                            jE.usuario = new Tecnico(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("sobrenome"), rs.getString("usuario"),
                                    rs.getString("senha").toCharArray(), rs.getString("cargo"));

                            jE.usuario.exibir(new JanelaTodasAsSolicitacoes(jE.usuario));

                            break;
                        case "Com":
                            jE.usuario = new Comandante(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("sobrenome"), rs.getString("usuario"),
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

    public KeyListener pesquisaDinamicaUsuarios(JanelaCadastrarUsuarios jCU) {
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
                jCU.conexao.conectar();
                jCU.painelLista.removeAll();

                //Monta o objeto para buscar
                jCU.usuario = new Comandante(Integer.parseInt("0" + jCU.tfId.getText()), jCU.tfNome.getText(),
                        jCU.tfSobrenome.getText(), jCU.tfUsuario.getText(),
                        "0000".toCharArray(), "000");

                //Busca-o na base de dados
                ResultSet rs = jCU.usuario.selectParaPesquisar(jCU.conexao, jCU.usuario);
                try {

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Usuario novoU = new Comandante(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("sobrenome"), rs.getString("usuario"),
                                    rs.getString("senha").toCharArray(), rs.getString("cargo"));

                            //Monta o cartao de cada Porto
                            JPanel cartao = new JPanel(new GridLayout(0, 1));
                            cartao.add(new JLabel("Id: " + String.valueOf(novoU.id)));
                            cartao.add(new JLabel("Usuario: " + novoU.usuario));
                            cartao.add(new JLabel("Nome: " + novoU.nome + " " + novoU.sobrenome));
                            cartao.add(new JLabel("Cargo: " + novoU.cargo));

                            jCU.painelLista.add(cartao);
                            jCU.painelLista.add(new JLabel());
                        } while (rs.next());

                    }

                } catch (SQLException ex) {
                    System.err.println("\n\n1-Exceção em Cadastro.Usuario.PesquisaDinamicaPortos()\n\n.");
                    System.err.println(ex);
                }

                jCU.painelLista.setVisible(false);
                jCU.painelLista.setVisible(true);

                //Desonecta a base de dados
                jCU.conexao.desconectar();
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

    public KeyListener pesquisaDinamicaServicos(JanelaCadastrarServicos jCS) {
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
                jCS.conexao.conectar();
                jCS.painelLista.removeAll();

                //Monta o objeto para buscar
                jCS.servico = new Servico(jCS.tfId.getText(), jCS.tfNome.getText(), jCS.tfDescricao.getText());

                //Busca-o na base de dados
                ResultSet rs = jCS.servico.select(jCS.conexao, jCS.servico);
                try {

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Servico novoS = new Servico(rs.getString("id"), rs.getString("nome"), rs.getString("descricao"));

                            //Monta o cartao de cada Embarcação
                            JPanel cartao = new JPanel(new GridLayout(0, 1));
                            cartao.add(new JLabel("Id: " + String.valueOf(novoS.id)));
                            cartao.add(new JLabel("Nome: " + novoS.nome));
                            cartao.add(new JLabel("Descrição: " + novoS.descricao));
                            jCS.painelLista.add(cartao);
                            jCS.painelLista.add(new JLabel());
                        } while (rs.next());

                    }

                } catch (SQLException ex) {
                    System.err.println("\n\n1-Exceção em Cadastro.Usuario.PesquisaDinamicaServicos()\n\n.");
                    System.err.println(ex);
                }

                jCS.painelLista.setVisible(false);
                jCS.painelLista.setVisible(true);

                //Desonecta a base de dados
                jCS.conexao.desconectar();
            }
        };
    }

    public KeyListener pesquisaDinamicaEquipamentos(JanelaCadastrarEquipamentos jCEq) {
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
                jCEq.conexao.conectar();
                jCEq.painelLista.removeAll();

                //Monta o objeto para buscar
                jCEq.equipamento = new Equipamento(jCEq.tfId.getText(), jCEq.tfNome.getText(),
                        String.valueOf(jCEq.cbQuantidade.getSelectedItem()) );

                //Busca-o na base de dados
                ResultSet rs = jCEq.equipamento.select(jCEq.conexao, jCEq.equipamento);
                try {

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Equipamento novoE = new Equipamento(rs.getString("id"), rs.getString("nome"),
                                    rs.getString("quantidade") );

                            //Monta o cartao de cada Equipamento
                            JPanel cartao = new JPanel(new GridLayout(0, 1));
                            cartao.add(new JLabel("Id: " + String.valueOf(novoE.id)));
                            cartao.add(new JLabel("Nome: " + novoE.nome));
                            cartao.add(new JLabel("Quantidade: " + novoE.quantidade));
                            jCEq.painelLista.add(cartao);
                            jCEq.painelLista.add(new JLabel());
                        } while (rs.next());

                    }

                } catch (SQLException ex) {
                    System.err.println("\n\n1-Exceção em Cadastro.Usuario.PesquisaDinamicaEquipamentos()\n\n.");
                    System.err.println(ex);
                }

                jCEq.painelLista.setVisible(false);
                jCEq.painelLista.setVisible(true);

                //Desonecta a base de dados
                jCEq.conexao.desconectar();
            }
        };
    }

    public void verSolicitacoes(JanelaTodasAsSolicitacoes jTAS) {
        jTAS.conexao.conectar();
        
        jTAS.painelSolicitacoes.removeAll();
        jTAS.painelMeusServicos.removeAll();
        jTAS.painelMinhasSolicitacoes.removeAll();
        //Busca todas as solicitacoes
        ResultSet rs = jTAS.solicitacao.selectAll(jTAS.conexao);
        try {
            //Se houver ao menos uma
            if(rs.next()){
            //Para cada uma exibe-a num painel alocado na scroll principal
                do{
                    Solicitacao novaSolicitacao = new Solicitacao(rs.getInt("id"), rs.getInt("encarregado"), 
                            rs.getString("inicio"), rs.getString("fim"), rs.getInt("solicitante"), 
                            rs.getString("embarcacao"), rs.getString("porto"), rs.getString("servicos"), 
                            rs.getString("equipamentos"), rs.getString("obs"));
                
                jTAS.painelSolicitacoes.add(jTAS.novoPainel(novaSolicitacao));
                }while(rs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger("Usuario.verSolicitacoes()" + ex);
        }
        //exibe o scrollpane
        jTAS.scrollPane.setViewportView(jTAS.painelSolicitacoes);
        jTAS.conexao.desconectar();
    }

    public void verMinhasSolicitacoes(JanelaTodasAsSolicitacoes jTAS) {
        jTAS.conexao.conectar();
        
        jTAS.painelSolicitacoes.removeAll();
        jTAS.painelMeusServicos.removeAll();
        jTAS.painelMinhasSolicitacoes.removeAll();
        //Busca as solicitacoes
        ResultSet rs = jTAS.solicitacao.selectMinhasSolicitacoes(jTAS.conexao, jTAS.usuario);
        try {
            //Se houver ao menos uma
            if(rs.next()){
            //Para cada uma exibe-a num painel alocado na scroll principal
                do{
                    Solicitacao novaSolicitacao = new Solicitacao(rs.getInt("id"), rs.getInt("encarregado"), 
                            rs.getString("inicio"), rs.getString("fim"), rs.getInt("solicitante"), 
                            rs.getString("embarcacao"), rs.getString("porto"), rs.getString("servicos"), 
                            rs.getString("equipamentos"), rs.getString("obs"));
                
                jTAS.painelMinhasSolicitacoes.add(jTAS.novoPainel(novaSolicitacao));
                }while(rs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger("Usuario.verMinhasSolicitacoes()" + ex);
        }
        //exibe o scrollpane
        jTAS.scrollPane.setViewportView(jTAS.painelMinhasSolicitacoes);
        jTAS.conexao.desconectar();
    }

    public boolean excluirSolicitacao(Solicitacao solicitacao, JanelaTodasAsSolicitacoes jTAS ) {

        jTAS.conexao.conectar();
        
        ResultSet rs = solicitacao.selectPorId(jTAS.conexao, solicitacao);
        
        try {
            if(rs.next()){
                //0 = YES, 1 = NO
                int permit = JOptionPane.showConfirmDialog(jTAS.janela, "Uma vez executada esta ação não pode ser desfeita."
                        + "\nO cliente (solicitante) "
                        + "NÃO RECEBERÁ uma notificação desta atividade. "
                        + "\nDeseja continuar?",
                        "Excluir Solicitação", JOptionPane.YES_NO_OPTION);
                
                if (permit == 0) {
                    if (solicitacao.delete(jTAS.conexao, solicitacao)) {
                        
                        
                        //Devolver Itens ao banco
                        //Recupera os equipamentos
                        String strEq = solicitacao.equipamentos;

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

                        //Faz a busca dos Equipamentos
                        for (String[] s : listaDeEquipamentos) {//Para cada objeto na lista de equipaments
                            Equipamento equipamentoSelecionado = new Equipamento(s[1], s[2], s[3]);//instancia-se o equipamento

                            try {
                                //Busca exatamente este na base de dados
                                ResultSet rs2 = equipamentoSelecionado.selectUnico(jTAS.conexao, equipamentoSelecionado);
                                if (rs2.next()) {//Se houver
                                    do {
                                        //Acrescenta a soma de quantidades do pedido e da base, e soma
                                        int novaQuantidade = Integer.valueOf(rs2.getString("quantidade")) + Integer.valueOf(s[3]);

                                        Equipamento novoEquipamento = new Equipamento(rs2.getString("id"), rs2.getString("nome"),
                                                String.valueOf(novaQuantidade));

                                            novoEquipamento.alter(jTAS.conexao, novoEquipamento);

                                    } while (rs2.next());
                                } else{// ou recadastra o item, se tiver sido excluido
                                    equipamentoSelecionado.insert(jTAS.conexao, equipamentoSelecionado);
                                }
                                
                            } catch (SQLException ex) {
                                System.err.println("JanelaSolicitar.enviar() " + ex);
                            }

                        }

                        
                        
                        
                        JOptionPane.showMessageDialog(jTAS.janela, "Excluído Permanentemente.",
                                "Excluir Solicitação", JOptionPane.OK_OPTION);
                        jTAS.conexao.desconectar();
                        return true;
                    }
                }
                
                
                jTAS.conexao.desconectar();
            } else {
                JOptionPane.showConfirmDialog(jTAS.janela, "Solicitação não encontrada, talvez já tenha sido excluída.",
                        "Falha",JOptionPane.OK_OPTION);
            }
        } catch (SQLException ex) {
            System.err.println("Cadastro.Usuario.excluirSolicitacao(). " + ex);
        }
        return false;

    }

    
}
