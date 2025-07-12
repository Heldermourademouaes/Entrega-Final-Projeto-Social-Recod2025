package com.agenda.app;

import com.agenda.manager.EventoManager;
import com.agenda.model.Evento;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static EventoManager eventoManager = new EventoManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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
                case 0:
                    System.out.println("Saindo do sistema. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println(); // Linha em branco para melhor visualização
        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("---- Menu da Agenda de Eventos ----");
        System.out.println("1. Adicionar Evento");
        System.out.println("2. Listar Eventos");
        System.out.println("3. Atualizar Evento");
        System.out.println("4. Remover Evento");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next(); // Consumir a entrada inválida
            System.out.print("Escolha uma opção: ");
        }
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha
        return opcao;
    }

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
                dataHora = LocalDateTime.parse(dataHoraStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                dataValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data/hora inválido. Use dd/MM/yyyy HH:mm.");
            }
        }

        System.out.print("Descrição do Evento: ");
        String descricao = scanner.nextLine();

        Evento novoEvento = new Evento(nome, local, dataHora, descricao);
        eventoManager.adicionarEvento(novoEvento);
    }

    private static void listarEventos() {
        System.out.println("\n---- Lista de Eventos ----");
        List<Evento> eventos = eventoManager.listarEventos();
        if (eventos.isEmpty()) {
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

        if (eventoManager.buscarEventoPorNome(nomeAntigo).isEmpty()) {
            System.out.println("Evento \"" + nomeAntigo + "\" não encontrado.");
            return;
        }

        System.out.println("Digite os novos dados para o evento (deixe em branco para manter o atual):");

        System.out.print("Novo Nome do Evento (atual: " + eventoManager.buscarEventoPorNome(nomeAntigo).get().getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (novoNome.isEmpty()) novoNome = eventoManager.buscarEventoPorNome(nomeAntigo).get().getNome();

        System.out.print("Novo Local do Evento (atual: " + eventoManager.buscarEventoPorNome(nomeAntigo).get().getLocal() + "): ");
        String novoLocal = scanner.nextLine();
        if (novoLocal.isEmpty()) novoLocal = eventoManager.buscarEventoPorNome(nomeAntigo).get().getLocal();

        LocalDateTime novaDataHora = null;
        boolean dataValida = false;
        while (!dataValida) {
            System.out.print("Nova Data e Hora do Evento (dd/MM/yyyy HH:mm) (atual: " + eventoManager.buscarEventoPorNome(nomeAntigo).get().getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "): ");
            String novaDataHoraStr = scanner.nextLine();
            if (novaDataHoraStr.isEmpty()) {
                novaDataHora = eventoManager.buscarEventoPorNome(nomeAntigo).get().getDataHora();
                dataValida = true;
            } else {
                try {
                    novaDataHora = LocalDateTime.parse(novaDataHoraStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                    dataValida = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de data/hora inválido. Use dd/MM/yyyy HH:mm.");
                }
            }
        }

        System.out.print("Nova Descrição do Evento (atual: " + eventoManager.buscarEventoPorNome(nomeAntigo).get().getDescricao() + "): ");
        String novaDescricao = scanner.nextLine();
        if (novaDescricao.isEmpty()) novaDescricao = eventoManager.buscarEventoPorNome(nomeAntigo).get().getDescricao();

        Evento eventoAtualizado = new Evento(novoNome, novoLocal, novaDataHora, novaDescricao);
        eventoManager.atualizarEvento(nomeAntigo, eventoAtualizado);
    }

    private static void removerEvento() {
        System.out.println("\n---- Remover Evento ----");
        System.out.print("Digite o NOME do evento que deseja remover: ");
        String nome = scanner.nextLine();
        eventoManager.removerEvento(nome);
    }
}


