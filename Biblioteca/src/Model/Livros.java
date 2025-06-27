package Model;

import java.util.List;
import java.util.Scanner;

import Dao.LivrosDao;

public class Livros {

    private int cd_livro;
    private String isbn;
    private String titulo_livro;
    private String ano_publicado;
    private String autor_livro;
    private String emprestado; // "S" ou "N"

    public Livros() {
    }

    public Livros(int cd_livro, String isbn, String titulo_livro, String ano_publicado, String autor_livro,
            String emprestado) {
        this.cd_livro = cd_livro;
        this.isbn = isbn;
        this.titulo_livro = titulo_livro;
        this.ano_publicado = ano_publicado;
        this.autor_livro = autor_livro;
        this.emprestado = emprestado;
    }

    public int getCd_livro() {
        return cd_livro;
    }

    public void setCd_livro(int cd_livro) {
        this.cd_livro = cd_livro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo_livro() {
        return titulo_livro;
    }

    public void setTitulo_livro(String titulo_livro) {
        this.titulo_livro = titulo_livro;
    }

    public String getAno_publicado() {
        return ano_publicado;
    }

    public void setAno_publicado(String ano_publicado) {
        this.ano_publicado = ano_publicado;
    }

    public String getAutor_livro() {
        return autor_livro;
    }

    public void setAutor_livro(String autor_livro) {
        this.autor_livro = autor_livro;
    }

    public String getEmprestado() {
        return emprestado;
    }

    public void setEmprestado(String emprestado) {
        this.emprestado = (emprestado != null && emprestado.equalsIgnoreCase("S")) ? "S" : "N";
    }

    @Override
    public String toString() {
        return "Código Livro: " + cd_livro +
                " | ISBN: " + isbn +
                " | Título: " + titulo_livro +
                " | Ano: " + ano_publicado +
                " | Autor: " + autor_livro +
                " | Emprestado: " + ("S".equalsIgnoreCase(emprestado) ? "Sim" : "Não");
    }

    // Métodos estáticos de interação com o DAO
    public static void inserirLivro(Scanner sc) {
        Livros l = new Livros();

        System.out.print("Título do livro: ");
        l.setTitulo_livro(sc.nextLine());

        System.out.print("Ano de publicação: ");
        l.setAno_publicado(sc.nextLine());

        System.out.print("Autor do livro: ");
        l.setAutor_livro(sc.nextLine());

        System.out.print("ISBN: ");
        l.setIsbn(sc.nextLine());

        l.setEmprestado("N");

        Dao.LivrosDao.inserirLivros(l);
        System.out.println("*** Livro cadastrado com sucesso ***");

    }

    public static void listarLivro() {
        List<Livros> livros = Dao.LivrosDao.getAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            for (Livros l : livros) {
                System.out.println(l);
            }
        }
    }

    public static void alterarLivro(Scanner sc) {
        System.out.print("Digite o código do livro: ");
        int id = sc.nextInt();
        sc.nextLine();

        Livros l = Dao.LivrosDao.getByIdLivros(id);

        if (l.getCd_livro() == 0 || l.getTitulo_livro() == null) {
            System.out.println("Livro não encontrado.");
            return;
        }

        System.out.println("Atual: " + l);

        System.out.print("Novo título: ");
        l.setTitulo_livro(sc.nextLine());

        System.out.print("Novo ISBN: ");
        l.setIsbn(sc.nextLine());

        System.out.print("Novo ano de publicação: ");
        l.setAno_publicado(sc.nextLine());

        System.out.print("Novo autor: ");
        l.setAutor_livro(sc.nextLine());

        Dao.LivrosDao.alterarLivros(l);
        System.out.println("*** Livro alterado com sucesso ***");

    }

    public static void excluirLivro(Scanner sc) {
        System.out.print("Digite o código do livro: ");
        int id = sc.nextInt();

        Livros l = LivrosDao.getByIdLivros(id);
        if (l == null || l.getTitulo_livro() == null) {
            System.out.println("Livro não encontrado");
        }
        Dao.LivrosDao.excluirLivro(id);
        System.out.println("*** Livro excluído com sucesso ***");

    }

    public static void buscarLivro(Scanner sc) {
        System.out.print("Digite o código do livro: ");
        int id = sc.nextInt();

        Livros l = Dao.LivrosDao.getByIdLivros(id);

        if (l == null) {
            System.out.println("Livro não encontrado.");
        } else {
            System.out.println(l);
        }
    }

    // Métodos auxiliares para empréstimo/devolução
    public static boolean estaEmprestado(int cdLivro) {
        Livros l = Dao.LivrosDao.getByIdLivros(cdLivro);
        return l != null && "S".equalsIgnoreCase(l.getEmprestado());
    }

    public static void marcarComoEmprestado(int cdLivro) {
        Dao.LivrosDao.atualizarStatusEmprestimo(cdLivro, "S");
    }

    public static void marcarComoDisponivel(int cdLivro) {
        Dao.LivrosDao.atualizarStatusEmprestimo(cdLivro, "N");
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

    public static void menuLivro(Scanner sc) {
        int opcao;

        do {
            System.out.println("*** Menu Livro ***");
            System.out.println("1: Inserir Livro");
            System.out.println("2: Listar Livros");
            System.out.println("3: Editar Livro");
            System.out.println("4: Excluir Livro");
            System.out.println("5: Buscar Livro por Código");
            System.out.println("0: Voltar");
            opcao = lerIntComValidacao(sc, "ESCOLHA UMA OPÇÃO:  \n");
            
            System.out.println();

            switch (opcao) {
                case 1 -> inserirLivro(sc);
                case 2 -> listarLivro();
                case 3 -> alterarLivro(sc);
                case 4 -> excluirLivro(sc);
                case 5 -> buscarLivro(sc);
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("Opção inválida.");
            }
           

        } while (opcao != 0);
    }

}
