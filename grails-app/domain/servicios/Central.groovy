package servicios

class Central {
    String zona
    String nombre
    Date dateCreated
    Date lastUpdated
     static hasMany = [sitios:Sitio, proyectos: Proyecto]
	 static belongsTo = Proyecto
     

    static constraints = {
      
        nombre nullable:false, blank:false
    }
    String toString() {
       nombre
    }
}




