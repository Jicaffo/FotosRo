package gui.pc;

// En el alumno deberia figurar una tilde de doble turno (tambien en institucion si es posible
//que haya doble turno) y a partir de eso que oculte lo que no se use.
import ar.com.jicaffo.clasesbd.*;
import ar.com.jicaffo.fotosro.BD;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Juan
 */
public class FramePrincipal extends javax.swing.JFrame {

    // private DefaultComboBoxModel<Institucion> modeloInstituciones; // TO DO
    private DefaultComboBoxModel<Curso> modeloCursos = new DefaultComboBoxModel();
    private DefaultTableModel modeloTabla = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    ListSelectionModel listSelectionModel;
    private DefaultComboBoxModel<String> modeloComboApariencia;

    private BD bd = new BD();
    private static int añoActual = 2018;
    private Curso cursoSeleccionado;
    private Alumno alumnoSeleccionado;
    private boolean datosAlumnoSeleccionadoModificados = false;

    public Preferences prefs;
    private static String rutaBD = "P:/Proyectos Personales pCloud/FotosRo/BD";
    private static String ultimaBD = "Prueba1";
    private static String rutaFotos = "C:\\Users\\Juan\\Desktop\\HumoPlantas"/*"P:/Proyectos Personales pCloud/FotosRo/PruebasFotos"*/;
    private static String preNroFoto = "_DSC";
    private static String postNroFoto = ".jpg";
    private static String rutaCopiasImprenta = "P:/Proyectos Personales pCloud/FotosRo/PruebasFotos";

