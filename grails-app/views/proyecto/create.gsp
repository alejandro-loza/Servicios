<%@ page import="servicios.Proyecto" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'proyecto.label', default: 'Proyecto')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
		 <g:javascript library="jquery" plugin="jquery"/>
		<link rel="stylesheet" href="${resource(dir: 'css', file: 'multi-select.css')}" type="text/css">
	</head>
	<body>
	<g:javascript src="jquery.multi-select.js"/>
	<script type="text/javascript">
 		$(document).ready(function() {
			$('#multicentral').multiSelect({
	 			selectableHeader: "<div class='custom-header text-center'>Centrales Disponibles</div>",
	  			selectionHeader: "<div class='custom-header text-center'>Centrales Seleccionadas</div>"
			});
 		});
	</script>
		<a href="#create-proyecto" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="create-proyecto" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${proyectoInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${proyectoInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form action="save" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
