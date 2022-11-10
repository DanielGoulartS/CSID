package Cadastro;

import Solicitacoes.JanelaSolicitar;
import Solicitacoes.Solicitacao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


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

    //Cria o objeto solicitacao e tomará ações com ele
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
