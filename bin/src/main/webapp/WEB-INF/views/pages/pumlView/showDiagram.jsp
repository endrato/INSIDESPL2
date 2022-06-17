<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../../tiles/templates/taglibs.jsp"%>
<div class="big-title">
	<spring:message code="puml.diagramTitle"></spring:message>
</div>
<table>
  <tr>
    <td><img src="../../static/img/diagramWithoutFunctionInfo.png"/></td>
    <td>
    	<ul style="list-style: none;">
		  <li><div class="big-title">
				<spring:message code="puml.legend"></spring:message>
			</div></li>
			<li><b><spring:message code="domain.product.products"></spring:message></b></li>
		  <c:forEach var="pr" items="${products2}">
				<li>${pr}</li>
			</c:forEach>
			<li><b><spring:message code="domain.feature.selected"></spring:message></b></li>
		 <c:forEach var="f" items="${features2}">
				<li>${f}</li>
		</c:forEach>
			<li><b><spring:message code="puml.colourDescription"></spring:message></b></li>
			<li><spring:message code="puml.colourDescriptionJOIN"></spring:message></li>
			<li><spring:message code="puml.colourDescriptionOR"></spring:message></li>
			
			<li><b><spring:message code="puml.selectedFolders"></spring:message></b></li>
		<c:forEach var="fol" items="${folders2}">
				<li>${fol}</li>
		</c:forEach>
		</ul>
	</td>
  </tr>
</table>
