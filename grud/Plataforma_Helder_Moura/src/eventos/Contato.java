package eventos;

public class Contato {
    private String nome;
    private String email;
    private String assunto;
    private String mensagem;

    public Contato(String nome, String email, String assunto, String mensagem) {
        this.nome = nome;
        this.email = email;
        this.assunto = assunto;
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + ", Email: " + email + ", Assunto: " + assunto + ", Mensagem: " + mensagem;
    }
}


