
package servicios

class Proyecto {
    String nombre
    String descripcion
    static hasMany = [usuarios: User, centrales: Central, servicios: TServicio ]
    Date dateCreated
    Date lastUpdated
 
    
   
    static constraints = {
        nombre nullable:false, blank:false
        
        
    }
     public String toString() {
         nombre
         
    
    }
}
