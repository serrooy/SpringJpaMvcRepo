<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<form action="/" method="post">
	<%
		boolean valido = request.getAttribute("valido") != null ? (boolean) request.getAttribute("valido") : false;
		if (valido) {
			String usu = (String) request.getAttribute("usuarioValido");
	%>
	<div class="container">
		<p>
		<p>
			<label for="uname"><b> Bienvenido usuario </b></label> <input
				type="text" name="logUsu" value=<%=usu%> readonly>
		<p>
			<button type="submit">Consulta LOG</button>
	</div>
	<%
		if (request.getAttribute("flagLog") != null) {
				List<String> listaLog = (List<String>) request.getAttribute("listaLog");

				for (String linea : listaLog) {
					out.println(linea);
	%>
	<p>
		<%
			}

				}

			} else {
				if (request.getAttribute("valido") != null) {
					out.print("Usuario no valido");
				}
		%>
	
	<div class="container">

		<p>
			<label for="uname"><b>Username</b></label> <input type="text"
				placeholder="Enter Username" name="uname" required>
		<p>
			<label for="psw"><b>Password</b></label> <input type="password"
				placeholder="Enter Password" name="psw" required value=<%%>>
		<p>
			<button type="submit">Login</button>
	</div>
	<%
		}
	%>
</form>