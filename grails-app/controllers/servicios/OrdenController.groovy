package servicios
import org.grails.plugin.filterpane.FilterPaneUtils
import org.springframework.dao.DataIntegrityViolationException
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

import servicios.Observacion
import groovy.sql.Sql

import org.hibernate.Session
import org.hibernate.Transaction

class OrdenController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }
     def springSecurityService
  
    def exportService
    def grailsApplication

   def list(Integer max) {
       
       if (params)
        params.max = Math.min(max ?: 50, 100)
        def ordenInstanceList = null
        def ordenInstanceTotal = null

        def filterParams = [:]
        
         if (session["sessionFilterOrden"] != null) {
            filterParams = session["sessionFilterOrden"]
            params.putAll(filterParams)
                System.out.println("con filtro sesion " + params) 
            ordenInstanceList = filterPaneService.filter(params, Orden)
           ordenInstanceTotal = filterPaneService.count(params, Orden)
            System.out.println("lista filtrada " + ordenInstanceList) 
            System.out.println("lista total " + ordenInstanceTotal) 
             
   
        }
        else {
          
            User usuario = User.findByUsername(springSecurityService.currentUser.username)//Obtiene el usuario logeado y lo busca en la bd r        
                 if (SpringSecurityUtils.ifAnyGranted('ROLE_USER,ROLE_SUPERVISOR')) {           
                      
                      def ordenes = Orden.findAllByProyectoInList(usuario.proyectos,[max: params.max, sort: "id", order: "desc", offset: params.offset]) 
                     
     
          
                    ordenInstanceList = ordenes
                    ordenInstanceTotal = ordenes.size()
            }
            
           else {
               
               ordenInstanceList = Orden.list(params)
               ordenInstanceTotal = Orden.count() 
               
           } 
        }
        
      
        if(params?.format && params.format != "html" && session["sessionFilterOrden"] == null)
        {
             System.out.println("sin filtro")
			    def fields = ["proyecto.nombre","status.nombre","dateCreated"]
                        def labels = ["proyecto.nombre":"Proyecto","status.nombre":"Estatus","dateCreated":"Fecha de creación"]
                            
   ordenInstanceList = filterPaneService.filter(params, Orden)
           ordenInstanceTotal = filterPaneService.count(params, Orden)
              response.contentType = grailsApplication.config.grails.mime.types[params.format]
              Map parameters = ["encoding":"UTF8"]
			response.setHeader("Content-disposition", "attachment; filename=ordenes.${params.extension}")

exportService.export(params.format, response.outputStream,ordenInstanceList, [:], [:])
             
		}
       
        
        if(params?.format && params.format != "html" && session["sessionFilterOrden"] != null)
            {
      
           filterParams = session["sessionFilterOrden"]
            params.putAll(filterParams)
                System.out.println("filtro + export  " + params) 
            ordenInstanceList = filterPaneService.filter(params, Orden)
           ordenInstanceTotal = filterPaneService.count(params, Orden)
              response.contentType = grailsApplication.config.grails.mime.types[params.format]
			response.setHeader("Content-disposition", "attachment; filename=ordenesFiltradas.${params.extension}")

exportService.export(params.format, response.outputStream,ordenInstanceList, [:], [:])
           /* def exportList = filterPaneService.filter(params, Orden)
            println "session export list:" + exportList
            export(response: response, extension: params.extension, format: params.format, exportList: exportList)
             */  
            }
     
       [ordenInstanceList: ordenInstanceList, ordenInstanceTotal: ordenInstanceTotal, filterParams: filterParams]
    }
    
       
    
