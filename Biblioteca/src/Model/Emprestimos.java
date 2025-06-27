package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import Dao.EmprestimosDao;
import Dao.UsuarioDao;
import Dao.LivrosDao;

public class Emprestimos {
    private int cd_emprestimo;
    private int cd_livro;
    private int cd_usuario;
    private int cd_funcionario;
    private String nomeLivro;
    private String nomeUsuario;
    private String nomeFuncionario;
    private LocalDate dt_emprestimo;
    private LocalDate dt_devolucao_prevista;
    private LocalDate dt_devolucao_real;

    // Getters e Setters
    public int getCd_emprestimo() {
        return cd_emprestimo;
    }

    public void setCd_emprestimo(int cd_emprestimo) {
        this.cd_emprestimo = cd_emprestimo;
    }

    public int getCd_livro() {
        return cd_livro;
    }

    public void setCd_livro(int cd_livro) {
        this.cd_livro = cd_livro;
    }

    public int getCd_usuario() {
        return cd_usuario;
    }

    public void setCd_usuario(int cd_usuario) {
        this.cd_usuario = cd_usuario;
    }

    public int getCd_funcionario() {
        return cd_funcionario;
    }

    public void setCd_funcionario(int cd_funcionario) {
        this.cd_funcionario = cd_funcionario;
    }

    public String getNomeLivro() {
        return nomeLivro;
    }

