<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../tiles/templates/taglibs.jsp"%>
<div class="big-title">
	<spring:message code="puml.title"></spring:message>
</div>
<form method="post" action="/puml/showDiagram/${splId}">
	<c:forEach var="folder" items="${folders}">
		<input id="id" name="${folder}" type="hidden" value="${folder}"/>
	</c:forEach>
	<c:forEach var="pr" items="${products}">
		<input id="id" name="${pr}" type="hidden" value="${pr}"/>
	</c:forEach>
	<c:forEach var="p" items="${features}">
		<input type="checkbox" name="${p}">${p}</><br><br>
	</c:forEach>
    <input type="submit" value="Continuar" />
</form>