    public FramePrincipal() {
        initPrefs();
        initLookAndFeel(prefs.get("LAF_PREFERIDO", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
        cargarModeloComboApariencia();
        initComponents();
        postInitCode();
        cargarModeloCursos();
        cargarModeloInstituciones();
        cargarModeloTabla();
        this.setVisible(true);
    }

    private void initPrefs() {
        prefs = Preferences.userNodeForPackage(ar.com.jicaffo.fotosro.FotosRo.class);
    }

    private void initLookAndFeel(String lafName) {
        try {
            UIManager.setLookAndFeel(lafName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    private void cargarModeloComboApariencia() {
        modeloComboApariencia = new DefaultComboBoxModel<String>();
        modeloComboApariencia.addElement("javax.swing.plaf.metal.MetalLookAndFeel");
        modeloComboApariencia.addElement("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.aero.AeroLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.fast.FastLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.luna.LunaLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.mint.MintLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.noire.NoireLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.smart.SmartLookAndFeel");
        modeloComboApariencia.addElement("com.jtattoo.plaf.texture.TextureLookAndFeel");
    }

    private void postInitCode() {
        ArrayList<javax.swing.text.JTextComponent> components = getJTextComponents();

        //TO DO: A encontrar la forma de insertar un listener en todos los componentes modificables para que muestre cuando se editó alguno.
        //c.setBackground(new Color(255,224,178));
    }

    private void cargarModeloCursos() {
        ArrayList<Curso> cursos = bd.obtenerCursos();
        if (cursos.isEmpty()) {
            modeloCursos.addElement(new Curso("Todos los cursos*", null));
            System.out.println("La base de datos no contiene cursos cargados.");
        } else {
            modeloCursos.addElement(new Curso("Todos los cursos*", null));
            for (Curso c : cursos) {
                modeloCursos.addElement(c);
            }
        }
    }

    private void cargarModeloInstituciones() {
        //TO DO
    }

    private void cargarModeloTabla() {
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Ind.");
        modeloTabla.addColumn("GrpM.");
        modeloTabla.addColumn("SñoM.");
        //modeloTabla.setColumnCount(3); //A REVISAR: No se por que lo había puesto, creo que no haría falta.

        //Cargar ListSelectionListener
        listSelectionModel = tablaAlumnos.getSelectionModel();
        System.out.println(tablaAlumnos.getSelectionModel()); // Muestra "javax.swing.DefaultListSelectionModel 1018547642 ={}"
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //TO DO Parece no tener efecto, a buscar resolver una vez funcione
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //El if hace que el cambio de valor se tome solo cuando se suelta el click
                if (e.getValueIsAdjusting()) {
                    return;
                }
                if (!datosAlumnoSeleccionadoModificados) {
                    int selectedRowIndex = listSelectionModel.getMinSelectionIndex();
                    if (selectedRowIndex != -1) {
                        setAlumnoSeleccionado((Alumno) modeloTabla.getValueAt(selectedRowIndex, 0));
                        mostrarDatosAlumnoSeleccionado();
                    }
                    tablaAlumnos.requestFocus();
                } else {
                    int confirmacion = JOptionPane.showConfirmDialog(null, "Los datos del alumno seleccionado se han modificado. \nDesea guardar estos datos antes de continuar?", "Datos Modificados", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (confirmacion == JOptionPane.YES_OPTION) {
                        System.out.println("Confirmaste el cambio de seleccion GUARDANDO datos.");
                    } else {
                        System.out.println("Confirmaste el cambio de seleccion SIN GUARDAR datos.");
                    }
                }

            }
        });
    }

    private void limpiarTabla() {
        int numFilas = modeloTabla.getRowCount();
        if (numFilas > 0) {
            for (int i = numFilas - 1; i >= 0; i--) {
                modeloTabla.removeRow(i);
            }
            System.out.println("modeloTabla se ha limpiado corectamente");
        }
        lblRegistrosEncontrados.setText("0 alumnos");
    }

    private void cargarTabla() {
        ArrayList<Alumno> listadoAlumnos = bd.obtenerAlumnos(cursoSeleccionado);
        int cantidadAlumnos = listadoAlumnos.size();
        modeloTabla.setNumRows(cantidadAlumnos);

        for (int i = 0; i < cantidadAlumnos; i++) {
            Alumno alumnoActual = listadoAlumnos.get(i);

            //int pedidoIndividual = listadoAlumnos.get(i).getPedidoIndividual();
            modeloTabla.setValueAt(alumnoActual, i, 0);
            modeloTabla.setValueAt(listadoAlumnos.get(i).getPedidoIndividual(), i, 1);
            modeloTabla.setValueAt(listadoAlumnos.get(i).getPedidoGrupalMña(), i, 2);
            modeloTabla.setValueAt(listadoAlumnos.get(i).getPedidoSeñoMña(), i, 3);
        }
        lblRegistrosEncontrados.setText(cantidadAlumnos + " alumnos");
    }

    private void mostrarDatosAlumnoSeleccionado() {
        System.out.println(alumnoSeleccionado);

        fieldNombre.setText(alumnoSeleccionado.getNombreAlumno());
        fieldIdAlumno.setText(String.valueOf(alumnoSeleccionado.getIdAlumno()));
        fieldTurno.setText("[Pendiente]");
        fieldSeño.setText("[Pendiente]");

        //Foto
        String rutaCompletaFoto = rutaFotos + "\\" + preNroFoto + alumnoSeleccionado.getArchivoFotoIndividual() + postNroFoto;
        System.out.println("La ruta completa de la foto es: " + rutaCompletaFoto);
        File archivoFoto = new File(rutaCompletaFoto);
        if (archivoFoto.exists()) {
            ImageIcon fotoRedim = redimensionarIconoProporcional(new ImageIcon(rutaCompletaFoto), lblFotoAlumno);
            lblFotoAlumno.setText("");
            lblFotoAlumno.setIcon(fotoRedim);
        }

        fieldPedidoIndividual.setText(String.valueOf(alumnoSeleccionado.getPedidoIndividual()));
        fieldPedidoGrupalM.setText(String.valueOf(alumnoSeleccionado.getPedidoGrupalMña()));
        fieldPedidoGrupalT.setText(String.valueOf(alumnoSeleccionado.getPedidoGrupalTde()));
        fieldPedidoSeñoM.setText(String.valueOf(alumnoSeleccionado.getPedidoSeñoMña()));
        fieldPedidoSeñoT.setText(String.valueOf(alumnoSeleccionado.getPedidoSeñoTde()));
        fieldPedidoCarnet.setText(String.valueOf(alumnoSeleccionado.getPedidoCarnet()));
        fieldPedidoParDeLlaveros.setText(String.valueOf(alumnoSeleccionado.getPedidoParDeLLaveros()));

        Curso c = bd.obtenerCursoBDxId(alumnoSeleccionado.getIdCursoAlumno());
        fieldFotoIndividual.setText(alumnoSeleccionado.getArchivoFotoIndividual());
        fieldFotoSeñoM.setText(alumnoSeleccionado.getArchivoFotoSeñoMña());
        fieldFotoSeñoT.setText(alumnoSeleccionado.getArchivoFotoSeñoTde());
        fieldFotoGrupalM.setText(c.getArchivoFotoGrupal()); //TO DO: Por el momento carga los dos iguales
        fieldFotoGrupalT.setText(c.getArchivoFotoGrupal()); //

        //Fechas fotos TO DO
        fieldFechaIndividual.setText(String.valueOf(alumnoSeleccionado.getFechaIndividual()));
        fieldFechaSeñoM.setText(String.valueOf(alumnoSeleccionado.getFechaSeñoMña()));
        fieldFechaSeñoT.setText(String.valueOf(alumnoSeleccionado.getFechaSeñoTde()));
        fieldFechaGrupalM.setText(String.valueOf(c.getFechaFotoGrupal())); // TO DO: Por el momento carga los dos iguales
        fieldFechaGrupalT.setText(String.valueOf(c.getFechaFotoGrupal())); //
        if (alumnoSeleccionado.faltoGrupalMña()) {
            chkFaltoGrupalMña.setSelected(true);
            fieldFechaFaltoGrupalMña.setEnabled(true);
            fieldFechaFaltoGrupalMña.setText("-");
        } else {
            chkFaltoGrupalMña.setSelected(false);
            fieldFechaFaltoGrupalMña.setEnabled(false);
            fieldFechaFaltoGrupalMña.setText(String.valueOf(alumnoSeleccionado.getFechaFotoFaltoGrupalMña()));
        }
        if (alumnoSeleccionado.faltoGrupalTde()) {
            chkFaltoGrupalTde.setSelected(true);
            fieldFechaFaltoGrupalTde.setEnabled(true);
            fieldFechaFaltoGrupalTde.setText("-");
        } else {
            chkFaltoGrupalTde.setSelected(false);
            fieldFechaFaltoGrupalTde.setEnabled(false);
            fieldFechaFaltoGrupalTde.setText(String.valueOf(alumnoSeleccionado.getFechaFotoFaltoGrupalTde()));
        }

        //Pagos: TO DO: Recuperar Correctamente los datos, en función del pedido seleccionado.
        fieldTotal.setText(String.valueOf(alumnoSeleccionado.getTotal()));
        fieldPago.setText(String.valueOf(alumnoSeleccionado.getMontoAbonado()));
        fieldFechaPago.setText(String.valueOf(alumnoSeleccionado.getFechaPago()));
        fieldRestante.setText(String.valueOf(alumnoSeleccionado.getRestaAbonar()));

        textObs.setText(alumnoSeleccionado.getObservacionesAlumno());

        replaceNullValues();
    }

    private void replaceNullValues() {
        ArrayList<JTextComponent> textComponents = getJTextComponents();

        for (JTextComponent tc : textComponents) {
            if (tc.getText().equals("null") || tc.getText().equals("")) {
                tc.setText("-");
            }
        }
    }

    private ArrayList<JTextComponent> getJTextComponents() {
        ArrayList<JTextComponent> components = new ArrayList();
        components.add(fieldPedidoIndividual);
        components.add(fieldPedidoGrupalM);
        components.add(fieldPedidoGrupalT);
        components.add(fieldPedidoSeñoM);
        components.add(fieldPedidoSeñoT);
        components.add(fieldPedidoCarnet);
        components.add(fieldPedidoParDeLlaveros);
        components.add(fieldFechaIndividual);
        components.add(fieldFechaSeñoM);
        components.add(fieldFechaSeñoT);
        components.add(fieldFechaGrupalM);
        components.add(fieldFechaGrupalT);
        components.add(fieldFechaFaltoGrupalMña);
        components.add(fieldFechaFaltoGrupalTde);
        components.add(fieldFotoIndividual);
        components.add(fieldFotoSeñoM);
        components.add(fieldFotoSeñoT);
        components.add(fieldFotoGrupalM);
        components.add(fieldFotoGrupalT);
        components.add(textObs);
        components.add(fieldPago);
        components.add(fieldFechaPago);
        return components;
    }

    public ImageIcon redimensionarIconoProporcional(ImageIcon img, int ancho, int alto) {
        // Tal vez se podría reemplazar los parámetros ancho y alto por el objeto JLabel
        //directamente y que el método calcule el ancho y el alto del mismo
        double anchoOriginal = img.getIconWidth();
        double altoOriginal = img.getIconHeight();
        Image imagen;

        if (anchoOriginal > (double) ancho //Si el alto o el ancho originales son mayores que los parámetros ingresados
                || altoOriginal > (double) alto) {
            double anchoRedim;
            double altoRedim;

            if (anchoOriginal > altoOriginal) { // Ancho es más grande
                anchoRedim = (double) ancho;
                altoRedim = anchoRedim / anchoOriginal * altoOriginal;
            } else {
                //Alto es mas grande o son iguales
                altoRedim = (double) alto;
                anchoRedim = altoRedim * anchoOriginal / altoOriginal;
            }
            imagen = img.getImage().getScaledInstance((int) anchoRedim, (int) altoRedim, Image.SCALE_DEFAULT);

        } else { //La imagen es igual o más chica que el contenedor por lo que no hace falta redimensionar.");
            imagen = img.getImage();
        }

        ImageIcon iconoRedimensionado = new ImageIcon(imagen);
        return iconoRedimensionado;
    }

    public ImageIcon redimensionarIconoProporcional(ImageIcon img, java.awt.Component component) {
        return redimensionarIconoProporcional(img, component.getWidth(), component.getHeight());
    }

//<editor-fold defaultstate="collapsed" desc="Setters y Getters">
    public static String getRutaBD() {
        return rutaBD;
    }

    public static String getUltimaBD() {
        return ultimaBD;
    }

    public static int getAñoActual() {
        return añoActual;
    }

    public Curso getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public void setCursoSeleccionado(Curso cursoSeleccionado) {
        try {
            this.cursoSeleccionado = cursoSeleccionado;
            System.out.println("Se seleccionó el curso: " + cursoSeleccionado);
        } catch (Exception e) {
            System.out.println("No se ha podido seleccionar correctamente un curso");
        }

    }

    public Alumno getAlumnoSeleccionado() {
        return alumnoSeleccionado;
    }

    public void setAlumnoSeleccionado(Alumno alumnoSeleccionado) {
        this.alumnoSeleccionado = alumnoSeleccionado;
    }
//</editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTabs = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        tabAlumnos = new javax.swing.JPanel();
        lblAño = new javax.swing.JLabel();
        cboAño = new javax.swing.JComboBox<>();
        lblInstitucion = new javax.swing.JLabel();
        cboInstitucion = new javax.swing.JComboBox<>();
        btAgregarInstitucion = new javax.swing.JButton();
        lblCurso = new javax.swing.JLabel();
        cboCurso = new javax.swing.JComboBox<>();
        btAgregarCurso = new javax.swing.JButton();
        panelAlumnos = new javax.swing.JScrollPane();
        tablaAlumnos = new javax.swing.JTable();
        btAgregarAlumno = new javax.swing.JButton();
        btModificarAlumno = new javax.swing.JButton();
        lblRegistrosEncontrados = new javax.swing.JLabel();
        tabDatos = new javax.swing.JTabbedPane();
        panelDatos = new javax.swing.JPanel();
        panelFotoAlumno = new javax.swing.JPanel();
        lblFotoAlumno = new javax.swing.JLabel();
        panelFotos = new javax.swing.JPanel();
        lblPedidos = new javax.swing.JLabel();
        lblFechaSacada = new javax.swing.JLabel();
        lblFotoNro = new javax.swing.JLabel();
        lblIndividual = new javax.swing.JLabel();
        btMenosIndividual = new javax.swing.JButton();
        fieldPedidoIndividual = new javax.swing.JTextField();
        btMasIndividual = new javax.swing.JButton();
        fieldFechaIndividual = new javax.swing.JTextField();
        fieldFotoIndividual = new javax.swing.JTextField();
        lblGrupalM = new javax.swing.JLabel();
        btMenosGrupalM = new javax.swing.JButton();
        fieldPedidoGrupalM = new javax.swing.JTextField();
        btMasGrupalM = new javax.swing.JButton();
        fieldFechaGrupalM = new javax.swing.JTextField();
        fieldFotoGrupalM = new javax.swing.JTextField();
        lblGrupalT = new javax.swing.JLabel();
        btMenosGrupalT = new javax.swing.JButton();
        fieldPedidoGrupalT = new javax.swing.JTextField();
        btMasGrupalT = new javax.swing.JButton();
        fieldFechaGrupalT = new javax.swing.JTextField();
        fieldFotoGrupalT = new javax.swing.JTextField();
        lblSeñoM = new javax.swing.JLabel();
        btMenosSeñoM = new javax.swing.JButton();
        fieldPedidoSeñoM = new javax.swing.JTextField();
        btMasSeñoM = new javax.swing.JButton();
        fieldFechaSeñoM = new javax.swing.JTextField();
        fieldFotoSeñoM = new javax.swing.JTextField();
        lblSeñoT = new javax.swing.JLabel();
        btMenosSeñoT = new javax.swing.JButton();
        fieldPedidoSeñoT = new javax.swing.JTextField();
        btMasSeñoT = new javax.swing.JButton();
        fieldFechaSeñoT = new javax.swing.JTextField();
        fieldFotoSeñoT = new javax.swing.JTextField();
        lblCarnet = new javax.swing.JLabel();
        btMenosCarnet = new javax.swing.JButton();
        fieldPedidoCarnet = new javax.swing.JTextField();
        btMasCarnet = new javax.swing.JButton();
        btMasCombo1 = new javax.swing.JButton();
        lblParDeLlaveros = new javax.swing.JLabel();
        btMenosParDeLlaveros = new javax.swing.JButton();
        fieldPedidoParDeLlaveros = new javax.swing.JTextField();
        btMasParDeLlaveros = new javax.swing.JButton();
        btMasCombo2 = new javax.swing.JButton();
        lblFaltoGrupal = new javax.swing.JLabel();
        lblFotoAEditar = new javax.swing.JLabel();
        chkFaltoGrupalMña = new javax.swing.JCheckBox();
        fieldFechaFaltoGrupalMña = new javax.swing.JTextField();
        chkFaltoGrupalTde = new javax.swing.JCheckBox();
        fieldFechaFaltoGrupalTde = new javax.swing.JTextField();
        panelDatosAlumno = new javax.swing.JPanel();
        lblIdAlumno = new javax.swing.JLabel();
        fieldIdAlumno = new javax.swing.JTextField();
        lblTurno = new javax.swing.JLabel();
        fieldTurno = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        lblMaestra = new javax.swing.JLabel();
        fieldSeño = new javax.swing.JTextField();
        panelObservaciones = new javax.swing.JPanel();
        scrollObs = new javax.swing.JScrollPane();
        textObs = new javax.swing.JTextArea();
        panelPago = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        fieldTotal = new javax.swing.JTextField();
        lblPago = new javax.swing.JLabel();
        fieldPago = new javax.swing.JTextField();
        lblFechaPago = new javax.swing.JLabel();
        fieldFechaPago = new javax.swing.JTextField();
        lblRestante = new javax.swing.JLabel();
        fieldRestante = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        panelHistorial = new javax.swing.JPanel();
        tabConfig = new javax.swing.JPanel();
        lblRutaBD = new javax.swing.JLabel();
        fieldRutaBD = new javax.swing.JTextField();
        btModificarRutaBD = new javax.swing.JButton();
        lblUltimaInstitucion = new javax.swing.JLabel();
        lblDetalleUltimaInstitucion = new javax.swing.JLabel();
        lblRutaFotos = new javax.swing.JLabel();
        fieldRutaFotos = new javax.swing.JTextField();
        btModificarRutaFotos = new javax.swing.JButton();
        lblNombreArchivos = new javax.swing.JLabel();
        fieldPreNroFoto = new javax.swing.JTextField();
        lblNroFoto = new javax.swing.JLabel();
        fieldPostNroFoto = new javax.swing.JTextField();
        btModificarNombreArchivos = new javax.swing.JButton();
        lblRutaCopiaImprenta = new javax.swing.JLabel();
        fieldRutaCopiaImprenta = new javax.swing.JTextField();
        btModificarRutaCopiaImprenta = new javax.swing.JButton();
        panelAccionesRapidas = new javax.swing.JPanel();
        btCrearCopiasImprenta = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        comboApariencia = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FotosRo - (Version BETA)");
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        panelTabs.setAlignmentX(0.0F);
        panelTabs.setAlignmentY(0.0F);

        lblAño.setText("Año:");

        cboAño.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2018" }));

        lblInstitucion.setText("Institución:");

        cboInstitucion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Maternal" }));

        btAgregarInstitucion.setText("+");

        lblCurso.setText("Curso:");

        cboCurso.setModel(modeloCursos);
        cboCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCursoActionPerformed(evt);
            }
        });