def filterPaneService


     def filter = {
        if (!params.max) params.max = 50

          def filterParams = org.grails.plugin.filterpane.FilterPaneUtils.extractFilterParams(params)

        session["sessionFilterOrden"] = filterParams
        System.out.println("filtro en session " + session.sessionFilterOrden) 
  
        redirect(action: "list", 
                model: [ordenInstanceList: filterPaneService.filter( params, Orden ),
                         ordenInstanceCount: filterPaneService.count( params, Orden ),
                       filterParams:filterParams,
                        params: params])
                        
    }
    

    
  def ticketdisp = {
      
        def ordenInstance = Orden.findByTicket(params.ticket)
        if (ordenInstance){
            render "¡Ya existe este ticket!"
        }
        else {
            render " "
        }
       
  }
    
 
    
   
     def create() {
       
		 User usuario = springSecurityService.getCurrentUser()
        //Procedimiento Create  
         def statusLista = Status.executeQuery(" from Status s " +
                     "where s.tipo = 'Proyecto'")
        
        def ordenInstance = new Orden()
         ordenInstance.properties = params
         ordenInstance.usuario = usuario
       
        
      def proyectoFiltro= usuario.proyectos  
    
     def idcActivos = Idc.findAll(sort:"nombre") {
     estatus == "Activo"
    }
     
        [ordenInstance: ordenInstance, proyecto:proyectoFiltro, idcActivos:idcActivos , statusLista : statusLista]
    }
    
    
    

    def save() {
        def ordenInstance = new Orden(params)
        if (!ordenInstance.save(flush: true)) {
            render(view: "create", model: [ordenInstance: ordenInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'orden.label', default: 'Orden'), ordenInstance.id])
        redirect(action: "show", id: ordenInstance.id)
    }
    
    def saveD() {
        def ordenInstance = new Orden(params)
        if (!ordenInstance.save(flush: true)) {
            render(view: "create", model: [ordenInstance: ordenInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'orden.label', default: 'Orden'), ordenInstance.id])
        redirect(controller:"observacion", action: "create", params:['orden.id': ordenInstance?.id])
      //  redirect(action: "show", id: ordenInstance.id)
    }
    def saveS() {
        def ordenInstance = new Orden(params)
        if (!ordenInstance.save(flush: true)) {
            render(view: "create", model: [ordenInstance: ordenInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'orden.label', default: 'Orden'), ordenInstance.id])
        redirect(controller:"seguimiento", action: "create", params:['orden.id': ordenInstance?.id , 'orden.sitio': ordenInstance?.sitio])
      //  redirect(action: "show", id: ordenInstance.id)
    }

    def show(Long id) {
        def ordenInstance = Orden.get(id)
       
        if (!ordenInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'orden.label', default: 'Orden'), id])
            redirect(action: "list")
            return
        }
      
        def ordenIns = Orden.findById(ordenInstance.id)       
      // def observacionesList =  Observacion.findAllByOrden(ordenInstance) 
        def observacionesList = ordenIns ? Observacion.findAllByOrden(ordenIns) : []
       System.out.println("objeto instance" + ordenInstance) 
       System.out.println("objeto id" + ordenInstance.id) 
       System.out.println("objeto id" + observacionesList) 
   
       
       
     
       
        [ordenInstance: ordenInstance , observacionesList:observacionesList ]
       // [ordenInstance: ordenInstance  ]
    }

    def edit(Long id) {
        def ordenInstance = Orden.get(id)
        
        if (!ordenInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'orden.label', default: 'Orden'), id])
            redirect(action: "list")
            return
        }

        [ordenInstance: ordenInstance]
    }

    def update(Long id, Long version) {
        def ordenInstance = Orden.get(id)
        if (!ordenInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'orden.label', default: 'Orden'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (ordenInstance.version > version) {
                ordenInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'orden.label', default: 'Orden')] as Object[],
                          "Another user has updated this Orden while you were editing")
                render(view: "edit", model: [ordenInstance: ordenInstance])
                return
            }
        }
        
  /*  
           System.out.println("objeto properties " + ordenInstance.properties.statusId)   
           System.out.println("objeto params " + params.status.id)
           String a = ordenInstance.properties.statusId
           String b = params.status.id
           System.out.println("objeto a " + a)   
           System.out.println("objeto b " + b)
           
      
        
      
        
        if ( a != b ) {
             System.out.println("no son iguales :(  "  )  
             System.out.println("id orden:(  " + ordenInstance?.id )  
       //     def ob = new Observacion (nombre:"Cambio de Status", descripcion:"Se cambio el estatus ", orden: ordenInstance?.id, usuario: 1)
         //   ob.save()
          //   System.out.println("observacion" +ob )
          
            Observacion ob = new Observacion (nombre:"Cambio de Status", descripcion:"Se cambio el estatus ", orden: ordenInstance?.id, usuario: 1).save()
           // ob.orden = ordenInstance
            //ob.save(flush: true)
            System.out.println("observacion" +ob )
            System.out.println("obs" +ob.orden )
        //  Observacion.executeQuery('insert into observacion (nombre, descripcion, orden) values (?,?,?)', ["Cambio de status", "si se pudo", ordenInstance.properties.id ])
        }
      //  if (a != b){ System.out.println("no son iguales")}
      */  
        
        ordenInstance.properties = params

        if (!ordenInstance.save(flush: true)) {
            render(view: "edit", model: [ordenInstance: ordenInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'orden.label', default: 'Orden'), ordenInstance.id])
        redirect(action: "show", id: ordenInstance.id)
    }

    def delete(Long id) {
        def ordenInstance = Orden.get(id)
        if (!ordenInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'orden.label', default: 'Orden'), id])
            redirect(action: "list")
            return
        }

        try {
            ordenInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'orden.label', default: 'Orden'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'orden.label', default: 'Orden'), id])
            redirect(action: "show", id: id)
        }
    }
}
