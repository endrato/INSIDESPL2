<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../tiles/templates/taglibs.jsp"%>
<style>
  /*
   Los estilos puedes ponerlos al inicio de tu documento HTML o puedes 
   meterlos en un archivo por separado
  */
  .auto{
   display:none;
 }
</style>
<script>
function mostrar(id){
	console.log(id);
	 if (document.getElementById(id).style.display === 'none') {
		 document.getElementById(id).style.display = 'block';

		  } else {
			  document.getElementById(id).style.display = 'none';
		  }
}
</script>
<form method="post" action="/puml/caracteristics/${splId}">
<div class="big-title">
		<spring:message code="puml.bigTitle"></spring:message>
	</div>
<c:forEach var="file" items="${folders}">
		<input id="id" name="${file.getName()}" type="hidden" value="${file.getName()}"/>
	</c:forEach>
<table>
  <tr>
    <td>
    <b>
		<spring:message code="puml.selectProducts"></spring:message>
	</b><br>
    <c:forEach var="p" items="${products}">
		<input type="checkbox" name="${p}">${p}</><br><br>
		</c:forEach>
	</td>
    <td>
    	<ul style="list-style: none;">
		  <li><b>
				<spring:message code="puml.selectedFolders"></spring:message>
			</b></li>
		  <c:forEach var="f1" items="${folders}">
				<li>${f1.getName()}<br>
				<input type="button" onclick="mostrar('${f1.getId()}')" value="mostrar/ocultar ficheros"></input>
				</li>
				<div id ="${f1.getId()}" class="auto">
				<c:forEach var="file" items="${f1.getFiles()}">
					<li>${file}</li>
				</c:forEach>
				</div>
			</c:forEach>
		</ul>
	</td>
  </tr>
</table>
    <input type="submit" value="Continuar" />
</form>


