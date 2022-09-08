package CSID;

import Model.Connection;
import View.Entrar;

/**
 *
 * @author Daniel
 */
//Classe principal
public class CSID{

    public static void main(String[] args) {
        Model.Connection conexao = new Connection();

        //Se não houverem as tabelas necesárias, crie-as
        primeiroAcesso(conexao);
        
        //Invocação da Solicitar principal
        new Entrar();

    }
    
    public static void primeiroAcesso(Connection conexao){
        
        conexao.conectar();
        
        if (conexao.execute("select * from solicitacao")) {
            System.out.println("Há solicitacao");
        } else {
            System.out.println("Não há solicitacao");
            conexao.execute("CREATE TABLE IF NOT EXISTS solicitacao (\n"
                    + "    id int not null AUTO_INCREMENT,\n"
                    + "    inicio varchar(10),\n"
                    + "    fim varchar(10),\n"
                    + "    navio varchar(40),\n"
                    + "    porto varchar(40),\n"
                    + "    cameras varchar(200),\n"
                    + "    internet varchar(200),\n"
                    + "    telefonia varchar(200),\n"
                    + "    itemSimples varchar(200),\n"
                    + "    itemUrgente varchar(200),\n"
                    + "    primary key (id)\n"
                    + ");");
        }
        
        if (conexao.execute("select * from usuarios")) {
            System.out.println("Há usuarios");
        } else {
            System.out.println("Não há usuarios");
            conexao.execute("CREATE TABLE IF NOT EXISTS `usuarios` ( "
                    + "`id` int NOT NULL AUTO_INCREMENT, "
                    + "`nome` varchar(40) DEFAULT NULL, "
                    + "`sobrenome` varchar(40) DEFAULT NULL, "
                    + "`usuario` varchar(40) DEFAULT NULL, "
                    + "`senha` varchar(40) DEFAULT NULL, "
                    + "`cargo` varchar(5) NOT NULL, "
                    + "PRIMARY KEY (`id`) "
                    + ");");
        }
        
        conexao.desconectar();
    }
}
