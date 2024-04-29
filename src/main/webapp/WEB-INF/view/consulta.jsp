<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>Consulta de Funcionários e Dependentes</title>
<link rel="stylesheet" href="./css/styles.css">
</head>
<body>
	<div>
		<jsp:include page="menu.jsp" />
	</div>

	<br />

	<div align="center" class="container">
		<form action="consulta" method="post">
			<p class="title">
				<b>Consulta</b>
			</p>

			<table>
				<tr>
					<td colspan="4" style="text-align: center;"><label
						for="codigo"
						style="display: inline-block; width: 30%; text-align: right;">Código
							Funcionario:</label> <input class="input_data_id" type="number" min="0"
						step="1" id="codigo" name="codigo" placeholder="Código" required
						style="display: inline-block; width: 30%;"> <input
						type="submit" id="botao" name="botao" value="Consultar"
						style="display: inline-block;"></td>
				</tr>
			</table>
		</form>



		<h2>Resultado da Consulta:</h2>

		<div align="center">
			<c:if test="${not empty erro}">
				<p style="color: red;">
					<c:out value="${erro }" />
				</p>
			</c:if>
		</div>

		<br />

		<div align="center">
			<c:if test="${not empty dependentes}">
				<table class="table_round">
					<thead>
						<tr>
							<th>Código Dependente</th>
							<th>Nome Dependente</th>
							<th>Salário Dependente</th>
							<th>Nome Funcionario</th>
							<th>Salário Funcionario</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="dependente" items="${dependentes}">
							<tr>

								<td><c:out value="${dependente.codigo }" /></td>
								<td><c:out value="${dependente.nome }" /></td>
								<td><fmt:formatNumber value="${dependente.salario}"
										type="currency" currencyCode="BRL" /></td>
								<td><c:out value="${dependente.funcionario.nome }" /></td>
								<td><fmt:formatNumber
										value="${dependente.funcionario.salario}" type="currency"
										currencyCode="BRL" /></td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div align="center">
					<h3>
						Soma do Salário dos Dependentes:
						<fmt:formatNumber value="${somaSalario}" type="currency"
							currencyCode="BRL" />
					</h3>
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>