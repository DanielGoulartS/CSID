package Cadastro;

import Model.Connection;
import java.sql.ResultSet;

/**
 *
 * @author Daniel
 */
public class Equipamento {

    String id, nome, quantidade;

    public Equipamento(String id, String nome, String quantidade) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public boolean insert(Connection conexao, Equipamento equipamento) {
        conexao.conectar();
        boolean permit;
        permit = conexao.execute("INSERT INTO `equipamentos`(`nome`, `quantidade`)"
                + " VALUES ('" + equipamento.nome + "','" + equipamento.quantidade + "');");

        conexao.desconectar();

        return permit;
    }

    public boolean delete(Connection conexao, Equipamento equipamento) {

        conexao.conectar();
        boolean result = false;

        if (conexao.execute("DELETE FROM equipamentos WHERE "
                + "`id` LIKE '%" + equipamento.id
                + "%' AND `nome` LIKE '%" + equipamento.nome + "%' ;")) {
            result = true;
        }

        conexao.desconectar();

        return result;
    }

    public ResultSet select(Connection conexao, Equipamento equipamento) {

        ResultSet rs;

        rs = conexao.executeQuery("SELECT * FROM equipamentos WHERE "
                + "`id` LIKE '%" + equipamento.id
                + "%' AND `nome` LIKE '%" + equipamento.nome + "%' ORDER BY `id` ASC;");

        return rs;
    }

    public boolean alter(Connection conexao, Equipamento equipamento) {

        conexao.conectar();
        boolean result;

        result = conexao.execute("UPDATE `equipamentos` SET `quantidade` = '" + equipamento.quantidade + "' WHERE "
                + "`nome` LIKE '%" + equipamento.nome + "%' AND `id` LIKE '%" + equipamento.id + "%';");

        conexao.desconectar();
        return result;
    }

}
