<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../tiles/templates/taglibs.jsp"%>
<div class="big-title">
	<spring:message code="puml.folderLevel"></spring:message>
</div>
<form method="post" action="/puml/folder/${splId}">
	<select name="level" id="level">
	<c:forEach var="p" items="${maxLevelList}">
		  <option value="${p}">${p}</option>
	</c:forEach>
	</select>
 <input type="submit" value="Continuar" />
 </form>