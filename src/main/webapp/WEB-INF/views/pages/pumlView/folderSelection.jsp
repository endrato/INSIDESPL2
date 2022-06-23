<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../tiles/templates/taglibs.jsp"%>
<div class="big-title">
	<spring:message code="puml.bigTitle"></spring:message>
</div>
<big><spring:message code="puml.title"></spring:message></big>
<form method="post" action="/puml/products/${splId}">
	<c:forEach var="p" items="${folders}">
		<input type="checkbox" name="${p}">${p}</><br><br>
	</c:forEach>
 <input type="submit" value="Continuar" />
</form>