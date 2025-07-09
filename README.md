# 📚 Sistema de Gerenciamento de Biblioteca

Este projeto é um sistema de gerenciamento de empréstimos de livros, desenvolvido em Java puro e com conexão a um banco de dados MySQL.

## 🚀 Como Rodar o Projeto

Para executar este projeto na sua máquina, siga os passos abaixo:

### ✅ Pré-requisitos

- Java instalado 
- MySQL instalado e configurado

### 🔧 Configuração

1. Abra a classe `ConexaoDB` no projeto.
2. Altere as variáveis `user` e `password` para o usuário e senha do seu MySQL local.

String user = "seu_usuario"; 

String password = "sua_senha";

### 🛠️ Comandos SQL para Criar o Banco de Dados e Tabelas

```
`-- Criar banco de dados
CREATE DATABASE biblioteca;
USE biblioteca;

-- Tabela de Funcionários
CREATE TABLE Funcionarios (
    cd_funcionario INT PRIMARY KEY AUTO_INCREMENT,
    nm_funcionario VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- Tabela de Livros
CREATE TABLE Livros (
    cd_livro INT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(20),
    titulo_livro VARCHAR(255),
    ano_publicado VARCHAR(4),
    autor_livro VARCHAR(255),
    emprestado CHAR(1) -- 'S' ou 'N'
);

-- Tabela de Usuários
CREATE TABLE Usuarios (
    cd_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nm_usuario VARCHAR(255) NOT NULL,
    nr_telefone VARCHAR(20),
    ds_email VARCHAR(255),
    senha VARCHAR(255) NOT NULL
);

-- Tabela de Empréstimos
CREATE TABLE Emprestimos (
    cd_emprestimo INT PRIMARY KEY AUTO_INCREMENT,
    cd_livro INT,
    cd_usuario INT,
    cd_funcionario INT,
    nomeLivro VARCHAR(255),
    nomeUsuario VARCHAR(255),
    nomeFuncionario VARCHAR(255),
    dt_emprestimo DATE,
    dt_devolucao_prevista DATE,
    dt_devolucao_real DATE,
    
    -- Definindo as chaves estrangeiras
    FOREIGN KEY (cd_livro) REFERENCES Livros(cd_livro),
    FOREIGN KEY (cd_usuario) REFERENCES Usuarios(cd_usuario),
    FOREIGN KEY (cd_funcionario) REFERENCES Funcionarios(cd_funcionario)
);
