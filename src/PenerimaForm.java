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

public class PenerimaForm extends javax.swing.JPanel {

    /**
     * Creates new form PenerimaForm
     */
    public PenerimaForm() {
        initComponents();
        loadTable();
        
    }
    
    private void loadTable() {
    // Atur kolom untuk JTable
    String[] columnNames = {"ID Penerima", "Nama", "Alamat", "Kontak", "Total Dana Diserahkan"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Inisialisasi DefaultTableModel
    DecimalFormat df = new DecimalFormat("#,###.###"); // Format angka dengan ribuan dan tiga desimal

    try {
        // Koneksi ke database
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            String query = "SELECT id_penerima, nama, alamat, kontak, total_dana_diserahkan FROM penerima";

            // Gunakan try-with-resources untuk PreparedStatement dan ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                // Loop untuk menambahkan data ke model JTable
                while (rs.next()) {
                    String formattedJumlah = df.format(rs.getDouble("total_dana_diserahkan")); // Format jumlah
                    Object[] row = {
                        rs.getInt("id_penerima"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("kontak"),
                        formattedJumlah // Gunakan jumlah yang sudah diformat
                    };
                    model.addRow(row);
                }
            }

            // Atur model JTable
            TabelPenerima.setModel(model);
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Data tidak dapat dimuat.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal memuat tabel penerima: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    
    private void addPenerima() {
    try {
        // Koneksi ke database
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            String query = "INSERT INTO penerima (nama, alamat, kontak) VALUES (?, ?, ?)";

            // Ambil data dari form
            String nama = jTextFieldNamaPenerima.getText();
            String alamat = jTextFieldAlamatPenerima.getText();
            String kontak = jTextFieldKontakPenerima.getText();

            // Validasi input
            if (nama.isEmpty() || alamat.isEmpty() || kontak.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Masukkan data ke tabel penerima
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, nama);
            pstmt.setString(2, alamat);
            pstmt.setString(3, kontak);
            pstmt.executeUpdate();

            // Tutup koneksi
            pstmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Penerima berhasil ditambahkan!");

            // Muat ulang tabel penerima
            loadTable();

            // Kosongkan input
            jTextFieldNamaPenerima.setText("");
            jTextFieldAlamatPenerima.setText("");
            jTextFieldKontakPenerima.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Penerima tidak dapat disimpan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menambahkan penerima: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jLabel8 = new javax.swing.JLabel();
        jTextFieldNamaPenerima = new javax.swing.JTextField();
        jTextFieldAlamatPenerima = new javax.swing.JTextField();
        jTextFieldKontakPenerima = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelPenerima = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel8.setText("TAMBAHKAN PENERIMA DONASI");

        jTextFieldNamaPenerima.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldNamaPenerima.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Penerima", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldAlamatPenerima.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldAlamatPenerima.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Alamat", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldKontakPenerima.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldKontakPenerima.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kontak", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        TabelPenerima.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Penerima", "Nama Penerima", "Alamat", "Kontak", "Total Dana Diserahkan"
            }
        ));
        jScrollPane1.setViewportView(TabelPenerima);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldNamaPenerima, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldAlamatPenerima, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldKontakPenerima, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNamaPenerima, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldAlamatPenerima, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldKontakPenerima, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    addPenerima();         // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelPenerima;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldAlamatPenerima;
    private javax.swing.JTextField jTextFieldKontakPenerima;
    private javax.swing.JTextField jTextFieldNamaPenerima;
    // End of variables declaration//GEN-END:variables
}
