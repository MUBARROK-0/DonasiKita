/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author rizqi mubarrok
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import javax.swing.table.DefaultTableModel;
import java.sql.Statement;
import java.sql.Date;
import java.text.DecimalFormat;

public class DonasiForm extends javax.swing.JPanel {

    /**
     * Creates new form DonasiForm
     */
    public DonasiForm() {
        initComponents();
        loadTable();
    }
    
   private void loadTable() {
    // Set kolom untuk JTable
    String[] columnNames = {"ID Donasi", "ID Donatur", "ID Kampanye", "Jumlah", "Tanggal"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Inisialisasi DefaultTableModel
    DecimalFormat df = new DecimalFormat("#,###.###");

    try {
        // Gunakan DatabaseConnection untuk mendapatkan koneksi
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            String query = "SELECT id_donasi, id_donatur, id_kampanye, jumlah, tanggal FROM donasi"; // Query untuk mengambil semua data dari tabel `donasi`
            
            // Gunakan try-with-resources untuk Statement dan ResultSet
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                // Loop melalui hasil query dan tambahkan ke model JTable
                while (rs.next()) {
                    int idDonasi = rs.getInt("id_donasi");
                    int idDonatur = rs.getInt("id_donatur");
                    int idKampanye = rs.getInt("id_kampanye");
                    double jumlah = rs.getDouble("jumlah");
                    String tanggal = rs.getDate("tanggal").toString(); // Ambil tanggal dalam format String
                    
                    String formattedjumlah = df.format(jumlah);

                    // Tambahkan data sebagai baris ke DefaultTableModel
                    Object[] row = {idDonasi, idDonatur, idKampanye, formattedjumlah, tanggal};
                    model.addRow(row);
                }
            }

            // Set model JTable
            TabelDonasi.setModel(model);
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Data tidak dapat dimuat.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace(); // Debugging jika ada error
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
   
   private void updateDanaTerkumpul(int idKampanye, double jumlah) {
    try {
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            String query = "UPDATE kampanye SET total_dana_terkumpul = total_dana_terkumpul + ? WHERE id_kampanye = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDouble(1, jumlah);
            pstmt.setInt(2, idKampanye);

            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal memperbarui dana terkumpul: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
   
   private void updateTotalTerkumpul(int idKampanye, double jumlahDonasi) {
    try {
        Connection conn = DatabaseConnection.connect();
        String query = "UPDATE pelacakan_dana SET total_terkumpul = total_terkumpul + ? WHERE id_kampanye = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setDouble(1, jumlahDonasi);
        pstmt.setInt(2, idKampanye);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal memperbarui total terkumpul: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
    private void addDonation() {
    try {
        // Dapatkan koneksi ke database
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            // Query SQL untuk menambahkan data ke tabel donasi
            String query = "INSERT INTO donasi (id_donatur, id_kampanye, jumlah, tanggal) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Ambil input dari JTextField
            int idDonatur = Integer.parseInt(jTextFieldDonatur.getText());
            int idKampanye = Integer.parseInt(jTextFieldKampanye.getText());
            double jumlah = Double.parseDouble(jTextFieldJumlah.getText());
            String tanggal = jTextFieldTanggal.getText().trim();

            // Set parameter query
            pstmt.setInt(1, idDonatur);
            pstmt.setInt(2, idKampanye);
            pstmt.setDouble(3, jumlah);

            // Periksa apakah input tanggal kosong
            if (tanggal.isEmpty()) {
                pstmt.setNull(4, java.sql.Types.DATE); // Gunakan nilai default dari tabel
            } else {
                pstmt.setDate(4, java.sql.Date.valueOf(tanggal)); // Gunakan input pengguna
            }

            // Eksekusi query
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                updateDanaTerkumpul(idKampanye, jumlah);
                updateTotalTerkumpul(idKampanye, jumlah);

                // Muat ulang tabel kampanye untuk memperbarui tampilan
                loadTable();
                JOptionPane.showMessageDialog(this, "Donasi berhasil ditambahkan!");
            }

            // Tutup koneksi
            pstmt.close();
            conn.close();

            // Perbarui JTable
            loadTable();

            // Kosongkan input
            jTextFieldDonatur.setText("");
            jTextFieldKampanye.setText("");
            jTextFieldJumlah.setText("");
            jTextFieldTanggal.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Donasi tidak dapat disimpan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Input tidak valid! Pastikan ID dan jumlah berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menambahkan donasi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jTextField7 = new javax.swing.JTextField();
        jTextFieldDonatur = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jTextFieldKampanye = new javax.swing.JTextField();
        jTextFieldJumlah = new javax.swing.JTextField();
        jTextFieldTanggal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelDonasi = new javax.swing.JTable();

        jTextField7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextField7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Donatur", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1000, 670));

        jTextFieldDonatur.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldDonatur.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Donatur", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jButton1.setBackground(new java.awt.Color(0, 93, 253));
        jButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Masukan");
        jButton1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 93, 253), new java.awt.Color(0, 93, 253)), javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 93, 253), new java.awt.Color(0, 93, 253))));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextFieldKampanye.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldKampanye.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kampanye", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldJumlah.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldJumlah.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Jumlah Donasi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldTanggal.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldTanggal.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tanggal Donasi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel8.setText("MASUKAN DONASI BARU");

        TabelDonasi.setBackground(new java.awt.Color(250, 250, 250));
        TabelDonasi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TabelDonasi.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TabelDonasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nama Donatur", "Kampanye", "Jumlah Donasi", "Tanggal Donasi"
            }
        ));
        TabelDonasi.setGridColor(new java.awt.Color(250, 250, 250));
        TabelDonasi.setSelectionBackground(new java.awt.Color(0, 93, 253));
        jScrollPane2.setViewportView(TabelDonasi);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldDonatur, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldKampanye, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDonatur, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKampanye, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     addDonation();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelDonasi;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextFieldDonatur;
    private javax.swing.JTextField jTextFieldJumlah;
    private javax.swing.JTextField jTextFieldKampanye;
    private javax.swing.JTextField jTextFieldTanggal;
    // End of variables declaration//GEN-END:variables
}
