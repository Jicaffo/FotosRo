package ar.com.jicaffo.clasesbd;

import java.util.Date;

public class Alumno {

    private int idAlumno; //tiene que ser único (crear static ultimoId?)
    private String nombreAlumno;
    private int idCursoAlumno;

    //Al sacar la foto, ya sea individual o grupal, se debería cargar manualmente el número de foto (Formato Nombre: "_DSC0000.JPG").
    //Ver si trabajar como String o File
    //private String archivoFotoGrupal = null; // Esta la sacaría de la clase Curso
    //Date fechaFotoGrupal = null;
    private String archivoFotoIndividual;
    private String archivoFotoSeñoMña;
    private String archivoFotoSeñoTde;
    private String archivoFotoFaltoGrupalMña; //tal vez se pueda clasificar en carpeta "para editar"
    private String archivoFotoFaltoGrupalTde; //tal vez se pueda clasificar en carpeta "para editar"

    private Date fechaFotoIndividual;
    private Date fechaFotoSeñoMña;
    private Date fechaFotoSeñoTde;
    private Date fechaFotoFaltoGrupalMña; //tal vez se pueda clasificar en carpeta "para editar"
    private Date fechaFotoFaltoGrupalTde; //tal vez se pueda clasificar en carpeta "para editar"

    // El pedido base Puede ser Combo1, Combo2 o Grupal.
    // A Su vez si es alguno de los combos puede tener agregado llaveros (en modulos de a 2) o de copia de individual
    private int pedidoIndividual = 0;
    private int pedidoGrupalMña = 0;
    private int pedidoGrupalTde = 0;
    private int pedidoSenoMna = 0;
    private int pedidoSenoTde = 0;
    private int pedidoCarnet = 0;
    private int pedidoParDeLLaveros = 0;

    private double total = 0;
    private double montoAbonado = 0;
    private double restaAbonar = 0;

    private boolean estuvoEnGrupalMña = true; //Debería modificarse si al momento de sacar la foto grupal se pone que estaba ausente
    private boolean estuvoEnGrupalTde = true; //Debería modificarse si al momento de sacar la foto grupal se pone que estaba ausente
    private boolean pagoAntesDeLaGrupal = false; //Debería modificarse en el momento que paga

    //Para prueba rápida o pre-carga con datos mínimos
    public Alumno(String nombre) {
        this.setNombreAlumno(nombre);
    }

    public Alumno(String nombre, int idCurso) {
        this(nombre);
        this.setIdCursoAlumno(idCurso);
    }
    //Constructor Completo
    public Alumno(int idAlumno, String nombreAlumno, int idCursoAlumno,
            String archivoFotoIndividual, String archivoFotoSeñoMña, String archivoFotoSeñoTde, String archivoFotoFaltoGrupalMña, String archivoFotoFaltoGrupalTde,
            Date fechaFotoIndividual, Date fechaFotoSeñoMña, Date fechaFotoSeñoTde, Date fechaFotoFaltoGrupalMña, Date fechaFotoFaltoGrupalTde,
            int pedidoIndividual, int pedidoGrupalMña, int pedidoGrupalTde, int pedidoSenoMna, int pedidoSenoTde, int pedidoCarnet, int pedidoParDeLLaveros,
            boolean estuvoEnGrupalMña, boolean estuvoEnGrupalTde, boolean pagoAntesDeLaGrupal,
            double total, double montoAbonado, double restaAbonar) {
        
        this.idAlumno = idAlumno;
        this.nombreAlumno = nombreAlumno;
        this.idCursoAlumno = idCursoAlumno;
        
        this.archivoFotoIndividual = archivoFotoIndividual;
        this.archivoFotoSeñoMña = archivoFotoSeñoMña;
        this.archivoFotoSeñoTde = archivoFotoSeñoTde;
        this.archivoFotoFaltoGrupalMña = archivoFotoFaltoGrupalMña;
        this.archivoFotoFaltoGrupalTde = archivoFotoFaltoGrupalTde;
        
        this.fechaFotoIndividual = fechaFotoIndividual;
        this.fechaFotoSeñoMña = fechaFotoSeñoMña;
        this.fechaFotoSeñoTde = fechaFotoSeñoTde;
        this.fechaFotoFaltoGrupalMña = fechaFotoFaltoGrupalMña;
        this.fechaFotoFaltoGrupalTde = fechaFotoFaltoGrupalTde;
        
        this.pedidoIndividual = pedidoIndividual;
        this.pedidoGrupalMña = pedidoGrupalMña;
        this.pedidoGrupalTde = pedidoGrupalTde;
        this.pedidoSenoMna = pedidoSenoMna;
        this.pedidoSenoTde = pedidoSenoTde;
        this.pedidoCarnet = pedidoCarnet;
        this.pedidoParDeLLaveros = pedidoParDeLLaveros;
        
        this.estuvoEnGrupalMña = estuvoEnGrupalMña;
        this.estuvoEnGrupalTde = estuvoEnGrupalTde;
        this.pagoAntesDeLaGrupal = pagoAntesDeLaGrupal;
        
        this.total = total;
        this.montoAbonado = montoAbonado;
        this.restaAbonar = restaAbonar;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public int getIdCursoAlumno() {
        return idCursoAlumno;
    }

    public void setIdCursoAlumno(int idCursoAlumno) {
        this.idCursoAlumno = idCursoAlumno;
    }

    @Override
    public String toString() {
        return nombreAlumno;
    }

}
