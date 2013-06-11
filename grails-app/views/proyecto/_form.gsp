<%@ page import="servicios.Proyecto" %>



<div class="fieldcontain ${hasErrors(bean: proyectoInstance, field: 'nombre', 'error')} required">
	<label for="nombre">
		<g:message code="proyecto.nombre.label" default="Nombre" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nombre" required="" value="${proyectoInstance?.nombre}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: proyectoInstance, field: 'centrales', 'error')} ">
	<label for="centrales">
		<g:message code="proyecto.centrales.label" default="Centrales" />
		
	</label>
	<g:select id="multicentral" name="centrales" from="${servicios.Central.list()}" multiple="multiple" optionKey="id" size="5" value="${proyectoInstance?.centrales*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: proyectoInstance, field: 'descripcion', 'error')} ">
	<label for="descripcion">
		<g:message code="proyecto.descripcion.label" default="Descripcion" />
		
	</label>
	<g:textField name="descripcion" value="${proyectoInstance?.descripcion}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: proyectoInstance, field: 'servicios', 'error')} ">
	<label for="servicios">
		<g:message code="proyecto.servicios.label" default="Servicios" />
		
	</label>
	<g:select name="servicios" from="${servicios.TServicio.list()}" multiple="multiple" optionKey="id" size="5" value="${proyectoInstance?.servicios*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: proyectoInstance, field: 'usuarios', 'error')} ">
	<label for="usuarios">
		<g:message code="proyecto.usuarios.label" default="Usuarios" />
		
	</label>
	<g:select name="usuarios" from="${servicios.User.list()}" multiple="multiple" optionKey="id" size="5" value="${proyectoInstance?.usuarios*.id}" class="many-to-many"/>
</div>

