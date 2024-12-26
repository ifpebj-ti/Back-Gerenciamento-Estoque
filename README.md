![Cobertura de Testes](.github/badges/coverage-badge.svg)














# Super Estoque 2000
Projeto desenvolvido para avaliação das cadeiras de tópicos avançados em programação II, tópicos avançados em redes I e segurança e validação.
## Como Rodar o Projeto

### Pré-requisitos

- [Java JDK](https://www.oracle.com/java/technologies/downloads/) 11 ou superior
- [Maven](https://maven.apache.org/download.cgi?.) 3.1 ou superior
### Passos

1. **Clonar o Repositório:**

   ### Caso esteja com a chave SSH configurada no seu computador
   ```bash
   git clone https://github.com/ifpebj-ti/Back-Gerenciamento-Estoque.git
   ````

   ### Caso não esteja com a chave SSH configurada no seu computador
    ```bash
    git clone git@github.com:ifpebj-ti/Back-Gerenciamento-Estoque.git
     ````
2. **Navegue até o diretório do projeto:**

    ```bash
    cd estoque

3. **Instale as dependências:**

    ```bash
    mvn install

4. **Inicie o servidor de desenvolvimento:**

    ```bash
   mvn spring-boot:run

## Documentação

  O projeto inclui documentação detalhada para facilitar o entendimento e a interação com a aplicação.
  A seguir estão os recursos de documentação disponíveis.

  ### Swagger

   A API é documentada usando o Swagger, que fornece uma interface interativa para explorar os endpoints 
  da aplicação.
  ### Acesso ao Swagger
  **Com o projeto rodando**
  
  O Swagger pode ser acessado através do link: [Swagger UI](http://localhost:8080/swagger-ui/index.html).
  
  A interface do Swagger oferece uma visão interativa dos endpoints, permitindo testar as operações
  diretamente na documentação.

## Perfis de configuração

  O projeto oferece um perfil de configuração : **test** (padrão), **prod**(produção).

  #### **Perfil de Teste (Padrão)**
  
  - **Banco de Dados**: Utiliza o H2 Database, dispensando configurações adicionais.

  #### **Perfil de Teste (Produção)**
  
  - **Banco de Dados**: Utiliza uma imagem do MySQL para persistência dos dados.
  - **Configuração do Banco de Dados**: As variáveis de ambiente necessárias para configurar o banco de dados (como nome do banco, usuário e senha) devem ser especificadas no arquivo `application-prod.properties`. Esse arquivo fornece suporte para o perfil de produção, com segurança e integração ao banco de dados.

## Variáveis de ambiente

  As seguintes variáveis de ambiente podem ser configuradas para ajustar o comportamento da aplicação:

  - **`APP_PROFILE`**: Define o perfil ativo da aplicação (`test` ou `prod`). Valor padrão: `test`.
  - **`CLIENT_ID`**: ID do cliente para autenticação OAuth2. Valor padrão: `myclientid`.
  - **`CLIENT_SECRET`**: Segredo do cliente para autenticação OAuth2. Valor padrão: `myclientsecret`.
  - **`JWT_DURATION`**: Duração do token JWT em segundos. Valor padrão: `86400` (24 horas).
  - **`CORS_ORIGINS`**: Configura as origens permitidas para requisições CORS. Valor padrão: `http://localhost:3000`.
  - **`DB_NAME`**: Nome do banco de dados (apenas no perfil `prod`).
  - **`DB_USERNAME`**: Nome de usuário do banco de dados (apenas no perfil `prod`).
  - **`DB_PASSWORD`**: Senha do banco de dados (apenas no perfil `prod`).
  - **`MYSQL_ROOT_PASSWORD`**: Senha do usuário root do MySQL.

## Licença
 Este projeto está licenciado sob a licença MIT.
