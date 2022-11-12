package Cadastro;

import Solicitacoes.JanelaSolicitar;
import Solicitacoes.JanelaTodasAsSolicitacoes;
import Solicitacoes.Solicitacao;
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


/**
 *
 * @author Daniel
 */
public class Comandante extends Usuario {

    public Comandante(int id, String nome, String sobrenome, String email, String usuario, char[] senha, String cargo) {
        super(id, nome, sobrenome, email, usuario, senha, cargo);
    }

    @Override
    public void exibir(Janela janela) {
        janela.exibirInterfaceComandante();

    }

    //Ações do Ator
    
    public static KeyListener pesquisaDinamicaUsuarios(JanelaCadastrarUsuarios jCU) {
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
                        jCU.tfSobrenome.getText(), jCU.tfEmail.getText(), jCU.tfUsuario.getText(),
                        "0000".toCharArray(), "000");

                //Busca-o na base de dados
                ResultSet rs = jCU.usuario.selectParaPesquisar(jCU.conexao, jCU.usuario);
                try {

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Usuario novoU = new Comandante(rs.getInt("id"), rs.getString("nome"),
                                    rs.getString("sobrenome"), rs.getString("email"), rs.getString("usuario"),
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

    public static KeyListener pesquisaDinamicaEmbarcacoes(JanelaCadastrarEmbarcacoes jCE) {
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

    public static KeyListener pesquisaDinamicaPortos(JanelaCadastrarPortos jCP) {
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

                try {
                    //Monta o objeto para buscar
                    jCP.porto = new Porto(jCP.tfId.getText(), jCP.tfNome.getText(), 0,
                            0, jCP.tfTelefone.getText(),
                            jCP.tfEmail.getText(), jCP.tfRua.getText(), 0,
                            jCP.tfCidade.getText(), jCP.tfEstado.getText(), jCP.tfPais.getText());

                    //Busca-o na base de dados
                    ResultSet rs = jCP.porto.select(jCP.conexao, jCP.porto);

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Porto novoP = new Porto(rs.getString("id"), rs.getString("nome"),
                                    rs.getInt("ddi"), rs.getInt("ddd"), rs.getString("telefone"),
                                    rs.getString("email"), rs.getString("rua"), rs.getInt("numero"),
                                    rs.getString("cidade"), rs.getString("estado"), rs.getString("pais"));

                            //Monta o cartao de cada Porto
                            JPanel cartao = new JPanel(new GridLayout(0, 1));
                            cartao.add(new JLabel("Id: " + String.valueOf(novoP.id)));
                            cartao.add(new JLabel("Nome: " + novoP.nome));
                            cartao.add(new JLabel("Telefone: " + novoP.ddi + " " + novoP.ddd + " " +novoP.telefone));
                            cartao.add(new JLabel("Endereço: " + novoP.rua + " - "+ novoP.numero + ", " 
                            + novoP.cidade + ", " + novoP.estado + ", " + novoP.pais));
                            cartao.add(new JLabel("E-mail: " + novoP.email));
                            jCP.painelLista.add(cartao);
                            jCP.painelLista.add(new JLabel());
                        } while (rs.next());

                    }

                } catch (Exception ex) {
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

    public static KeyListener pesquisaDinamicaServicos(JanelaCadastrarServicos jCS) {
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
                jCS.servico = new Servico(jCS.tfId.getText(), jCS.tfNome.getText(), jCS.tfDescricao.getText(),0);

                //Busca-o na base de dados
                ResultSet rs = jCS.servico.select(jCS.conexao, jCS.servico);
                try {

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Servico novoS = new Servico(rs.getString("id"), rs.getString("nome"),
                                    rs.getString("descricao"),0);

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

    public static KeyListener pesquisaDinamicaEquipamentos(JanelaCadastrarEquipamentos jCEq) {
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
                        jCEq.cbQuantidade.getSelectedIndex() + 1, 0 );

                //Busca-o na base de dados
                ResultSet rs = jCEq.equipamento.select(jCEq.conexao, jCEq.equipamento);
                try {

                    if (rs.next()) {
                        //Se encontra-los Cria um painel com eles e os adiciona a lista
                        do {
                            Equipamento novoE = new Equipamento(rs.getString("id"), rs.getString("nome"),
                                    rs.getInt("quantidade"), 0 );

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

    public static void verSolicitacoes(JanelaTodasAsSolicitacoes jTAS) {
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
                            rs.getString("embarcacao"), rs.getString("porto"), rs.getString("obs"));
        
                
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

    public static void verMinhasSolicitacoes(JanelaTodasAsSolicitacoes jTAS) {
        jTAS.conexao.conectar();
        
        jTAS.painelSolicitacoes.removeAll();
        jTAS.painelMeusServicos.removeAll();
        jTAS.painelMinhasSolicitacoes.removeAll();
        //Busca as solicitacoes
        ResultSet rs = jTAS.solicitacao.selectPorSolicitante(jTAS.conexao, jTAS.usuario);
        try {
            //Se houver ao menos uma
            if(rs.next()){
            //Para cada uma exibe-a num painel alocado na scroll principal
                do{
                    Solicitacao novaSolicitacao = new Solicitacao(rs.getInt("id"), rs.getInt("encarregado"), 
                            rs.getString("inicio"), rs.getString("fim"), rs.getInt("solicitante"), 
                            rs.getString("embarcacao"), rs.getString("porto"), rs.getString("obs"));
                
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

    public static boolean excluirSolicitacao(Solicitacao solicitacao, JanelaTodasAsSolicitacoes jTAS ) {

        jTAS.conexao.conectar();
        
        ResultSet rsSolicitacao = solicitacao.selectPorId(jTAS.conexao, solicitacao);
        
        try {
            if(rsSolicitacao.next()){
                //0 = YES, 1 = NO, 2 = CANCEL
                int permit = JOptionPane.showConfirmDialog(jTAS.janela,"A Solicitação foi atendida com sucesso?\n\n"
                        + "(YES, SIM): O Sistema dará baixa nos equipamentos utilizados.\n"
                        + "(NO, NÃO): Os Equipamentos serão devolvidos ao estoque.\n"
                        + "(CANCEL, CANCELAR): Aborta a operação.\n\n"
                        + "(Uma vez executada, esta ação não pode ser desfeita.)\n"
                        + "(O cliente (solicitante) NÃO RECEBERÁ uma notificação desta atividade.)\n",
                                "Excluir Solicitação", JOptionPane.YES_NO_CANCEL_OPTION);
                
                if (permit == 0) {//YES

                    solicitacao.delete(jTAS.conexao, solicitacao);
                    //Apaga os Equipamentos Vinculados a esta solicitação
                    //Recupera os equipamentos pedidos nesta solicitacao
                    Equipamento equipamento = new Equipamento("", "", 0, solicitacao.id);
                    ResultSet rsEquipamentosPedidos = equipamento.selectPorSolicitacao(jTAS.conexao, solicitacao);

                    while (rsEquipamentosPedidos.next()) {//Para cada um dos equipamentos pedidos
                        equipamento = new Equipamento(rsEquipamentosPedidos.getString("id"), "",
                                rsEquipamentosPedidos.getInt("quantidade"), rsEquipamentosPedidos.getInt("solicitacao"));

                        equipamento.deleteEquipamentoSolicitado(jTAS.conexao, solicitacao);

                    }
                    return true;

                } else if (permit == 1) {//NO

                    solicitacao.delete(jTAS.conexao, solicitacao);
                    //Devolver Itens ao banco
                    //Recupera os equipamentos pedidos nesta solicitacao
                    Equipamento equipamento = new Equipamento("", "", 0, solicitacao.id);
                    ResultSet rsEquipamentosPedidos = equipamento.selectPorSolicitacao(jTAS.conexao, solicitacao);

                    while (rsEquipamentosPedidos.next()) {//Para cada um dos equipamentos pedidos
                        equipamento = new Equipamento(rsEquipamentosPedidos.getString("id"), "",
                                rsEquipamentosPedidos.getInt("quantidade"), rsEquipamentosPedidos.getInt("solicitacao"));
                        //Busca os equipamentos originais correspondentes pelo id
                        ResultSet rsRegistroDeEquipamento = equipamento.selectPorId(jTAS.conexao, equipamento);
                        if (rsRegistroDeEquipamento.next()) {
                            int quantidadeSomada = equipamento.quantidade + rsRegistroDeEquipamento.getInt("quantidade");
                            equipamento.quantidade = quantidadeSomada;
                            equipamento.alterQuantidadePorId(jTAS.conexao, equipamento);//Devolve-os
                        }
                    }
                    return true;

                } else if (permit == 2) {//CANCEL
                    return false;
                }

                JOptionPane.showMessageDialog(jTAS.janela, "Excluído Permanentemente.",
                        "Excluir Solicitação", JOptionPane.OK_OPTION);

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

    
    
    public static ActionListener solicitar(JanelaSolicitar jS) {
        return (ActionEvent e) -> {
            
            jS.conexao.conectar();
            
            //Solicita preenchimento caso as datas estejam vazias
            if(jS.tfInicio.getText().equals("") || jS.tfFim.getText().equals("") ||
                    jS.tfInicio.getText().equals("  /  /    ") || 
                    jS.tfFim.getText().equals("  /  /    ") ||
                    jS.comboPorto.getSelectedIndex() == -1 || jS.comboEmbarcacao.getSelectedIndex() == -1){
                JOptionPane.showMessageDialog(jS.janela, "Preencha devidamente ao menos as datas, Porto e Embarcação!",
                        "Atenção",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            //Recupera a embarcação selecionada
                Embarcacao embarcacaoFinal = jS.embarcacoes.get(jS.comboEmbarcacao.getSelectedIndex());
                
            //Recupera o Porto selecionado
                Porto portoFinal = jS.portos.get(jS.comboPorto.getSelectedIndex());

            
            //Cria a solicitacao
            Solicitacao solicitacao = new Solicitacao(0, 0, jS.tfInicio.getText(), jS.tfFim.getText(), 
                    jS.usuario.id, embarcacaoFinal.id, portoFinal.id, jS.tfObs.getText());
            
            //Insere a solicitacao na base
            solicitacao.insert(jS.conexao, solicitacao);
            ResultSet rsId = solicitacao.selectMaxIdPorUsuario(jS.conexao, jS.usuario);
            try {
                rsId.next();
                solicitacao.id = rsId.getInt("MAX(id)");
            } catch (SQLException ex) {
                System.err.println(Comandante.class.getName() + ex);
            }
            
            //solicitacoes nao aparecem na tela principal
                            
            //Cadastra os Serviços pedidos na base de Serviços Solicitados
            for(Servico servico : jS.meusServicos){
                servico.insertServicoSolicitado(jS.conexao, servico, solicitacao);
            }
            
            //Cadastra os Equipamentos
            
            //Recupera os equipamentos
            for (Equipamento equipamento : jS.meusEquipamentos) {//Para cada objeto na lista de equipaments
                try {
                    //Busca exatamente este na base de dados
                    ResultSet rs = equipamento.selectPorId(jS.conexao, equipamento);
                    if (rs.next()) {
                        
                        do {
                            //diminui a quantidade pedida da disponível e subtrai o item na base e registra os pedidos
                            int novaQuantidade = rs.getInt("quantidade") - equipamento.quantidade;

                            Equipamento novoEquipamento = new Equipamento(rs.getString("id"), rs.getString("nome"),
                                    novaQuantidade, 0);
                            novoEquipamento.alterQuantidadePorId(jS.conexao, novoEquipamento);//subtrai
                            
                            //novo solicitado
                            equipamento.insertEquipamentoSolicitado(jS.conexao, equipamento, solicitacao);

                        } while (rs.next());
                    }
                } catch (SQLException ex) {
                    System.err.println("JanelaSolicitar.enviar() " + ex);
                }

            }
            
            
            JOptionPane.showMessageDialog(jS.painel, "Solicitação de Serviço criada.\nAcompanhe-a no menu Solicitação > "
                    + "Minhas Solicitações, para saber seu status.", "Sucesso", JOptionPane.PLAIN_MESSAGE);
            jS.janela.dispose();
            
            jS.conexao.desconectar();
        };
    }

}
