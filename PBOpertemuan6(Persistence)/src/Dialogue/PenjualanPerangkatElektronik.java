/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Dialogue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Logger;

/**
 *
 * @author LEGION
 */
public class PenjualanPerangkatElektronik extends javax.swing.JFrame {

    private EntityManagerFactory emf;
    private EntityManager em;
    private static final Logger logger = Logger.getLogger(PenjualanPerangkatElektronik.class.getName());

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/PBO_Praktikum_5";
    private static final String USER = "postgres";
    private static final String PASS = "0000";

    public PenjualanPerangkatElektronik() {
        initComponents();
        connectToDatabase();
        loadDataToTable();
        setupTableSelectionListener();
    }

    private void connectToDatabase() {
        try {
            emf = Persistence.createEntityManagerFactory("PBOpertemuan6PU");
            em = emf.createEntityManager();
            System.out.println("Koneksi database berhasil (JPA)!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal terhubung ke database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadDataToTable() {
        try {
            if (em != null && em.isOpen()) {
                em.clear();
            }

            String query = "SELECT p FROM PenjualanPerangkatElektronik_1 p";
            TypedQuery<PenjualanPerangkatElektronik_1> typedQuery = em.createQuery(query, PenjualanPerangkatElektronik_1.class);
            java.util.List<PenjualanPerangkatElektronik_1> dataList = typedQuery.getResultList();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            model.addColumn("nomor_seri");
            model.addColumn("jenis_perangkat");
            model.addColumn("merek_perangkat");
            model.addColumn("nama_perangkat");
            model.addColumn("model_perangkat");

            model.setRowCount(0);

            for (PenjualanPerangkatElektronik_1 data : dataList) {
                Object[] row = {
                    data.getNomorSeri(),
                    data.getJenisPerangkat(),
                    data.getMerekPerangkat(),
                    data.getNamaPerangkat(),
                    data.getModelPerangkat()
                };
                model.addRow(row);
            }

            jTable1.setModel(model);

            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < model.getColumnCount(); i++) {
                jTable1.getColumnModel().getColumn(i).setPreferredWidth(150);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchData() {
        String keyword = JOptionPane.showInputDialog(this, "Masukkan kata kunci pencarian:");

        if (keyword == null) {
            return;
        }
        keyword = keyword.trim();

        try {
            if (keyword.isEmpty()) {
                loadDataToTable();
                return;
            }

            String query = "SELECT p FROM PenjualanPerangkatElektronik_1 p WHERE "
                    + "LOWER(p.nomorSeri) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.jenisPerangkat) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.merekPerangkat) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.namaPerangkat) LIKE LOWER(:keyword) OR "
                    + "LOWER(p.modelPerangkat) LIKE LOWER(:keyword)";

            TypedQuery<PenjualanPerangkatElektronik_1> typedQuery = em.createQuery(query, PenjualanPerangkatElektronik_1.class);
            typedQuery.setParameter("keyword", "%" + keyword + "%");

            java.util.List<PenjualanPerangkatElektronik_1> searchResults = typedQuery.getResultList();

            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("nomor_seri");
            model.addColumn("jenis_perangkat");
            model.addColumn("merek_perangkat");
            model.addColumn("nama_perangkat");
            model.addColumn("model_perangkat");

            for (PenjualanPerangkatElektronik_1 data : searchResults) {
                Object[] row = {
                    data.getNomorSeri(),
                    data.getJenisPerangkat(),
                    data.getMerekPerangkat(),
                    data.getNamaPerangkat(),
                    data.getModelPerangkat()
                };
                model.addRow(row);
            }

            jTable1.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencari data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isNomorSeriExists(String nomorSeri) {
        try {
            String query = "SELECT COUNT(p) FROM PenjualanPerangkatElektronik_1 p WHERE p.nomorSeri = :nomorSeri";
            TypedQuery<Long> typedQuery = em.createQuery(query, Long.class);
            typedQuery.setParameter("nomorSeri", nomorSeri);
            return typedQuery.getSingleResult() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int processCSVFile(java.io.File csvFile) {
        int recordsProcessed = 0;
        int recordsSkipped = 0;

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(csvFile))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().contains("nomor") || line.toLowerCase().contains("seri")) {
                        continue;
                    }
                }

                String[] data = line.split(",");

                if (data.length >= 5) {
                    String nomorSeri = data[0].trim();
                    String jenis = data[1].trim();
                    String merek = data[2].trim();
                    String nama = data[3].trim();
                    String model = data[4].trim();

                    if (!isNomorSeriExists(nomorSeri)) {
                        PenjualanPerangkatElektronik_1 newData = new PenjualanPerangkatElektronik_1();
                        newData.setNomorSeri(nomorSeri);
                        newData.setJenisPerangkat(jenis);
                        newData.setMerekPerangkat(merek);
                        newData.setNamaPerangkat(nama);
                        newData.setModelPerangkat(model);
                        newData.setWarna("Default");
                        newData.setTahunRilis(2023);
                        newData.setHarga(java.math.BigInteger.valueOf(0L));
                        newData.setStok(0);

                        em.getTransaction().begin();
                        em.persist(newData);
                        em.getTransaction().commit();

                        recordsProcessed++;
                    } else {
                        System.out.println("Nomor seri " + nomorSeri + " sudah ada, dilewati.");
                        recordsSkipped++;
                    }
                }
            }

            System.out.println("Upload selesai: " + recordsProcessed + " record diproses, " + recordsSkipped + " record dilewati.");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(this,
                    "Error memproses file CSV: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return recordsProcessed;
    }

    private void cetakLaporan() {
        try {
            if (!CompileJRXML.fileJasperAda()) {
                int pilihan = JOptionPane.showConfirmDialog(this,
                        "File laporan perlu dicompile terlebih dahulu.\nCompile sekarang?",
                        "Compile Laporan",
                        JOptionPane.YES_NO_OPTION);

                if (pilihan == JOptionPane.YES_OPTION) {
                    boolean compileBerhasil = CompileJRXML.compileLaporanWithDialog(this);
                    if (!compileBerhasil) {
                        return;
                    }
                } else {
                    return;
                }
            }

            String jasperPath = "laporan_perangkat.jasper";
            java.io.File jasperFile = new java.io.File(jasperPath);

            if (!jasperFile.exists()) {
                JOptionPane.showMessageDialog(this,
                        "File laporan tidak ditemukan: " + jasperPath,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("Membuat laporan dari: " + jasperPath);

            java.util.Map<String, Object> parameters = new java.util.HashMap<>();
            parameters.put("REPORT_TITLE", "LAPORAN PERANGKAT ELEKTRONIK");

            Connection jdbcConn = DriverManager.getConnection(DB_URL, USER, PASS);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperPath,
                    parameters,
                    jdbcConn
            );

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("Laporan Perangkat Elektronik - ElectroShop");
            viewer.setVisible(true);
            jdbcConn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saat mencetak laporan: " + e.getMessage()
                    + "\n\nPastikan:\n• File laporan_perangkat.jasper ada\n• Database sedang berjalan",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        try {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    private void setupTableSelectionListener() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = jTable1.getSelectedRow();
                if (row >= 0) {
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowActivated(java.awt.event.WindowEvent e) {
            }
        });
    }

    private void refreshTable() {
        try {
            if (em != null && em.isOpen() && !em.getTransaction().isActive()) {
                em.clear();
            }

            loadDataToTable();

            JOptionPane.showMessageDialog(this, "Tabel telah di-refresh dengan data terbaru!",
                    "Refresh Berhasil", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saat refresh: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void showInsertDialog() {
        Dialog dialog = new Dialog();
        dialog.setOperation("INSERT");
        dialog.setFieldValues("", "", "", "", "");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            refreshTable();
        }
    }

    private void showUpdateDialog() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diupdate terlebih dahulu!");
            return;
        }

        String nomorSeri = jTable1.getValueAt(selectedRow, 0).toString();
        String jenis = jTable1.getValueAt(selectedRow, 1).toString();
        String merek = jTable1.getValueAt(selectedRow, 2).toString();
        String nama = jTable1.getValueAt(selectedRow, 3).toString();
        String model = jTable1.getValueAt(selectedRow, 4).toString();

        Dialog dialog = new Dialog();
        dialog.setOperation("UPDATE");
        dialog.setFieldValues(nomorSeri, jenis, merek, nama, model);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            refreshTable();
        }
    }

    private void showDeleteDialog() {
        int selectedRow = jTable1.getSelectedRow();
        String nomorSeri = "";

        if (selectedRow != -1) {
            nomorSeri = jTable1.getValueAt(selectedRow, 0).toString();
        } else {
            nomorSeri = JOptionPane.showInputDialog(this, "Masukkan nomor seri yang akan dihapus:");
            if (nomorSeri == null || nomorSeri.trim().isEmpty()) {
                return;
            }
        }

        Dialog dialog = new Dialog();
        dialog.setOperation("DELETE");
        dialog.setFieldValues(nomorSeri, "", "", "", "");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            refreshTable();
        }
    }

    private void clearForm() {
        refreshTable();
    }

    private void uploadCSV() {
        try {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Pilih File CSV");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

            int result = fileChooser.showOpenDialog(this);

            if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File selectedFile = fileChooser.getSelectedFile();
                System.out.println("File dipilih: " + selectedFile.getAbsolutePath());

                int recordsProcessed = processCSVFile(selectedFile);

                if (recordsProcessed > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Berhasil mengupload " + recordsProcessed + " record dari file CSV!",
                            "Upload Berhasil",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Tidak ada data yang berhasil diupload.",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saat upload CSV: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 204, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "title 5"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnRefresh.setText("Refresh");
        btnRefresh.setMaximumSize(new java.awt.Dimension(70, 25));
        btnRefresh.setMinimumSize(new java.awt.Dimension(70, 25));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnInsert.setText("Insert");
        btnInsert.setMaximumSize(new java.awt.Dimension(70, 25));
        btnInsert.setMinimumSize(new java.awt.Dimension(70, 25));
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.setMaximumSize(new java.awt.Dimension(70, 25));
        btnUpdate.setMinimumSize(new java.awt.Dimension(70, 25));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(70, 25));
        btnDelete.setMinimumSize(new java.awt.Dimension(70, 25));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel1.setText("DATA PENJUALAN PERANGKAT");

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnCetak.setText("Cetak");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(307, 307, 307)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch)
                .addGap(103, 103, 103)
                .addComponent(btnCetak)
                .addGap(18, 18, 18)
                .addComponent(btnUpload)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDelete, btnInsert, btnUpdate});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCetak)
                    .addComponent(btnUpload))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnDelete, btnInsert, btnRefresh, btnUpdate});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        showDeleteDialog();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        showUpdateDialog();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        showInsertDialog();
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        searchData();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        // TODO add your handling code here:
        cetakLaporan();
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        // TODO add your handling code here:
        uploadCSV();
    }//GEN-LAST:event_btnUploadActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new PenjualanPerangkatElektronik().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnUpload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
