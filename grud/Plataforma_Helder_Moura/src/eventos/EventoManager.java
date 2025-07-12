package eventos;

import conexao.conexao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventoManager {

    public boolean adicionarEvento(Evento evento) {
        String sql = "INSERT INTO eventos (nome, local, data_hora, descricao) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, evento.getNome());
            stmt.setString(2, evento.getLocal());
            stmt.setTimestamp(3, Timestamp.valueOf(evento.getDataHora()));
            stmt.setString(4, evento.getDescricao());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar evento: " + e.getMessage());
            return false;
        }
    }

    public List<Evento> listarEventos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM eventos";
        try (Connection conn = conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Evento evento = new Evento(
                        rs.getString("nome"),
                        rs.getString("local"),
                        rs.getTimestamp("dataHora").toLocalDateTime(),
                        rs.getString("descricao")
                );
                eventos.add(evento);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar eventos: " + e.getMessage());
        }
        return eventos;
    }

    public Optional<Evento> buscarEventoPorNome(String nome) {
        String sql = "SELECT * FROM eventos WHERE nome = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Evento evento = new Evento(
                            rs.getString("nome"),
                            rs.getString("local"),
                            rs.getTimestamp("dataHora").toLocalDateTime(),
                            rs.getString("descricao")
                    );
                    return Optional.of(evento);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar evento: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean atualizarEvento(String nomeAntigo, Evento eventoAtualizado) {
        String sql = "UPDATE eventos SET nome = ?, local = ?, dataHora = ?, descricao = ? WHERE nome = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventoAtualizado.getNome());
            stmt.setString(2, eventoAtualizado.getLocal());
            stmt.setTimestamp(3, Timestamp.valueOf(eventoAtualizado.getDataHora()));
            stmt.setString(4, eventoAtualizado.getDescricao());
            stmt.setString(5, nomeAntigo);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar evento: " + e.getMessage());
            return false;
        }
    }

    public boolean removerEvento(String nome) {
        String sql = "DELETE FROM eventos WHERE nome = ?";
        try (Connection conn = conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover evento: " + e.getMessage());
            return false;
        }
    }
}