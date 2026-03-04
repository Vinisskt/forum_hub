# Forum Hub - API REST 🚀
> Desafio do programa Oracle Next Education (ONE) em parceria com a Alura.

O **Forum Hub** é uma API desenvolvida em Java com Spring Boot para gerenciar um fórum de discussões. A aplicação permite que usuários autenticados criem tópicos, acompanhem respostas e interajam em uma comunidade organizada.

---

## 🛠️ Tecnologias Utilizadas (Tech Stack)

As principais ferramentas (tools) e frameworks utilizados no desenvolvimento foram:

*   **Java 17**: Linguagem robusta para o back-end.
*   **Spring Boot 3**: Framework para agilizar o desenvolvimento.
*   **Spring Security & JWT**: Segurança baseada em tokens para autenticação (authentication).
*   **Spring Data JPA**: Abstração para persistência de dados.
*   **Flyway**: Gerenciamento de migrações (migrations) do banco de dados.
*   **MySQL**: Banco de dados relacional para armazenamento.
*   **Lombok**: Biblioteca para reduzir código repetitivo (boilerplate).
*   **Maven**: Gerenciador de dependências e automação de build.

---

## ✨ Principais Funcionalidades (Key Features)

- [x] **Autenticação de Usuários**: Login via JWT para acesso seguro às rotas.
- [x] **Gestão de Tópicos (CRUD)**:
    - Criar novos tópicos (vinculados ao usuário logado).
    - Listar tópicos com paginação e ordenação automática por data.
    - Detalhar um tópico específico por ID.
    - Atualizar informações de um tópico existente.
    - Excluir tópicos.
- [x] **Validações de Regras de Negócio**: Impede tópicos duplicados e garante a integridade dos dados.
- [x] **Gestão de Respostas**: Controle de interações dentro dos tópicos.

---

## 📂 Estrutura do Projeto (Project Structure)

O projeto segue a estrutura padrão (standard structure) do Spring Boot:

- `src/main/java`: Contém o código fonte (source code) da aplicação.
- `src/main/resources/db/migration`: Scripts SQL gerenciados pelo Flyway.
- `controller/`: Classes que expõem os endpoints da API.
- `infra/`: Configurações de segurança, filtros e validações globais.
- `repository/`: Interfaces para comunicação com o banco de dados.

---

## 🚀 Como Executar o Projeto (How to Run)

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/seu-usuario/forum-hub.git
   ```

2. **Configure o Banco de Dados**:
   Edite o arquivo `src/main/resources/application.properties` com suas credenciais (credentials) do MySQL:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/forum_hub
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

3. **Execute a aplicação**:
   Utilize sua IDE favorita ou o Maven via terminal:
   ```bash
   mvn spring-boot:run
   ```

---

## 📡 Endpoints da API

Abaixo estão as principais rotas da aplicação e exemplos de como utilizá-las (how to use them).

### 🔐 Autenticação e Usuários
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/usuarios` | Cadastra um novo usuário no sistema |
| `POST` | `/login` | Autentica um usuário e retorna o Token JWT |

**Exemplo de Corpo (Body) para Cadastro e Login:**
```json
{
  "login": "seu_usuario",
  "senha": "12345678" 
}
```
> *Obs: A senha deve conter exatamente 8 dígitos numéricos conforme a validação do sistema.*

### 📝 Tópicos
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/topicos` | Lista todos os tópicos (paginado e ordenado) |
| `POST` | `/topicos` | Cadastra um novo tópico (requer Token) |
| `GET` | `/topicos/{id}` | Detalha um tópico específico |
| `PUT` | `/topicos/{id}` | Atualiza um tópico existente |
| `DELETE` | `/topicos/{id}` | Remove um tópico |

**Exemplo de Corpo (Body) para Cadastro/Atualização:**
```json
{
  "titulo": "Dúvida sobre Spring Boot",
  "mensagem": "Como configurar o Spring Security corretamente?",
  "curso": "PROGRAMACAO" 
}
```
> *Os valores aceitos para o campo `curso` dependem das categorias definidas no sistema (ex: JAVA, PYTHON).*

### 💬 Respostas
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/respostas` | Envia uma resposta para um tópico |

**Exemplo de Corpo (Body):**
```json
{
  "usuario": { "id": 1 },
  "resposta": "Para configurar o Security, você deve criar uma classe de configuração com a anotação @Configuration."
}
```

---

## 🧪 Testes Automatizados (Automated Tests)

O projeto conta com uma suíte de testes para garantir a estabilidade e o comportamento esperado da API:

*   **Tecnologias**: JUnit 5, AssertJ e MockMvc.
*   **Foco dos Testes**:
    *   **Validação de Inputs**: Garante que campos como senha (8 dígitos) e campos obrigatórios sejam respeitados.
    *   **Segurança**: Verifica o comportamento da autenticação e proteção de rotas.
    *   **Regras de Negócio**: Valida o fluxo de cadastro e atualização de tópicos e usuários.
    *   **Retornos HTTP**: Confirma se a API retorna os status corretos (201 Created, 200 OK, 400 Bad Request, etc).

Para rodar os testes, utilize o comando:
```bash
mvn test
```

---

## 🚧 Status do Projeto (Project Status)

Este projeto segue em **desenvolvimento ativo** (active development). As próximas etapas (next steps) incluem:
*   [ ] Refatoração e melhorias no sistema, identificando gargalos para um melhor funcionamento.
> Ajustando endpoints conforme o aprendizado de novos recursos e estratégias.
*   [ ] Expansão da cobertura de testes automatizados (test coverage).
*   [ ] Implementação completa e refinamento dos endpoints para o gerenciamento de respostas (responses).
*   [ ] Implementação da funcionalidade de modificação do status do tópico pelo autor.

---

## 📄 Licença (License)

Este projeto está sob a licença MIT. Sinta-se à vontade para utilizá-lo!

---
> Este projeto foi desenvolvido com foco no aprendizado — pode conter erros de lógica e pode não seguir todas as melhores práticas; será modificado e aprimorado conforme o aprendizado de novos recursos.

> Foi utilizada IA para entender conceitos e aprender sintaxe; não foi utilizada para geração automática de código.

---
Desenvolvido por Vinicius Feitosa de Souza Filho 👋
