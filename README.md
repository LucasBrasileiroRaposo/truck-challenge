# Truck-Challenge

## Descrição
Este projeto consiste em um sistema de gerenciamento de clientes de uma loja automotiva, desenvolvido como uma API REST. O projeto foi concebido como parte de um desafio técnico em um processo seletivo, com o objetivo de demonstrar habilidades em desenvolvimento backend utilizando as melhores práticas e tecnologias modernas.

## Lógica de Negócio
O sistema possui duas entidades principais: **Usuários** e **Veículos**. Usuários podem ser divididos em **Clientes** e **Administradores**:
- **Clientes**: Podem visualizar seus dados e os dados dos seus veículos, sem permissão para modificá-los.
- **Administradores**: Têm controle total sobre o sistema, incluindo a criação e edição de Clientes, bem como a gestão completa dos Veículos.

A relação entre Clientes e Veículos é **one-to-many** bidirecional, onde um Cliente pode possuir vários Veículos, mas cada Veículo só pode pertencer a um Cliente. Essa estrutura foi implementada utilizando JPA, garantindo a integridade e consistência dos dados.

O desenvolvimento seguiu o padrão **Service-Repository** juntamente com o **MVC** para manter a separação de responsabilidades e facilitar a manutenção do código. O sistema de autenticação e autorização foi implementado com **Spring Security** e **JWT**, garantindo a segurança das operações. Além disso, foram incluídos testes unitários e a documentação dos endpoints utilizando Swagger.

## Tecnologias e Frameworks Utilizados
- **Java 17**
- **Maven**
- **Spring Boot**
- **Lombok**
- **Mockito**
- **Spring Data JPA**
- **Spring Web**
- **Spring Security**
- **Swagger**
- **MySQL**
- **JUnit**
- **Bean Validation**

## Como Usar a API

### Configuração
Após clonar o repositório, você pode optar por usar o banco de dados H2 em memória ou configurar um banco de dados MySQL local. As configurações do banco de dados podem ser ajustadas no arquivo `application.properties`.

### Dados para iniciar o uso da API
Na primeira execução da aplicação, um usuário padrão do tipo **Admin** será criado automaticamente para facilitar o acesso e a utilização das funcionalidades administrativas:
- **Login**: `admin@example.com`
- **Senha**: `12345`

### Autenticação
Após realizar o login, todas as operações subsequentes exigem que o usuário esteja autenticado. O token JWT gerado durante o login deve ser incluído no cabeçalho das requisições subsequentes:
- **Authorization**: `Bearer {token}`

> **Nota:** Certifique-se de incluir a palavra `Bearer` seguida de um espaço antes do token JWT.

### Utilizando o Swagger
A documentação da API está disponível em: [Swagger UI](http://localhost:8080/swagger-ui.html). É importante atentar-se aos campos necessários nos corpos das requisições JSON, especialmente nas operações de criação (`POST`). Por exemplo:
- Ao criar um **Usuário**, evite incluir o atributo `vehicles` e todo `value`.
- Ao criar um **Veículo**, inclua apenas o campo `id` do objeto proprietário, já que os demais campos relacionados ao usuário serão gerenciados automaticamente.
