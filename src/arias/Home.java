/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arias;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

/**
 *
 * @author vatsal
 */
public class Home extends javax.swing.JFrame {

    private static DB db = new DB();
    private final static HashMap<String, Integer> month_map = new HashMap<String, Integer>();
    private final static HashMap<Integer, String> map_month = new HashMap<Integer, String>();
    private final static HashMap<Integer, Integer> month_day = new HashMap<Integer, Integer>();
//    DefaultTableCellRenderer cellrenderer = new DefaultTableCellHeaderRenderer();

    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point p = new Point((int) ((screenSize.width / 2) - 406), (int) ((screenSize.height / 2) - 269));
        this.setLocation(p);
        month_map.put("Jan", 1);
        month_map.put("Feb", 2);
        month_map.put("Mar", 3);
        month_map.put("Apr", 4);
        month_map.put("May", 5);
        month_map.put("Jun", 6);
        month_map.put("Jul", 7);
        month_map.put("Aug", 8);
        month_map.put("Sep", 9);
        month_map.put("Oct", 10);
        month_map.put("Nov", 11);
        month_map.put("Dec", 12);
        map_month.put(1, "Jan");
        map_month.put(2, "Feb");
        map_month.put(3, "Mar");
        map_month.put(4, "Apr");
        map_month.put(5, "May");
        map_month.put(6, "Jun");
        map_month.put(7, "Jul");
        map_month.put(8, "Aug");
        map_month.put(9, "Sep");
        map_month.put(10, "Oct");
        map_month.put(11, "Nov");
        map_month.put(12, "Dec");
        month_day.put(1, 31);
        month_day.put(2, 28);
        month_day.put(3, 31);
        month_day.put(4, 30);
        month_day.put(5, 31);
        month_day.put(6, 30);
        month_day.put(7, 31);
        month_day.put(8, 31);
        month_day.put(9, 30);
        month_day.put(10, 31);
        month_day.put(11, 30);
        month_day.put(12, 31);
//        cellrenderer.setHorizontalAlignment(JLabel.CENTER);
        populateLevel();
        populateDate();
        if (Integer.parseInt(tyear.getSelectedItem().toString()) > 2015) {
            tbpgp.setEnabled(false);
            pgp.setEnabled(false);
        }
    }

    private void populateDate() {
        for (int i = 1; i < 32; i++) {
            fday.addItem(String.valueOf(i));
            tday.addItem(String.valueOf(i));
        }
        for (int i = 2006; i < 2051; i++) {
            fyear.addItem(String.valueOf(i));
            tyear.addItem(String.valueOf(i));
        }
    }

    private void populateLevel() {
        ResultSet r = db.getGP();
        if (r != null) {
            try {
                while (r.next()) {
                    level.addItem(r.getString("level"));
                }
            } catch (Exception e) {
            }

        }
        r = db.getPM();
        if (r != null) {
            try {
                while (r.next()) {
                    level.addItem(r.getString("level"));
                }
            } catch (Exception e) {
            }
        }
    }

