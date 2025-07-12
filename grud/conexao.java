package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    
    // Configurações da conexão
    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_helder_moura_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    
    private static Connection conn;
    
    // Método para obter a conexão
    public static Connection getConexao() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexão estabelecida com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
    
    // Método para fechar a conexão
    public static void fecharConexao() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexão fechada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Método para testar a conexão
    public static boolean testarConexao() {
        try {
            Connection teste = getConexao();
            return teste != null && !teste.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
}

// Classe de exemplo para uso da conexão
class ExemploUso {
    public static void main(String[] args) {
        // Testando a conexão
        if (Conexao.testarConexao()) {
            System.out.println("Banco de dados conectado e funcionando!");
        } else {
            System.out.println("Falha na conexão com o banco de dados.");
        }
        
        // Fechando a conexão ao final
        Conexao.fecharConexao();
    }
}S