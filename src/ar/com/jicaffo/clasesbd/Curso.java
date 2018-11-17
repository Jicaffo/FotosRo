package ar.com.jicaffo.clasesbd;

import java.sql.Date;

public class Curso {

    private int idCurso;
    private String nombreCurso;
    private String turno;
    private String archivoFotoGrupal = null;
    private Date fechaFotoGrupal = null;

    public Curso(String nombreCurso, String turno) {
        this.nombreCurso = nombreCurso;
        this.turno = turno;
    }

    public Curso(String nombreCurso, String turno, String archivoFotoGrupal, Date fechaFotoGrupal) {
        this(nombreCurso, turno);
        this.archivoFotoGrupal = archivoFotoGrupal;
        this.fechaFotoGrupal = fechaFotoGrupal;
    }

    public Curso(int idCurso, String nombreCurso, String turno, String archivoFotoGrupal, Date fechaFotoGrupal) {
        this(nombreCurso, turno, archivoFotoGrupal, fechaFotoGrupal);
        this.idCurso = idCurso;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getArchivoFotoGrupal() {
        return archivoFotoGrupal;
    }

    public void setArchivoFotoGrupal(String archivoFotoGrupal) {
        this.archivoFotoGrupal = archivoFotoGrupal;
    }

    public Date getFechaFotoGrupal() {
        return fechaFotoGrupal;
    }

    public void setFechaFotoGrupal(Date fechaFotoGrupal) {
        this.fechaFotoGrupal = fechaFotoGrupal;
    }

    @Override
    public String toString() {
        return nombreCurso;
    }
}
