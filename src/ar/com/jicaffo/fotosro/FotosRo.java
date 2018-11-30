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

Crear persistencia mediante Properties o Preferences (todas las cosas de configuración
 */
package ar.com.jicaffo.fotosro;

import gui.pc.FramePrincipal;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class FotosRo {

    public static void main(String[] args) {
        System.out.println("Look And Feel Actual: "+UIManager.getLookAndFeel().getClass().getName());
        System.out.println("System Look And Feel: "+UIManager.getSystemLookAndFeelClassName());
        System.out.println("Cross Platform Look And Feel: "+UIManager.getCrossPlatformLookAndFeelClassName());
        System.out.println("Look And Feels implementations available: "+UIManager.getInstalledLookAndFeels());
        
        
        
        /*BD bd = new BD();
        bd.setUrlBD("jdbc:sqlite:BD/2018/Prueba1.db");
        bd.mostrarEnConsolaTodosLosCursos();
        System.out.println(bd.obtenerCursos());*/
        FramePrincipal fp = new FramePrincipal();

    }
}