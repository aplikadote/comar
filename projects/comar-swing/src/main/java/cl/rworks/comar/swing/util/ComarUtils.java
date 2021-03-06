/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.swing.util;

import cl.rworks.comar.core.model.Metrica;
import cl.rworks.comar.core.util.ComarNumberFormat;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.optionpane.WebOptionPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
import static javax.swing.JComponent.WHEN_FOCUSED;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 *
 * @author rgonzalez
 */
public class ComarUtils {

    public static final Dimension BUTTON_PREF_DIM = new Dimension(195, 35);
    public static final DecimalFormat dfDecs = new DecimalFormat("000");

    public static void showInfo(Component parent, String msg) {
        WebOptionPane.showMessageDialog(parent, msg, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarn(Component parent, String msg) {
        WebOptionPane.showMessageDialog(parent, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static void showError(Component parent, String msg) {
        WebOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static int showYesNo(Component owner, String msg, String title) {
        return WebOptionPane.showConfirmDialog(owner, msg, title, WebOptionPane.YES_NO_OPTION, WebOptionPane.QUESTION_MESSAGE);
    }

    public static String format(BigDecimal bd) {
        return ComarNumberFormat.format(bd);
    }
    
    public static String formatPercentual(BigDecimal bd) {
        return ComarNumberFormat.formatPercentual(bd);
    }

    public static BigDecimal parse(String str) throws ParseException {
        return ComarNumberFormat.parse(str);
    }

    public static String formatDbl(double dblValue) {
        return ComarNumberFormat.formatDbl(dblValue);
    }

    public static double parseDbl(String strValue) throws ParseException {
        return ComarNumberFormat.parseDbl(strValue);
    }

    public static String formatIva(int iva) {
        return Integer.toString(iva);
    }

    public static double parseIva(String strIva) throws ParseException {
        return Integer.parseInt(strIva);
    }

    public static String formatInt(int intValue) {
        return Integer.toString(intValue);
    }

    public static String formatDec(int decsValues) {
        return dfDecs.format(decsValues);
    }

    public static void installCloseKey(JDialog dialog, final ActionListener closeAction) {
        JPanel panel = (JPanel) dialog.getContentPane();
        panel.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ESCAPE"), "comar.close.dialog");
        panel.getActionMap().put("comar.close.dialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAction.actionPerformed(e);
            }
        });

        panel.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ESCAPE"), "comar.close.dialog");
        panel.getActionMap().put("comar.close.dialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAction.actionPerformed(e);
            }
        });

        panel.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "comar.close.dialog");
        panel.getActionMap().put("comar.close.dialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAction.actionPerformed(e);
            }
        });
    }
    
    public static void initTable(JTable table) {
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        table.setCellSelectionEnabled(true);
        
        table.setDefaultRenderer(Metrica.class, new MetricaTableRenderer());
        table.setDefaultEditor(Metrica.class, new MetricaTableEditor());
      
        table.setDefaultRenderer(BigDecimal.class, new BigDecimalTableRenderer());
        table.setDefaultEditor(BigDecimal.class, new BigDecimalTableEditor());
        
        table.setDefaultRenderer(Boolean.class, new BooleanTableRenderer());
        table.setDefaultEditor(Boolean.class, new BooleanTableEditor());
        
        table.setDefaultRenderer(String.class, new StringTableRenderer());
//        table.setDefaultEditor(String.class, new StringTableEditor());
    }
    
    

}
