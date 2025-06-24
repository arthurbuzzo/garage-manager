# Sistema de Gestão de Estacionamentos

Sistema desenvolvido em Java 17 com Spring Boot seguindo arquitetura hexagonal para gerenciar vagas de estacionamento, entrada/saída de veículos e faturamento, para essa aplicação foi utilizado também o lombok a fim de diminir a verbosidade do 
codigo mantendo assim mais legivel o que se mostrou no resultado final bem positivo na decisão de utilizar o java ou kotlin.
O banco de dados para simplificação dos testes foi utilizado o H2, porem a aplicação foi desenvolvida para ser agnostica ao banco de dados, sendo assim apenas necessário alterar a string de conexão para o postgree ou mysql.
Foi optado também pela aplicação do conceito de API First onde o contrato é criado primeiro, tornando-se o responsavel pela geração do código e interfaces do contrato, permitindo assim a automação deste sem a necessidade de criação desses objetos e interfaces. 

## Importante
Devido a limitação de tempo do candidato para a elaboração do teste não foi possivel despender o tempo necessário para conclusão do teste, de forma que fosse garantido que todas as regras de negocio estão funcionais e sem nenhum bug, mas de forma geral esta funcional, e fica aqui a principal ideia que é a demonstração do uso das ferramentas escolhidas e a aplicação da Arquitetura Hexagonal. 
Os testes unitários também não foram desenvolvidos pelo motivo relatado, porem seria o proximo passado. 
Melhorias pontuais também são aplicaveis, como algumas refatorações de código a fim de melhorar as responsabilidades de cada camada e a escrita de codigo, como por exemplo a utilização de conversores para entidades e domains o que reduziria consideravelmente o codigo escrito. 

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (em memória)
- OpenAPI 3.0 (Swagger)
- Maven
- Arquitetura Hexagonal

## Estrutura do Projeto

```
src/main/
├── java/com/parking/
│   ├── domain/                 # Camada de domínio
│   │   ├── model/             # Entidades de domínio
│   │   ├── port/              # Portas (interfaces)
│   │   └── service/           # Serviços de domínio
│   ├── application/           # Camada de aplicação
│   │   └── usecase/          # Casos de uso
│   ├── adapter/               # Adaptadores
│   │   ├── in/               # Adaptadores de entrada
│   │   │   └── web/          # Controladores REST
│   │   └── out/              # Adaptadores de saída
│   │       └── persistence/  # Persistência JPA
│   └── config/               # Configurações
└── resources/
    ├── application.properties
    └── openapi.yaml
```

## Como Executar

1. **Pré-requisitos**
   - Java 17+
   - Maven 3.6+

2. **Compilar o projeto**
   ```bash
   mvn clean compile
   ```

3. **Gerar código a partir do OpenAPI**
   ```bash
   mvn generate-sources
   ```

4. **Executar a aplicação**
   ```bash
   mvn spring-boot:run
   ```

A aplicação será iniciada na porta 3003.

## Endpoints Disponíveis

### Webhook (Simulador)
- `POST /webhook` - Recebe eventos de entrada, estacionamento e saída

### Configuração
- `GET /garage` - Obtém configuração da garagem e inicializa o sistema

### Consultas
- `POST /plate-status` - Consulta status de uma placa
- `POST /spot-status` - Consulta status de uma vaga

### Faturamento
- `GET /revenue?date={date}&sector={sector}` - Consulta faturamento

## Documentação da API

Após iniciar a aplicação, acesse:
- Swagger UI: http://localhost:3003/swagger-ui.html
- OpenAPI Spec: http://localhost:3003/api-docs

## Banco de Dados H2

Console H2 disponível em: http://localhost:3003/h2-console
- URL: `jdbc:h2:mem:parkingdb`
- Usuário: `sa`
- Senha: (vazio)

## Fluxo de Uso

1. **Inicializar o sistema**
   ```bash
   curl -X GET http://localhost:3003/garage
   ```

2. **Entrada de veículo**
   ```bash
   curl -X POST http://localhost:3003/webhook \
     -H "Content-Type: application/json" \
     -d '{
       "license_plate": "ZUL0001",
       "entry_time": "2025-01-01T12:00:00.000Z",
       "event_type": "ENTRY"
     }'
   ```

3. **Veículo estaciona**
   ```bash
   curl -X POST http://localhost:3003/webhook \
     -H "Content-Type: application/json" \
     -d '{
       "license_plate": "ZUL0001",
       "lat": -23.561684,
       "lng": -46.655981,
       "event_type": "PARKED"
     }'
   ```

4. **Consultar status da placa**
   ```bash
   curl -X POST http://localhost:3003/plate-status \
     -H "Content-Type: application/json" \
     -d '{
       "license_plate": "ZUL0001"
     }'
   ```

5. **Saída do veículo**
   ```bash
   curl -X POST http://localhost:3003/webhook \
     -H "Content-Type: application/json" \
     -d '{
       "license_plate": "ZUL0001",
       "exit_time": "2025-01-01T14:00:00.000Z",
       "event_type": "EXIT"
     }'
   ```

6. **Consultar faturamento**
   ```bash
   curl -X GET "http://localhost:3003/revenue?date=2025-01-01&sector=A"
   ```

## Regras de Negócio

### Preço Dinâmico
- Lotação < 25%: 10% de desconto
- Lotação 25-50%: Preço normal
- Lotação 50-75%: 10% de aumento
- Lotação 75-100%: 25% de aumento

### Lotação
- Com 100% de lotação, o setor é fechado para novas entradas
- Só permite novos veículos após a saída de algum já estacionado

## Desenvolvimento

### Adicionar novas funcionalidades
1. Defina a operação no `openapi.yaml`
2. Execute `mvn generate-sources`
3. Implemente o caso de uso na camada de aplicação
4. Crie/atualize os adaptadores necessários
5. Implemente o controlador
