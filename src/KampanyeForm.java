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

public class KampanyeForm extends javax.swing.JPanel {

    /**
     * Creates new form KampanyeForm
     */
    public KampanyeForm() {
        initComponents();
        loadTable();
    }
    
    private void loadTable() {
    // Set kolom untuk JTable
    String[] columnNames = {"ID Kampanye", "Judul", "Deskripsi", "Tanggal Mulai", "Tanggal Berakhir", "Dana Dibutuhkan", "Dana Terkumpul", "Status"};
    DefaultTableModel model = new DefaultTableModel(columnNames, 0);
    DecimalFormat df = new DecimalFormat("#,###.000");// Inisialisasi DefaultTableModel
    
    

    try {
        // Dapatkan koneksi ke database
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            String query = "SELECT * FROM kampanye"; // Query untuk mengambil semua data dari tabel `kampanye`
            
            // Gunakan try-with-resources untuk memastikan Statement dan ResultSet ditutup
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int idKampanye = rs.getInt("id_kampanye");
                    String judul = rs.getString("judul");
                    String deskripsi = rs.getString("deskripsi");
                    Date tanggalMulai = rs.getDate("tanggal_mulai");
                    Date tanggalBerakhir = rs.getDate("tanggal_berakhir");
                    double danaDibutuhkan = rs.getDouble("total_uang_dibutuhkan");
                    double danaTerkumpul = rs.getDouble("total_dana_terkumpul");

                    // Tentukan status kampanye
                    String status = danaTerkumpul >= danaDibutuhkan ? "Tercapai" : "Berjalan";
                    String formattedTotal = df.format(danaDibutuhkan);
                    String formattedDana = df.format(danaTerkumpul);

                    // Tambahkan data ke model JTable
                    Object[] row = {idKampanye, judul, deskripsi, tanggalMulai, tanggalBerakhir, formattedTotal, formattedDana, status};
                    model.addRow(row);
                }
            }

            // Set model JTable
            tabelKampanye.setModel(model);
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Data tidak dapat dimuat.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal memuat data kampanye: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void addKampanye() {
    try {
        // Dapatkan koneksi ke database
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            // Query SQL untuk menambahkan data ke tabel kampanye
            String queryKampanye = "INSERT INTO kampanye (judul, deskripsi, tanggal_mulai, tanggal_berakhir, total_uang_dibutuhkan) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmtKampanye = conn.prepareStatement(queryKampanye);

            // Ambil input dari JTextField
            String judul = jTextFieldJudul.getText();
            String deskripsi = jTextAreaDeskripsi.getText(); // Menggunakan JTextArea untuk deskripsi panjang
            String tanggalMulai = jTextFieldTanggalMulai.getText().trim();
            String tanggalBerakhir = jTextFieldTanggalBerakhir.getText().trim();
            String danaDibutuhkan = jTextFieldDanaDibutuhkan.getText().trim();

            // Validasi input
            if (judul.isEmpty() || deskripsi.isEmpty() || tanggalMulai.isEmpty() || tanggalBerakhir.isEmpty() || danaDibutuhkan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua kolom!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double totalUangDibutuhkan = Double.parseDouble(danaDibutuhkan);
            java.sql.Date sqlTanggalMulai = java.sql.Date.valueOf(tanggalMulai); // Format harus YYYY-MM-DD
            java.sql.Date sqlTanggalBerakhir = java.sql.Date.valueOf(tanggalBerakhir);

            // Set parameter query
            pstmtKampanye.setString(1, judul);
            pstmtKampanye.setString(2, deskripsi);
            pstmtKampanye.setDate(3, sqlTanggalMulai);
            pstmtKampanye.setDate(4, sqlTanggalBerakhir);
            pstmtKampanye.setDouble(5, totalUangDibutuhkan);

            // Eksekusi query untuk menambahkan kampanye
            int rowsInserted = pstmtKampanye.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Kampanye berhasil ditambahkan!");

                // Tambahkan entri ke tabel pelacakan_dana
                String queryPelacakan = "INSERT INTO pelacakan_dana (id_kampanye) SELECT id_kampanye FROM kampanye WHERE judul = ?";
                PreparedStatement pstmtPelacakan = conn.prepareStatement(queryPelacakan);
                pstmtPelacakan.setString(1, judul);
                pstmtPelacakan.executeUpdate();
                pstmtPelacakan.close();
            }

            // Tutup koneksi
            pstmtKampanye.close();
            conn.close();

            // Perbarui JTable Kampanye
            loadTable();

            // Kosongkan input
            jTextFieldJudul.setText("");
            jTextAreaDeskripsi.setText("");
            jTextFieldTanggalMulai.setText("");
            jTextFieldTanggalBerakhir.setText("");
            jTextFieldDanaDibutuhkan.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal. Kampanye tidak dapat disimpan.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Input tidak valid! Pastikan dana berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, "Format tanggal tidak valid! Gunakan format YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal menambahkan kampanye: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldJudul = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKampanye = new javax.swing.JTable();
        jTextAreaDeskripsi = new javax.swing.JTextField();
        jTextFieldTanggalMulai = new javax.swing.JTextField();
        jTextFieldTanggalBerakhir = new javax.swing.JTextField();
        jTextFieldDanaDibutuhkan = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jButton2.setBackground(new java.awt.Color(0, 93, 253));
        jButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Masukan");
        jButton2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 93, 253), new java.awt.Color(0, 93, 253)), javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 93, 253), new java.awt.Color(0, 93, 253))));

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel8.setText("MASUKAN KAMPANYE BARU");

        jTextFieldJudul.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldJudul.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Judul", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        tabelKampanye.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Judul", "Deskripsi", "Tanggal Mulai", "Tanggal Berakhir", "Dana Dibutuhkan", "Dana Terkumpul", "Status"
            }
        ));
        jScrollPane1.setViewportView(tabelKampanye);

        jTextAreaDeskripsi.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextAreaDeskripsi.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Deskripsi", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldTanggalMulai.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldTanggalMulai.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tanggal Mulai", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jTextFieldTanggalBerakhir.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldTanggalBerakhir.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tanggal Berakhir", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jTextFieldTanggalBerakhir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldTanggalBerakhirActionPerformed(evt);
            }
        });

        jTextFieldDanaDibutuhkan.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextFieldDanaDibutuhkan.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Dana Dibutuhkan", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jTextFieldDanaDibutuhkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDanaDibutuhkanActionPerformed(evt);
            }
        });

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextAreaDeskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldTanggalMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldTanggalBerakhir, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldDanaDibutuhkan, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
                        .addGap(8, 8, 8))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextAreaDeskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTanggalMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTanggalBerakhir, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDanaDibutuhkan, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldTanggalBerakhirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldTanggalBerakhirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldTanggalBerakhirActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    addKampanye();  // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldDanaDibutuhkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDanaDibutuhkanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDanaDibutuhkanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextAreaDeskripsi;
    private javax.swing.JTextField jTextFieldDanaDibutuhkan;
    private javax.swing.JTextField jTextFieldJudul;
    private javax.swing.JTextField jTextFieldTanggalBerakhir;
    private javax.swing.JTextField jTextFieldTanggalMulai;
    private javax.swing.JTable tabelKampanye;
    // End of variables declaration//GEN-END:variables
}
