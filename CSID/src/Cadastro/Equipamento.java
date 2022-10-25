package Cadastro;

import Model.Connection;
import java.sql.ResultSet;

/**
 *
 * @author Daniel
 */
public class Equipamento {

    public String id, nome, quantidade;

    public Equipamento(String id, String nome, String quantidade) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public boolean insert(Connection conexao, Equipamento equipamento) {
        boolean permit;
        permit = conexao.execute("INSERT INTO `equipamentos`(`nome`, `quantidade`)"
                + " VALUES ('" + equipamento.nome + "','" + equipamento.quantidade + "');");

        return permit;
    }

    public boolean delete(Connection conexao, Equipamento equipamento) {
        boolean result = false;

        if (conexao.execute("DELETE FROM equipamentos WHERE "
                + "`id` = '" + equipamento.id
                + "' AND `nome` = '" + equipamento.nome + "' ;")) {
            result = true;
        }

        return result;
    }

    public ResultSet select(Connection conexao, Equipamento equipamento) {

        ResultSet rs;

        rs = conexao.executeQuery("SELECT * FROM equipamentos WHERE "
                + "`id` LIKE '%" + equipamento.id
                + "%' AND `nome` LIKE '%" + equipamento.nome + "%' ORDER BY `id` ASC;");

        return rs;
    }

    public ResultSet selectUnico(Connection conexao, Equipamento equipamento) {

        ResultSet rs;

        rs = conexao.executeQuery("SELECT * FROM equipamentos WHERE "
                + "`id` = '" + equipamento.id
                + "' AND `nome` = '" + equipamento.nome + "' ORDER BY `id` ASC;");

        return rs;
    }


    public boolean alter(Connection conexao, Equipamento equipamento) {
        boolean result;

        result = conexao.execute("UPDATE `equipamentos` SET `quantidade` = '" + equipamento.quantidade + "' WHERE "
                + "`nome` = '" + equipamento.nome + "' AND `id` = '" + equipamento.id + "';");

        return result;
    }

}
