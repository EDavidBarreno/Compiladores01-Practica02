package org.zafkiel.Frontend;

import compilerTools.*;
import java_cup.runtime.Symbol;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePage extends JFrame {
    public JPanel panelHomePage;
    private JTabbedPane PanelClienteServidor;
    private JPanel panelManejadorWeb;
    private JPanel panelReportes;
    private JPanel panelArboles;
    private JPanel panelSalir;
    private JButton botonSalirDelSistema;
    private JButton botonManejadorWebAnalizar;
    private JButton botonManejadorWebLimpiar;
    private JTextPane textPaneManejadorWebEntrada;
    private JTextPane textPaneManejadorWebSalida;
    private JTable tablaReportesLexico;
    private JTable tablaReportesSintactico;
    private JButton botonManejadorWebEjecutar;
    private JButton botonManejadorWebAbrirXML;
    private JButton botonManejadorWebGuardarXML;
    private JButton botonManejadorWebMostrarXML;
    private JLabel labelBaseDeDatosEnUso;
    private JButton botonManejadorWebNuevoXML;
    private JButton botonManejadorWebEditarXML;
    private JButton botonManejadorWebActualizarXML;
    private JButton botonManejadorWebInsertarCodigoXML;
    private JComboBox comboBoxManejadorWebPreCargarCodigoXML;
    private JButton botonSalir;
    private JButton botonPanelSalirSalir;


    //Variables

    private String contenidoXML;
    private ArrayList<Token> tokenArrayList;
    private ArrayList<ErrorLSSL> errorLSSLArrayList;
    private ArrayList<Production> productionArrayList;
    private HashMap<String, String> identificadores;
    private boolean codigoCompilado = false;
    private Timer timerKey;
    private String rutaDB;

    public HomePage() {
        //Empezamos en limpio
        limpiarCampos();
        limpiarBaseDeDatos();
        Functions.setLineNumberOnJTextComponent(textPaneManejadorWebEntrada);
        Functions.setLineNumberOnJTextComponent(textPaneManejadorWebSalida);

        DefaultTableModel modelo1 = new DefaultTableModel();
        modelo1.addColumn("Componente Lexico  ");
        modelo1.addColumn("Lexema  ");
        modelo1.addColumn("[ Linea, Columna ]");
        tablaReportesLexico.setModel(modelo1);

        tokenArrayList = new ArrayList<>();
        errorLSSLArrayList = new ArrayList<>();
        productionArrayList = new ArrayList<>();
        identificadores = new HashMap<>();

        timerKey = new Timer(0,( (e) ->{
            timerKey.stop();
        }));

        Functions.setAutocompleterJTextComponent(new String[]{"Estuardo","David","Barreno","Nimatuj"},textPaneManejadorWebEntrada, () ->{
            timerKey.restart();
        });

        String[] elementosComboBox = {"---   Seleccione una opcion   ---", "Elemento 1", "Elemento 2", "Elemento 3", "Elemento 4"};
        DefaultComboBoxModel<String> modeloCB = new DefaultComboBoxModel<>(elementosComboBox);
        comboBoxManejadorWebPreCargarCodigoXML.setModel(modeloCB);

        botonSalirDelSistema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "De parte de los administradores,\n" +
                            "le deseamos un excelente dia.", "---   SALIENDO DEL SISTEMA   ---", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
        });
        botonManejadorWebLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Se ha limpiado el area de trabajo\n" +
                        "con exito.","---   LIMPIAR   ---",JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            }
        });
        botonManejadorWebMostrarXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(labelBaseDeDatosEnUso.getText().equals("Sin base de datos")){
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione y/o cargue\n" +
                            "una base de datos valida.", "---   SIN BASE DE DATOS   ---", JOptionPane.WARNING_MESSAGE);
                }else {
                    textPaneManejadorWebSalida.setText(contenidoXML);
                }
            }
        });
        botonManejadorWebAbrirXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                // Establecer filtro para mostrar solo archivos XML
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos XML", "xml");
                fileChooser.setFileFilter(filter);

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (selectedFile.getName().endsWith(".xml")) {
                        try {
                            StringBuilder content = new StringBuilder();
                            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                content.append(line).append("\n");
                            }
                            reader.close();
                            limpiarCampos();
                            contenidoXML = content.toString();
                            labelBaseDeDatosEnUso.setText(selectedFile.getName());
                            rutaDB = fileChooser.getSelectedFile().getAbsolutePath();
                            JOptionPane.showMessageDialog(null, "Se cargo la base de datos : "+labelBaseDeDatosEnUso.getText()
                                    +"\nla cual se usara para realizar las operaciones correspondientes.");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, selecciona un archivo XML.", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                }
            }
        });

        botonManejadorWebNuevoXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Se elimino la base de datos en uso : "+labelBaseDeDatosEnUso.getText()
                +"\npor favor recuerde guardar su nueva base de datos.");
                limpiarBaseDeDatos();
                textPaneManejadorWebEntrada.setText("<sitios_web>\n" +
                        "\t<id>IDENTIFICADOR_DEL_SITIO</id>\n" +
                        "\t<usuario_creacion>ID_DEL_USUARIO</usuario_creacion>\n" +
                        "\t<fecha_creacion>FECHA_DE_CREACION</fecha_creacion>\n" +
                        "\t<fecha_modificacion>FECHA_DE_MODIFICACION</fecha_modificacion>\n" +
                        "\t<usuario_modificacion>ID_DEL_USUARIO_MODIFICADOR</usuario_modificacion>\n" +
                        "\t<paginas>\n" +
                        "\t\t<pagina>\n" +
                        "\t\t\t<id>IDENTIFICADOR_DE_LA_PAGINA</id>\n" +
                        "\t\t\t<titulo>TITULO_DE_LA_PAGINA</titulo>\n" +
                        "\t\t\t<sitio>SITIO_AL_QUE_PERTENECE</sitio>\n" +
                        "\t\t\t<padre>PAGINA_PADRE_SI_EXISTE</padre>\n" +
                        "\t\t\t<usuario_creacion>ID_DEL_USUARIO</usuario_creacion>\n" +
                        "\t\t\t<fecha_creacion>FECHA_DE_CREACION</fecha_creacion>\n" +
                        "\t\t\t<fecha_modificacion>FECHA_DE_MODIFICACION</fecha_modificacion>\n" +
                        "\t\t\t<usuario_modificacion>ID_DEL_USUARIO_MODIFICADOR</usuario_modificacion>\n" +
                        "\t\t\t<etiquetas>\n" +
                        "\t\t\t</etiquetas>\n" +
                        "\t\t\t<componentes>\n" +
                        "\t\t\t</componentes>\n" +
                        "\t\t</pagina>\n" +
                        "\t</paginas>\n" +
                        "</sitios_web>");
            }
        });
        botonManejadorWebGuardarXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".xml")) {
                        filePath += ".xml";
                    }
                    try {
                        FileWriter fileWriter = new FileWriter(filePath);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(textPaneManejadorWebEntrada.getText());
                        bufferedWriter.close();
                        JOptionPane.showMessageDialog(null, "Archivo XML guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        limpiarCampos();
                        limpiarBaseDeDatos();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al guardar el archivo XML.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        botonManejadorWebAnalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(labelBaseDeDatosEnUso.getText().equals("Sin base de datos")){
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione y/o cargue\n" +
                            "una base de datos valida.", "---   SIN BASE DE DATOS   ---", JOptionPane.WARNING_MESSAGE);
                }else {
                    String input = textPaneManejadorWebEntrada.getText();
                    DefaultTableModel modelo1 = (DefaultTableModel) tablaReportesLexico.getModel(); // Obtener el modelo de la tabla

                    modelo1.setRowCount(0); // Limpiar la tabla antes de agregar nuevos datos

                    // Crear una instancia del analizador léxico
                    Analizador_Lexico lexer = new Analizador_Lexico(new StringReader(input));

                    try {
                        // Obtener tokens de la entrada
                        Symbol token;
                        do {
                            token = lexer.next_token();
                            if (token.sym != Simbolos.EOF) {
                                Object[] rowData = {
                                        Analizador_Lexico.getNombreToken(token.sym),               // Tipo de token
                                        lexer.yytext(),          // Lexema
                                        "[" + lexer.yyline + ", " + lexer.yycolumn + "]" // Posición
                                };
                                modelo1.addRow(rowData); // Agregar nueva fila a la tabla
                            }
                        } while (token.sym != Simbolos.EOF);
                    } catch (Exception ex) {
                        // En caso de error, imprimir la traza y agregar información al texto
                        ex.printStackTrace();
                        textPane2.setText("Error al analizar el código:\n" + ex.getMessage());
                    }
                }
            }
        });
        botonManejadorWebEjecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(labelBaseDeDatosEnUso.getText().equals("Sin base de datos")){
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione y/o cargue\n" +
                            "una base de datos valida.", "---   SIN BASE DE DATOS   ---", JOptionPane.WARNING_MESSAGE);
                }else {
                    botonManejadorWebAnalizar.doClick();
                        if(!errorLSSLArrayList.isEmpty()){
                            JOptionPane.showMessageDialog(null,"No se puede ejecutar porque se econtraron\n" +
                                    "uno o mas errores en la instruccion proporcionada.");
                        }else {

                        }

                }
            }
        });

        botonManejadorWebEditarXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(labelBaseDeDatosEnUso.getText().equals("Sin base de datos")){
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione y/o cargue\n" +
                            "una base de datos valida.", "---   SIN BASE DE DATOS   ---", JOptionPane.WARNING_MESSAGE);
                }else {
                    textPaneManejadorWebEntrada.setText(contenidoXML);
                }
            }
        });
        botonManejadorWebActualizarXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileWriter escritor = new FileWriter(rutaDB);
                    escritor.write(textPaneManejadorWebEntrada.getText());
                    escritor.close();
                    contenidoXML = textPaneManejadorWebEntrada.getText();
                    limpiarCampos();
                   JOptionPane.showMessageDialog(null, "Se actualizo el contenido en:\n" +
                           labelBaseDeDatosEnUso.getText()+" Exitosamente", "---   ACTUALIZADO   ---", JOptionPane.INFORMATION_MESSAGE);

                } catch (IOException es) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar archivo", "---   ERROR   ---", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        botonManejadorWebInsertarCodigoXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) comboBoxManejadorWebPreCargarCodigoXML.getSelectedItem();

                if (selectedItem.equals("---   Seleccione una opcion   ---")) {
                } else {
                    String textoAInsertar = "";
                    switch (selectedItem) {
                        case "Elemento 1":
                            textoAInsertar = "Aca va el elemento 1";
                            break;
                        case "Elemento 2":
                            break;
                    }
                    if (textPaneManejadorWebEntrada.getSelectedText() != null) {
                        textPaneManejadorWebEntrada.replaceSelection(textoAInsertar);
                    } else {
                        textPaneManejadorWebEntrada.replaceSelection(textoAInsertar);
                    }
                }
            }
        });


    }
    //---   METODOS   ---
    private void limpiarCampos(){
        textPaneManejadorWebEntrada.setText("");
        textPaneManejadorWebSalida.setText("");
    }
    private void limpiarBaseDeDatos(){
        labelBaseDeDatosEnUso.setText("Sin base de datos");
        textPaneManejadorWebSalida.setText("");
        contenidoXML = "";
        rutaDB = "";
    }

}