//    private String calculateDA(long new_basic, String month, int year) {
//        ResultSet r = db.getDA(month, year);
//        try {
//            if (r != null && r.next()) {
//                float rate = r.getFloat("rate");
//                return String.valueOf((int) (new_basic * rate) / 100);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//        return null;
//    }
    private String calculateHRA(long basic) {
        float r = db.getHRA();
        return String.valueOf((int) (r * basic) / 100);
    }

    private void gp_table_refresh() {
//        gp_table.getColumnModel().getColumn(0).setCellRenderer(cellrenderer);
//        gp_table.getColumnModel().getColumn(1).setCellRenderer(cellrenderer);
//        gp_table.getTableHeader().setDefaultRenderer(cellrenderer);
        DefaultTableModel m = (DefaultTableModel) gp_table.getModel();
        m.setRowCount(0);
        ResultSet rs = db.getGP();
        if (rs != null) {
            try {
                while (rs.next()) {
                    m.addRow(new Object[]{rs.getString("level"), rs.getString("rate")});
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
        } else {
            System.out.println("can't get da results");
        }
    }

    private void da_table_refresh() {
//        da_table.getColumnModel().getColumn(0).setCellRenderer(cellrenderer);
//        da_table.getColumnModel().getColumn(1).setCellRenderer(cellrenderer);
//        da_table.getColumnModel().getColumn(2).setCellRenderer(cellrenderer);
//        da_table.getTableHeader().setDefaultRenderer(cellrenderer);
        DefaultTableModel m = (DefaultTableModel) da_table.getModel();
        m.setRowCount(0);
        ResultSet rs = db.getDA();
        if (rs != null) {
            try {
                while (rs.next()) {
                    m.addRow(new Object[]{rs.getString("month"), Integer.parseInt(rs.getString("year")), Float.parseFloat(rs.getString("rate"))});
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("can't get da results");
        }
    }

    private void pm_table_refresh() {
//        pm_table.getColumnModel().getColumn(0).setCellRenderer(cellrenderer);
//        pm_table.getColumnModel().getColumn(1).setCellRenderer(cellrenderer);
//        pm_table.getTableHeader().setDefaultRenderer(cellrenderer);
        DefaultTableModel m = (DefaultTableModel) pm_table.getModel();
        m.setRowCount(0);
        ResultSet rs = db.getPM();
        if (rs != null) {
            try {
                while (rs.next()) {
                    m.addRow(new Object[]{rs.getString("level"), rs.getString("pay")});
                }
            } catch (Exception e) {
            }
        } else {
            System.out.println("cant get results");
        }
    }

    private void da_init() {
        da_edit.setVisible(false);
        da_year.removeAllItems();
        for (int i = 2006; i < 2050; i++) {
            da_year.addItem(Integer.toString(i));
        }
        da_table_refresh();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        da_delete = new javax.swing.JMenuItem();
        da_table_popup = new javax.swing.JPopupMenu();
        pm_delete = new javax.swing.JMenuItem();
        gp_delete = new javax.swing.JMenuItem();
        main = new javax.swing.JPanel();
        dashboard = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        eName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        designation = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        detailLine = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        level = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tbpb = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        pb = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        tratbp = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        trap = new javax.swing.JTextField();
        hra = new javax.swing.JCheckBox();
        generateTable = new javax.swing.JButton();
        fday = new javax.swing.JComboBox<>();
        fmonth = new javax.swing.JComboBox<>();
        fyear = new javax.swing.JComboBox<>();
        tday = new javax.swing.JComboBox<>();
        tmonth = new javax.swing.JComboBox<>();
        tyear = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        tbpgp = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        pgp = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        hratbp = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        hrap = new javax.swing.JTextField();
        showhra = new javax.swing.JCheckBox();
        da_panel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        da_month = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        da_year = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        da_rate = new javax.swing.JTextField();
        da_add = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        da_table = new javax.swing.JTable();
        da_edit = new javax.swing.JButton();
        pm_panel = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        pm_level = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        pm_pay = new javax.swing.JTextField();
        pm_add = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        pm_table = new javax.swing.JTable();
        pm_edit = new javax.swing.JButton();
        table = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tt = new javax.swing.JTable();
        gp = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        grade = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        gp_pay = new javax.swing.JTextField();
        gp_add = new javax.swing.JButton();
        gp_edit = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        gp_table = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        menu = new javax.swing.JMenuBar();
        settings = new javax.swing.JMenu();
        home = new javax.swing.JMenuItem();
        da = new javax.swing.JMenuItem();
        paymatrix = new javax.swing.JMenuItem();
        set_hra = new javax.swing.JMenuItem();
        gradepay = new javax.swing.JMenuItem();

        da_delete.setText("jMenuItem1");
        da_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                da_deleteActionPerformed(evt);
            }
        });

        pm_delete.setText("jMenuItem1");
        pm_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pm_deleteActionPerformed(evt);
            }
        });

        gp_delete.setText("jMenuItem1");
        gp_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gp_deleteActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("::VFly Soft Solutions:: Arrears Solution");
        setLocation(new java.awt.Point(350, 500));
        setResizable(false);

        main.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Dashboard");

        jLabel2.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel2.setText("School Name");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Emp. Name");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Designation");

        jLabel5.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Detail Line");

        detailLine.setColumns(20);
        detailLine.setRows(5);
        jScrollPane1.setViewportView(detailLine);

        jLabel6.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Level");

        jLabel7.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("From Date");

        jLabel8.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("To Date");

        jLabel9.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("To be Paid Basic");

        jLabel10.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Paid Basic");

        jLabel11.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("TRA to be Paid");

        jLabel12.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("TRA Paid");

        hra.setText("Calculate HRA?");
        hra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hraActionPerformed(evt);
            }
        });

        generateTable.setText("Generate Excel");
        generateTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateTableActionPerformed(evt);
            }
        });

        fmonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));

        tmonth.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));

        tyear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tyearActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("To be Paid GP");

        jLabel31.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Paid GP");

        jLabel32.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("HRA to be Paid");

        jLabel33.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("HRA Paid");

        showhra.setText("Show HRA?");

        javax.swing.GroupLayout dashboardLayout = new javax.swing.GroupLayout(dashboard);
        dashboard.setLayout(dashboardLayout);
        dashboardLayout.setHorizontalGroup(
            dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(eName)
                            .addComponent(sName)))
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(designation))
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(level, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dashboardLayout.createSequentialGroup()
                                .addComponent(fday, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fmonth, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fyear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(dashboardLayout.createSequentialGroup()
                                .addComponent(tday, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tmonth, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tyear, 0, 122, Short.MAX_VALUE))
                            .addGroup(dashboardLayout.createSequentialGroup()
                                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tratbp, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tbpb, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tbpgp, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hratbp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(trap, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                                    .addComponent(hrap)
                                    .addComponent(pb)
                                    .addComponent(pgp)))))
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addComponent(hra)
                        .addGap(27, 27, 27)
                        .addComponent(showhra)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(generateTable)
                .addGap(349, 349, 349))
        );
        dashboardLayout.setVerticalGroup(
            dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(fday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fmonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fyear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(eName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(tday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tmonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tyear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(designation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(tbpb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(pb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(tbpgp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31)
                            .addComponent(pgp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(tratbp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(trap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(hratbp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)
                            .addComponent(hrap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel5)))
                .addGap(13, 13, 13)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hra)
                        .addComponent(showhra)))
                .addGap(67, 67, 67)
                .addComponent(generateTable)
                .addContainerGap(119, Short.MAX_VALUE))
        );

        main.add(dashboard, "card2");

        jLabel13.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Daily Allowance Calculation");

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel19.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Add new entry");

        jLabel20.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Month");

        da_month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jan", "Jul" }));

        jLabel21.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Year");

        jLabel22.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Rate");

        da_add.setText("Add");
        da_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                da_addActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Master DA Table");

        da_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Month", "Year", "Rate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        da_table.setGridColor(new java.awt.Color(255, 255, 255));
        da_table.getTableHeader().setReorderingAllowed(false);
        da_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                da_tableMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                da_tableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(da_table);

        da_edit.setText("Edit");
        da_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                da_editActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout da_panelLayout = new javax.swing.GroupLayout(da_panel);
        da_panel.setLayout(da_panelLayout);
        da_panelLayout.setHorizontalGroup(
            da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(da_panelLayout.createSequentialGroup()
                .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(da_panelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(da_panelLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(da_panelLayout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(da_year, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(da_panelLayout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(da_month, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(da_panelLayout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(da_rate)
                                    .addGroup(da_panelLayout.createSequentialGroup()
                                        .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(da_add, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                                            .addComponent(da_edit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        da_panelLayout.setVerticalGroup(
            da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(da_panelLayout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(da_panelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2))
                    .addGroup(da_panelLayout.createSequentialGroup()
                        .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(da_panelLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel19))
                            .addGroup(da_panelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel23)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(da_panelLayout.createSequentialGroup()
                                .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20)
                                    .addComponent(da_month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21)
                                    .addComponent(da_year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(da_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(da_rate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(da_add)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(da_edit)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(da_panelLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addContainerGap())))))
        );

        main.add(da_panel, "card3");

        jLabel14.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Pay Matrix");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel15.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Add new Level");

        jLabel16.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Current Levels and their Pay");

        jLabel17.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Level");

        jLabel18.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Pay");

        pm_add.setText("Add");
        pm_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pm_addActionPerformed(evt);
            }
        });

        pm_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Level", "Pay"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        pm_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pm_tableMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pm_tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(pm_table);

        pm_edit.setText("Edit");
        pm_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pm_editActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pm_panelLayout = new javax.swing.GroupLayout(pm_panel);
        pm_panel.setLayout(pm_panelLayout);
        pm_panelLayout.setHorizontalGroup(
            pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
            .addGroup(pm_panelLayout.createSequentialGroup()
                .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pm_panelLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(pm_panelLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pm_panelLayout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pm_level, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pm_panelLayout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pm_pay, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(pm_edit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pm_add, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pm_panelLayout.setVerticalGroup(
            pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pm_panelLayout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(pm_panelLayout.createSequentialGroup()
                        .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pm_panelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pm_level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pm_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pm_pay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(pm_add)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pm_edit)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pm_panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
        );

        main.add(pm_panel, "card4");

        tt.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Period", "Pay", "DA", "HRA", "TRA", "Total"
            }
        ));
        jScrollPane4.setViewportView(tt);

        javax.swing.GroupLayout tableLayout = new javax.swing.GroupLayout(table);
        table.setLayout(tableLayout);
        tableLayout.setHorizontalGroup(
            tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
        );
        tableLayout.setVerticalGroup(
            tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tableLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        main.add(table, "card5");

        jLabel24.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Grade Pay");

        jLabel25.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Current Grade and their Pay");

        jLabel26.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Grade");

        jLabel27.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Add new Grade and Pay");

        jLabel28.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Pay");

        gp_add.setText("Add");
        gp_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gp_addActionPerformed(evt);
            }
        });

        gp_edit.setText("Edit");
        gp_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gp_editActionPerformed(evt);
            }
        });

        gp_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Level", "Pay"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        gp_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                gp_tableMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gp_tableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(gp_table);

        javax.swing.GroupLayout gpLayout = new javax.swing.GroupLayout(gp);
        gp.setLayout(gpLayout);
        gpLayout.setHorizontalGroup(
            gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gpLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(gpLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(gpLayout.createSequentialGroup()
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(grade, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(gpLayout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(gp_pay, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(gp_edit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(gp_add, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gpLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(gpLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        gpLayout.setVerticalGroup(
            gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gpLayout.createSequentialGroup()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gpLayout.createSequentialGroup()
                        .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel25))
                        .addGap(18, 18, 18)
                        .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(grade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(gpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(gp_pay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(gp_add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(gp_edit))
                    .addGroup(gpLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        main.add(gp, "card6");

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Designed and Developed by VFly Soft Solutions Contact +91 9408203201");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        settings.setText("Settings");

        home.setText("Dashboard");
        home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeActionPerformed(evt);
            }
        });
        settings.add(home);

        da.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        da.setText("DA");
        da.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daActionPerformed(evt);
            }
        });
        settings.add(da);

        paymatrix.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        paymatrix.setText("Pay Matrix");
        paymatrix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymatrixActionPerformed(evt);
            }
        });
        settings.add(paymatrix);

        set_hra.setText("HRA");
        set_hra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set_hraActionPerformed(evt);
            }
        });
        settings.add(set_hra);

        gradepay.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        gradepay.setText("Grade Pay");
        gradepay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradepayActionPerformed(evt);
            }
        });
        settings.add(gradepay);

        menu.add(settings);

        setJMenuBar(menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paymatrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymatrixActionPerformed
        main.removeAll();
        main.repaint();
        main.revalidate();
        main.add(pm_panel);
        main.repaint();
        main.revalidate();
        pm_table_refresh();
        pm_edit.setVisible(false);
    }//GEN-LAST:event_paymatrixActionPerformed

    private void homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeActionPerformed
        main.removeAll();
        main.repaint();
        main.revalidate();
        main.add(dashboard);
        main.repaint();
        main.revalidate();
    }//GEN-LAST:event_homeActionPerformed

    private void daActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_daActionPerformed
        main.removeAll();
        main.repaint();
        main.revalidate();
        main.add(da_panel);
        main.repaint();
        main.revalidate();
        da_init();
    }//GEN-LAST:event_daActionPerformed

    private void set_hraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_set_hraActionPerformed
        float hra = db.getHRA();
        String hra_per = JOptionPane.showInputDialog(null, "Enter HRA percentage. Current rate is " + hra + ".");
        if (hra_per != null && hra_per != "") {
            if (db.setHRA(Float.parseFloat(hra_per)) == 1) {
                JOptionPane.showMessageDialog(null, "HRA Updated.");
            }
        }
    }//GEN-LAST:event_set_hraActionPerformed

    private void da_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_da_addActionPerformed
        String month = da_month.getSelectedItem().toString();
        int year = Integer.parseInt(da_year.getSelectedItem().toString());
        float rate = Float.parseFloat(da_rate.getText());
        da_edit.setVisible(false);
        db.insertDA(month, year, rate);
        da_table_refresh();
        da_edit.setVisible(false);
    }//GEN-LAST:event_da_addActionPerformed

    private void da_tableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_da_tableMousePressed
        if (evt.getClickCount() == 2 && da_table.getSelectedColumnCount() != -1) {
            int row = da_table.getSelectedRow();
            String month = (String) da_table.getValueAt(row, 0);
            int year = (int) da_table.getValueAt(row, 1);
            float rate = (float) da_table.getValueAt(row, 2);
            da_month.setSelectedItem(month);
            da_month.setEnabled(false);
            da_year.setSelectedItem(year);
            da_year.setEnabled(false);
            da_rate.setText(String.valueOf(rate));
            da_edit.setVisible(true);
            da_add.setEnabled(false);
        }
    }//GEN-LAST:event_da_tableMousePressed

    private void da_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_da_tableMouseClicked
        if (evt.getButton() == 3) {
            Point p = evt.getPoint();
            int row = da_table.rowAtPoint(p);
            ListSelectionModel m = da_table.getSelectionModel();
            m.addSelectionInterval(row, row);
            da_delete.setText("Delete");
            da_table_popup.add(da_delete);
            da_table.add(da_table_popup);
            da_table_popup.show(da_table, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_da_tableMouseClicked

    private void da_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_da_deleteActionPerformed
        int r = da_table.getSelectedRow();
        DefaultTableModel m = (DefaultTableModel) da_table.getModel();
        if (db.removeDA(m.getValueAt(r, 0).toString(), (int) m.getValueAt(r, 1), (float) m.getValueAt(r, 2)) == 1) {
            da_table_refresh();
        } else {
            JOptionPane.showMessageDialog(null, "Something went wrong. Can't delete.");
        }
    }//GEN-LAST:event_da_deleteActionPerformed

    private void da_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_da_editActionPerformed
        String month = da_month.getSelectedItem().toString();
        int year = Integer.parseInt(da_year.getSelectedItem().toString());
        float rate = Float.parseFloat(da_rate.getText());
        if (db.updateDA(month, year, rate) == 1) {
            da_month.setEnabled(true);
            da_year.setEnabled(true);
            da_edit.setVisible(false);
            da_add.setEnabled(true);
            da_table_refresh();
        } else {
            System.out.println("in else");
        }
    }//GEN-LAST:event_da_editActionPerformed

    private void pm_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pm_addActionPerformed
        int r = db.addPM(pm_level.getText(), pm_pay.getText());
        if (r == 1) {
            pm_table_refresh();
        } else if (r == -1) {
            JOptionPane.showMessageDialog(null, "The level already exist. Please edit that level.");
        }
    }//GEN-LAST:event_pm_addActionPerformed

    private void pm_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pm_tableMouseClicked
        if (evt.getButton() == 3) {
            Point p = evt.getPoint();
            int row = pm_table.rowAtPoint(p);
            System.out.println(row);
            ListSelectionModel m = da_table.getSelectionModel();
            m.addSelectionInterval(row, row);
            pm_delete.setText("Delete");
            da_table_popup.add(pm_delete);
            pm_table.add(da_table_popup);
            da_table_popup.show(pm_table, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_pm_tableMouseClicked

    private void pm_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pm_deleteActionPerformed
        int r = pm_table.getSelectedRow();
        DefaultTableModel m = (DefaultTableModel) pm_table.getModel();
        if (db.removePM(m.getValueAt(r, 0).toString()) == 1) {
            pm_table_refresh();
        } else {
            JOptionPane.showMessageDialog(null, "Something went wrong. Can't delete.");
        }
    }//GEN-LAST:event_pm_deleteActionPerformed

    private void pm_tableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pm_tableMousePressed
        if (evt.getClickCount() == 2 && pm_table.getSelectedColumnCount() != -1) {
            int row = pm_table.getSelectedRow();
            String level = pm_table.getValueAt(row, 0).toString();
            String pay = pm_table.getValueAt(row, 1).toString();
            pm_pay.setText(String.valueOf(pay));
            pm_level.setText(level);
            pm_level.setEnabled(false);
            pm_edit.setVisible(true);
            pm_add.setEnabled(false);
        }
    }//GEN-LAST:event_pm_tableMousePressed

    private void pm_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pm_editActionPerformed
        String level = pm_level.getText();
        String pay = pm_pay.getText();
        if (db.updatePM(level, pay) == 1) {
            pm_level.setEnabled(true);
            pm_level.setText("");
            pm_pay.setText("");
            pm_add.setEnabled(true);
            pm_edit.setVisible(false);
            pm_table_refresh();
        }
    }//GEN-LAST:event_pm_editActionPerformed

    private void gp_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gp_deleteActionPerformed
        int r = gp_table.getSelectedRow();
        DefaultTableModel m = (DefaultTableModel) gp_table.getModel();
        if (db.removeGP(m.getValueAt(r, 0).toString()) == 1) {
            gp_table_refresh();
        } else {
            JOptionPane.showMessageDialog(null, "Something went wrong. Can't delete.");
        }
    }//GEN-LAST:event_gp_deleteActionPerformed

    private void gradepayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradepayActionPerformed
        main.removeAll();
        main.repaint();
        main.revalidate();
        main.add(gp);
        main.repaint();
        main.revalidate();
        gp_table_refresh();
        gp_edit.setVisible(false);
    }//GEN-LAST:event_gradepayActionPerformed

    private void gp_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gp_tableMouseClicked
        if (evt.getButton() == 3) {
            Point p = evt.getPoint();
            int row = gp_table.rowAtPoint(p);
            System.out.println(row);
            ListSelectionModel m = gp_table.getSelectionModel();
            m.addSelectionInterval(row, row);
            gp_delete.setText("Delete");
            da_table_popup.add(gp_delete);
            gp_table.add(da_table_popup);
            da_table_popup.show(gp_table, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_gp_tableMouseClicked

    private void gp_tableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gp_tableMousePressed
        if (evt.getClickCount() == 2 && pm_table.getSelectedColumnCount() != -1) {
            int row = gp_table.getSelectedRow();
            String g = gp_table.getValueAt(row, 0).toString();
            String pay = gp_table.getValueAt(row, 1).toString();
            gp_pay.setText(String.valueOf(pay));
            grade.setText(g);
            grade.setEnabled(false);
            gp_edit.setVisible(true);
            gp_add.setEnabled(false);
        }
    }//GEN-LAST:event_gp_tableMousePressed

    private void gp_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gp_editActionPerformed
        String g = grade.getText();
        String pay = gp_pay.getText();
        if (db.updateGP(g, pay) == 1) {
            grade.setEnabled(true);
            grade.setText("");
            gp_pay.setText("");
            gp_add.setEnabled(true);
            gp_edit.setVisible(false);
            gp_table_refresh();
        }
    }//GEN-LAST:event_gp_editActionPerformed

    private void gp_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gp_addActionPerformed
        int r = db.addGP(grade.getText(), gp_pay.getText());
        switch (r) {
            case 1:
                gp_table_refresh();
                break;
            case -1:
                JOptionPane.showMessageDialog(null, "The level already exist. Please edit that level.");
                break;
            default:
                JOptionPane.showMessageDialog(null, "Something went wrong. Please contact admin.");
                break;
        }
    }//GEN-LAST:event_gp_addActionPerformed

    private void tyearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tyearActionPerformed
        if (Integer.parseInt(tyear.getSelectedItem().toString()) > 2015) {
            tbpgp.setEnabled(false);
            pgp.setEnabled(false);
        } else {
            tbpgp.setEnabled(true);
            pgp.setEnabled(true);
        }
    }//GEN-LAST:event_tyearActionPerformed

    private void generateTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateTableActionPerformed
        String fy = fyear.getSelectedItem().toString();
        String ty = tyear.getSelectedItem().toString();
        String fm = fmonth.getSelectedItem().toString();
        String tm = tmonth.getSelectedItem().toString();
        String fd = fday.getSelectedItem().toString();
        float safe_da_rate = 0;

        long tbp_basic = Long.parseLong(tbpb.getText());
        long p_basic = Long.parseLong(pb.getText());
        long n_tbp_basic = tbp_basic;
        long n_p_basic = p_basic;

