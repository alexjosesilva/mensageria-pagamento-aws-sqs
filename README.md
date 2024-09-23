
# Projeto Pagamento com Mensageria AWS SQS

## Descrição

Este projeto é uma solução de processamento de pagamentos, onde vendedores e faturas são validados antes de enviar os dados para diferentes filas do **AWS SQS**. Dependendo do valor pago, o sistema determina se o pagamento foi parcial, total ou excedente, e envia os dados para a fila correspondente. Além disso, o projeto utiliza o banco de dados H2 para armazenar os dados localmente e simular a persistência em um ambiente de desenvolvimento.

## Funcionalidades

- Processamento de pagamentos com validação de vendedores e faturas.
- Integração com **AWS SQS** para envio de mensagens baseadas no status de pagamento.
- Persistência de dados no banco H2 em modo de arquivo, com suporte para manter dados após reinicialização.
- Console H2 habilitado para fácil visualização e manipulação dos dados.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot** 3.1.x
- **H2 Database** em modo arquivo
- **AWS SDK** para integração com SQS
- **LocalStack** (para simulação do AWS SQS localmente)
- **Maven** ou **Gradle** como gerenciador de dependências

## Configuração do Projeto

### Banco de Dados H2

O projeto utiliza o H2 como banco de dados em modo arquivo, para garantir que os dados sejam persistidos entre reinicializações da aplicação.

**Configuração no `application.properties`:**
```properties
spring.datasource.url=jdbc:h2:file:./data/desafioSan;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=1234567

spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Acesse o console H2 em: `http://localhost:8080/h2-console`.  
**JDBC URL**: `jdbc:h2:file:./data/desafioSan`

### AWS SQS e LocalStack

O projeto está configurado para usar o **AWS SQS** para enviar mensagens sobre o status dos pagamentos. No ambiente de desenvolvimento, você pode usar o **LocalStack** para simular o SQS localmente.

**Configuração no `application.properties`:**
```properties
aws.sqs.endpoint=http://localhost:4566
aws.sqs.queue.partial=http://sqs.us-east-2.localhost.localstack.cloud:4566/000000000000/PartialQueue
aws.sqs.queue.total=http://sqs.us-east-2.localhost.localstack.cloud:4566/000000000000/TotalQueue
aws.sqs.queue.excess=http://sqs.us-east-2.localhost.localstack.cloud:4566/000000000000/ExcessQueue
aws.region=us-east-2
```

### Executando LocalStack com Docker

Para rodar o LocalStack localmente e simular o SQS, execute o comando Docker abaixo:

```bash
docker run --rm -it -p 4566:4566 -p 4571:4571 localstack/localstack
```

Depois, crie as filas no LocalStack:

```bash
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name PartialQueue
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name TotalQueue
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name ExcessQueue
```

### Dependências

Certifique-se de que as seguintes dependências estão configuradas no `pom.xml` (Maven) ou `build.gradle` (Gradle):

**Maven:**
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>sqs</artifactId>
    <version>2.20.0</version>
</dependency>
```

**Gradle:**
```groovy
implementation 'software.amazon.awssdk:sqs:2.20.0'
```

## Como Executar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/desafio-san-giorgio.git
   ```

2. **Execute o projeto Spring Boot:**
   Se estiver usando Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

   Se estiver usando Gradle:
   ```bash
   ./gradlew bootRun
   ```

3. **Acesse o H2 Console (opcional):**
   Acesse o console H2 no navegador: `http://localhost:8080/h2-console`.

4. **Teste a aplicação via API:**
   Utilize ferramentas como o **Postman** ou **cURL** para fazer requisições à API e processar pagamentos.

## Licença

Este projeto é distribuído sob a licença MIT. Consulte o arquivo LICENSE para mais informações.
