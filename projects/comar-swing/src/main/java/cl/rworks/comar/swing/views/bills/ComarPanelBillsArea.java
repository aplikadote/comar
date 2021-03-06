/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.rworks.comar.swing.views.bills;

import cl.rworks.comar.swing.main.ComarSystem;
import cl.rworks.comar.swing.model.ComarBill;
import cl.rworks.comar.swing.model.ComarBillUnit;
import cl.rworks.comar.swing.model.ComarControllerException;
import cl.rworks.comar.swing.model.ComarProduct;
import cl.rworks.comar.swing.util.ComarActionSimple;
import cl.rworks.comar.swing.util.ComarPanel;
import cl.rworks.comar.swing.util.ComarPanelOptionsArea;
import cl.rworks.comar.swing.util.ComarPanelDate;
import cl.rworks.comar.swing.util.ComarPanelFactory;
import cl.rworks.comar.swing.util.ComarPanelTitle;
import cl.rworks.comar.swing.util.ComarPanelView;
import cl.rworks.comar.swing.util.ComarUtils;
import cl.rworks.comar.swing.util.LocalDateTableCellRenderer;
import cl.rworks.comar.swing.util.PercentualTableModel;
import cl.rworks.comar.swing.util.WebTextFieldFactory;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.text.WebTextField;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author aplik
 */
public class ComarPanelBillsArea extends ComarPanelView {

    private ComarPanel panelContent;
    private ComarPanelBillsController controller;
    //
    private ComarPanel panelLeft;
    private ComarPanel panelRight;
    //
    private ComarPanelDate panelBillDateSearch;
    private WebTextField textBillCodeSearch;
    private WebTable tableBills;
    private TableModelBills tableModelBills;
    private ComarPanelOptionsArea panelBillOptionsDown;
    //
    private ComarPanel panelRightTableArea;
    private ComarPanelOptionsArea panelRightTableButtonsArea;
    private ComarPanelTitle panelSelectedBillTitle;
    private WebPopupMenu popupTableBills;
    private WebTextField textProductCode;
    private WebTable tableBillUnits;
    private TableModelBillUnits tableModelBillUnits;
    private WebTextField textSearch;
    private TableRowSorter sorter;
    //
    private ComarBill selectedBill;

    public ComarPanelBillsArea() {
        super("Administrar Facturas");
        initComponents();
    }

    private void initComponents() {
        controller = new ComarPanelBillsController();

        panelContent = new ComarPanelFactory().boxLayoutPage().create();
        getPanelContent().add(panelContent, BorderLayout.CENTER);

        WebSplitPane split = new WebSplitPane(WebSplitPane.HORIZONTAL_SPLIT, initLeft(), initRight());
        split.setDividerLocation(350);
        panelContent.add(split);
    }

    private ComarPanel initLeft() {
        panelLeft = new ComarPanelFactory().borderLayout().create();

        WebTabbedPane tabbed = new WebTabbedPane();
        ComarPanelOptionsArea panelSearchBillByDate = new ComarPanelOptionsArea();
        panelSearchBillByDate.addCenter(panelBillDateSearch = new ComarPanelDate());
        panelSearchBillByDate.addCenter(new WebButton("Buscar", e -> searchBillByDateAction()));
        tabbed.addTab("Por Fecha", panelSearchBillByDate);

        panelBillDateSearch.getComboDay().setSelectedItem(-1);

        ComarPanelOptionsArea panelSearchBillByCode = new ComarPanelOptionsArea();
        panelSearchBillByCode.addCenter(textBillCodeSearch = new WebTextField(20));
        panelSearchBillByCode.addCenter(new WebButton("Buscar", e -> searchBillByCodeAction()));
        tabbed.addTab("Por Codigo", panelSearchBillByCode);

        panelLeft.setBorder(new TitledBorder("Facturas"));
        panelLeft.add(tabbed, BorderLayout.NORTH);
        panelLeft.add(new WebScrollPane(tableBills = new WebTable()), BorderLayout.CENTER);
        panelLeft.add(panelBillOptionsDown = new ComarPanelOptionsArea(), BorderLayout.SOUTH);

        tableModelBills = new TableModelBills();
        tableBills.setModel(tableModelBills);
        tableBills.setDefaultRenderer(LocalDate.class, new LocalDateTableCellRenderer());
        tableBills.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setSelectedBill(getSelectedBill());
            }

        });

        tableBills.setComponentPopupMenu(popupTableBills = new WebPopupMenu());
        popupTableBills.add(new ComarActionSimple("Editar", e -> editBillAction()));
        popupTableBills.add(new ComarActionSimple("Eliminar", e -> deleteBillAction()));

        panelBillOptionsDown.addRight(new WebButton("Agregar", e -> addBillAction()));
        panelBillOptionsDown.addRight(new WebButton("Editar", e -> editBillAction()));
        panelBillOptionsDown.addRight(new WebButton("Eliminar", e -> deleteBillAction()));