//        DefaultTableModel t = (DefaultTableModel) tt.getModel();
        List<Detail> detail_array = new ArrayList<>();

        Headers h = new Headers();
        h.setSchoolName(sName.getText());
        h.setEmpName(eName.getText());
        h.setDesignation(designation.getText());
        h.setDetailLine(detailLine.getText());
        h.setLevel(level.getSelectedItem().toString());

        if (Integer.parseInt(ty) < 2016) {
            System.out.println(db.getGP(level.getSelectedItem().toString()));
            h.setPay(db.getGP(level.getSelectedItem().toString()));
        } else {
            System.out.println(db.getPM(level.getSelectedItem().toString()));
            h.setPay(db.getPM(level.getSelectedItem().toString()));
        }

        for (int year = Integer.parseInt(fy); year <= Integer.parseInt(ty); year++) {
            System.out.println(year);
            for (int month = 1; month < 13; month++) {
                String tbpda;
                String pda;
                float month_da_rate;
                if (Integer.parseInt(ty) > 2016) {
                    if (Integer.parseInt(fy) == year && month < month_map.get(fm)) {
                        continue;
                    }
                    if (!"1".equals(fd) && month == month_map.get(fm) && year == Integer.parseInt(fy)) {
//                        basic calculation for days
                        long t_tbp_basic = (Math.round(tbp_basic / (float) month_day.get(month)) * (month_day.get(month) - Integer.valueOf(fd) + 1));
                        long t_p_basic = (Math.round(p_basic / (float) month_day.get(month)) * (month_day.get(month) - Integer.valueOf(fd) + 1));
                        if (month == month_map.get(tm) + 1 && year == Integer.parseInt(ty)) {
                            break;
                        }

                        if (month < 7) {
                            try {
                                month_da_rate = db.getDA("Jan", year);
                                if (month_da_rate >= 0) {
                                    safe_da_rate = month_da_rate;
                                }
                            } catch (Exception e) {
                                System.out.println("catch");
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                month_da_rate = db.getDA("Jul", year);
                                if (month_da_rate >= 0) {
                                    safe_da_rate = month_da_rate;
                                }
                            } catch (Exception e) {
                                System.out.println("catch");
                            }
                        }

                        tbpda = String.valueOf((int) (t_tbp_basic * safe_da_rate) / 100);
                        pda = String.valueOf((int) (t_p_basic * safe_da_rate) / 100);
                        String tbphra;
                        String phra;
                        if (hra.isSelected()) {
                            tbphra = calculateHRA(t_tbp_basic);
                            phra = calculateHRA(t_p_basic);
                        } else {
                            tbphra = hratbp.getText();
                            phra = hrap.getText();
                        }
                        Detail e = new Detail();
                        e.setPeriod(fd + "-" + map_month.get(month) + "-" + String.valueOf(year).substring(2));
                        e.setTbp_basic((int) t_tbp_basic);
                        e.setTbp_da(Integer.parseInt(tbpda));
                        e.setTbp_hra(Integer.parseInt(tbphra));
                        e.setTbp_tra(Integer.parseInt(tratbp.getText()));
                        e.setTbp_total((int) t_tbp_basic + Integer.parseInt(tbpda) + Integer.parseInt(tbphra) + Integer.parseInt(tratbp.getText()));
                        e.setP_basic((int) t_p_basic);
                        e.setP_da(Integer.parseInt(pda));
                        e.setP_hra(Integer.parseInt(phra));
                        e.setP_tra(Integer.parseInt(trap.getText()));
                        e.setP_total((int) t_p_basic + Integer.parseInt(pda) + Integer.parseInt(phra) + Integer.parseInt(trap.getText()));
                        e.setD_basic(e.getTbp_basic() - e.getP_basic());
                        e.setD_da(e.getTbp_da() - e.getP_da());
                        e.setD_hra(e.getTbp_hra() - e.getP_hra());
                        e.setD_tra(e.getTbp_tra() - e.getP_tra());
                        e.setD_total(e.getTbp_total() - e.getP_total());

                        detail_array.add(e);
                    } else {
                        if (month == month_map.get(tm) + 1 && year == Integer.parseInt(ty)) {
                            break;
                        }

                        if (month == 7) {
                            n_tbp_basic = ((((tbp_basic * 3) / 100 + tbp_basic) + 99) / 100) * 100;
                            tbp_basic = n_tbp_basic;
                            n_p_basic = ((((p_basic * 3) / 100 + p_basic) + 99) / 100) * 100;
                            p_basic = n_p_basic;
                        }

                        if (month < 7) {
                            try {
                                month_da_rate = db.getDA("Jan", year);
                                if (month_da_rate >= 0) {
                                    safe_da_rate = month_da_rate;
                                }
                            } catch (Exception e) {
                                System.out.println("catch");
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                month_da_rate = db.getDA("Jul", year);
                                if (month_da_rate >= 0) {
                                    safe_da_rate = month_da_rate;
                                }
                            } catch (Exception e) {
                                System.out.println("catch");
                            }
                        }

                        tbpda = String.valueOf((int) (n_tbp_basic * safe_da_rate) / 100);
                        pda = String.valueOf((int) (n_p_basic * safe_da_rate) / 100);
                        String tbphra;
                        String phra;
                        if (hra.isSelected()) {
                            tbphra = calculateHRA(n_tbp_basic);
                            phra = calculateHRA(n_p_basic);
                        } else {
                            tbphra = hratbp.getText();
                            phra = hrap.getText();
                        }

                        Detail e = new Detail();
                        e.setPeriod(map_month.get(month) + "-" + String.valueOf(year).substring(2));
                        e.setTbp_basic((int) n_tbp_basic);
                        e.setTbp_da(Integer.valueOf(tbpda));
                        e.setTbp_hra(Integer.valueOf(tbphra));
                        e.setTbp_tra(Integer.parseInt(tratbp.getText()));
                        e.setTbp_total((int) n_tbp_basic + Integer.parseInt(tbpda) + Integer.parseInt(tbphra) + Integer.parseInt(tratbp.getText()));
                        e.setP_basic((int) n_p_basic);
                        e.setP_da(Integer.parseInt(pda));
                        e.setP_hra(Integer.parseInt(phra));
                        e.setP_tra(Integer.parseInt(trap.getText()));
                        e.setP_total((int) n_p_basic + Integer.parseInt(pda) + Integer.parseInt(phra) + Integer.parseInt(trap.getText()));
                        e.setD_basic(e.getTbp_basic() - e.getP_basic());
                        e.setD_da(e.getTbp_da() - e.getP_da());
                        e.setD_hra(e.getTbp_hra() - e.getP_hra());
                        e.setD_tra(e.getTbp_tra() - e.getP_tra());
                        e.setD_total(e.getTbp_total() - e.getP_total());

                        detail_array.add(e);

                    }
                } else {
                    // grade pay calculation part
                    if (Integer.parseInt(fy) == year && month < month_map.get(fm)) {
                        continue;
                    }
                    if (!"1".equals(fd) && month == month_map.get(fm) && year == Integer.parseInt(fy)) {
//                        basic calculation for days
                        long t_tbp_basic = Math.round(tbp_basic / (float) month_day.get(month)) * (month_day.get(month) - Integer.valueOf(fd));
                        long t_p_basic = Math.round(p_basic / (float) month_day.get(month)) * (month_day.get(month) - Integer.valueOf(fd) - 1);
                        if (month == month_map.get(tm) + 1 && year == Integer.parseInt(ty)) {
                            break;
                        }

                        if (month < 7) {
                            try {
                                month_da_rate = db.getDA("Jan", year);
                                if (month_da_rate >= 0) {
                                    safe_da_rate = month_da_rate;
                                }
                            } catch (Exception e) {
                                System.out.println("catch");
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                month_da_rate = db.getDA("Jul", year);
                                if (month_da_rate >= 0) {
                                    safe_da_rate = month_da_rate;
                                }
                            } catch (Exception e) {
                                System.out.println("catch");
                            }
                        }
                        String p_gp = pgp.getText();
                        String tbp_gp = tbpgp.getText();
                        tbpda = String.valueOf((int) ((t_tbp_basic + Integer.valueOf(tbp_gp)) * safe_da_rate) / 100);
                        pda = String.valueOf((int) ((t_p_basic + Integer.valueOf(p_gp)) * safe_da_rate) / 100);
                        String tbphra;
                        String phra;

                        if (hra.isSelected()) {
                            tbphra = calculateHRA(t_tbp_basic + Integer.valueOf(tbp_gp));
                            phra = calculateHRA(t_p_basic + Integer.valueOf(tbp_gp));
                        } else {
                            tbphra = hratbp.getText();
                            phra = hrap.getText();
                        }

                        Detail e = new Detail();
                        e.setPeriod(fd + "-" + map_month.get(month) + "-" + String.valueOf(year).substring(2));
                        e.setTbp_basic((int) t_tbp_basic);
                        e.setTbp_da(Integer.parseInt(tbpda));
                        e.setTbp_gp(Integer.parseInt(tbpgp.getText()));
                        e.setTbp_hra(Integer.parseInt(tbphra));
                        e.setTbp_tra(Integer.parseInt(tratbp.getText()));
                        e.setTbp_total((int) t_tbp_basic + Integer.parseInt(tbpda) + Integer.parseInt(tbphra) + Integer.parseInt(tratbp.getText()));
                        e.setP_basic((int) t_p_basic);
                        e.setP_da(Integer.parseInt(pda));
                        e.setP_gp(Integer.parseInt(pgp.getText()));
                        e.setP_hra(Integer.parseInt(phra));
                        e.setP_tra(Integer.parseInt(trap.getText()));
                        e.setP_total((int) t_p_basic + Integer.parseInt(pda) + Integer.parseInt(phra) + Integer.parseInt(trap.getText()));
                        e.setD_basic(e.getTbp_basic() - e.getP_basic());
                        e.setD_da(e.getTbp_da() - e.getP_da());
                        e.setD_gp(e.getTbp_gp() - e.getP_gp());
                        e.setD_hra(e.getTbp_hra() - e.getP_hra());
                        e.setD_tra(e.getTbp_tra() - e.getP_tra());
                        e.setD_total(e.getTbp_total() - e.getP_total());

                        detail_array.add(e);
                    } else {
                        if (month == month_map.get(tm) + 1 && year == Integer.parseInt(ty)) {
                            break;
                        }

                        if (month == 7) {
                            n_tbp_basic = ((((tbp_basic * 3) / 100 + tbp_basic) + 9) / 10) * 10;
                            tbp_basic = n_tbp_basic;
                            n_p_basic = ((((p_basic * 3) / 100 + p_basic) + 9) / 10) * 10;
                            p_basic = n_p_basic;
                        }

                        if (month < 7) {
                            try {
                                month_da_rate = db.getDA("Jan", year);
                                if (month_da_rate >= 0) {
                                    safe_da_rate = month_da_rate;
                                }
                            } catch (Exception e) {
                                System.out.println("catch");
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                month_da_rate = db.getDA("Jul", year);
                                if (month_da_rate >= 0) {
                                    safe_da_rate = month_da_rate;
                                }
                            } catch (Exception e) {
                                System.out.println("catch");
                            }
                        }
                        String p_gp = pgp.getText();
                        String tbp_gp = tbpgp.getText();
                        tbpda = String.valueOf((int) ((n_tbp_basic + Integer.valueOf(tbp_gp)) * safe_da_rate) / 100);
                        pda = String.valueOf((int) ((n_p_basic + Integer.valueOf(p_gp)) * safe_da_rate) / 100);
                        String tbphra;
                        String phra;
                        if (hra.isSelected()) {
                            tbphra = calculateHRA(n_tbp_basic + Integer.valueOf(tbp_gp));
                            phra = calculateHRA(n_p_basic + Integer.valueOf(p_gp));
                        } else {
                            tbphra = hratbp.getText();
                            phra = hrap.getText();
                        }

                        Detail e = new Detail();
                        e.setPeriod(map_month.get(month) + "-" + String.valueOf(year).substring(2));
                        e.setTbp_basic((int) n_tbp_basic);
                        e.setTbp_da(Integer.valueOf(tbpda));
                        e.setTbp_gp(Integer.parseInt(tbpgp.getText()));
                        e.setTbp_hra(Integer.valueOf(tbphra));
                        e.setTbp_tra(Integer.parseInt(tratbp.getText()));
                        e.setTbp_total((int) n_tbp_basic + Integer.parseInt(tbpda) + Integer.parseInt(tbphra) + Integer.parseInt(tratbp.getText()));
                        e.setP_basic((int) n_p_basic);
                        e.setP_da(Integer.parseInt(pda));
                        e.setP_gp(Integer.parseInt(pgp.getText()));
                        e.setP_hra(Integer.parseInt(phra));
                        e.setP_tra(Integer.parseInt(trap.getText()));
                        e.setP_total((int) n_p_basic + Integer.parseInt(pda) + Integer.parseInt(phra) + Integer.parseInt(trap.getText()));
                        e.setD_basic(e.getTbp_basic() - e.getP_basic());
                        e.setD_da(e.getTbp_da() - e.getP_da());
                        e.setD_gp(e.getTbp_gp() - e.getP_gp());
                        e.setD_hra(e.getTbp_hra() - e.getP_hra());
                        e.setD_tra(e.getTbp_tra() - e.getP_tra());
                        e.setD_total(e.getTbp_total() - e.getP_total());

                        detail_array.add(e);
                    }
                }
            }
        }