        btAgregarCurso.setText("+");
        btAgregarCurso.setAlignmentX(0.5F);
        btAgregarCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAgregarCursoActionPerformed(evt);
            }
        });

        tablaAlumnos.setModel(modeloTabla);
        tablaAlumnos.setGridColor(new java.awt.Color(204, 204, 204));
        panelAlumnos.setViewportView(tablaAlumnos);

        btAgregarAlumno.setText("Agregar");

        btModificarAlumno.setText("Modificar");

        lblRegistrosEncontrados.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRegistrosEncontrados.setText(" XXX alumnos");

        panelFotoAlumno.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblFotoAlumno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFotoAlumno.setText("[Foto: Pendiente]");

        javax.swing.GroupLayout panelFotoAlumnoLayout = new javax.swing.GroupLayout(panelFotoAlumno);
        panelFotoAlumno.setLayout(panelFotoAlumnoLayout);
        panelFotoAlumnoLayout.setHorizontalGroup(
            panelFotoAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFotoAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
        );
        panelFotoAlumnoLayout.setVerticalGroup(
            panelFotoAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblFotoAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
        );

        panelFotos.setBorder(javax.swing.BorderFactory.createTitledBorder("Fotos"));

        lblPedidos.setText("Pedidos");

        lblFechaSacada.setText("Fecha Sacada");

        lblFotoNro.setText("Foto Nº");

        lblIndividual.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIndividual.setText("Individual:");

        btMenosIndividual.setText("-");
        btMenosIndividual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMenosIndividualActionPerformed(evt);
            }
        });

        fieldPedidoIndividual.setEditable(false);
        fieldPedidoIndividual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoIndividual.setText("0");
        fieldPedidoIndividual.setFocusable(false);

        btMasIndividual.setText("+");
        btMasIndividual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMasIndividualActionPerformed(evt);
            }
        });

        fieldFechaIndividual.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoIndividual.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblGrupalM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGrupalM.setText("Grupal (Mña):");

        btMenosGrupalM.setText("-");

        fieldPedidoGrupalM.setEditable(false);
        fieldPedidoGrupalM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoGrupalM.setText("0");

        btMasGrupalM.setText("+");

        fieldFechaGrupalM.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoGrupalM.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblGrupalT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGrupalT.setText("Grupal (Tde):");

        btMenosGrupalT.setText("-");

        fieldPedidoGrupalT.setEditable(false);
        fieldPedidoGrupalT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoGrupalT.setText("0");

        btMasGrupalT.setText("+");

        fieldFechaGrupalT.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoGrupalT.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblSeñoM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSeñoM.setText("Seño (Mña):");

        btMenosSeñoM.setText("-");

        fieldPedidoSeñoM.setEditable(false);
        fieldPedidoSeñoM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoSeñoM.setText("0");

        btMasSeñoM.setText("+");

        fieldFechaSeñoM.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoSeñoM.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblSeñoT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSeñoT.setText("Seño (Tde):");

        btMenosSeñoT.setText("-");

        fieldPedidoSeñoT.setEditable(false);
        fieldPedidoSeñoT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoSeñoT.setText("0");

        btMasSeñoT.setText("+");

        fieldFechaSeñoT.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoSeñoT.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblCarnet.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCarnet.setText("Carnet:");

        btMenosCarnet.setText("-");

        fieldPedidoCarnet.setEditable(false);
        fieldPedidoCarnet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoCarnet.setText("0");

        btMasCarnet.setText("+");

        btMasCombo1.setText("C1+ [Pendiente]");
        btMasCombo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btMasCombo1ActionPerformed(evt);
            }
        });

        lblParDeLlaveros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblParDeLlaveros.setText("Llaveros:");

        btMenosParDeLlaveros.setText("-");

        fieldPedidoParDeLlaveros.setEditable(false);
        fieldPedidoParDeLlaveros.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoParDeLlaveros.setText("0");

        btMasParDeLlaveros.setText("+");

        btMasCombo2.setText("C2+ [Pendiente]");
        btMasCombo2.setEnabled(false);

        lblFaltoGrupal.setText("Falto a Grupal");

        lblFotoAEditar.setText("(Foto a editar)");

        chkFaltoGrupalMña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFaltoGrupalMñaActionPerformed(evt);
            }
        });

        fieldFechaFaltoGrupalMña.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        fieldFechaFaltoGrupalMña.setEnabled(false);

        chkFaltoGrupalTde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFaltoGrupalTdeActionPerformed(evt);
            }
        });

        fieldFechaFaltoGrupalTde.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        fieldFechaFaltoGrupalTde.setEnabled(false);

        javax.swing.GroupLayout panelFotosLayout = new javax.swing.GroupLayout(panelFotos);
        panelFotos.setLayout(panelFotosLayout);
        panelFotosLayout.setHorizontalGroup(
            panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelFotosLayout.createSequentialGroup()
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addComponent(lblSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btMenosSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldPedidoSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btMasSeñoT))
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addComponent(lblCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btMenosCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldPedidoCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btMasCarnet))
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addComponent(lblParDeLlaveros, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btMenosParDeLlaveros, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldPedidoParDeLlaveros, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btMasParDeLlaveros)))
                        .addGap(29, 29, 29)
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addComponent(fieldFechaSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldFotoSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblFotoNro)))
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btMasCombo1, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                    .addComponent(btMasCombo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panelFotosLayout.createSequentialGroup()
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblGrupalM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIndividual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblGrupalT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblSeñoM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addComponent(btMenosSeñoM, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldPedidoSeñoM, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btMasSeñoM)
                                .addGap(29, 29, 29)
                                .addComponent(fieldFechaSeñoM))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFotosLayout.createSequentialGroup()
                                .addComponent(btMenosGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldPedidoGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btMasGrupalT)
                                .addGap(29, 29, 29)
                                .addComponent(fieldFechaGrupalT))
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addComponent(btMenosGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldPedidoGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btMasGrupalM)
                                .addGap(29, 29, 29)
                                .addComponent(fieldFechaGrupalM))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFotosLayout.createSequentialGroup()
                                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panelFotosLayout.createSequentialGroup()
                                        .addComponent(btMenosIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fieldPedidoIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6))
                                    .addGroup(panelFotosLayout.createSequentialGroup()
                                        .addComponent(lblPedidos)
                                        .addGap(1, 1, 1)))
                                .addComponent(btMasIndividual)
                                .addGap(29, 29, 29)
                                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelFotosLayout.createSequentialGroup()
                                        .addComponent(lblFechaSacada)
                                        .addGap(3, 3, 3))
                                    .addComponent(fieldFechaIndividual))))
                        .addGap(18, 18, 18)
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldFotoSeñoM, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldFotoIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldFotoGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldFotoGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelFotosLayout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblFotoAEditar, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblFaltoGrupal, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(panelFotosLayout.createSequentialGroup()
                                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(chkFaltoGrupalTde)
                                            .addComponent(chkFaltoGrupalMña))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fieldFechaFaltoGrupalMña, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(fieldFechaFaltoGrupalTde, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        panelFotosLayout.setVerticalGroup(
            panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotosLayout.createSequentialGroup()
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFotosLayout.createSequentialGroup()
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPedidos)
                            .addComponent(lblFechaSacada))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIndividual)
                            .addComponent(btMenosIndividual)
                            .addComponent(btMasIndividual)
                            .addComponent(fieldPedidoIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFechaIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblFotoNro)
                    .addGroup(panelFotosLayout.createSequentialGroup()
                        .addComponent(lblFaltoGrupal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFotoAEditar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelFotosLayout.createSequentialGroup()
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGrupalM)
                            .addComponent(btMenosGrupalM)
                            .addComponent(btMasGrupalM)
                            .addComponent(fieldPedidoGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFechaGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGrupalT)
                            .addComponent(btMenosGrupalT)
                            .addComponent(btMasGrupalT)
                            .addComponent(fieldPedidoGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFechaGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFotosLayout.createSequentialGroup()
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldFechaFaltoGrupalMña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkFaltoGrupalMña))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldFechaFaltoGrupalTde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chkFaltoGrupalTde))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSeñoM)
                    .addComponent(btMenosSeñoM)
                    .addComponent(btMasSeñoM)
                    .addComponent(fieldPedidoSeñoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldFechaSeñoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldFotoSeñoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btMasSeñoT, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btMenosSeñoT)
                        .addComponent(fieldFechaSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fieldPedidoSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSeñoT)
                        .addComponent(fieldFotoSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCarnet)
                    .addComponent(btMenosCarnet)
                    .addComponent(btMasCarnet)
                    .addComponent(fieldPedidoCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btMasCombo1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblParDeLlaveros)
                    .addComponent(btMenosParDeLlaveros)
                    .addComponent(btMasParDeLlaveros)
                    .addComponent(fieldPedidoParDeLlaveros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btMasCombo2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelDatosAlumno.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        lblIdAlumno.setText("id Alumno:");

        lblTurno.setText("Turno?:");

        lblNombre.setText("Nombre Alumno:");

        lblMaestra.setText("Nombre Maestra");

        javax.swing.GroupLayout panelDatosAlumnoLayout = new javax.swing.GroupLayout(panelDatosAlumno);
        panelDatosAlumno.setLayout(panelDatosAlumnoLayout);
        panelDatosAlumnoLayout.setHorizontalGroup(
            panelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosAlumnoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNombre)
                    .addComponent(lblMaestra)
                    .addGroup(panelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelDatosAlumnoLayout.createSequentialGroup()
                            .addGroup(panelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblIdAlumno)
                                .addComponent(fieldIdAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(panelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fieldTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(panelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fieldSeño, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(fieldNombre, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelDatosAlumnoLayout.setVerticalGroup(
            panelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosAlumnoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelDatosAlumnoLayout.createSequentialGroup()
                        .addComponent(lblIdAlumno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldIdAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelDatosAlumnoLayout.createSequentialGroup()
                        .addComponent(lblTurno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addComponent(lblNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMaestra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldSeño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelObservaciones.setBorder(javax.swing.BorderFactory.createTitledBorder("Observaciones"));

        textObs.setColumns(20);
        textObs.setRows(5);
        textObs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textObsKeyTyped(evt);
            }
        });
        scrollObs.setViewportView(textObs);

        javax.swing.GroupLayout panelObservacionesLayout = new javax.swing.GroupLayout(panelObservaciones);
        panelObservaciones.setLayout(panelObservacionesLayout);
        panelObservacionesLayout.setHorizontalGroup(
            panelObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelObservacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollObs)
                .addContainerGap())
        );
        panelObservacionesLayout.setVerticalGroup(
            panelObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelObservacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollObs, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        panelPago.setBorder(javax.swing.BorderFactory.createTitledBorder("Pago"));

        lblTotal.setText("Total:");

        fieldTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblPago.setText("Pagó:");

        fieldPago.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        lblFechaPago.setText("Fecha Pago:");

        lblRestante.setText("Restante:");

        fieldRestante.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout panelPagoLayout = new javax.swing.GroupLayout(panelPago);
        panelPago.setLayout(panelPagoLayout);
        panelPagoLayout.setHorizontalGroup(
            panelPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPagoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(lblPago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldPago, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lblFechaPago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldFechaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(lblRestante)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldRestante, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(218, 218, 218))
        );
        panelPagoLayout.setVerticalGroup(
            panelPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPagoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelPagoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotal)
                    .addComponent(fieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldRestante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRestante)
                    .addComponent(fieldPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPago)
                    .addComponent(lblFechaPago)
                    .addComponent(fieldFechaPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDatosLayout = new javax.swing.GroupLayout(panelDatos);
        panelDatos.setLayout(panelDatosLayout);
        panelDatosLayout.setHorizontalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelFotoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelDatosAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelFotos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelObservaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addComponent(panelPago, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelDatosLayout.setVerticalGroup(
            panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addComponent(panelFotos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelObservaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelDatosLayout.createSequentialGroup()
                        .addComponent(panelFotoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panelDatosAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabDatos.addTab("Datos", panelDatos);

        javax.swing.GroupLayout panelHistorialLayout = new javax.swing.GroupLayout(panelHistorial);
        panelHistorial.setLayout(panelHistorialLayout);
        panelHistorialLayout.setHorizontalGroup(
            panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 714, Short.MAX_VALUE)
        );
        panelHistorialLayout.setVerticalGroup(
            panelHistorialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 481, Short.MAX_VALUE)
        );

        tabDatos.addTab("Historial [Pendiente]", panelHistorial);

        javax.swing.GroupLayout tabAlumnosLayout = new javax.swing.GroupLayout(tabAlumnos);
        tabAlumnos.setLayout(tabAlumnosLayout);
        tabAlumnosLayout.setHorizontalGroup(
            tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabAlumnosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabAlumnosLayout.createSequentialGroup()
                        .addComponent(lblAño)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboAño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(tabAlumnosLayout.createSequentialGroup()
                        .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(tabAlumnosLayout.createSequentialGroup()
                                .addComponent(btAgregarAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btModificarAlumno)
                                .addGap(18, 18, 18)
                                .addComponent(lblRegistrosEncontrados)))
                        .addGap(18, 18, 18)
                        .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabAlumnosLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblInstitucion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btAgregarInstitucion)
                                .addGap(66, 66, 66)
                                .addComponent(lblCurso)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btAgregarCurso))
                            .addGroup(tabAlumnosLayout.createSequentialGroup()
                                .addComponent(tabDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        tabAlumnosLayout.setVerticalGroup(
            tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabAlumnosLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInstitucion)
                    .addComponent(lblCurso)
                    .addComponent(lblAño)
                    .addComponent(cboAño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btAgregarCurso)
                    .addComponent(btAgregarInstitucion))
                .addGap(18, 18, 18)
                .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(tabDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tabAlumnosLayout.createSequentialGroup()
                        .addComponent(panelAlumnos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btAgregarAlumno)
                            .addComponent(btModificarAlumno)
                            .addComponent(lblRegistrosEncontrados))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Alumnos", tabAlumnos);

        lblRutaBD.setText("Ruta de acceso a las bases de datos:");

        fieldRutaBD.setText(rutaBD);
        fieldRutaBD.setEnabled(false);

        btModificarRutaBD.setText("Modificar");

        lblUltimaInstitucion.setText("Última institucion utilizada:");

        lblDetalleUltimaInstitucion.setText(ultimaBD);

        lblRutaFotos.setText("Ruta de acceso a las fotos:");

        fieldRutaFotos.setText(rutaFotos);
        fieldRutaFotos.setEnabled(false);

        btModificarRutaFotos.setText("Modificar");

        lblNombreArchivos.setText("Nombres de archivos:");

        fieldPreNroFoto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fieldPreNroFoto.setText(preNroFoto);
        fieldPreNroFoto.setEnabled(false);

        lblNroFoto.setText("+ Nº de Foto +");

        fieldPostNroFoto.setText(postNroFoto);
        fieldPostNroFoto.setEnabled(false);

        btModificarNombreArchivos.setLabel("Modificar");

        lblRutaCopiaImprenta.setText("Ruta de copias para imprenta:");

        fieldRutaCopiaImprenta.setText("P:\\Proyectos Personales pCloud\\FotosRo\\PruebasFotos");
        fieldRutaCopiaImprenta.setToolTipText("");
        fieldRutaCopiaImprenta.setEnabled(false);

        btModificarRutaCopiaImprenta.setText("Modificar");

        panelAccionesRapidas.setBorder(javax.swing.BorderFactory.createTitledBorder("Acciones Rápidas"));

        btCrearCopiasImprenta.setText("Crear copias de las fotos para la imprenta");

        javax.swing.GroupLayout panelAccionesRapidasLayout = new javax.swing.GroupLayout(panelAccionesRapidas);
        panelAccionesRapidas.setLayout(panelAccionesRapidasLayout);
        panelAccionesRapidasLayout.setHorizontalGroup(
            panelAccionesRapidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesRapidasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btCrearCopiasImprenta)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelAccionesRapidasLayout.setVerticalGroup(
            panelAccionesRapidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAccionesRapidasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btCrearCopiasImprenta)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jLabel1.setText("Apariencia:");

        comboApariencia.setModel(modeloComboApariencia);
        comboApariencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboAparienciaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabConfigLayout = new javax.swing.GroupLayout(tabConfig);
        tabConfig.setLayout(tabConfigLayout);
        tabConfigLayout.setHorizontalGroup(
            tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabConfigLayout.createSequentialGroup()
                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabConfigLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(panelAccionesRapidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(tabConfigLayout.createSequentialGroup()
                        .addContainerGap(54, Short.MAX_VALUE)
                        .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabConfigLayout.createSequentialGroup()
                                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblRutaBD)
                                    .addComponent(lblUltimaInstitucion)
                                    .addComponent(lblRutaFotos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(tabConfigLayout.createSequentialGroup()
                                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNombreArchivos, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblRutaCopiaImprenta, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(38, 38, 38)))
                        .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabConfigLayout.createSequentialGroup()
                                .addComponent(fieldPreNroFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblNroFoto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldPostNroFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btModificarNombreArchivos))
                            .addGroup(tabConfigLayout.createSequentialGroup()
                                .addComponent(fieldRutaFotos, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btModificarRutaFotos))
                            .addGroup(tabConfigLayout.createSequentialGroup()
                                .addComponent(fieldRutaBD, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btModificarRutaBD))
                            .addComponent(lblDetalleUltimaInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(tabConfigLayout.createSequentialGroup()
                                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(comboApariencia, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(fieldRutaCopiaImprenta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(btModificarRutaCopiaImprenta)))))
                .addContainerGap(225, Short.MAX_VALUE))
        );
        tabConfigLayout.setVerticalGroup(
            tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabConfigLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRutaBD)
                    .addComponent(fieldRutaBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btModificarRutaBD))
                .addGap(27, 27, 27)
                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUltimaInstitucion)
                    .addComponent(lblDetalleUltimaInstitucion))
                .addGap(26, 26, 26)
                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRutaFotos)
                    .addComponent(fieldRutaFotos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btModificarRutaFotos))
                .addGap(18, 18, 18)
                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombreArchivos)
                    .addComponent(fieldPreNroFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNroFoto)
                    .addComponent(fieldPostNroFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btModificarNombreArchivos))
                .addGap(18, 18, 18)
                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRutaCopiaImprenta)
                    .addComponent(fieldRutaCopiaImprenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btModificarRutaCopiaImprenta))
                .addGap(18, 18, 18)
                .addGroup(tabConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboApariencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                .addComponent(panelAccionesRapidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        tabbedPane.addTab("Configuración", tabConfig);

        javax.swing.GroupLayout panelTabsLayout = new javax.swing.GroupLayout(panelTabs);
        panelTabs.setLayout(panelTabsLayout);
        panelTabsLayout.setHorizontalGroup(
            panelTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );
        panelTabsLayout.setVerticalGroup(
            panelTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTabs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTabs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            // TO DO: refrescar frame
            System.out.println("Apretaste F5");
            /*dispose();
            FramePrincipal fp = new FramePrincipal();*/
        }
    }//GEN-LAST:event_formKeyReleased

    private void btAgregarCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAgregarCursoActionPerformed
        DialogCurso nc = new DialogCurso(this, "Nuevo Curso", true);

        //modeloCursos.removeAllElements();
        cargarModeloCursos();
        modeloCursos.setSelectedItem(modeloCursos.getElementAt(modeloCursos.getSize() - 1));
        //setCursoSeleccionado((Curso) modeloCursos.getElementAt(modeloCursos.getSize() - 1));
        //cargarModeloTabla();
        //cargarTabla();
    }//GEN-LAST:event_btAgregarCursoActionPerformed

    private void cboCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCursoActionPerformed
        if (modeloCursos.getSize() != 0) {
            setCursoSeleccionado((Curso) cboCurso.getSelectedItem());
            limpiarTabla();
            cargarTabla();
            tablaAlumnos.changeSelection(0, 0, false, false);
        } else {
            System.out.println("cboCursoActionPerformed(): Size = 0 (no hay elementos en modeloCursos)");
        }
        tablaAlumnos.requestFocus();
    }//GEN-LAST:event_cboCursoActionPerformed

    private void btMasIndividualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMasIndividualActionPerformed
        if (alumnoSeleccionado != null) {
            int nuevoPedido = Integer.parseInt(fieldPedidoIndividual.getText()) + 1;
            fieldPedidoIndividual.setText(String.valueOf(nuevoPedido));
            //fieldPedidoIndividual.setBackground(new Color(255,233,212));
            alumnoSeleccionado.setPedidoIndividual(nuevoPedido);
            //bd.updateAlumno(alumnoSeleccionado); //si hay un botón de guardar general no hace falta.
        }
    }//GEN-LAST:event_btMasIndividualActionPerformed

    private void btMenosIndividualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMenosIndividualActionPerformed
        if (Integer.parseInt(fieldPedidoIndividual.getText()) < 1) {
            JOptionPane.showMessageDialog(this, "La cantidad pedida no puede ser inferior a cero.", "Cantidad mínima alcanzada", JOptionPane.ERROR_MESSAGE);
        } else {
            int nuevoPedido = Integer.parseInt(fieldPedidoIndividual.getText()) - 1;
            fieldPedidoIndividual.setText(String.valueOf(nuevoPedido));
            alumnoSeleccionado.setPedidoIndividual(nuevoPedido);
            //bd.updateAlumno(alumnoSeleccionado); //si hay un botón de guardar general no hace falta.
        }
    }//GEN-LAST:event_btMenosIndividualActionPerformed

    private void comboAparienciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboAparienciaActionPerformed
        String laf = (String) comboApariencia.getSelectedItem();
        prefs.put("LAF_PREFERIDO", laf);
        initLookAndFeel(laf);
        SwingUtilities.updateComponentTreeUI(this);

    }//GEN-LAST:event_comboAparienciaActionPerformed

    private void btMasCombo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMasCombo1ActionPerformed
        // individual / grupal Carnet
        if (alumnoSeleccionado != null) {
            //TODO: Debería funcionar según el detalle cargado de los combos de cada institución
            int nuevoPedido = Integer.parseInt(fieldPedidoIndividual.getText()) + 1;
            fieldPedidoIndividual.setText(String.valueOf(nuevoPedido));
            alumnoSeleccionado.setPedidoIndividual(nuevoPedido);
            
            nuevoPedido = Integer.parseInt(fieldPedidoGrupalM.getText()) + 1;
            fieldPedidoGrupalM.setText(String.valueOf(nuevoPedido));
            alumnoSeleccionado.setPedidoGrupalMña(nuevoPedido);
            
            nuevoPedido = Integer.parseInt(fieldPedidoCarnet.getText()) + 1;
            fieldPedidoCarnet.setText(String.valueOf(nuevoPedido));
            alumnoSeleccionado.setPedidoCarnet(nuevoPedido);
            
            //bd.updateAlumno(alumnoSeleccionado);
        } else {
            JOptionPane.showMessageDialog(this, 
                    "No se ha seleccionado ningún alumno.\nSeleccione un alumno y vuelva a intentarlo", 
                    "Ningún alumno seleccionado", 
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btMasCombo1ActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        //Los cambios se realizan al modificarse los campos en alumnoSeleccionado directamente
        bd.updateAlumno(alumnoSeleccionado);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void chkFaltoGrupalMñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFaltoGrupalMñaActionPerformed
        if (chkFaltoGrupalMña.isSelected()){
            fieldFechaFaltoGrupalMña.setEnabled(true);
            alumnoSeleccionado.setFaltoGrupalMña(true);
        } else {
            fieldFechaFaltoGrupalMña.setEnabled(false);
            alumnoSeleccionado.setFaltoGrupalMña(false);
        }
    }//GEN-LAST:event_chkFaltoGrupalMñaActionPerformed

    private void chkFaltoGrupalTdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFaltoGrupalTdeActionPerformed
        if (chkFaltoGrupalTde.isSelected()){
            fieldFechaFaltoGrupalTde.setEnabled(true);
            alumnoSeleccionado.setFaltoGrupalTde(true);
        } else {
            fieldFechaFaltoGrupalTde.setEnabled(false);
            alumnoSeleccionado.setFaltoGrupalTde(false);
        }
    }//GEN-LAST:event_chkFaltoGrupalTdeActionPerformed

    private void textObsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textObsKeyTyped
        //A REVISAR: Por algún motivo borra el último caracter al llamar getText, revisar con busqueda de ejecricio sistema
        System.out.println("En revisión: el texto obtenido te textObs es: "+textObs.getText());
        alumnoSeleccionado.setObservacionesAlumno(textObs.getText());
    }//GEN-LAST:event_textObsKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAgregarAlumno;
    private javax.swing.JButton btAgregarCurso;
    private javax.swing.JButton btAgregarInstitucion;
    private javax.swing.JButton btCrearCopiasImprenta;
    private javax.swing.JButton btMasCarnet;
    private javax.swing.JButton btMasCombo1;
    private javax.swing.JButton btMasCombo2;
    private javax.swing.JButton btMasGrupalM;
    private javax.swing.JButton btMasGrupalT;
    private javax.swing.JButton btMasIndividual;
    private javax.swing.JButton btMasParDeLlaveros;
    private javax.swing.JButton btMasSeñoM;
    private javax.swing.JButton btMasSeñoT;
    private javax.swing.JButton btMenosCarnet;
    private javax.swing.JButton btMenosGrupalM;
    private javax.swing.JButton btMenosGrupalT;
    private javax.swing.JButton btMenosIndividual;
    private javax.swing.JButton btMenosParDeLlaveros;
    private javax.swing.JButton btMenosSeñoM;
    private javax.swing.JButton btMenosSeñoT;
    private javax.swing.JButton btModificarAlumno;
    private javax.swing.JButton btModificarNombreArchivos;
    private javax.swing.JButton btModificarRutaBD;
    private javax.swing.JButton btModificarRutaCopiaImprenta;
    private javax.swing.JButton btModificarRutaFotos;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<String> cboAño;
    private javax.swing.JComboBox<Curso> cboCurso;
    private javax.swing.JComboBox<String> cboInstitucion;
    private javax.swing.JCheckBox chkFaltoGrupalMña;
    private javax.swing.JCheckBox chkFaltoGrupalTde;
    private javax.swing.JComboBox<String> comboApariencia;
    private javax.swing.JTextField fieldFechaFaltoGrupalMña;
    private javax.swing.JTextField fieldFechaFaltoGrupalTde;
    private javax.swing.JTextField fieldFechaGrupalM;
    private javax.swing.JTextField fieldFechaGrupalT;
    private javax.swing.JTextField fieldFechaIndividual;
    private javax.swing.JTextField fieldFechaPago;
    private javax.swing.JTextField fieldFechaSeñoM;
    private javax.swing.JTextField fieldFechaSeñoT;
    private javax.swing.JTextField fieldFotoGrupalM;
    private javax.swing.JTextField fieldFotoGrupalT;
    private javax.swing.JTextField fieldFotoIndividual;
    private javax.swing.JTextField fieldFotoSeñoM;
    private javax.swing.JTextField fieldFotoSeñoT;
    private javax.swing.JTextField fieldIdAlumno;
    private javax.swing.JTextField fieldNombre;
    private javax.swing.JTextField fieldPago;
    private javax.swing.JTextField fieldPedidoCarnet;
    private javax.swing.JTextField fieldPedidoGrupalM;
    private javax.swing.JTextField fieldPedidoGrupalT;
    private javax.swing.JTextField fieldPedidoIndividual;
    private javax.swing.JTextField fieldPedidoParDeLlaveros;
    private javax.swing.JTextField fieldPedidoSeñoM;
    private javax.swing.JTextField fieldPedidoSeñoT;
    private javax.swing.JTextField fieldPostNroFoto;
    private javax.swing.JTextField fieldPreNroFoto;
    private javax.swing.JTextField fieldRestante;
    private javax.swing.JTextField fieldRutaBD;
    private javax.swing.JTextField fieldRutaCopiaImprenta;
    private javax.swing.JTextField fieldRutaFotos;
    private javax.swing.JTextField fieldSeño;
    private javax.swing.JTextField fieldTotal;
    private javax.swing.JTextField fieldTurno;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAño;
    private javax.swing.JLabel lblCarnet;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblDetalleUltimaInstitucion;
    private javax.swing.JLabel lblFaltoGrupal;
    private javax.swing.JLabel lblFechaPago;
    private javax.swing.JLabel lblFechaSacada;
    private javax.swing.JLabel lblFotoAEditar;
    private javax.swing.JLabel lblFotoAlumno;
    private javax.swing.JLabel lblFotoNro;
    private javax.swing.JLabel lblGrupalM;
    private javax.swing.JLabel lblGrupalT;
    private javax.swing.JLabel lblIdAlumno;
    private javax.swing.JLabel lblIndividual;
    private javax.swing.JLabel lblInstitucion;
    private javax.swing.JLabel lblMaestra;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombreArchivos;
    private javax.swing.JLabel lblNroFoto;
    private javax.swing.JLabel lblPago;
    private javax.swing.JLabel lblParDeLlaveros;
    private javax.swing.JLabel lblPedidos;
    private javax.swing.JLabel lblRegistrosEncontrados;
    private javax.swing.JLabel lblRestante;
    private javax.swing.JLabel lblRutaBD;
    private javax.swing.JLabel lblRutaCopiaImprenta;
    private javax.swing.JLabel lblRutaFotos;
    private javax.swing.JLabel lblSeñoM;
    private javax.swing.JLabel lblSeñoT;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JLabel lblUltimaInstitucion;
    private javax.swing.JPanel panelAccionesRapidas;
    private javax.swing.JScrollPane panelAlumnos;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JPanel panelDatosAlumno;
    private javax.swing.JPanel panelFotoAlumno;
    private javax.swing.JPanel panelFotos;
    private javax.swing.JPanel panelHistorial;
    private javax.swing.JPanel panelObservaciones;
    private javax.swing.JPanel panelPago;
    private javax.swing.JPanel panelTabs;
    private javax.swing.JScrollPane scrollObs;
    private javax.swing.JPanel tabAlumnos;
    private javax.swing.JPanel tabConfig;
    private javax.swing.JTabbedPane tabDatos;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tablaAlumnos;
    private javax.swing.JTextArea textObs;
    // End of variables declaration//GEN-END:variables

}
