<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="./css/styles.css">
<title>Dependente</title>
</head>
<div>
	<jsp:include page="menu.jsp" />
</div>
<br />
<div align="center" class="container">
	<form action="dependente" method="post">
		<p class="title">
			<b>Manutenção Dependente</b>
		</p>

		<table>
			<tr>
				<td style="width: 20%;"><label for="codigo">Código:</label></td>
				<td colspan="2" style="width: 60%;"><input
					class="input_data_id" type="number" min="0" step="1" id="codigo"
					name="codigo" placeholder="Código"
					value='<c:out value="${dependente.codigo}"></c:out>'></td>
				<td style="width: 20%;"><input type="submit" id="botao"
					name="botao" value="Buscar"></td>
			</tr>

			<tr>
				<td><label for="nome">Nome:</label></td>
				<td colspan="4"><input class="input_data" type="text" id="nome"
					name="nome" placeholder="Nome do Dependente"
					value='<c:out value="${dependente.nome}"></c:out>'></td>
			</tr>

			<tr>
				<td><label for="salario">Salário:</label></td>
				<td colspan="4"><input class="input_data" type="number"
					id="salario" name="salario" placeholder="Salário do Dependente"
					value='<c:out value="${dependente.salario}"></c:out>'></td>
			</tr>

			<tr>
				<td><label for="funcionario">Funcionário:</label></td>
				<td colspan="4"><select class="input_data" id="funcionario"
					name="funcionario">
						<option value="0">Escolha um Funcionário</option>
						<c:forEach var="f" items="${funcionarios}">
							<c:if
								test="${empty dependente or f.codigo ne dependente.funcionario.codigo}">
								<option value="${f.codigo}">
									<c:out value="${f}" />
								</option>
							</c:if>
							<c:if test="${f.codigo eq dependente.funcionario.codigo}">
								<option value="${f.codigo}" selected="selected">
									<c:out value="${f}" />
								</option>
							</c:if>
						</c:forEach>
				</select></td>
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
				<td><input type="submit" id="botao" name="botao" value="Listar">
				<td><input type="submit" id="botao" name="botao" value="Limpar">
				</td>

			</tr>
		</table>
	</form>
</div>
<br />
<div align="center">
	<c:if test="${not empty erro }">
		<h2>
			<b><c:out value="${erro }" /></b>
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
	<c:if test="${not empty dependentes }">
		<table class="table_round">
			<thead>
				<tr>
					<th>Selecionar</th>
					<th>Código</th>
					<th>Nome Dependente</th>
					<th>Salário</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="d" items="${dependentes }">
					<tr>
						<td><input type="radio" name="opcao" value="${d.codigo}"
							onclick="editarDependente(this.value)"
							${d.codigo eq codigoEdicao ? 'checked' : ''} /></td>
						<td><c:out value="${d.codigo }" /></td>
						<td><c:out value="${d.nome }" /></td>
						<td><c:out value="${d.salario }" /></td>
						<td><c:out value="${d.funcionario.codigo }" /></td>
						<td><button onclick="excluirDependente('${d.codigo}')">EXCLUIR</button></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</div>
<script>
	function editarDependente(codigo) {
		window.location.href = 'dependente?cmd=alterar&codigo=' + codigo;
	}

	function excluirDependente(codigo) {
		if (confirm("Tem certeza que deseja excluir este Dependente?")) {
			window.location.href = 'dependente?cmd=excluir&codigo=' + codigo;
		}
	}
</script>
</body>
</html>