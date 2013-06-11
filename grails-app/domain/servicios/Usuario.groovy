package servicios

class Usuario  extends User{
	String nombre
	
	Date fechaNacimiento
	String direccion
	String numeroTelefonico
	String email
	Date dateCreated
	Date lastUpdated
	String confirmacionPassword
	  static hasMany = [proyectos:Proyecto]
      static belongsTo = Proyecto

    static constraints = {
		
    }
}
