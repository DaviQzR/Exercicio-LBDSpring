USE master
CREATE DATABASE SQLServerUDF
GO

USE SQLServerUDF

GO
CREATE TABLE funcionario (
codigo		INT			NOT NULL,
nome		VARCHAR(50)		NULL,
salario		DECIMAL(7,2)	NULL
PRIMARY KEY (codigo)
)
GO

CREATE TABLE dependente(
codigo		INT					NOT NULL,
nome		VARCHAR(50)			NOT NULL,
salario		DECIMAL(7,2)		NULL,
funcionario	INT					NULL
PRIMARY KEY (codigo)
FOREIGN KEY (funcionario)  REFERENCES  funcionario(codigo)
)
GO

INSERT INTO funcionario  (codigo,nome, salario)
VALUES
	(1, 'João Silva', 500.0),
    (2, 'Maria Oliveira', 550),
    (3, 'Carlos Santos', 480),
    (4, 'Ana Pereira', 520),
    (5, 'Paulo Oliveira', 530),
    (6, 'Juliana Martins', 510.32),
    (7, 'Lucas Silva', 4900.00),
    (8, 'Mariana Santos', 54000),
    (9, 'Pedro Oliveira', 4700),
    (10, 'Camila Martins', 5600)

INSERT INTO dependente VALUES
	(1, 'João Silva Filho', 1500.00, 1),
    (2, 'Maria Oliveira Filha', 9900.00, 1),
    (3, 'Pedro Santos Filho', 9750.00, 1),
    (4, 'Ana Costa Filha', 20003.00, 2),
    (5, 'Carlos Pereira Filho', 500.00, 3),
    (6, 'Luísa Martins Filha', 40060.00, 4),
    (7, 'Lucas Fernandes Filho', 950.00, 5),
    (8, 'Mariana Almeida Filha', 860.00, 6),
    (9, 'Rafael Lima Filho', 120.00, 7),
    (10, 'Tatiane Souza Filha', 1020.00, 8)

--1. Criar uma database, criar as tabelas abaixo, definindo o tipo de dados e a relação PK/FK e popular com alguma massa de dados de teste (Suficiente para testar UDFs)
--Funcionário (Código, Nome, Salário)
--Dependendente (Código_Dep, Código_Funcionário, Nome_Dependente, Salário_Dependente)
--a) Código no Github ou Pastebin de uma Function que Retorne uma tabela:
--(Nome_Funcionário, Nome_Dependente, Salário_Funcionário, Salário_Dependente)

CREATE FUNCTION fn_funcionarios_dependentes()
RETURNS TABLE
AS
RETURN
(
    SELECT 
        d.codigo AS codigoDependente, 
        f.codigo AS codigoFuncionario,
        f.nome AS nomeFuncionario,
        d.nome AS nomeDependente,
		d.salario AS salarioDependente,
		f.salario AS salarioFuncionario
    FROM funcionario f 
    INNER JOIN dependente d ON f.codigo = d.funcionario
)

	SELECT * FROM fn_funcionarios_dependentes()

CREATE FUNCTION fn_consultar_funcionarios_dependentes(@codigoFuncionario INT)
RETURNS TABLE
AS
RETURN
(
	SELECT
		d.codigo AS codigoDependente,
		f.codigo AS codigoFuncionario,
		f.nome AS nomeFuncionario,
		d.nome AS nomeDependente,
			d.salario AS salarioDependente,
			f.salario AS salarioFuncionario
	FROM
		funcionario f
	INNER JOIN
		dependente d ON f.codigo = d.funcionario
	WHERE
		f.codigo = @codigoFuncionario
)
 
 SELECT * FROM fn_consultar_funcionarios_dependentes(1)

 --b) Código no Github ou Pastebin de uma Scalar Function que Retorne a soma dos Salários dos dependentes, mais a do funcionário. 

CREATE FUNCTION fn_soma_salario (@codigoFuncionario INT)
RETURNS DECIMAL(7,2)
AS
BEGIN
    DECLARE @totalSalario DECIMAL(7,2);

    SELECT @totalSalario = ISNULL(SUM(d.salario), 0) + ISNULL(f.salario, 0)
    FROM funcionario f
    LEFT JOIN dependente d ON f.codigo = d.funcionario
    WHERE f.codigo = @codigoFuncionario
    GROUP BY f.salario;

    RETURN @totalSalario;
END

SELECT dbo.fn_soma_salario(4) AS "Soma Salarios"

CREATE PROCEDURE sp_iud_funcionario 
    @acao CHAR(1), 
    @codigo INT, 
    @nome VARCHAR(50), 
    @salario DECIMAL(7,2),
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    IF (@acao = 'I')
    BEGIN
        INSERT INTO funcionario (codigo, nome, salario) 
        VALUES (@codigo, @nome, @salario)
        SET @saida = 'Funcionário inserido com sucesso'
    END
    ELSE IF (@acao = 'U')
    BEGIN
        UPDATE funcionario 
        SET nome = @nome, salario = @salario
        WHERE codigo = @codigo
        SET @saida = 'Funcionário alterado com sucesso'
    END
    ELSE IF (@acao = 'D')
    BEGIN
        IF EXISTS (SELECT 1 FROM funcionario WHERE codigo = @codigo)
        BEGIN
            DELETE FROM funcionario WHERE codigo = @codigo
            SET @saida = 'Funcionário excluído com sucesso'
        END
        ELSE
        BEGIN
            SET @saida = 'Funcionário não encontrado'
        END
    END
    ELSE
    BEGIN
        RAISERROR('Operação inválida', 16, 1)
        RETURN
    END
END

CREATE PROCEDURE sp_iud_dependente 
    @acao CHAR(1), 
    @codigo INT, 
    @nome VARCHAR(50), 
    @salario DECIMAL(7,2),
    @funcionario INT,
    @saida VARCHAR(100) OUTPUT
AS
BEGIN
    IF (@acao = 'I')
    BEGIN
        INSERT INTO dependente(codigo, nome, salario, funcionario) 
        VALUES (@codigo, @nome, @salario, @funcionario)
        SET @saida = 'Dependente inserido com sucesso'
    END
    ELSE IF (@acao = 'U')
    BEGIN
        UPDATE dependente 
        SET nome = @nome, salario = @salario, funcionario = @funcionario
        WHERE codigo = @codigo
        SET @saida = 'Dependente alterado com sucesso'
    END
    ELSE IF (@acao = 'D')
    BEGIN
        IF EXISTS (SELECT 1 FROM dependente WHERE codigo = @codigo)
        BEGIN
            DELETE FROM dependente WHERE codigo = @codigo
            SET @saida = 'Dependente excluído com sucesso'
        END
        ELSE
        BEGIN
            SET @saida = 'Dependente não encontrado'
        END
    END
    ELSE
    BEGIN
        RAISERROR('Operação inválida', 16, 1)
        RETURN
    END
END
		
		