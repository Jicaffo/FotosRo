/*

Ruta de bajado de fotos para backup:
Bajada general de Backup: "\Escuela\curso"

Para la imprenta:
"\Escuela\Curso\Medida\_DSC####.JPG"
(al final del nombre se le agrega para futura referencia junto con sus cantidades que combo y si lleva copia y/o llavero)
(esto se usa tambien para identificar cuando vuelven las fotos de la imprenta que es lo que quiere c/u)
la cantidad de copias tiene que ir al principio del nombre de cada archivo en caso de ser más de 1 unidad
las grupales de todos los cursos van en una carpeta aparte al nivel de los cursos
- seria ideal que cuando falta un pibe se pueda vincular a la grupal

- Las Bases de Datos las guardaría en 1 carpeta por año y un archivo DB por institución con todos los cursos
- Los filtros deberían filtrar segun ese criterio
- Por defecto debería cargarse la última BD y curso utilizados al abrir el programa y el año vigente si no se seleccionó ninguno.

 */
package ar.com.jicaffo.fotosro;

import ar.com.jicaffo.clasesbd.Alumno;
import gui.pc.FramePrincipal;

public class FotosRo {

    public static void main(String[] args) {
        /*BD bd = new BD();
        bd.setUrlBD("jdbc:sqlite:BD/2018/Prueba1.db");
        bd.mostrarEnConsolaTodosLosCursos();
        System.out.println(bd.obtenerCursos());*/
        FramePrincipal fp = new FramePrincipal();
        
        
        
    }

    private static void cargarAlumnosPrueba(BD bd) {
        Alumno al1 = new Alumno("Rocío Gonzalez",1);
        Alumno al2 = new Alumno("Pablo Betancurt",2);
        Alumno al3 = new Alumno("Marian Leytur",3);
        Alumno al4 = new Alumno("Martin Leytur",1);
        Alumno al5 = new Alumno("Matias Leytur",2);
        
        bd.insertarAlumno(al1);
        bd.insertarAlumno(al2);
        bd.insertarAlumno(al3);
        bd.insertarAlumno(al4);
        bd.insertarAlumno(al5);
    }
}
