package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;

public class gitarViewFrame {
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public gitarViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        hapusButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi hapus data",
                    JOptionPane.YES_NO_OPTION
            );

            if (pilihan == 0 ){
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM penyewaan_gitar WHERE id = ?";
                Connection c = Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        cariButton.addActionListener(e -> {
            String keyword = "%" + cariTextField.getText() + "%";
            String keyword1 = "%" + cariTextField.getText() + "%";
            String keyword2 = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT table2.id,nama, tipe.tipe " +
                    "FROM ((table2 INNER JOIN tipe ON table2.id = id) " +
                    "INNER JOIN tipe ON table2.tipe = tipe) "+
                    "WHERE tipe like ? OR nama like ? OR tipe like ? ";

            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ps.setString(2, keyword1);
                ps.setString(3, keyword2);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[4];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama_penyewa");
                    row[2] = rs.getString("nama_gitar");
                    row[3] = rs.getString("tipe");
                    row[4] = rs.getString("alamat");
                    row[5] = rs.getString("telp");
                    row[6] = rs.getString("tgl");

                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        tambahButton.addActionListener(e->{
            gitarViewFrame inputFrame = new gitarViewFrame();
            inputFrame.setVisible(true);
        });
        ubahButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            TableModel tm = viewTable.getModel();
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id = Integer.parseInt(idString);

            gitarViewFrame inputFrame = new gitarViewFrame();
            inputFrame.setId(id);
            inputFrame.isi();
            inputFrame.setVisible(true);
        });
        isiTable();
        init();
    }

    public void init(){
        setTitle("Data Penyewa");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiTable(){
        String selectSQL = "SELECT penyewaan.id,penyewaan.nama,tipe.tipe " +
                "FROM ((table2 INNER JOIN tipe ON table2.id = id) " +
                "INNER JOIN tipe ON table2.tipe = tipe) "+
                "WHERE tipe like ? OR nama like ? OR tipe like ? ";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String header[] = {"id","nama_penyewa","nama_gitar","tipe","alamat","telp","tgl"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);

            Object[] row = new Object[4];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama_penyewa");
                row[2] = rs.getString("nama_gitar");
                row[3] = rs.getString("tipe");
                row[4] = rs.getString("alamat");
                row[5] = rs.getString("telp");
                row[6] = rs.getString("tgl");

                dtm.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}