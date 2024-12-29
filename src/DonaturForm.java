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

public class DonaturForm extends javax.swing.JPanel {

    /**
     * Creates new form DonaturForm
     */
    public DonaturForm() {
        initComponents();
        loadTable();
    }
    
    private void loadTable() {
    // Set kolom untuk JTable
    String[] columnNames = {"ID Donatur", "Nama", "Email", "Telepon", "Alamat"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Inisialisasi DefaultTableModel

    try {
        // Gunakan DatabaseConnection untuk mendapatkan koneksi
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            String query = "SELECT id_donatur, nama, email, telepon, alamat FROM donatur"; // Query untuk mengambil semua data dari tabel `donatur`

            // Gunakan try-with-resources untuk memastikan Statement dan ResultSet ditutup secara otomatis
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                // Loop melalui hasil query dan tambahkan ke model JTable
                while (rs.next()) {
                    int idDonatur = rs.getInt("id_donatur");
                    String nama = rs.getString("nama");
                    String email = rs.getString("email");
                    String telepon = rs.getString("telepon");
                    String alamat = rs.getString("alamat");

                    // Tambahkan data sebagai baris ke DefaultTableModel
                    Object[] row = {idDonatur, nama, email, telepon, alamat};
                    model.addRow(row);
                }
            }

            // Set model JTable
            TabelDonatur.setModel(model);
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Data tidak dapat dimuat.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace(); // Debugging jika ada error
        JOptionPane.showMessageDialog(this, "Gagal memuat data donatur: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void addDonatur() {
    try {
        // Dapatkan koneksi ke database
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            // Query SQL untuk menambahkan data ke tabel donatur
            String query = "INSERT INTO donatur (nama, email, telepon, alamat) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            // Ambil input dari JTextField
            String nama = jTextFieldDonatur.getText();
            String email = jTextFieldEmail.getText();
            String telepon = jTextFieldTelepon.getText();
            String alamat = jTextFieldAlamat.getText();

            // Validasi input
            if (nama.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama dan Email tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Set parameter query
            pstmt.setString(1, nama);
            pstmt.setString(2, email);
            pstmt.setString(3, telepon.isEmpty() ? null : telepon); // Null jika telepon kosong
            pstmt.setString(4, alamat.isEmpty() ? null : alamat);  // Null jika alamat kosong

            // Eksekusi query
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Donatur berhasil ditambahkan!");
            }

            // Tutup koneksi
            pstmt.close();
            conn.close();

            // Perbarui JTable
            loadTable();

            // Kosongkan input
            jTextFieldDonatur.setText("");
            jTextFieldEmail.setText("");
            jTextFieldTelepon.setText("");
            jTextFieldAlamat.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Donatur tidak dapat disimpan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal menambahkan donatur: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menambahkan donatur: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        TabelDonatur = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldDonatur = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jTextFieldAlamat = new javax.swing.JTextField();
        jTextFieldTelepon = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        TabelDonatur.setBackground(new java.awt.Color(250, 250, 250));
        TabelDonatur.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TabelDonatur.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TabelDonatur.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nama Donatur", "Email Donatur", "Alamat Donatur", "Telepon Donatur"
            }
        ));
        TabelDonatur.setGridColor(new java.awt.Color(250, 250, 250));
        jScrollPane2.setViewportView(TabelDonatur);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel8.setText("MASUKAN DONATUR BARU");

        jTextFieldDonatur.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldDonatur.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Donatur", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldEmail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldEmail.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Email Donatur", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldAlamat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldAlamat.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Alamat Donatur", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jTextFieldAlamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldAlamatActionPerformed(evt);
            }
        });

        jTextFieldTelepon.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldTelepon.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Telepon Donatur", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

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

        jButton3.setBackground(new java.awt.Color(0, 93, 253));
        jButton3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Masukan");
        jButton3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 93, 253), new java.awt.Color(0, 93, 253)), javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 93, 253), new java.awt.Color(0, 93, 253))));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldDonatur, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(439, 439, 439)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                    .addGap(439, 439, 439)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDonatur, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(317, 317, 317)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(318, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
               // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    addDonatur();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextFieldAlamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldAlamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldAlamatActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelDonatur;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextFieldAlamat;
    private javax.swing.JTextField jTextFieldDonatur;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldTelepon;
    // End of variables declaration//GEN-END:variables
}