//        panelDateFilter.getButtonSearch().addActionListener(e -> searchBillsAction());

        return panelLeft;
    }

    private ComarPanel initRight() {
        panelRight = new ComarPanelFactory().borderLayout().create();

        panelRight.add(panelSelectedBillTitle = new ComarPanelTitle(""), BorderLayout.NORTH);
        panelRight.add(panelRightTableArea = new ComarPanelFactory().borderLayout().border(new EmptyBorder(10, 10, 10, 10)).create(), BorderLayout.CENTER);

        panelRightTableArea.add(panelRightTableButtonsArea = new ComarPanelOptionsArea(), BorderLayout.NORTH);
        panelRightTableArea.add(new WebScrollPane(tableBillUnits = new WebTable()), BorderLayout.CENTER);

        panelRightTableButtonsArea.addLeft(new WebLabel("Codigo"));
        panelRightTableButtonsArea.addLeft(textProductCode = new WebTextFieldFactory().cols(20).actionListener(e -> addBillUnitAction()).create());
        panelRightTableButtonsArea.addLeft(new WebButton("Agregar", e -> addBillUnitAction()));
        panelRightTableButtonsArea.addRight(new WebButton("Eliminar", e -> deleteBillUnitsAction()));

        tableModelBillUnits = new TableModelBillUnits();
        tableBillUnits.setModel(tableModelBillUnits);
        ComarUtils.initTable(tableBillUnits);
        
        popupTableBills.add(new ComarActionSimple("Eliminar", e -> deleteBillAction()));

        sorter = new TableRowSorter(tableModelBillUnits);
        tableBillUnits.setRowSorter(sorter);

        ComarPanelOptionsArea panelSearch = new ComarPanelOptionsArea();
        panelSearch.addLeft(new WebLabel("Buscar"));
        panelSearch.addLeft(textSearch = new WebTextFieldFactory().cols(30).actionListener(e -> searchBillUnitAction()).create());
        panelSearch.addLeft(new WebButton("Buscar", e -> searchBillUnitAction()));
        panelRightTableArea.add(panelSearch, BorderLayout.SOUTH);

        return panelRight;
    }

    private class TableModelBills extends AbstractTableModel {

        private String[] colNames = new String[]{"Factura", "Fecha"};
        private List<ComarBill> rows = null;

        public List<ComarBill> getRows() {
            return rows;
        }

        public void setRows(List<ComarBill> bills) {
            this.rows = bills;
        }

        @Override
        public int getRowCount() {
            return rows != null ? rows.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return colNames.length;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return LocalDate.class;
                default:
                    return String.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ComarBill bill = rows.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return bill.getEntity().getCodigo();
                case 1:
                    return bill.getEntity().getFecha();
                default:
                    return "";
            }
        }

    }

    private class TableModelBillUnits extends AbstractTableModel implements PercentualTableModel{

        private Object[][] cols = new Object[][]{
            {"Codigo", String.class, false, false},
            {"Descripcion", String.class, false, false},
            {"Precio Neto Compra", BigDecimal.class, true, false},
            {"Cantidad", BigDecimal.class, true, false},
            {"IVA", BigDecimal.class, false, true},
            {"Imp.Extra", BigDecimal.class, false, true},
            {"Precio Bruto Compra", BigDecimal.class, false, false},
            {"% Ganancia", BigDecimal.class, false, true},
            {"Precio Venta Sugerido", BigDecimal.class, false, false},
            {"Subtotal", BigDecimal.class, false, false}
        };

        private boolean contains(String code) {
            List<ComarBillUnit> units = selectedBill != null ? selectedBill.getUnits() : null;
            if (units != null) {
                return units.stream().anyMatch(e -> e.getProduct().getEntity().getCodigo().equals(code));
            } else {
                return false;
            }

        }

        @Override
        public int getRowCount() {
            List<ComarBillUnit> units = selectedBill != null ? selectedBill.getUnits() : null;
            return units != null ? units.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return (Class) cols[columnIndex][1];
        }

        @Override
        public String getColumnName(int columnIndex) {
            return (String) cols[columnIndex][0];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            List<ComarBillUnit> units = selectedBill.getUnits();
            ComarBillUnit billUnit = units.get(rowIndex);

            BigDecimal price = billUnit.getEntity().getPrecioNetoCompra();
            BigDecimal tax1 = billUnit.getProduct().getCategory().getEntity().getImpuestoPrincipal();
            BigDecimal tax2 = billUnit.getProduct().getCategory().getEntity().getImpuestoSecundario();
            BigDecimal priceReal = price.multiply(BigDecimal.ONE.add(tax1).add(tax2));
            BigDecimal profit = billUnit.getProduct().getCategory().getEntity().getPorcentajeGanancia();
            BigDecimal quantity = billUnit.getEntity().getCantidad();
            switch (columnIndex) {
                case 0:
                    return billUnit.getProduct().getEntity().getCodigo();
                case 1:
                    return billUnit.getProduct().getEntity().getDescripcion();
                case 2:
                    return price;
                case 3:
                    return quantity;
                case 4:
                    return tax1;
                case 5:
                    return tax2;
                case 6:
                    return priceReal;
                case 7:
                    return profit;
                case 8:
                    return priceReal.multiply(BigDecimal.ONE.add(profit));
                case 9:
                    return priceReal.multiply(quantity);
                default:
                    return "";
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return (Boolean) cols[columnIndex][2];
        }
        
        @Override
        public boolean isPercentual(int rowIndex, int columnIndex) {
            return (Boolean) cols[columnIndex][3];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            List<ComarBillUnit> units = selectedBill.getUnits();
            ComarBillUnit billUnit = units.get(rowIndex);
            switch (columnIndex) {
                case 2: {
                    try {
                        BigDecimal precio = (BigDecimal) aValue;
                        controller.updateBillUnitPrice(billUnit, precio);
                        fireTableCellUpdated(rowIndex, columnIndex);
                        fireTableCellUpdated(rowIndex, 6);
                        fireTableCellUpdated(rowIndex, 8);
                        fireTableCellUpdated(rowIndex, 9);
                    } catch (ComarControllerException ex) {
                        ComarUtils.showWarn(null, ex.getMessage());
                    }
                    break;
                }
                case 3: {
                    try {
                        BigDecimal cantidad = (BigDecimal) aValue;
                        controller.updateBillUnitQuatity(billUnit, cantidad);
                        fireTableCellUpdated(rowIndex, columnIndex);
                        fireTableCellUpdated(rowIndex, 6);
                        fireTableCellUpdated(rowIndex, 8);
                        fireTableCellUpdated(rowIndex, 9);
                    } catch (ComarControllerException ex) {
                        ComarUtils.showWarn(null, ex.getMessage());
                    }
                    break;
                }
                default:
                    break;
            }
        }

    }

    private void searchBillByDateAction() {
        int[] value = panelBillDateSearch.getValue();
        List<ComarBill> bills = controller.searchBillsByDate(value);
        tableModelBills.setRows(bills);
        tableModelBills.fireTableDataChanged();

        setSelectedBill(null);
    }

    private void searchBillByCodeAction() {
        String text = textBillCodeSearch.getText();
        if (text == null) {
            return;
        }

        text = text.trim();
        if (text.isEmpty()) {
            return;
        }

        List<ComarBill> bills = controller.searchBillsByCode(text);
        tableModelBills.setRows(bills);
        tableModelBills.fireTableDataChanged();

        setSelectedBill(null);
    }

    private void addBillAction() {
        ComarDialogBillInsert dialog = new ComarDialogBillInsert(ComarSystem.getInstance().getFrame(), "Agregar Factura");
        dialog.getPanel().updateForm(LocalDate.now(), "");
        dialog.showMe();

        if (dialog.isOk()) {
            try {
                ComarBill bill = dialog.getBill();
                controller.addBill(bill);
                searchBillByDateAction();
            } catch (ComarControllerException ex) {
                ex.printStackTrace();
                ComarUtils.showWarn(this, ex.getMessage());
            }
        }
    }

    private void editBillAction() {
        ComarBill updateBill = getSelectedBill();
        if (updateBill == null) {
            ComarUtils.showInfo(this, "Seleccione al menos una factura");
            return;
        }

        ComarDialogBillInsert dialog = new ComarDialogBillInsert(ComarSystem.getInstance().getFrame(), "Editar Factura");
        dialog.getPanel().updateForm(updateBill.getEntity().getFecha(), updateBill.getEntity().getCodigo());
        dialog.showMe();

        if (dialog.isOk()) {
            try {
                ComarBill bill = dialog.getBill();
                updateBill.getEntity().setFecha(bill.getEntity().getFecha());
                updateBill.getEntity().setCodigo(bill.getEntity().getCodigo());
                controller.updateBill(updateBill);
                searchBillByDateAction();
            } catch (ComarControllerException ex) {
                ex.printStackTrace();
                ComarUtils.showWarn(this, ex.getMessage());
            }
        }
    }

    private List<ComarBill> getSelectedBills() {
        int[] vrows = this.tableBills.getSelectedRows();
        if (vrows.length == 0) {
            return null;
        }

        List<ComarBill> list = new ArrayList<>();
        for (int i = 0; i < vrows.length; i++) {
            int vrow = vrows[i];
            int mrow = this.tableBills.convertRowIndexToModel(vrow);
            ComarBill bill = this.tableModelBills.getRows().get(mrow);
            list.add(bill);
        }

        return list;
    }

    private ComarBill getSelectedBill() {
        int vrow = this.tableBills.getSelectedRow();
        if (vrow == -1) {
            return null;
        }

        int mrow = this.tableBills.convertRowIndexToModel(vrow);
        return this.tableModelBills.getRows().get(mrow);
    }

    private void deleteBillAction() {
        List<ComarBill> bills = getSelectedBills();
        if (bills == null) {
            ComarUtils.showInfo(this, "Seleccione al menos una factura");
            return;
        }

        int r = ComarUtils.showYesNo(this, "Desea eliminar las facturas seleccionadas?", "Eliminar Facturas");
        if (r == JOptionPane.YES_OPTION) {
            try {
                controller.deleteBills(bills);
                searchBillByDateAction();
            } catch (ComarControllerException ex) {
                ComarUtils.showWarn(this, ex.getMessage());
            }
        }
    }

    private void addBillUnitAction() {
        if (selectedBill == null) {
            ComarUtils.showWarn(this, "Seleccione una factura");
            return;
        }

        String code = this.textProductCode.getText();
        if (code == null || code.isEmpty()) {
            ComarUtils.showWarn(this, "Ingrese un codigo de producto existente");
            return;
        }

        ComarProduct product = controller.getProduct(code);
        if (product == null) {
            ComarUtils.showWarn(this, "Producto no encontrado");
            return;
        }

        if (tableModelBillUnits.contains(code)) {
            return;
        }

        try {
            ComarBillUnit unit = new ComarBillUnit(selectedBill, product);
            controller.addBillUnit(unit);
            tableModelBillUnits.fireTableDataChanged();
        } catch (ComarControllerException ex) {
            ComarUtils.showWarn(this, ex.getMessage());
        }

    }

    private void deleteBillUnitsAction() {
        if (selectedBill == null) {
            ComarUtils.showWarn(this, "Seleccione una factura");
            return;
        }

        List<ComarBillUnit> units = getSelectedBillUnits();
        if (units == null) {
            ComarUtils.showWarn(this, "Seleccione al menos un elemento de la tabla");
            return;
        }

        int r = ComarUtils.showYesNo(this, "Desea eliminar los elementos seleccionados?", "Eliminar");
        if (r == WebOptionPane.YES_OPTION) {
            try {
                controller.deleteBillUnits(units, selectedBill);
                tableModelBillUnits.fireTableDataChanged();
            } catch (ComarControllerException ex) {
                ComarUtils.showWarn(this, ex.getMessage());
            }
        }
    }

    private void setSelectedBill(ComarBill sBill) {
        selectedBill = sBill;
        if (selectedBill != null) {
            LocalDate date = selectedBill.getEntity().getFecha();
            String code = selectedBill.getEntity().getCodigo();
            panelSelectedBillTitle.setTitle(String.format("%s: %s", "Factura", code));
            panelSelectedBillTitle.setTitleEast(String.format("%s", date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            tableModelBillUnits.fireTableDataChanged();
        } else {
            panelSelectedBillTitle.setTitle("");
            panelSelectedBillTitle.setTitleEast("");
            tableModelBillUnits.fireTableDataChanged();
        }

    }

    private List<ComarBillUnit> getSelectedBillUnits() {
        if (selectedBill == null) {
            return null;
        }

        int[] vrows = this.tableBillUnits.getSelectedRows();
        if (vrows.length == 0) {
            return null;
        }

        List<ComarBillUnit> units = new ArrayList<>();
        for (int i = 0; i < vrows.length; i++) {
            int vrow = vrows[i];
            int mrow = tableBillUnits.convertRowIndexToModel(vrow);
            ComarBillUnit unit = selectedBill.getUnits().get(mrow);
            units.add(unit);
        }

        return units;
    }

    public void searchBillUnitAction() {
        String text = textSearch.getText();
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text + "", 0, 1));
        sorter.sort();
    }

}
