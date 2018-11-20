package gui.pc;

// En el alumno deberia figurar una tilde de doble turno (tambien en institucion si es posible
//que haya doble turno) y a partir de eso que oculte lo que no se use.
import ar.com.jicaffo.clasesbd.Alumno;
import ar.com.jicaffo.clasesbd.Curso;
import ar.com.jicaffo.fotosro.BD;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

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
    private BD bd = new BD();
    private static String rutaBD = "P:/Proyectos Personales pCloud/FotosRo/BD";
    private static int añoActual = 2018;
    private static String ultimaBD = "Prueba1";
    private Curso cursoSeleccionado;
    private Alumno alumnoSeleccionado;

    public FramePrincipal() {
        initComponents();
        cargarModelos();
        this.setVisible(true);
    }

    public static String getRutaBD() {
        return rutaBD;
    }

    public static String getUltimaBD() {
        return ultimaBD;
    }

    public static int getAñoActual() {
        return añoActual;
    }

    private void cargarModelos() {
        cargarModeloCursos();
        cargarModeloInstituciones();
        cargarModeloTabla();
    }

    private void cargarModeloCursos() {
        ArrayList<Curso> cursos = bd.obtenerCursos();
        if (cursos.isEmpty()) {
            modeloCursos.addElement(new Curso("Todos los cursos*", null));
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
        modeloTabla.setColumnCount(2);

        seleccionTablaARevisar();
    }

    private void seleccionTablaARevisar() {

        listSelectionModel = tablaAlumnos.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //TO DO Parece no tener efecto, a buscar resolver una vez funcione
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                int selectedRowIndex = listSelectionModel.getMinSelectionIndex();
                if (selectedRowIndex != -1) {
                    alumnoSeleccionado = (Alumno) modeloTabla.getValueAt(selectedRowIndex, 0);
                    mostrarDatosAlumnoSeleccionado();
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
        }
    }

    private void cargarTabla() {
        //A obtener el curso de la seleccion del combo
        ArrayList<Alumno> listadoAlumnos = bd.obtenerAlumnos(cursoSeleccionado);
        int cantidadAlumnos = listadoAlumnos.size();
        modeloTabla.setNumRows(cantidadAlumnos);

        for (int i = 0; i < cantidadAlumnos; i++) {
            Alumno alumnoActual = listadoAlumnos.get(i);

            int pedidoIndividual = listadoAlumnos.get(i).getPedidoIndividual();

            modeloTabla.setValueAt(alumnoActual, i, 0);
            modeloTabla.setValueAt(pedidoIndividual, i, 1);
        }
    }

    private void mostrarDatosAlumnoSeleccionado() {
        fieldNombre.setText(alumnoSeleccionado.getNombreAlumno());
        fieldIdAlumno.setText(String.valueOf(alumnoSeleccionado.getIdAlumno()));
        fieldTurno.setText("[Pendiente]");
        fieldSeño.setText("[Pendiente]");

        //Foto
        
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

        fieldFechaIndividual.setText(String.valueOf(alumnoSeleccionado.getFechaIndividual()));
        fieldFechaSeñoM.setText(String.valueOf(alumnoSeleccionado.getFechaSeñoMña()));
        fieldFechaSeñoT.setText(String.valueOf(alumnoSeleccionado.getFechaSeñoTde()));
        fieldFechaGrupalM.setText(String.valueOf(c.getFechaFotoGrupal())); // TO DO: Por el momento carga los dos iguales
        fieldFechaGrupalT.setText(String.valueOf(c.getFechaFotoGrupal())); //

        fieldTotal.setText(String.valueOf(alumnoSeleccionado.getTotal()));
        fieldPago.setText(String.valueOf(alumnoSeleccionado.getMontoAbonado()));
        fieldRestante.setText(String.valueOf(alumnoSeleccionado.getRestaAbonar()));

        textObs.setText(alumnoSeleccionado.getObservacionesAlumno());
    }

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
        panelDatosAlumno = new javax.swing.JPanel();
        lblIdAlumno = new javax.swing.JLabel();
        fieldIdAlumno = new javax.swing.JTextField();
        lblTurno = new javax.swing.JLabel();
        fieldTurno = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        fieldNombre = new javax.swing.JTextField();
        lblMaestra = new javax.swing.JLabel();
        fieldSeño = new javax.swing.JTextField();
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
        lblParDeLlaveros = new javax.swing.JLabel();
        btMenosParDeLlaveros = new javax.swing.JButton();
        fieldPedidoParDeLlaveros = new javax.swing.JTextField();
        btMasParDeLlaveros = new javax.swing.JButton();
        btMasCombo1 = new javax.swing.JButton();
        btMasCombo2 = new javax.swing.JButton();
        chkFaltoGrupal = new javax.swing.JCheckBox();
        fieldFechaFaltoGrupal = new javax.swing.JTextField();
        fieldFotoFaltoGrupal = new javax.swing.JTextField();
        panelPago = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        fieldTotal = new javax.swing.JTextField();
        lblPago = new javax.swing.JLabel();
        fieldPago = new javax.swing.JTextField();
        lblFechaPago = new javax.swing.JLabel();
        fieldFechaPago = new javax.swing.JTextField();
        lblRestante = new javax.swing.JLabel();
        fieldRestante = new javax.swing.JTextField();
        panelObservaciones = new javax.swing.JPanel();
        scrollObs = new javax.swing.JScrollPane();
        textObs = new javax.swing.JTextArea();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FotosRo - (Version BETA)");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

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
        tablaAlumnos.setFocusable(false);
        panelAlumnos.setViewportView(tablaAlumnos);

        btAgregarAlumno.setText("Agregar");

        btModificarAlumno.setText("Modificar");

        lblRegistrosEncontrados.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRegistrosEncontrados.setText(" XXX alumnos");

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

        panelFotoAlumno.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblFotoAlumno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFotoAlumno.setText("[Foto: Pendiente]");

        javax.swing.GroupLayout panelFotoAlumnoLayout = new javax.swing.GroupLayout(panelFotoAlumno);
        panelFotoAlumno.setLayout(panelFotoAlumnoLayout);
        panelFotoAlumnoLayout.setHorizontalGroup(
            panelFotoAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotoAlumnoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFotoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panelFotoAlumnoLayout.setVerticalGroup(
            panelFotoAlumnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotoAlumnoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFotoAlumno, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addContainerGap())
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

        fieldPedidoIndividual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoIndividual.setText("0");

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

        fieldPedidoGrupalM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoGrupalM.setText("0");

        btMasGrupalM.setText("+");

        fieldFechaGrupalM.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoGrupalM.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblGrupalT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGrupalT.setText("Grupal (Tde):");

        btMenosGrupalT.setText("-");

        fieldPedidoGrupalT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoGrupalT.setText("0");

        btMasGrupalT.setText("+");

        fieldFechaGrupalT.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoGrupalT.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblSeñoM.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSeñoM.setText("Seño (Mña):");

        btMenosSeñoM.setText("-");

        fieldPedidoSeñoM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoSeñoM.setText("0");

        btMasSeñoM.setText("+");

        fieldFechaSeñoM.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoSeñoM.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblSeñoT.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSeñoT.setText("Seño (Tde):");

        btMenosSeñoT.setText("-");

        fieldPedidoSeñoT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoSeñoT.setText("0");

        btMasSeñoT.setText("+");

        fieldFechaSeñoT.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        fieldFotoSeñoT.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lblCarnet.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCarnet.setText("Carnet:");

        btMenosCarnet.setText("-");

        fieldPedidoCarnet.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoCarnet.setText("0");

        btMasCarnet.setText("+");

        lblParDeLlaveros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblParDeLlaveros.setText("Llaveros:");

        btMenosParDeLlaveros.setText("-");

        fieldPedidoParDeLlaveros.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldPedidoParDeLlaveros.setText("0");

        btMasParDeLlaveros.setText("+");

        btMasCombo1.setText("C1+");

        btMasCombo2.setText("C2+");

        chkFaltoGrupal.setText("Faltó a grupal");

        fieldFechaFaltoGrupal.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        fieldFechaFaltoGrupal.setEnabled(false);

        fieldFotoFaltoGrupal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldFotoFaltoGrupal.setEnabled(false);

        javax.swing.GroupLayout panelFotosLayout = new javax.swing.GroupLayout(panelFotos);
        panelFotos.setLayout(panelFotosLayout);
        panelFotosLayout.setHorizontalGroup(
            panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addGap(28, 28, 28)
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldFotoIndividual, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoGrupalM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoGrupalT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoSeñoM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoSeñoT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFotoNro)))
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
                            .addComponent(chkFaltoGrupal)
                            .addComponent(fieldFechaSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFotosLayout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(fieldFotoFaltoGrupal, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fieldFechaFaltoGrupal, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 22, Short.MAX_VALUE)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btMasCombo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btMasCombo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelFotosLayout.setVerticalGroup(
            panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotosLayout.createSequentialGroup()
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPedidos)
                    .addComponent(lblFechaSacada)
                    .addComponent(lblFotoNro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelFotosLayout.createSequentialGroup()
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIndividual)
                            .addComponent(btMenosIndividual)
                            .addComponent(btMasIndividual)
                            .addComponent(fieldPedidoIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFechaIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoIndividual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblGrupalM)
                            .addComponent(btMenosGrupalM)
                            .addComponent(btMasGrupalM)
                            .addComponent(fieldPedidoGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFechaGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldFotoGrupalM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btMasCombo1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGrupalT)
                    .addComponent(btMenosGrupalT)
                    .addComponent(btMasGrupalT)
                    .addComponent(fieldPedidoGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldFechaGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldFotoGrupalT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelFotosLayout.createSequentialGroup()
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
                                .addComponent(fieldFotoSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fieldPedidoSeñoT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblSeñoT))))
                    .addComponent(btMasCombo2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCarnet)
                    .addComponent(btMenosCarnet)
                    .addComponent(btMasCarnet)
                    .addComponent(fieldPedidoCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkFaltoGrupal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFotosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblParDeLlaveros)
                    .addComponent(btMenosParDeLlaveros)
                    .addComponent(btMasParDeLlaveros)
                    .addComponent(fieldPedidoParDeLlaveros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldFechaFaltoGrupal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fieldFotoFaltoGrupal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(43, 43, 43)
                .addComponent(lblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(lblPago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldPago, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(lblFechaPago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldFechaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblRestante)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fieldRestante, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
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

        panelObservaciones.setBorder(javax.swing.BorderFactory.createTitledBorder("Observaciones"));

        textObs.setColumns(20);
        textObs.setRows(5);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
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
                        .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(tabAlumnosLayout.createSequentialGroup()
                                .addComponent(btAgregarAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btModificarAlumno)
                                .addGap(18, 18, 18)
                                .addComponent(lblRegistrosEncontrados)))
                        .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabAlumnosLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelFotoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelDatosAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelFotos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(panelObservaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(47, 47, 47))
                            .addGroup(tabAlumnosLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(panelPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
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
                .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tabAlumnosLayout.createSequentialGroup()
                        .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tabAlumnosLayout.createSequentialGroup()
                                .addComponent(panelFotos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelObservaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(tabAlumnosLayout.createSequentialGroup()
                                .addComponent(panelFotoAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelDatosAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelPago, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(tabAlumnosLayout.createSequentialGroup()
                        .addComponent(panelAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tabAlumnosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btAgregarAlumno)
                            .addComponent(btModificarAlumno)
                            .addComponent(lblRegistrosEncontrados))))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Alumnos", tabAlumnos);

        lblRutaBD.setText("Ruta de acceso a las bases de datos:");

        fieldRutaBD.setText(rutaBD);
        fieldRutaBD.setEnabled(false);

        btModificarRutaBD.setText("Modificar");

        lblUltimaInstitucion.setText("Última institucion utilizada:");

        lblDetalleUltimaInstitucion.setText(ultimaBD);

        lblRutaFotos.setText("Ruta de acceso a las fotos:");

        fieldRutaFotos.setText("P:\\Proyectos Personales pCloud\\FotosRo\\PruebasFotos");
        fieldRutaFotos.setEnabled(false);

        btModificarRutaFotos.setText("Modificar");

        lblNombreArchivos.setText("Nombres de archivos:");

        fieldPreNroFoto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fieldPreNroFoto.setText("_DSC");
        fieldPreNroFoto.setEnabled(false);

        lblNroFoto.setText("+ Nº de Foto +");

        fieldPostNroFoto.setText(".jpg");
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
                        .addContainerGap(92, Short.MAX_VALUE)
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
                                    .addComponent(lblRutaCopiaImprenta, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(fieldRutaCopiaImprenta, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btModificarRutaCopiaImprenta))
                            .addGroup(tabConfigLayout.createSequentialGroup()
                                .addComponent(fieldRutaFotos, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btModificarRutaFotos))
                            .addGroup(tabConfigLayout.createSequentialGroup()
                                .addComponent(fieldRutaBD, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btModificarRutaBD))
                            .addComponent(lblDetalleUltimaInstitucion, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(177, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                .addComponent(panelAccionesRapidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        tabbedPane.addTab("Configuración", tabConfig);

        javax.swing.GroupLayout panelTabsLayout = new javax.swing.GroupLayout(panelTabs);
        panelTabs.setLayout(panelTabsLayout);
        panelTabsLayout.setHorizontalGroup(
            panelTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTabsLayout.createSequentialGroup()
                .addComponent(tabbedPane)
                .addContainerGap())
        );
        panelTabsLayout.setVerticalGroup(
            panelTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTabsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(panelTabs, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        modeloCursos.removeAllElements();
        cargarModelos();
    }//GEN-LAST:event_btAgregarCursoActionPerformed

    private void cboCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCursoActionPerformed
        cursoSeleccionado = (Curso) cboCurso.getSelectedItem();
        System.out.println(cursoSeleccionado);
        limpiarTabla();
        cargarTabla();
        tablaAlumnos.changeSelection(0, 0, false, false);
        //mostrarDatosSeleccion();
    }//GEN-LAST:event_cboCursoActionPerformed

    private void btMasIndividualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMasIndividualActionPerformed
        if (alumnoSeleccionado != null) {
            int nuevoPedido = Integer.parseInt(fieldPedidoIndividual.getText()) + 1;
            fieldPedidoIndividual.setText(String.valueOf(nuevoPedido));
            alumnoSeleccionado.setPedidoIndividual(nuevoPedido);
            //bd.updateAlumno(alumnoSeleccionado);
        }
    }//GEN-LAST:event_btMasIndividualActionPerformed

    private void btMenosIndividualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btMenosIndividualActionPerformed
        if (Integer.parseInt(fieldPedidoIndividual.getText()) < 1) {
            JOptionPane.showMessageDialog(this, "La cantidad pedida no puede ser inferior a cero.", "Cantidad mínima alcanzada", JOptionPane.ERROR_MESSAGE);
        } else {
            int nuevoPedido = Integer.parseInt(fieldPedidoIndividual.getText()) - 1;
            fieldPedidoIndividual.setText(String.valueOf(nuevoPedido));
        }
    }//GEN-LAST:event_btMenosIndividualActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
 /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
         */
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FramePrincipal().setVisible(true);
            }
        });
    }

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
    private javax.swing.JComboBox<String> cboAño;
    private javax.swing.JComboBox<Curso> cboCurso;
    private javax.swing.JComboBox<String> cboInstitucion;
    private javax.swing.JCheckBox chkFaltoGrupal;
    private javax.swing.JTextField fieldFechaFaltoGrupal;
    private javax.swing.JTextField fieldFechaGrupalM;
    private javax.swing.JTextField fieldFechaGrupalT;
    private javax.swing.JTextField fieldFechaIndividual;
    private javax.swing.JTextField fieldFechaPago;
    private javax.swing.JTextField fieldFechaSeñoM;
    private javax.swing.JTextField fieldFechaSeñoT;
    private javax.swing.JTextField fieldFotoFaltoGrupal;
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
    private javax.swing.JLabel lblAño;
    private javax.swing.JLabel lblCarnet;
    private javax.swing.JLabel lblCurso;
    private javax.swing.JLabel lblDetalleUltimaInstitucion;
    private javax.swing.JLabel lblFechaPago;
    private javax.swing.JLabel lblFechaSacada;
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
    private javax.swing.JPanel panelDatosAlumno;
    private javax.swing.JPanel panelFotoAlumno;
    private javax.swing.JPanel panelFotos;
    private javax.swing.JPanel panelObservaciones;
    private javax.swing.JPanel panelPago;
    private javax.swing.JPanel panelTabs;
    private javax.swing.JScrollPane scrollObs;
    private javax.swing.JPanel tabAlumnos;
    private javax.swing.JPanel tabConfig;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tablaAlumnos;
    private javax.swing.JTextArea textObs;
    // End of variables declaration//GEN-END:variables

}
