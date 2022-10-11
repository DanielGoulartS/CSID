package Cadastro;

import Model.Connection;
import java.sql.ResultSet;

/**
 *
 * @author Daniel
 */
public class Porto {

    public String id, telefone, nome, endereco, email;

    public Porto(String id, String telefone, String nome, String endereco, String email) {
        this.id = id;
        this.telefone = telefone;
        this.nome = nome;
        this.endereco = endereco;
        this.email = email;
    }

    public boolean insert(Connection conexao, Porto porto) {
        conexao.conectar();
        boolean permit;
        permit = conexao.execute("INSERT INTO `portos`(`nome`, `endereco`, `telefone`, `email`)"
                + " VALUES ('" + porto.nome + "','" + porto.endereco + "','" + porto.telefone + "','" + porto.email + "');");

        conexao.desconectar();

        return permit;
    }

    public boolean delete(Connection conexao, Porto porto) {

        conexao.conectar();
        boolean result = false;

        if (conexao.execute("DELETE FROM `portos` WHERE `nome` = '" + porto.nome + "';")) {
            result = true;
        }

        conexao.desconectar();

        return result;
    }

    public ResultSet select(Connection conexao, Porto porto){
        ResultSet rs;

        rs = conexao.executeQuery("SELECT * FROM portos WHERE "
                + "`id` LIKE '%" + porto.id 
                + "%' AND `nome` LIKE '%" + porto.nome
                + "%' AND `endereco` LIKE '%" + porto.endereco
                + "%' AND `telefone` LIKE '%" + porto.telefone
                + "%' AND `email` LIKE '%" + porto.email + "%';");

        return rs;
    }

}