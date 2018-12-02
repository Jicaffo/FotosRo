/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.jicaffo.fotosro;

import ar.com.jicaffo.clasesbd.Alumno;
import ar.com.jicaffo.clasesbd.Curso;
import gui.pc.FramePrincipal;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * @author Juan
 */
public class BD {

    Connection conn;
    //db parameters (formato "jdbc:subprotocol:subname")
    private String urlBD;

    private String anio = "2018"; //a reemplazar por año actual
    private PreparedStatement ps;
    private Statement st;

    //Crea un objeto BD para conectarse a una base de datos ya existente (no crea nuevos archivos)
    public BD() {
        String rutaDB = FramePrincipal.getRutaBD() + "/" + FramePrincipal.getAñoActual() + "/" + FramePrincipal.getUltimaBD() + ".db";
        File dbFile = new File(rutaDB);
        if (dbFile.exists()) {
            conn = null;

            try {
                //create a connection to the database
                urlBD = "jdbc:sqlite:BD/" + FramePrincipal.getAñoActual() + "/" + FramePrincipal.getUltimaBD() + ".db";
                conn = DriverManager.getConnection(urlBD);
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Se ha conectado mediante \'" + meta.getDriverName() + "\' a la base de datos \'" + FramePrincipal.getUltimaBD() + "\'");
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    //Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getMessage());
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "La nueva base de datos a la que estás queriendo conectarte no existe."
                    + "Creala primero para poder acceder a ella.");
        }
    }

