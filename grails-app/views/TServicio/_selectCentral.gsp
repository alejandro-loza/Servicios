<td  >

 <label for="central">
   <span class="required-indicator">*</span>
    <g:message code="orden.central.label" default="Central" />
    
     </label>
    <g:if test="${centralesList}">
        <g:select  name="central.id" from="${centralesList}" optionKey="id" required="" noSelection="['':'Selecciona una central']" />
        
        
        
    </g:if>
    <g:else>
        No existen centrales para este proyecto
    </g:else>
</td>



   
 