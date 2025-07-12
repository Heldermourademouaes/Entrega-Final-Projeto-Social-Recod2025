package eventos;

import conexao.conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContatoManager {

    public boolean adicionarContato(Contato contato) {
        String sql = "INSERT INTO contatos (nome, email, assunto, mensagem) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getEmail());
            stmt.setString(3, contato.getAssunto());
            stmt.setString(4, contato.getMensagem());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar contato: " + e.getMessage());
            return false;
        }
    }

    public List<Contato> listarContatos() {
        List<Contato> contatos = new ArrayList<>();
        String sql = "SELECT * FROM contatos";
        try (Connection conn = conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Contato contato = new Contato(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("assunto"),
                        rs.getString("mensagem")
                );
                contatos.add(contato);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar contatos: " + e.getMessage());
        }
        return contatos;
    }

    public Optional<Contato> buscarContatoPorEmail(String email) {
        String sql = "SELECT * FROM contatos WHERE email = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Contato contato = new Contato(
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("assunto"),
                            rs.getString("mensagem")
                    );
                    return Optional.of(contato);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar contato: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean atualizarContato(String emailAntigo, Contato contatoAtualizado) {
        String sql = "UPDATE contatos SET nome = ?, email = ?, assunto = ?, mensagem = ? WHERE email = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contatoAtualizado.getNome());
            stmt.setString(2, contatoAtualizado.getEmail());
            stmt.setString(3, contatoAtualizado.getAssunto());
            stmt.setString(4, contatoAtualizado.getMensagem());
            stmt.setString(5, emailAntigo);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar contato: " + e.getMessage());
            return false;
        }
    }

    public boolean removerContato(String email) {
        String sql = "DELETE FROM contatos WHERE email = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover contato: " + e.getMessage());
            return false;
        }
    }
}