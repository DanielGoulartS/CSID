package Cadastro;

import Model.Connection;
import java.sql.ResultSet;

/**
 *
 * @author Daniel
 */
public class Servico {

    public String id, nome, descricao;

    public Servico(String id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public boolean insert(Connection conexao, Servico servico) {
        boolean permit;
        permit = conexao.execute("INSERT INTO `servicos`(`nome`, `descricao`)"
                + " VALUES ('" + servico.nome + "','" + servico.descricao + "');");


        return permit;
    }

    public boolean delete(Connection conexao, Servico servico) {

        boolean result = false;

        if (conexao.execute("DELETE FROM servicos WHERE "
                + "`id` LIKE '%" + servico.id
                + "%' AND `nome` LIKE '%" + servico.nome
                + "%' AND `descricao` LIKE '%" + servico.descricao + "%';")) {
            result = true;
        }


        return result;
    }

    public ResultSet select(Connection conexao, Servico servico) {
        ResultSet rs;

        rs = conexao.executeQuery("SELECT * FROM servicos WHERE "
                + "`id` LIKE '%" + servico.id
                + "%' AND `nome` LIKE '%" + servico.nome
                + "%' AND `descricao` LIKE '%" + servico.descricao + "%' ORDER BY `id` ASC;");

        return rs;
    }

}