    public void setNomeLivro(String nomeLivro) {
        this.nomeLivro = nomeLivro;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public LocalDate getDt_emprestimo() {
        return dt_emprestimo;
    }

    public void setDt_emprestimo(LocalDate dt_emprestimo) {
        this.dt_emprestimo = dt_emprestimo;
    }

    public LocalDate getDt_devolucao_prevista() {
        return dt_devolucao_prevista;
    }

    public void setDt_devolucao_prevista(LocalDate dt_devolucao_prevista) {
        this.dt_devolucao_prevista = dt_devolucao_prevista;
    }

    public LocalDate getDt_devolucao_real() {
        return dt_devolucao_real;
    }

    public void setDt_devolucao_real(LocalDate dt_devolucao_real) {
        this.dt_devolucao_real = dt_devolucao_real;
    }

    // Lógica de
    // empréstimo------------------------------------------------------------
    public static void realizarEmprestimo(Scanner sc, int cdFuncionario) {
        Emprestimos emprestimo = new Emprestimos();

        System.out.print("ID do Livro: ");
        int cdLivro = sc.nextInt();
        sc.nextLine(); // consumir quebra de linha

        // Verifica se o livro existe
        if (!LivrosDao.livroExiste(cdLivro)) {
            System.out.println(" Livro com ID " + cdLivro + " não encontrado. Empréstimo cancelado.");
            return;
        }

        // Verifica se o livro já está emprestado e não devolvido
        if (EmprestimosDao.livroEstaEmprestado(cdLivro)) {
            System.out.println(" Este livro já está emprestado e não foi devolvido.");
            return;
        }

        emprestimo.setCd_livro(cdLivro);

        System.out.print("ID do Usuário: ");
        int cdUsuario = sc.nextInt();
        sc.nextLine();

        // Verifica se o usuário tem empréstimos pendentes (atrasados)
        if (EmprestimosDao.usuarioTemEmprestimosPendentes(cdUsuario)) {
            System.out.println(" Usuário possui empréstimo em atraso. Regularize antes de fazer novos empréstimos.");
            return;
        }

        // Verifica se o usuário já tem 3 empréstimos ativos
        int qtdAtivos = EmprestimosDao.contarEmprestimosAtivos(cdUsuario);
        if (qtdAtivos >= 3) {
            System.out.println(" Usuário já possui 3 empréstimos ativos. Devolva algum livro antes de pegar outro.");
            return;
        }

        emprestimo.setCd_usuario(cdUsuario);

        // Validação da senha do usuário
        System.out.print("Senha do usuário para autorizar o empréstimo: ");
        String senhaUsuario = sc.nextLine().trim();

        boolean senhaValida = UsuarioDao.validarSenhaUsuario(cdUsuario, senhaUsuario);
        if (!senhaValida) {
            System.out.println(" Senha incorreta! Empréstimo cancelado.");
            return;
        }

        // Define o funcionário e as datas
        emprestimo.setCd_funcionario(cdFuncionario);
        emprestimo.setDt_emprestimo(LocalDate.now());
        emprestimo.setDt_devolucao_prevista(LocalDate.now().plusDays(-1));// Tempo de emprestimo

        // Registra o empréstimo no banco
        EmprestimosDao.registrarEmprestimo(emprestimo);

        // Atualiza o status do livro para emprestado
        LivrosDao.atualizarStatusEmprestimo(cdLivro, "S");

        System.out.println("Livro emprestado com sucesso!");
        System.out.println("Data de devolução prevista: "
                + emprestimo.getDt_devolucao_prevista().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    // Registra o empréstimo no banco

    public static void registrarDevolucao(Scanner sc) {
        System.out.print("Informe o código do livro para devolução: ");
        int cdLivro = sc.nextInt();
        sc.nextLine();

        Emprestimos emprestimo = EmprestimosDao.buscarEmprestimoAtivoPorLivro(cdLivro);

        if (emprestimo != null) {
            System.out.println("Usuário que pegou o livro: " + emprestimo.getNomeUsuario());
            EmprestimosDao.registrarDevolucao(emprestimo.getCd_emprestimo(), LocalDate.now());
            LivrosDao.atualizarStatusEmprestimo(cdLivro, "N");
            System.out.println("Devolução registrada com sucesso!");
        } else {
            System.out.println("Nenhum empréstimo ativo encontrado para este livro.");
        }
    }

    public static void listarTodosEmprestimos() {
        List<Emprestimos> lista = EmprestimosDao.listarEmprestimosNaoDevolvidos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (lista.isEmpty()) {
            System.out.println("Nenhum empréstimo em aberto.");
        } else {
            System.out.println("=== Empréstimos Ativos (Não Devolvidos) ===");
            for (Emprestimos e : lista) {
                System.out.println("Empréstimo ID: " + e.getCd_emprestimo());
                System.out.println("Usuário: " + e.getNomeUsuario());
                System.out.println("Livro: " + e.getNomeLivro());
                System.out.println("Funcionário: " + e.getNomeFuncionario());
                System.out.println("Data do Empréstimo: " + e.getDt_emprestimo().format(formatter));
                System.out.println("Data Prevista de Devolução: " + e.getDt_devolucao_prevista().format(formatter));
                System.out.println("-------------------------------------------");
            }
        }
    }

    public static void listarEmprestimosPendentes() {
        List<Emprestimos> pendentes = EmprestimosDao.listarEmprestimosPendentes();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (pendentes.isEmpty()) {
            System.out.println("Nenhum empréstimo pendente ou vencido no momento.");
        } else {
            System.out.println("=== Empréstimos Pendentes ou Vencidos ===");
            for (Emprestimos e : pendentes) {
                long diasAtrasado = java.time.temporal.ChronoUnit.DAYS.between(
                        e.getDt_devolucao_prevista(), LocalDate.now());

                System.out.println("Nome do Usuário: " + e.getNomeUsuario());
                System.out.println("Livro: " + e.getNomeLivro());
                System.out.println("Data Prevista de Devolução: " + e.getDt_devolucao_prevista().format(formatter));
                System.out.println("Dias em Atraso: " + diasAtrasado);
                System.out.println("----------------------------------------");
            }
        }
    }

    public static void imprimirHistoricoPorUsuario(Scanner sc) {
        System.out.print("ID do Usuário: ");
        int cdUsuario = sc.nextInt();
        List<Emprestimos> lista = EmprestimosDao.listarHistoricoPorUsuario(cdUsuario);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Usuarios usuario = UsuarioDao.getByIdUsuarios(cdUsuario);
    if (usuario == null || usuario.getCd_usuario() == 0) {
        System.out.println("Usuário não encontrado. Verifique o ID informado.");
        return;
    }

        // Verifica se o usuário possui empréstimos registrados
        if (lista.isEmpty()) {
            System.out.println("Este usuário não possui nenhum empréstimo registrado.");
            return;
        }

        for (Emprestimos e : lista) {
            System.out.println("Livro: " + e.getNomeLivro());
            System.out.println("Empréstimo: " + e.getDt_emprestimo().format(formatter));
            System.out.println("Devolução: "
                    + (e.getDt_devolucao_real() != null ? e.getDt_devolucao_real().format(formatter)
                            : "Ainda não devolvido"));
            System.out.println("-----------------------------");
        }
    }

    private static int lerIntComValidacao(Scanner sc, String mensagem) {
        int valor;
        while (true) {
            System.out.print(mensagem);
            if (sc.hasNextInt()) {
                valor = sc.nextInt();
                sc.nextLine(); // consumir a quebra de linha pendente
                break;
            } else {
                System.out.println("Entrada inválida! Digite uma opção válida.");
                sc.nextLine(); // descarta a entrada inválida
            }
        }
        return valor;
    }

    public static void menuEmprestimo(Scanner sc, int cdFuncionario) {
        int opcao;
        do {
            System.out.println("\n*** MENU EMPRÉSTIMOS ***");
            System.out.println("1: Realizar Empréstimo");
            System.out.println("2: Realizar Devolução");
            System.out.println("3: Listar Empréstimos Ativos");
            System.out.println("4: Consultar Histórico por Usuário");
            System.out.println("5: Verificar Empréstimos Pendentes");
            System.out.println("0: Sair");
            opcao = lerIntComValidacao(sc, "ESCOLHA UMA OPÇÃO:  \n");
            System.out.println();

            switch (opcao) {
                case 1 -> realizarEmprestimo(sc, cdFuncionario);
                case 2 -> registrarDevolucao(sc);
                case 3 -> listarTodosEmprestimos();
                case 4 -> imprimirHistoricoPorUsuario(sc);
                case 5 -> listarEmprestimosPendentes();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }
}
