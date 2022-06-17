<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../tiles/templates/taglibs.jsp"%>
<div class="big-title">
	<spring:message code="puml.title"></spring:message>
</div>
<form method="post" action="/puml/caracteristics/${splId}">
	<c:forEach var="folder" items="${folders}">
		<input id="id" name="${folder}" type="hidden" value="${folder}"/>
	</c:forEach>
	<c:forEach var="p" items="${products}">
		<input type="checkbox" name="${p}">${p}</><br><br>
	</c:forEach>
    <input type="submit" value="Continuar" />
</form>


