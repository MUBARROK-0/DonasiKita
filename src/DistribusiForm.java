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

public class DistribusiForm extends javax.swing.JPanel {

    /**
     * Creates new form DistribusiForm
     */
    public DistribusiForm() {
        initComponents();
        loadTable();
    }
    
    private void loadTable() {
    // Set kolom untuk JTable
    String[] columnNames = {"ID Distribusi", "Kampanye", "Penerima", "Jumlah", "Tanggal"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0); // Inisialisasi DefaultTableModel
    DecimalFormat df = new DecimalFormat("#,###.###");

    try {
        // Gunakan DatabaseConnection untuk mendapatkan koneksi
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            String query = "SELECT d.id_distribusi, k.judul AS kampanye, p.nama AS penerima, d.jumlah, d.tanggal " +
                           "FROM distribusi d " +
                           "JOIN kampanye k ON d.id_kampanye = k.id_kampanye " +
                           "JOIN penerima p ON d.id_penerima = p.id_penerima";
            
            // Gunakan try-with-resources untuk PreparedStatement dan ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
                // Loop melalui hasil query dan tambahkan ke model JTable
                while (rs.next()) {
                    int idDistribusi = rs.getInt("id_distribusi");
                    String kampanye = rs.getString("kampanye");
                    String penerima = rs.getString("penerima");
                    double jumlah = rs.getDouble("jumlah");
                    String tanggal = rs.getDate("tanggal").toString(); // Ambil tanggal dalam format String

                    String formattedJumlah = df.format(jumlah);

                    // Tambahkan data sebagai baris ke DefaultTableModel
                    Object[] row = {idDistribusi, kampanye, penerima, formattedJumlah, tanggal};
                    model.addRow(row);
                }
            }

            // Set model JTable
            TabelDistribusi.setModel(model);
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Data tidak dapat dimuat.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace(); // Debugging jika ada error
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void updateTotalDidistribusikan(int idKampanye, double jumlahDistribusi) {
    try {
        Connection conn = DatabaseConnection.connect();
        String query = "UPDATE pelacakan_dana SET total_didistribusikan = total_didistribusikan + ? WHERE id_kampanye = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setDouble(1, jumlahDistribusi);
        pstmt.setInt(2, idKampanye);
        pstmt.executeUpdate();
        pstmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal memperbarui total didistribusikan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    

    
    private void addDistribusi() {
    try {
        // Koneksi ke database
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            // Query dengan kolom tanggal
            String queryDistribusi = "INSERT INTO distribusi (id_kampanye, id_penerima, jumlah, tanggal) VALUES (?, ?, ?, ?)";
            String queryUpdatePenerima = "UPDATE penerima SET total_dana_diserahkan = total_dana_diserahkan + ? WHERE id_penerima = ?";

            // Ambil data dari form
            int idKampanye = Integer.parseInt(jTextFieldIdKampanye.getText());
            int idPenerima = Integer.parseInt(jTextFieldIdPenerima.getText());
            double jumlah = Double.parseDouble(jTextFieldJumlah.getText());

            // Tanggal distribusi
            String tanggalInput = jTextFieldTanggal.getText(); // Format: YYYY-MM-DD
            Date tanggalDistribusi = Date.valueOf(tanggalInput); // Konversi ke java.sql.Date

            // Tambahkan data ke tabel distribusi
            PreparedStatement pstmtDistribusi = conn.prepareStatement(queryDistribusi);
            pstmtDistribusi.setInt(1, idKampanye);
            pstmtDistribusi.setInt(2, idPenerima);
            pstmtDistribusi.setDouble(3, jumlah);
            pstmtDistribusi.setDate(4, tanggalDistribusi);
            pstmtDistribusi.executeUpdate();

            // Perbarui total dana diserahkan di tabel penerima
            PreparedStatement pstmtUpdatePenerima = conn.prepareStatement(queryUpdatePenerima);
            pstmtUpdatePenerima.setDouble(1, jumlah);
            pstmtUpdatePenerima.setInt(2, idPenerima);
            pstmtUpdatePenerima.executeUpdate();
            updateTotalDidistribusikan(idKampanye, jumlah);

            // Tutup koneksi
            pstmtDistribusi.close();
            pstmtUpdatePenerima.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Distribusi berhasil ditambahkan!");

            // Muat ulang tabel distribusi
            loadTable();

            // Kosongkan input
            jTextFieldIdKampanye.setText("");
            jTextFieldIdPenerima.setText("");
            jTextFieldJumlah.setText("");
            jTextFieldTanggal.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Distribusi tidak dapat disimpan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menambahkan distribusi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        jTextFieldIdKampanye = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelDistribusi = new javax.swing.JTable();
        jTextFieldIdPenerima = new javax.swing.JTextField();
        jTextFieldJumlah = new javax.swing.JTextField();
        jTextFieldTanggal = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel8.setText("DISTRIBUSIKAN DONASI");

        jTextFieldIdKampanye.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldIdKampanye.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Kampanye", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        TabelDistribusi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Distribusi", "ID Kampanye", "Nama Penerima", "Jumlah", "Tanggal Distribusi"
            }
        ));
        jScrollPane1.setViewportView(TabelDistribusi);

        jTextFieldIdPenerima.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldIdPenerima.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Penerima", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldJumlah.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldJumlah.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Jumlah", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldTanggal.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldTanggal.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tanggal Distribusi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

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
                    .addComponent(jLabel8)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextFieldIdKampanye, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldIdPenerima, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldIdKampanye, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldIdPenerima, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    addDistribusi();  // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelDistribusi;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldIdKampanye;
    private javax.swing.JTextField jTextFieldIdPenerima;
    private javax.swing.JTextField jTextFieldJumlah;
    private javax.swing.JTextField jTextFieldTanggal;
    // End of variables declaration//GEN-END:variables
}
