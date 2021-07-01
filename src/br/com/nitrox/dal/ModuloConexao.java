package br.com.nitrox.dal;

import java.sql.*;

public class ModuloConexao {

    public static Connection conector() {
        Connection conexao = null;
        // A linha abaixo "chama" o driver
        String driver = "com.mysql.cj.jdbc.Driver";
        // Armazenando informações referentes ao banco
        String url = "jdbc:mysql://localhost:3306/dbnitroxprojeto";
        String user = "root";
        String password = "220100";
        // Estabelecendo a conexao com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            // A linha abaixo serve para exclarecer o erro
            // System.out.println(e);
            return null;
        }
    }
}