//        detail_array.forEach((d) -> {
//            t.addRow(new Object[]{d.getPeriod(), d.getTbp_basic(), d.getTbp_da(), d.getTbp_hra(), d.getTbp_tra(), d.getTbp_total()});
//        });
        try {
            InputStream is;
            boolean checkbox = showhra.isSelected();
            if (Integer.parseInt(ty) > 2016) {
                if (checkbox) {
                    is = new java.io.FileInputStream(new File("template/template.xls"));

                } else {
                    is = new java.io.FileInputStream(new File("template/template1.xls"));

                }
            } else {
                if (checkbox) {
                    is = new java.io.FileInputStream(new File("template/template2.xls"));
                } else {
                    is = new java.io.FileInputStream(new File("template/template3.xls"));
                }
            }
            OutputStream os = new FileOutputStream(eName.getText() + ".xls");
            Context c = new Context();
            c.putVar("detail_array", detail_array);
            c.putVar("headers", h);
            JxlsHelper.getInstance().processTemplate(is, os, c);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }

//        main.removeAll();
//        main.repaint();
//        main.revalidate();
//        main.add(table);
//        main.repaint();
//        main.revalidate();
    }//GEN-LAST:event_generateTableActionPerformed

    private void hraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hraActionPerformed
        if (hra.isSelected()) {
            hratbp.setEnabled(false);
            hrap.setEnabled(false);
        } else {
            hratbp.setEnabled(true);
            hrap.setEnabled(true);
        }
    }//GEN-LAST:event_hraActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem da;
    private javax.swing.JButton da_add;
    private javax.swing.JMenuItem da_delete;
    private javax.swing.JButton da_edit;
    private javax.swing.JComboBox<String> da_month;
    private javax.swing.JPanel da_panel;
    private javax.swing.JTextField da_rate;
    private javax.swing.JTable da_table;
    private javax.swing.JPopupMenu da_table_popup;
    private javax.swing.JComboBox<String> da_year;
    private javax.swing.JPanel dashboard;
    private javax.swing.JTextField designation;
    private javax.swing.JTextArea detailLine;
    private javax.swing.JTextField eName;
    private javax.swing.JComboBox<String> fday;
    private javax.swing.JComboBox<String> fmonth;
    private javax.swing.JComboBox<String> fyear;
    private javax.swing.JButton generateTable;
    private javax.swing.JPanel gp;
    private javax.swing.JButton gp_add;
    private javax.swing.JMenuItem gp_delete;
    private javax.swing.JButton gp_edit;
    private javax.swing.JTextField gp_pay;
    private javax.swing.JTable gp_table;
    private javax.swing.JTextField grade;
    private javax.swing.JMenuItem gradepay;
    private javax.swing.JMenuItem home;
    private javax.swing.JCheckBox hra;
    private javax.swing.JTextField hrap;
    private javax.swing.JTextField hratbp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JComboBox<String> level;
    private javax.swing.JPanel main;
    private javax.swing.JMenuBar menu;
    private javax.swing.JMenuItem paymatrix;
    private javax.swing.JTextField pb;
    private javax.swing.JTextField pgp;
    private javax.swing.JButton pm_add;
    private javax.swing.JMenuItem pm_delete;
    private javax.swing.JButton pm_edit;
    private javax.swing.JTextField pm_level;
    private javax.swing.JPanel pm_panel;
    private javax.swing.JTextField pm_pay;
    private javax.swing.JTable pm_table;
    private javax.swing.JTextField sName;
    private javax.swing.JMenuItem set_hra;
    private javax.swing.JMenu settings;
    private javax.swing.JCheckBox showhra;
    private javax.swing.JPanel table;
    private javax.swing.JTextField tbpb;
    private javax.swing.JTextField tbpgp;
    private javax.swing.JComboBox<String> tday;
    private javax.swing.JComboBox<String> tmonth;
    private javax.swing.JTextField trap;
    private javax.swing.JTextField tratbp;
    private javax.swing.JTable tt;
    private javax.swing.JComboBox<String> tyear;
    // End of variables declaration//GEN-END:variables
}
