package Cadastro;

import Solicitacoes.JanelaTodasAsSolicitacoes;
import Solicitacoes.Solicitacao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;


/**
 *
 * @author Daniel
 */
public class Tecnico extends Usuario {

    
    public static ResultSet selectPainelMeusServicos(JanelaTodasAsSolicitacoes jTAS) {
        //Painel Meus Serviços
        ResultSet rs = jTAS.conexao.executeQuery("SELECT * FROM solicitacao WHERE encarregado = "
                        + jTAS.usuario.id + ";");
        return rs;
    }

    public Tecnico(int id, String nome, String sobrenome, String usuario, char[] senha, String cargo) {
        super(id, nome, sobrenome, usuario, senha, cargo);
    }

    @Override
    public void exibir(Janela janela) {

        janela.exibirInterfaceTecnico();

    }

    
    public static void verMeusServicos(JanelaTodasAsSolicitacoes jTAS) {
        jTAS.conexao.conectar();
        
        jTAS.painelSolicitacoes.removeAll();
        jTAS.painelMeusServicos.removeAll();
        jTAS.painelMinhasSolicitacoes.removeAll();
        //Busca as solicitacoes
        ResultSet rs = jTAS.solicitacao.selectMeusServicos(jTAS.conexao, jTAS.usuario);
        try {
            //Se houver ao menos uma
            if(rs.next()){
            //Para cada uma exibe-a num painel alocado na scroll principal
                do{
                    Solicitacao novaSolicitacao = new Solicitacao(rs.getInt("id"), rs.getInt("encarregado"), 
                            rs.getString("inicio"), rs.getString("fim"), rs.getInt("solicitante"), 
                            rs.getString("embarcacao"), rs.getString("porto"), rs.getString("servicos"), 
                            rs.getString("equipamentos"), rs.getString("obs"));
                
                jTAS.painelMeusServicos.add(jTAS.novoPainel(novaSolicitacao));
                }while(rs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger("Usuario.verMeusServicos()" + ex);
        }
        //exibe o scrollpane
        jTAS.scrollPane.setViewportView(jTAS.painelMeusServicos);
        jTAS.conexao.desconectar();
    }

    
}
