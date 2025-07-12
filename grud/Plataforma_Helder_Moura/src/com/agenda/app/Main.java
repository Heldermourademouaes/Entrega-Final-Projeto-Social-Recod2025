package com.agenda.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import conexao.conexao;
import eventos.Contato;
import eventos.ContatoManager;
import eventos.Evento;
import eventos.EventoManager;

public class Main {
    private static EventoManager eventoManager = new EventoManager();
    private static ContatoManager contatoManager = new ContatoManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (!conexao.testarConexao()) {
            System.out.println("Falha na conexão com o banco de dados.");
            return;
        }
        System.out.println("Banco de dados conectado e funcionando!");

        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarEvento();
                    break;
                case 2:
                    listarEventos();
                    break;
                case 3:
                    atualizarEvento();
                    break;
                case 4:
                    removerEvento();
                    break;
                case 5:
                    adicionarContato();
                    break;
                case 6:
                    listarContatos();
                    break;
                case 7:
                    atualizarContato();
                    break;
                case 8:
                    removerContato();
                    break;
                case 0:
                    System.out.println("Saindo do sistema. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        } while (opcao != 0);

        conexao.fecharConexao();
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("---- Menu da Agenda de Eventos ----");
        System.out.println("1. Adicionar Evento");
        System.out.println("2. Listar Eventos");
        System.out.println("3. Atualizar Evento");
        System.out.println("4. Remover Evento");
        System.out.println("---- Menu de Contatos ----");
        System.out.println("5. Adicionar Contato");
        System.out.println("6. Listar Contatos");
        System.out.println("7. Atualizar Contato");
        System.out.println("8. Remover Contato");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next();
            System.out.print("Escolha uma opção: ");
        }
        int opcao = scanner.nextInt();
        scanner.nextLine();
        return opcao;
    }

    // CRUD EVENTO
    private static void adicionarEvento() {
        System.out.println("\n---- Adicionar Novo Evento ----");
        System.out.print("Nome do Evento: ");
        String nome = scanner.nextLine();

        System.out.print("Local do Evento: ");
        String local = scanner.nextLine();

        LocalDateTime dataHora = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Data e Hora do Evento (dd/MM/yyyy HH:mm): ");
            String dataHoraStr = scanner.nextLine();
            try {
                dataHora = LocalDateTime.parse(dataHoraStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                dataValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data/hora inválido. Use dd/MM/yyyy HH:mm.");
            }
        }

        System.out.print("Descrição do Evento: ");
        String descricao = scanner.nextLine();

        Evento novoEvento = new Evento(nome, local, dataHora, descricao);
        boolean sucesso =1 eventoManager.adicionarEvento(novoEvento);
        if (sucesso) {
            System.out.println("Evento adicionado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar evento.");
        }
    }

    private static void listarEventos() {
        System.out.println("\n---- Lista de Eventos ----");
        List<Evento> eventos = eventoManager.listarEventos();
        if (eventos == null || eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
        } else {
            for (int i = 0; i < eventos.size(); i++) {
                System.out.println((i + 1) + ". " + eventos.get(i));
            }
        }
    }

    private static void atualizarEvento() {
        System.out.println("\n---- Atualizar Evento ----");
        System.out.print("Digite o NOME do evento que deseja atualizar: ");
        String nomeAntigo = scanner.nextLine();

        Optional<Evento> eventoOpt = eventoManager.buscarEventoPorNome(nomeAntigo);
        if (eventoOpt.isEmpty()) {
            System.out.println("Evento \"" + nomeAntigo + "\" não encontrado.");
            return;
        }
        Evento eventoAntigo = eventoOpt.get();

        System.out.println("Digite os novos dados para o evento (deixe em branco para manter o atual):");

        System.out.print("Novo Nome do Evento (atual: " + eventoAntigo.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (novoNome.isEmpty()) novoNome = eventoAntigo.getNome();

        System.out.print("Novo Local do Evento (atual: " + eventoAntigo.getLocal() + "): ");
        String novoLocal = scanner.nextLine();
        if (novoLocal.isEmpty()) novoLocal = eventoAntigo.getLocal();

        LocalDateTime novaDataHora = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Nova Data e Hora do Evento (dd/MM/yyyy HH:mm) (atual: " + eventoAntigo.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "): ");
            String novaDataHoraStr = scanner.nextLine();
            if (novaDataHoraStr.isEmpty()) {
                novaDataHora = eventoAntigo.getDataHora();
                dataValida = true;
            } else {
                try {
                    novaDataHora = LocalDateTime.parse(novaDataHoraStr, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    dataValida = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data/hora inválido. Use dd/MM/yyyy HH:mm.");
                }
            }
        }

        System.out.print("Nova Descrição do Evento (atual: " + eventoAntigo.getDescricao() + "): ");
        String novaDescricao = scanner.nextLine();
        if (novaDescricao.isEmpty()) novaDescricao = eventoAntigo.getDescricao();

        Evento eventoAtualizado = new Evento(novoNome, novoLocal, novaDataHora, novaDescricao);
        boolean sucesso = eventoManager.atualizarEvento(nomeAntigo, eventoAtualizado);
        if (sucesso) {
            System.out.println("Evento atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar evento.");
        }
    }

    private static void removerEvento() {
        System.out.println("\n---- Remover Evento ----");
        System.out.print("Digite o NOME do evento que deseja remover: ");
        String nome = scanner.nextLine();
        boolean sucesso = eventoManager.removerEvento(nome);
        if (sucesso) {
            System.out.println("Evento removido com sucesso!");
        } else {
            System.out.println("Erro ao remover evento.");
        }
    }

    // CRUD CONTATO
    private static void adicionarContato() {
        System.out.println("\n---- Adicionar Novo Contato ----");
        System.out.print("Nome do Contato: ");
        String nome = scanner.nextLine();

        System.out.print("Email do Contato: ");
        String email = scanner.nextLine();

        System.out.print("Assunto do Contato: ");
        String assunto = scanner.nextLine();

        System.out.print("Mensagem do Contato: ");
        String mensagem = scanner.nextLine();

        Contato novoContato = new Contato(nome, email, assunto, mensagem);
        boolean sucesso = contatoManager.adicionarContato(novoContato);
        if (sucesso) {
            System.out.println("Contato adicionado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar contato.");
        }
    }

    private static void listarContatos() {
        System.out.println("\n---- Lista de Contatos ----");
        List<Contato> contatos = contatoManager.listarContatos();
        if (contatos == null || contatos.isEmpty()) {
            System.out.println("Nenhum contato cadastrado.");
        } else {
            for (int i = 0; i < contatos.size(); i++) {
                System.out.println((i + 1) + ". " + contatos.get(i));
            }
        }
    }

    private static void atualizarContato() {
        System.out.println("\n---- Atualizar Contato ----");
        System.out.print("Digite o EMAIL do contato que deseja atualizar: ");
        String emailAntigo = scanner.nextLine();

        Optional<Contato> contatoOpt = contatoManager.buscarContatoPorEmail(emailAntigo);
        if (contatoOpt.isEmpty()) {
            System.out.println("Contato com email \"" + emailAntigo + "\" não encontrado.");
            return;
        }
        Contato contatoAntigo = contatoOpt.get();

        System.out.println("Digite os novos dados para o contato (deixe em branco para manter o atual):");

        System.out.print("Novo Nome do Contato (atual: " + contatoAntigo.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (novoNome.isEmpty()) novoNome = contatoAntigo.getNome();

        System.out.print("Novo Email do Contato (atual: " + contatoAntigo.getEmail() + "): ");
        String novoEmail = scanner.nextLine();
        if (novoEmail.isEmpty()) novoEmail = contatoAntigo.getEmail();

        System.out.print("Novo Assunto do Contato (atual: " + contatoAntigo.getAssunto() + "): ");
        String novoAssunto = scanner.nextLine();
        if (novoAssunto.isEmpty()) novoAssunto = contatoAntigo.getAssunto();

        System.out.print("Nova Mensagem do Contato (atual: " + contatoAntigo.getMensagem() + "): ");
        String novaMensagem = scanner.nextLine();
        if (novaMensagem.isEmpty()) novaMensagem = contatoAntigo.getMensagem();

        Contato contatoAtualizado = new Contato(novoNome, novoEmail, novoAssunto, novaMensagem);
        boolean sucesso = contatoManager.atualizarContato(emailAntigo, contatoAtualizado);
        if (sucesso) {
            System.out.println("Contato atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar contato.");
        }
    }

    private static void removerContato() {
        System.out.println("\n---- Remover Contato ----");
        System.out.print("Digite o EMAIL do contato que deseja remover: ");
        String email = scanner.nextLine();
        boolean sucesso = contatoManager.removerContato(email);
        if (sucesso) {
            System.out.println("Contato removido com sucesso!");
        } else {
            System.out.println("Erro ao remover contato.");
        }
    }
}