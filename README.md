# Testes com Selenium

Atividade de Qualidade e Teste de Software usando Java, Selenium e JUnit no site [Automation Exercise](https://automationexercise.com/).

O projeto testa o login com e-mail e senha incorretos (Test Case 3) e o cadastro de usuário (Test Case 1). São cinco entradas para cada funcionalidade, totalizando dez testes. Os valores escolhidos e os resultados esperados estão em [docs/CASOS-DE-TESTE.md](docs/CASOS-DE-TESTE.md).

## Como executar

É necessário ter JDK 17, Maven 3.9 ou superior e Google Chrome instalado. Na pasta do projeto, execute:

```bash
mvn clean test
```

Para executar sem abrir a janela do navegador:

```bash
mvn clean test -Dheadless=true
```

Para executar apenas uma das funcionalidades:

```bash
mvn test -Dtest=LoginIncorretoTest
mvn test -Dtest=RegistrarUsuarioTest
```

O Selenium Manager configura o ChromeDriver automaticamente. A primeira execução precisa de internet para baixar o driver e as dependências. Os relatórios ficam em `target/surefire-reports/`.

## Arquivos

- `BaseTest.java`: abre e fecha o navegador e reúne os métodos usados pelos dois testes.
- `LoginIncorretoTest.java`: contém as cinco entradas de login.
- `RegistrarUsuarioTest.java`: contém as cinco entradas de cadastro.

Os testes usam Selenium 4.26.0, a mesma versão do exemplo da professora, e JUnit 5.11.3. O projeto usa Java 17 porque essa versão do Selenium não funciona com Java 8.

Cada teste abre um navegador novo. Os e-mails são gerados com UUID para evitar repetir um cadastro. Os dados de endereço são fictícios. Nos cadastros válidos, o teste também verifica o usuário conectado e exclui a conta pelo site. Se a execução falhar antes da etapa de exclusão, a conta pode permanecer cadastrada.

## Verificação

Os dez testes foram executados no GitHub Actions em 05/10/2026, com Java 17 e Chrome. Todos passaram, sem falhas, erros ou testes ignorados. O resultado está nesta [execução](https://github.com/cmrgsPedro/selenium-automationexercise-qa/actions/runs/37383977200).

Durante a verificação, um anúncio do Google bloqueou alguns links. O método de clique foi ajustado para seguir o endereço do próprio link quando aparece `#google_vignette`. O cadastro continua verificando a criação da conta, o usuário conectado e a exclusão.

Referência dos cenários: [Test Cases do Automation Exercise](https://automationexercise.com/test_cases).
