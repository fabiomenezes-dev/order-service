# Order Service

[![Status do Projeto](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-21-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-used-informational)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen)
![Docker](https://img.shields.io/badge/Docker-enabled-blue)
[![Licença](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Descrição

> Este projeto tem como objetivo a demonstração da capacidade tecnica, para o processo seletivo em uma nova empresa. Desta forma, este projeto é uma API RESTful construída com Spring Boot para gerenciar pedidos de uma loja online. Ele permite criar, visualizar, atualizar e cancelar pedidos, além de gerenciar os produtos associados a cada pedido. O objetivo principal é fornecer uma base robusta e escalável para o backend da plataforma de e-commerce.

### Desafio proposto:

<img src="src/main/resources/img/NOVO TESTE JAVA - AMBEV  (3).jpg">


## Funcionalidades Principais

* Criação de novos pedidos com múltiplos produtos.
* Visualização detalhada de um pedido específico.
* Listagem paginada de todos os pedidos.
* Recebimento e processamento de dados de um novo pedido.

## Tecnologias Utilizadas

* Java
* Spring Boot
* JPA/Hibernate
* Maven
* PostgreSQL
* Redis
* Docker
* Swagger

## Pré-requisitos

* Docker instalado na sua máquina.
* Docker Compose instalado na sua máquina.

## Como Executar com Docker Compose

Este projeto pode ser facilmente executado utilizando Docker Compose. Siga os passos abaixo:

1.  **Clone o repositório do projeto (se ainda não o fez):**
    Clique aqui para clonar o repositório: [https://github.com/fabiomenezes-dev/order-service.git](https://github.com/fabiomenezes-dev/order-service.git)
    ```bash
    cd order-service
    ```

2.  **Certifique-se de que o arquivo `docker-compose.yml` (que você forneceu) está na raiz do seu projeto.**

3.  **Execute o Docker Compose para iniciar os serviços:**
    ```bash
    docker-compose up -d
    ```
    Este comando irá criar e iniciar os containers para o Redis (`redis-cache`), o banco de dados PostgreSQL (`db-postgres-order`) e o serviço de pedidos (`order-service`).

4.  **Aguarde até que todos os serviços estejam em execução.** Você pode verificar o status dos containers com o seguinte comando:
    ```bash
    docker ps
    ```

## Endpoints da API

A API de gerenciamento de pedidos estará disponível na porta `8080` do seu localhost (conforme a configuração padrão do Spring Boot).

**Endpoints:**

* **Listar todos os pedidos (paginado):**
    ```
    GET /v1/order/all
    ```
    * **Parâmetros (Query):**
        * `page`: Número da página a ser consultada (começa em 0).
        * `size`: Número de elementos por página.
        * `sort`: Critério de ordenação no formato `property,direction` (ex: `createdAt,desc`).
    * **Response:** Retorna uma página de objetos `OrderDTO`.

* **Buscar pedido por ID:**
    ```
    GET /v1/order/{orderId}
    ```
    * **Parâmetro (Path):**
        * `orderId`: ID do pedido a ser buscado.
    * **Response:** Retorna um objeto `OrderDTO` se o pedido for encontrado. Retorna um erro 404 se não encontrado.

* **Receber e processar um novo pedido:**
    ```
    POST /v1/order/receive
    ```
    * **Request Body:** Um objeto `OrderDTO` no formato JSON contendo os dados do pedido a ser processado.
    * **Response:** Retorna uma string indicando o sucesso do recebimento do pedido. Retorna um erro 400 se a validação dos dados do pedido falhar.

## Documentação do Swagger

Para acessar a documentação completa da API, incluindo todos os endpoints, modelos de dados e exemplos de requisição e resposta, você pode acessar a interface do Swagger UI através do seguinte endereço no seu navegador:

```
http://localhost:8080/swagger-ui.html
```

## Configuração

As configurações específicas da aplicação (como perfis de ambiente) estão sendo gerenciadas através das variáveis de ambiente no `docker-compose.yml`. O perfil `prod` está ativo (`SPRING_PROFILES_ACTIVE=prod`).

As configurações do banco de dados PostgreSQL estão definidas no bloco `db-postgres` do `docker-compose.yml`.

A configuração do Redis está no bloco `redis`.

## Contribuição

Pull requests são bem-vindos. Para grandes mudanças, por favor, abra uma issue primeiro para discutir o que você gostaria de alterar.

## Licença

Este projeto está licenciado sob a [MIT License](https://opensource.org/licenses/MIT).

## Autor

[Fábio Augusto Vieira de Menezes](https://www.linkedin.com/in/fabioavmenezes/)


## Status do Projeto

Em Desenvolvimento
