package frame;

import helpers.ComboBoxitem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class gitarinputFrame extends JFrame {
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JComboBox tipecb;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public gitarinputFrame(){
        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            ComboBoxitem tipe = (ComboBoxitem) tipecb.getSelectedItem();
            int id_shiff = tipe.getValue();
//            String cls = jeniscb.getSelectedItem().toString();
//            String mrk = tipecb.getSelectedItem().toString();
//            int tipe;
//
//
//            if(mrk == "Listrik"){
//                id_merk = 1;
//            } else if(mrk == "Classic"){
//                id_merk = 2;
//            } else if(mrk == "Akustik"){
//                id_merk = 3;
//            } else if(mrk == "Bass"){
//                id_merk = 4;
//            } else {
//                id_merk = 5;
//            }
//
            if(nama.equals("")){
                JOptionPane.showMessageDialog(
                        null,
                        "Lengkapi Nama Penyewa",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (this.id == 0) {
                    String cekSQL = "SELECT * FROM table2 WHERE nama_penyewa = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Penyewa Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO table2 SET nama_penyewa = ?, id = ?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM table WHERE nama_penyewa=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "nama Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    String updateSQL = "UPDATE table SET nama_penyewa=?, id=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        tipecb();
        init();
    }

    public void init() {
        setTitle("Input Data");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isi() {
        idTextField.setText(String.valueOf(this.id));

        String findSQL = "SELECT * FROM table2 WHERE id = ?";

        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void tipecb() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM tipe ORDER by tipe";

        Statement s = null;
        try {
            s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            tipecb.addItem(new ComboBoxitem(0, "Pilih tipe"));
            while (rs.next()){
                tipecb.addItem(new ComboBoxitem(
                        rs.getInt("id"),
                        rs.getString("tipe")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
