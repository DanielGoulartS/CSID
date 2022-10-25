package Solicitacoes;

import Cadastro.Usuario;
import Model.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class Solicitacao {

    //Variaveis
    public Date inicio, fim;
    public int id, encarregado, solicitante;
    public String embarcacao, porto, servicos, equipamentos, obs;

    //Consruções do Objeto
    public Solicitacao(int id, int encarregado, String inicio, String fim, int solicitante, String embarcacao,
            String porto, String servicos, String equipamentos, String itemUrgente) {

        this.id = id;
        this.encarregado = encarregado;
        this.inicio = stringToDate(inicio);
        this.fim = stringToDate(fim);
        this.solicitante = solicitante;
        this.embarcacao = embarcacao;
        this.porto = porto;
        this.servicos = servicos;
        this.equipamentos = equipamentos;
        this.obs = itemUrgente;
    }

    //Impressão do objeto
    @Override
    public String toString() {
        return dateToString(this.inicio) + "\n"
                + dateToString(this.fim) + "\n"
                + this.embarcacao + "\n"
                + this.porto + "\n"
                + this.servicos + "\n"
                + this.equipamentos + "\n"
                + this.obs;
    }

    //Converte a String recebida em Date
    public final Date stringToDate(String data) {
        Date data1 = new Date();
        try {
            SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
            data1 = sdt.parse(data);
        } catch (ParseException ex) {
            System.err.println("Falha na conversão: CSID.Classes.Solicitacao.formatarData()" + ex);
        }
        return data1;
    }

    //Converte a Date recebida em String
    public String dateToString(Date data) {
        SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
        String data1 = sdt.format(data);
        return data1;
    }

    public boolean insert(Connection conexao, Solicitacao solicitacao) {
        boolean result;

        result = true;
        conexao.execute("INSERT INTO `solicitacao`( `inicio`, `fim`, `encarregado`, "
                + "`solicitante`, `embarcacao`, `porto`, `servicos`, `equipamentos`, `obs`)"
                + " VALUES ('" + dateToString(solicitacao.inicio) + "','" + dateToString(solicitacao.fim) + "','"
                + solicitacao.encarregado + "','" + solicitacao.solicitante + "','" + solicitacao.embarcacao + "','"
                + solicitacao.porto + "','" + solicitacao.servicos + "','" + solicitacao.equipamentos
                + "','" + solicitacao.obs + "');");

        return result;
    }

    public boolean delete(Connection conexao, Solicitacao solicitacao) {
        boolean result = true;
        conexao.execute("DELETE FROM solicitacao WHERE `id` = "
                + solicitacao.id + ";");
        return result;
    }

    public ResultSet selectAll(Connection conexao) {
        //Painel Meus Serviços
        ResultSet rs = conexao.executeQuery("SELECT * FROM solicitacao ORDER BY `id`;");
        return rs;
    }

    public ResultSet selectPorId(Connection conexao, Solicitacao solicitacao) {
        //Painel Meus Serviços
        ResultSet rs = conexao.executeQuery("SELECT * FROM solicitacao WHERE `id` = " + solicitacao.id + " ;");
        return rs;
    }

    public ResultSet selectMinhasSolicitacoes(Connection conexao, Usuario usuario) {
        //Painel Meus Serviços
        ResultSet rs = conexao.executeQuery("SELECT * FROM solicitacao WHERE solicitante = "
                + usuario.id + ";");
        return rs;
    }

    public ResultSet selectMeusServicos(Connection conexao, Usuario usuario) {
        //Painel Meus Serviços
        ResultSet rs = conexao.executeQuery("SELECT * FROM solicitacao WHERE encarregado = "
                + usuario.id + ";");
        return rs;
    }

}
