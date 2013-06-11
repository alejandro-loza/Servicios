
<td  >

 <label for="servicio">
   <span class="required-indicator">*</span>
        <g:message code="orden.servicio.label" default="Servicio"/>
       
    </label>
    <g:if test="${serviciosList}">
        <g:select  name="servicio.id" from="${serviciosList}" optionKey="id" required="" noSelection="['':' ']"/>
    </g:if>
    <g:else>
        No existen servicios para este proyecto
    </g:else>


</td>