    //Crea una nueva BD con el nombre especificado y la estructura armada y copiada manualmente desde SQLiteStudio
    public BD(String nombreBD, Boolean crearBD) {

        if (crearBD) {
            File dbFile = new File(FramePrincipal.getRutaBD() + "/" + nombreBD + ".db");
            if (dbFile.exists()) {
                JOptionPane.showMessageDialog(null, "La nueva base de datos que estás queriendo crear ya existe. Elige otro nombre");
                System.out.println("No se ha creado ninguna base de datos.");
            } else {
                conn = null;

                try {
                    //create a connection to the database
                    urlBD = "jdbc:sqlite:BD/" + anio + "/" + nombreBD + ".db";
                    conn = DriverManager.getConnection(urlBD);
                    DatabaseMetaData meta = conn.getMetaData();
                    System.out.println("La conexión con " + meta.getDriverName() + " se ha establecido. Se ha creado la nueva base de datos\"" + nombreBD + "\".");

                    st = conn.createStatement();
                    // create new tables
                    String sql = "CREATE TABLE alumnos (\n"
                            + "    id_alumno                     INTEGER PRIMARY KEY AUTOINCREMENT  UNIQUE,\n"
                            + "    nombre_alumno                 STRING  NOT NULL,\n"
                            + "    id_curso_alumno               STRING  REFERENCES cursos (id_curso)  NOT NULL,\n"
                            + "    archivo_foto_individual       STRING  UNIQUE,\n"
                            + "    archivo_foto_seño_mña         STRING  UNIQUE,\n"
                            + "    archivo_foto_seño_tde         STRING  UNIQUE,\n"
                            + "    archivo_foto_falto_grupal_mña STRING  UNIQUE,\n"
                            + "    archivo_foto_falto_grupal_tde STRING  UNIQUE,\n"
                            + "    fecha_foto_individual         DATE,\n"
                            + "    fecha_foto_seño_mña           DATE,\n"
                            + "    fecha_foto_seño_tde           DATE,\n"
                            + "    fecha_foto_falto_grupal_mña   DATE,\n"
                            + "    fecha_foto_falto_grupal_tde   DATE    DEFAULT NULL,\n"
                            + "    pedido_individual             INTEGER DEFAULT (0),\n"
                            + "    pedido_grupal_mña             INTEGER DEFAULT (0),\n"
                            + "    pedido_grupal_tde             INTEGER DEFAULT (0),\n"
                            + "    pedido_seño_mña               INTEGER DEFAULT (0),\n"
                            + "    pedido_seño_tde               INTEGER DEFAULT (0),\n"
                            + "    pedido_carnet                 INTEGER DEFAULT (0),\n"
                            + "    pedido_llaveros               INTEGER DEFAULT (0),\n"
                            + "    falto_grupal_mña              BOOLEAN DEFAULT (false),\n"
                            + "    falto_grupal_tde              BOOLEAN DEFAULT (false),\n"
                            + "    pago_antes_de_grupal          BOOLEAN DEFAULT (false),\n"
                            + "    total_a_pagar                 DOUBLE  DEFAULT (0.0),\n"
                            + "    monto_abonado                 DOUBLE  DEFAULT (0.0),\n"
                            + "    fecha_pago                    DATE,\n"
                            + "    resta_abonar                  DOUBLE  DEFAULT (0.0),\n"
                            + "    observaciones_alumno          STRING\n"
                            + ");";
                    st.execute(sql);

                    sql = "CREATE TABLE cursos (\n"
                            + "    id_curso            INTEGER PRIMARY KEY AUTOINCREMENT\n"
                            + "                                UNIQUE,\n"
                            + "    turno               STRING  NOT NULL,\n"
                            + "    nombre_curso        STRING  UNIQUE\n"
                            + "                                NOT NULL,\n"
                            + "    archivo_foto_grupal STRING  UNIQUE,\n"
                            + "    fecha_foto_grupal   DATE\n"
                            + ");";
                    st.execute(sql);

                    sql = "CREATE TABLE fotos (\n"
                            + "    id_foto   INTEGER PRIMARY KEY AUTOINCREMENT\n"
                            + "                      UNIQUE,\n"
                            + "    tipo_foto STRING  NOT NULL,\n"
                            + "    desc_foto STRING\n"
                            + ");";
                    st.execute(sql);
                    System.out.println("Se han creado las 3 tablas de la BD correctamente: Alumnos, Cursos y Fotos.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        //Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }

    public String getUrlBD() {
        return urlBD;
    }

    public void setUrlBD(String urlBD) {
        this.urlBD = urlBD;
    }

    public void insertarAlumno(Alumno al) {
        // SQL statement for creating a new table
        String sql = "INSERT INTO alumnos(nombre_alumno,id_curso_alumno) VALUES(?,?);";

        try {
            conn = DriverManager.getConnection(urlBD);
            ps = conn.prepareStatement(sql);
            ps.setString(1, al.getNombreAlumno());
            ps.setInt(2, al.getIdCursoAlumno());
            ps.executeUpdate();
            System.out.println("Se ha insertado correctamente a " + al.getNombreAlumno() + " a la tabla alumnos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void insertarAlumnos(ArrayList<Alumno> listaAlumnos) {
        conn = null;
        String sql;
        if (listaAlumnos.size() == 1) {
            sql = "INSERT INTO alumnos(nombre_alumno,id_curso_alumno) "
                    + "VALUES ('" + listaAlumnos.get(0).getNombreAlumno() + "'," + listaAlumnos.get(0).getIdCursoAlumno() + ");";
        } else {
            sql = "INSERT INTO alumnos(nombre_alumno,id_curso_alumno) "
                    + "VALUES ('";
            for (Alumno a : listaAlumnos) {
                if (listaAlumnos.indexOf(a) != (listaAlumnos.size() - 1)) {
                    sql += a.getNombreAlumno() + "', " + a.getIdCursoAlumno() + "), ('";
                } else {
                    sql += a.getNombreAlumno() + "', " + a.getIdCursoAlumno() + ");";
                }
            }
        }
        try {
            conn = DriverManager.getConnection(urlBD);
            st = conn.createStatement();
            st.execute(sql);
            System.out.println("Se ha insertado correctamente a " + listaAlumnos + " a la tabla alumnos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateAlumno(Alumno a) {
        // Este update no incluye modificación del nombre o el ID de curso.
        String sql = "UPDATE alumnos SET \n"
                + "    archivo_foto_individual = "+a.getArchivoFotoIndividual()+",\n"
                + "    archivo_foto_seño_mña = "+a.getArchivoFotoSeñoMña()+",\n"
                + "    archivo_foto_seño_tde = "+a.getArchivoFotoSeñoTde()+",\n"
                + "    archivo_foto_falto_grupal_mña = "+a.getArchivoFotoFaltoGrupalMña()+",\n"
                + "    archivo_foto_falto_grupal_tde = "+a.getArchivoFotoFaltoGrupalTde()+",\n"
                + "    fecha_foto_individual = "+a.getFechaIndividual()+",\n"
                + "    fecha_foto_seño_mña = "+a.getFechaSeñoMña()+",\n"
                + "    fecha_foto_seño_tde = "+a.getFechaSeñoTde()+",\n"
                + "    fecha_foto_falto_grupal_mña = "+a.getFechaFotoFaltoGrupalMña()+",\n"
                + "    fecha_foto_falto_grupal_tde = "+a.getFechaFotoFaltoGrupalTde()+",\n"
                + "    pedido_individual = "+a.getPedidoIndividual()+",\n"
                + "    pedido_grupal_mña = "+a.getPedidoGrupalMña()+",\n"
                + "    pedido_grupal_tde = "+a.getPedidoGrupalTde()+",\n"
                + "    pedido_seño_mña = "+a.getPedidoSeñoMña()+",\n"
                + "    pedido_seño_tde = "+a.getPedidoSeñoTde()+",\n"
                + "    pedido_carnet = "+a.getPedidoCarnet()+",\n"
                + "    pedido_llaveros = "+a.getPedidoParDeLLaveros()+",\n"
                + "    falto_grupal_mña = "+a.faltoGrupalMña()+",\n"
                + "    falto_grupal_tde = "+a.faltoGrupalTde()+",\n"
                + "    pago_antes_de_grupal = "+a.pagoAntesDeLaGrupal()+",\n"
                + "    total_a_pagar = "+a.getTotal()+",\n"
                + "    monto_abonado = "+a.getMontoAbonado()+",\n"
                + "    fecha_pago = "+a.getFechaPago()+",\n"
                + "    resta_abonar = "+a.getRestaAbonar()+",\n"
                + "    observaciones_alumno = \'"+a.getObservacionesAlumno()+"\' \n"
                + "    WHERE "
                + "    id_alumno = "+a.getIdAlumno()+";";
        try {
            conn = DriverManager.getConnection(urlBD);
            st = conn.createStatement();
            st.execute(sql);
            System.out.println("Se ha actualizado Correctamente a " + a.getNombreAlumno() + ", con las siguientes observaciones "+a.getObservacionesAlumno());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void insertarCursoSinFoto(Curso c) {
        // SQL statement for creating a new table
        String sql = "INSERT INTO cursos(nombre_curso, turno) VALUES(?,?)";

        try {
            conn = DriverManager.getConnection(urlBD);
            ps = conn.prepareStatement(sql);
            ps.setString(1, c.getNombreCurso());
            ps.setString(2, c.getTurno());
            ps.executeUpdate();
            System.out.println("Se ha insertado correctamente \"" + c.getNombreCurso() + "\" a la tabla cursos");
        } catch (SQLException ex) {
            ex.printStackTrace();
            //AREVISAR: intentar distinguir de una excepcion por nombre ya ingresado en la base de datos y otras excepciones SQL
            JOptionPane.showMessageDialog(null, "El nuevo curso \"" + c.getNombreCurso() + "\" no ha podido instertarse en la Base de Datos.\nEs probable que ya hubiera un curso con ese nombre, pruebe nuevamente.", "Error de escritura en Base de datos", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void insertarCursoConFoto(Curso c) {
        // SQL statement for creating a new table
        String sql = "INSERT INTO cursos(nombre_curso, turno, archivo_foto_grupal, fecha_foto_grupal) VALUES(?,?,?,?)";

        try {
            conn = DriverManager.getConnection(urlBD);
            ps = conn.prepareStatement(sql);
            ps.setString(1, c.getNombreCurso());
            ps.setString(2, c.getTurno());
            ps.setString(3, c.getArchivoFotoGrupal());
            ps.setDate(4, c.getFechaFotoGrupal());
            ps.executeUpdate();
            System.out.println("Se ha insertado correctamente \"" + c.getNombreCurso() + "\" a la tabla cursos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<Alumno> obtenerAlumnos(Curso c) {
        ArrayList<Alumno> alumnos = new ArrayList();
        String sql = "SELECT * FROM alumnos WHERE id_curso_alumno = " + c.getIdCurso() + ";";
        try {
            conn = DriverManager.getConnection(this.urlBD);
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            //loop through the result set
            while (rs.next()) {
                int idAlumno = rs.getInt("id_alumno");
                String nombreAlumno = rs.getString("nombre_alumno");
                int idCursoAlumno = rs.getInt("id_curso_alumno");

                String archivoFotoIndividual = rs.getString("archivo_foto_individual");
                String archivoFotoSeñoMña = rs.getString("archivo_foto_seño_mña");
                String archivoFotoSeñoTde = rs.getString("archivo_foto_seño_tde");
                String archivoFotoFaltoGrupalMña = rs.getString("archivo_foto_falto_grupal_mña");
                String archivoFotoFaltoGrupalTde = rs.getString("archivo_foto_falto_grupal_tde");

                Date fechaFotoIndividual = rs.getDate("fecha_foto_individual");
                Date fechaFotoSeñoMña = rs.getDate("fecha_foto_seño_mña");
                Date fechaFotoSeñoTde = rs.getDate("fecha_foto_seño_tde");
                Date fechaFotoFaltoGrupalMña = rs.getDate("fecha_foto_falto_grupal_mña");
                Date fechaFotoFaltoGrupalTde = rs.getDate("fecha_foto_falto_grupal_tde");

                int pedidoIndividual = rs.getInt("pedido_individual");
                int pedidoGrupalMña = rs.getInt("pedido_grupal_mña");
                int pedidoGrupalTde = rs.getInt("pedido_grupal_tde");
                int pedidoSenoMna = rs.getInt("pedido_seño_mña");
                int pedidoSenoTde = rs.getInt("pedido_seño_tde");
                int pedidoCarnet = rs.getInt("pedido_carnet");
                int pedidoParDeLLaveros = rs.getInt("pedido_llaveros");

                boolean faltoGrupalMña = rs.getBoolean("falto_grupal_mña");
                boolean faltoGrupalTde = rs.getBoolean("falto_grupal_tde");
                boolean pagoAntesDeLaGrupal = rs.getBoolean("pago_antes_de_grupal");

                double total = rs.getDouble("total_a_pagar");
                double montoAbonado = rs.getDouble("monto_abonado");
                Date fechaPago = rs.getDate("fecha_pago");
                double restaAbonar = rs.getDouble("resta_abonar");

                String observacionesAlumno = rs.getString("observaciones_alumno");

                Alumno a = new Alumno(idAlumno, nombreAlumno, idCursoAlumno,
                        archivoFotoIndividual, archivoFotoSeñoMña, archivoFotoSeñoTde, archivoFotoFaltoGrupalMña, archivoFotoFaltoGrupalTde,
                        fechaFotoIndividual, fechaFotoSeñoMña, fechaFotoSeñoTde, fechaFotoFaltoGrupalMña, fechaFotoFaltoGrupalTde,
                        pedidoIndividual, pedidoGrupalMña, pedidoGrupalTde, pedidoSenoMna, pedidoSenoTde, pedidoCarnet, pedidoParDeLLaveros,
                        faltoGrupalMña, faltoGrupalTde, pagoAntesDeLaGrupal,
                        total, montoAbonado, fechaPago, restaAbonar, observacionesAlumno);

                alumnos.add(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return alumnos;
    }

    public ArrayList<Curso> obtenerCursos() {
        String sql = "SELECT * FROM cursos";
        ArrayList<Curso> todosLosCursos = new ArrayList();
        try {
            conn = DriverManager.getConnection(this.urlBD);
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id_curso");
                String nombre = rs.getString("nombre_curso");
                String turno = rs.getString("turno");
                String archivoFoto = rs.getString("archivo_foto_grupal");
                Date fechaFoto = rs.getDate("fecha_foto_grupal");

                todosLosCursos.add(new Curso(id, nombre, turno, archivoFoto, fechaFoto));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return todosLosCursos;
    }

    public Curso obtenerCursoBD(Curso c) {
        Curso cursoObtenido = null;
        String sql = "SELECT * FROM cursos WHERE nombre_curso = ?";
        try {
            conn = DriverManager.getConnection(this.urlBD);
            ps = conn.prepareStatement(sql);
            ps.setString(1, c.getNombreCurso());
            ResultSet rs = ps.executeQuery();
            //loop through the result set
            while (rs.next()) {
                int id = rs.getInt("id_curso");
                String nombre = rs.getString("nombre_curso");
                String turno = rs.getString("turno");
                String archivoFoto = rs.getString("archivo_foto_grupal");
                Date fechaFoto = rs.getDate("fecha_foto_grupal");

                cursoObtenido = new Curso(id, nombre, turno, archivoFoto, fechaFoto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return cursoObtenido;
    }

    public Curso obtenerCursoBDxId(int idCurso) {
        Curso cursoObtenido = null;
        String sql = "SELECT * FROM cursos WHERE id_curso = ?";
        try {
            conn = DriverManager.getConnection(this.urlBD);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCurso);
            ResultSet rs = ps.executeQuery();
            //loop through the result set
            while (rs.next()) {
                int id = rs.getInt("id_curso");
                String nombre = rs.getString("nombre_curso");
                String turno = rs.getString("turno");
                String archivoFoto = rs.getString("archivo_foto_grupal");
                Date fechaFoto = rs.getDate("fecha_foto_grupal");

                cursoObtenido = new Curso(id, nombre, turno, archivoFoto, fechaFoto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return cursoObtenido;
    }

    public void mostrarEnConsolaTodosLosCursos() { //TEST de prueba, finalmente borrar
        String sql = "SELECT * FROM cursos";
        try {
            conn = DriverManager.getConnection(this.urlBD);
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            //loop through the result set
            System.out.println("\n*** Comienzo de volcado de datos ***");
            while (rs.next()) {
                System.out.println(rs.getInt("id_curso") + "\t" + rs.getString("nombre_curso") + "\tFoto: (" + rs.getString("archivo_foto_grupal") + " / " + rs.getString("fecha_foto_grupal") + ")");
            }
            System.out.println("*** Fin de volcado de datos ***\n");

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void mostrarEnConsolaTodosLosAlumnos() { //de prueba, finalmente borrar
        String sql = "SELECT * FROM alumnos";
        try {
            conn = DriverManager.getConnection(this.urlBD);
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            //loop through the result set
            System.out.println("\n*** Comienzo de volcado de datos ***");
            while (rs.next()) {
                System.out.println(rs.getInt("id_alumno") + "\t" + rs.getString("nombre_alumno") + "\tid_curso: " + rs.getInt("id_curso_alumno"));
            }
            System.out.println("*** Fin de volcado de datos ***\n");

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void connect()/*Metodo comentado, previo a eliminar*/ {
        /*conn = null;
        try {
            //create a connection to the database
            conn = DriverManager.getConnection(urlBD);
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("La conexión con " + meta.getDriverName() + " se ha establecido. Si no existía la base de datos \"" + urlBD + "\", se ha creado.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                //Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
        }*/
    }

    public void createNewTable(String tableName)/*Metodo comentado, previo a eliminar*/ {
        // SQL statement for creating a new table
        /*String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                + "	idAlumno integer PRIMARY KEY,\n"
                + "	nombreAlumno text NOT NULL,\n"
                + "	curso text \n"
                + ");";

        try {
            conn = DriverManager.getConnection(urlBD);
            Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                //Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
        }*/
    }
}
