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

    private Date fechaIndividual;
    private Date fechaSeñoMña;
    private Date fechaSeñoTde;
    private Date fechaFotoFaltoGrupalMña; //tal vez se pueda clasificar en carpeta "para editar"
    private Date fechaFotoFaltoGrupalTde; //tal vez se pueda clasificar en carpeta "para editar"

    // El pedido base Puede ser Combo1, Combo2 o Grupal.
    // A Su vez si es alguno de los combos puede tener agregado llaveros (en modulos de a 2) o de copia de individual
    private int pedidoIndividual = 0;
    private int pedidoGrupalMña = 0;
    private int pedidoGrupalTde = 0;
    private int pedidoSeñoMña = 0;
    private int pedidoSeñoTde = 0;
    private int pedidoCarnet = 0;
    private int pedidoParDeLLaveros = 0;

    private double total = 0;
    private double montoAbonado = 0;
    private Date fechaPago;

    
    private double restaAbonar = 0;

    private boolean faltoGrupalMña = false; //Debería modificarse si al momento de sacar la foto grupal se pone que estaba ausente
    private boolean faltoGrupalTde = false; //Debería modificarse si al momento de sacar la foto grupal se pone que estaba ausente
    private boolean pagoAntesDeLaGrupal = false; //Debería modificarse en el momento que paga
    
    private String observacionesAlumno;
    
    //Para prueba rápida o pre-carga con datos mínimos
    public Alumno(String nombre) {
        this.setNombreAlumno(nombre);
    }

    public Alumno(String nombre, int idCurso) {
        this(nombre);
        this.setIdCursoAlumno(idCurso); //TO DO: Confirmar si no es completamente en vano (la BD genera solo el ID)
    }
    //Constructor Completo
    public Alumno(int idAlumno, String nombreAlumno, int idCursoAlumno,
            String archivoFotoIndividual, String archivoFotoSeñoMña, String archivoFotoSeñoTde, String archivoFotoFaltoGrupalMña, String archivoFotoFaltoGrupalTde,
            Date fechaFotoIndividual, Date fechaFotoSeñoMña, Date fechaFotoSeñoTde, Date fechaFotoFaltoGrupalMña, Date fechaFotoFaltoGrupalTde,
            int pedidoIndividual, int pedidoGrupalMña, int pedidoGrupalTde, int pedidoSenoMna, int pedidoSenoTde, int pedidoCarnet, int pedidoParDeLLaveros,
            boolean faltoGrupalMña, boolean faltoGrupalTde, boolean pagoAntesDeLaGrupal,
            double total, double montoAbonado, Date fechaPago, double restaAbonar, String observacionesAlumno) {
        
        this.idAlumno = idAlumno;
        this.nombreAlumno = nombreAlumno;
        this.idCursoAlumno = idCursoAlumno;
        
        this.archivoFotoIndividual = archivoFotoIndividual;
        this.archivoFotoSeñoMña = archivoFotoSeñoMña;
        this.archivoFotoSeñoTde = archivoFotoSeñoTde;
        this.archivoFotoFaltoGrupalMña = archivoFotoFaltoGrupalMña;
        this.archivoFotoFaltoGrupalTde = archivoFotoFaltoGrupalTde;
        
        this.fechaIndividual = fechaFotoIndividual;
        this.fechaSeñoMña = fechaFotoSeñoMña;
        this.fechaSeñoTde = fechaFotoSeñoTde;
        this.fechaFotoFaltoGrupalMña = fechaFotoFaltoGrupalMña;
        this.fechaFotoFaltoGrupalTde = fechaFotoFaltoGrupalTde;
        
        this.pedidoIndividual = pedidoIndividual;
        this.pedidoGrupalMña = pedidoGrupalMña;
        this.pedidoGrupalTde = pedidoGrupalTde;
        this.pedidoSeñoMña = pedidoSenoMna;
        this.pedidoSeñoTde = pedidoSenoTde;
        this.pedidoCarnet = pedidoCarnet;
        this.pedidoParDeLLaveros = pedidoParDeLLaveros;
        
        this.faltoGrupalMña = faltoGrupalMña;
        this.faltoGrupalTde = faltoGrupalTde;
        this.pagoAntesDeLaGrupal = pagoAntesDeLaGrupal;
        
        this.total = total;
        this.montoAbonado = montoAbonado;
        this.fechaPago = fechaPago;
        this.restaAbonar = restaAbonar;
        
        this.observacionesAlumno = observacionesAlumno;
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

    public String getArchivoFotoIndividual() {
        return archivoFotoIndividual;
    }

    public void setArchivoFotoIndividual(String archivoFotoIndividual) {
        this.archivoFotoIndividual = archivoFotoIndividual;
    }

    public String getArchivoFotoSeñoMña() {
        return archivoFotoSeñoMña;
    }

    public void setArchivoFotoSeñoMña(String archivoFotoSeñoMña) {
        this.archivoFotoSeñoMña = archivoFotoSeñoMña;
    }

    public String getArchivoFotoSeñoTde() {
        return archivoFotoSeñoTde;
    }

    public void setArchivoFotoSeñoTde(String archivoFotoSeñoTde) {
        this.archivoFotoSeñoTde = archivoFotoSeñoTde;
    }

    public String getArchivoFotoFaltoGrupalMña() {
        return archivoFotoFaltoGrupalMña;
    }

    public void setArchivoFotoFaltoGrupalMña(String archivoFotoFaltoGrupalMña) {
        this.archivoFotoFaltoGrupalMña = archivoFotoFaltoGrupalMña;
    }

    public String getArchivoFotoFaltoGrupalTde() {
        return archivoFotoFaltoGrupalTde;
    }

    public void setArchivoFotoFaltoGrupalTde(String archivoFotoFaltoGrupalTde) {
        this.archivoFotoFaltoGrupalTde = archivoFotoFaltoGrupalTde;
    }

    public Date getFechaIndividual() {
        return fechaIndividual;
    }

    public void setFechaIndividual(Date fechaIndividual) {
        this.fechaIndividual = fechaIndividual;
    }

    public Date getFechaSeñoMña() {
        return fechaSeñoMña;
    }

    public void setFechaSeñoMña(Date fechaSeñoMña) {
        this.fechaSeñoMña = fechaSeñoMña;
    }

    public Date getFechaSeñoTde() {
        return fechaSeñoTde;
    }

    public void setFechaSeñoTde(Date fechaSeñoTde) {
        this.fechaSeñoTde = fechaSeñoTde;
    }

    public Date getFechaFotoFaltoGrupalMña() {
        return fechaFotoFaltoGrupalMña;
    }

    public void setFechaFotoFaltoGrupalMña(Date fechaFotoFaltoGrupalMña) {
        this.fechaFotoFaltoGrupalMña = fechaFotoFaltoGrupalMña;
    }

    public Date getFechaFotoFaltoGrupalTde() {
        return fechaFotoFaltoGrupalTde;
    }

    public void setFechaFotoFaltoGrupalTde(Date fechaFotoFaltoGrupalTde) {
        this.fechaFotoFaltoGrupalTde = fechaFotoFaltoGrupalTde;
    }

    public int getPedidoIndividual() {
        return pedidoIndividual;
    }

    public void setPedidoIndividual(int pedidoIndividual) {
        this.pedidoIndividual = pedidoIndividual;
    }

    public int getPedidoGrupalMña() {
        return pedidoGrupalMña;
    }

    public void setPedidoGrupalMña(int pedidoGrupalMña) {
        this.pedidoGrupalMña = pedidoGrupalMña;
    }

    public int getPedidoGrupalTde() {
        return pedidoGrupalTde;
    }

    public void setPedidoGrupalTde(int pedidoGrupalTde) {
        this.pedidoGrupalTde = pedidoGrupalTde;
    }

    public int getPedidoSeñoMña() {
        return pedidoSeñoMña;
    }

    public void setPedidoSeñoMña(int pedidoSenoMna) {
        this.pedidoSeñoMña = pedidoSenoMna;
    }

    public int getPedidoSeñoTde() {
        return pedidoSeñoTde;
    }

    public void setPedidoSeñoTde(int pedidoSeñoTde) {
        this.pedidoSeñoTde = pedidoSeñoTde;
    }

    public int getPedidoCarnet() {
        return pedidoCarnet;
    }

    public void setPedidoCarnet(int pedidoCarnet) {
        this.pedidoCarnet = pedidoCarnet;
    }

    public int getPedidoParDeLLaveros() {
        return pedidoParDeLLaveros;
    }

    public void setPedidoParDeLLaveros(int pedidoParDeLLaveros) {
        this.pedidoParDeLLaveros = pedidoParDeLLaveros;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMontoAbonado() {
        return montoAbonado;
    }

    public void setMontoAbonado(double montoAbonado) {
        this.montoAbonado = montoAbonado;
    }

    public double getRestaAbonar() {
        return restaAbonar;
    }

    public void setRestaAbonar(double restaAbonar) {
        this.restaAbonar = restaAbonar;
    }

    public boolean faltoGrupalMña() {
        return faltoGrupalMña;
    }

    public void setFaltoGrupalMña(boolean faltoGrupalMña) {
        this.faltoGrupalMña = faltoGrupalMña;
    }

    public boolean faltoGrupalTde() {
        return faltoGrupalTde;
    }

    public void setFaltoGrupalTde(boolean faltoGrupalTde) {
        this.faltoGrupalTde = faltoGrupalTde;
    }

    public boolean pagoAntesDeLaGrupal() {
        return pagoAntesDeLaGrupal;
    }

    public void setPagoAntesDeLaGrupal(boolean pagoAntesDeLaGrupal) {
        this.pagoAntesDeLaGrupal = pagoAntesDeLaGrupal;
    }

    public String getObservacionesAlumno() {
        return observacionesAlumno;
    }

    public void setObservacionesAlumno(String observacionesAlumno) {
        this.observacionesAlumno = observacionesAlumno;
    }
    
    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    @Override
    public String toString() {
        return nombreAlumno;
    }
}
