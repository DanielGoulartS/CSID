package Model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Daniel
 */
public class Connection {

    private java.sql.Connection connection = null;

    public boolean conectar() {
        
        boolean valid = false;
        
        try {
            //URL da base de dados
            String url = "jdbc:mysql://localhost:3306/csid";
            //Se mão estiver conectado, conecte
            if (this.connection == null || !this.connection.isValid(10)) {
                this.connection = DriverManager.getConnection(url, "root", "");
            }
            //Está?
            valid = this.connection.isValid(10);
            if (valid) {
                System.out.println("Conectado!");
            }
            
        } catch (SQLException e) {
            System.err.println("Falha na conexão, Model.Connection.conectar()");
            System.err.println(e);
        }
        
        return valid;
    }

    public boolean desconectar() {

        try {
            if (!this.connection.isClosed()) {
                this.connection.close();
            }
            System.out.println("Desconectado!");
        } catch (SQLException e) {
            System.out.println("Erro ao Desconectar!");
            return false;
        }
        return true;
    }

    public Statement createStatement() {
        try {
            return this.connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Falha na criação do Statement");
            return null;
        }
    }

    public java.sql.Connection getConnection() {
        return this.connection;
    }

    public ResultSet executeQuery(String sql) {

        try {
            Statement stmt = createStatement();
            System.out.println("ExecuteQuery executada com sucesso.");
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }

    public boolean execute(String sql) {

        try {
            Statement stmt = createStatement();
            System.out.println("Execute executada com sucesso.");
            return stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println(e);
        }
        return false;
    }

}
