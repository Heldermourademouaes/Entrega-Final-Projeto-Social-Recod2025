package eventos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento {
    private String nome;
    private String local;
    private LocalDateTime dataHora;
    private String descricao;

    public Evento(String nome, String local, LocalDateTime dataHora, String descricao) {
        this.nome = nome;
        this.local = local;
        this.dataHora = dataHora;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Nome: " + nome + ", Local: " + local + ", Data/Hora: " + dataHora.format(formatter) + ", Descrição: " + descricao;
    }
}