<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css"
	href='<c:url value = "./resources/css/styles.css" />'>
<title>Funcionário</title>
</head>
<body>
	<div>
		<jsp:include page="menu.jsp" />
	</div>
	<br />
	<div align="center" class="container">
		<form action="funcionario" method="post">
			<p class="title">
				<b>Manter Funcionário</b>
			</p>


			<table>
				<tr>
					<td><label for="codigo">Código:</label></td>
					<td><input class="input_data_id" type="number" min="0"
						step="1" id="codigo" name="codigo" placeholder="Código"
						value='<c:out value="${funcionario.codigo }"></c:out>'></td>
					<td><input type="submit" id="botao" name="botao"
						value="Buscar"></td>
				</tr>
				<tr>
					<td><label for="nome">Nome:</label></td>
					<td><input class="input_data" type="text" id="nome"
						name="nome" placeholder="Nome"
						value='<c:out value="${funcionario.nome }"></c:out>'></td>
				</tr>
				<tr>
					<td><label for="salario">Salário:</label></td>
					<td><input class="input_data" type="text" id="salario"
						name="salario" placeholder="Salário"
						onkeyup="formatarMoeda(this);"
						value='<c:out value="${funcionario.salario }"></c:out>'></td>
				</tr>

			</table>
			<table>
				<tr>
					<td><input type="submit" id="botao" name="botao"
						value="Cadastrar"></td>
					<td><input type="submit" id="botao" name="botao"
						value="Alterar"></td>
					<td><input type="submit" id="botao" name="botao"
						value="Excluir"></td>

					<td><input type="submit" id="botao" name="botao"
						value="Listar"></td>

				</tr>
			</table>
		</form>
	</div>
	<br />

	<div align="center">
		<c:if test="${not empty erro}">
			<h2 style="color: red;">
				<b><c:out value="${erro}" /></b>
			</h2>
		</c:if>
	</div>



	<br />
	<div align="center">
		<c:if test="${not empty saida }">
			<h3>
				<b><c:out value="${saida }" /></b>
			</h3>
		</c:if>
	</div>

	<br />
	<div align="center">
		<c:if test="${not empty funcionarios }">
			<table class="table_round">
				<thead>
					<tr>
						<th>Selecionar</th>
						<th>Código</th>
						<th>Nome</th>
						<th>Salário</th>
						<th></th>

					</tr>
				</thead>
				<tbody>
					<c:forEach var="f" items="${funcionarios}">
						<tr>
							<td><input type="radio" name="opcao" value="${f.codigo}"
								onclick="editarFuncionario(this.value)"
								${f.codigo eq codigoEdicao ? 'checked' : ''} /></td>
							<td><c:out value="${f.codigo}" /></td>
							<td><c:out value="${f.nome}" /></td>
							<td><fmt:formatNumber value="${f.salario}" type="currency"
									currencyCode="BRL" /></td>
							<td><button onclick="excluirFuncionario('${f.codigo}')">Excluir</button></td>
							<td><button onclick="consultarFuncionario('${f.codigo}')">Consultar</button></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<script>
				function consultarFuncionario(codigo) {
					window.location.href = 'consulta?codigo='
							+ codigo;
				}
			</script>

			<script>
				function editarFuncionario(codigo) {
					window.location.href = 'funcionario?cmd=alterar&codigo='
							+ codigo;
				}

				function excluirFuncionario(codigo) {
					if (confirm("Tem certeza que deseja excluir este funcionario?")) {
						window.location.href = 'funcionario?cmd=excluir&codigo='
								+ codigo;
					}
				}
			</script>



		</c:if>
	</div>
</body>
</html>
