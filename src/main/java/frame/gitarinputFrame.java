
package frame;
import helpers.Koneksi;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class gitarinputFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JTextField tipeTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private int idLabel;
    private JPanel buttonPanel;
    private JLabel namaLabel;
    private JLabel tipeLabel;
    public gitarinputFrame(){
        batalButton.addActionListener(e -> {
            dispose();
        });
        simpanButton.addActionListener(e -> {
            String nama_gitar = namaTextField.getText();
            String tipe = tipeTextField.getText();
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            if (nama_gitar.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data nama",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }
            try {
                String cekSQL;
                if (this.idLabel == 0) { //jika TAMBAH
                    cekSQL = "SELECT * FROM table2 WHERE nama_gitar=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_gitar);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "nama yang sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO table2 (id,nama_gitar,tipe) VALUES (NULL, ?, ?, ?)";
                    insertSQL = "INSERT INTO `table2` (`id`, `nama_gitar`, `tipe`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `table2` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO table2 (nama_gitar,tipe) VALUES (?)";
                    insertSQL = "INSERT INTO table2 SET nama_gitar=?, tipe=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama_gitar);
                    ps.setString(2, tipe);
                    ps.executeUpdate();
                    dispose();
                } else {
                    cekSQL = "SELECT * FROM table2 WHERE nama_gitar=? AND tipe=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama_gitar);
                    ps.setString(2, tipe);
                    ps.setInt(3,idLabel);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Data sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String updateSQL = "UPDATE table2 SET nama_gitar=?,tipe=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama_gitar);
                    ps.setString(2, tipe);
                    ps.setInt(3, idLabel);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        init();
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("input table2");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM obat WHERE id_obat = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, idLabel);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama_gitar"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setId(int id) {
    }
}