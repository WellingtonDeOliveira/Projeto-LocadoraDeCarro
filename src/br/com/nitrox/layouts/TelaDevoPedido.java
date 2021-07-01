package br.com.nitrox.layouts;

import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import java.sql.*;
import br.com.nitrox.dal.ModuloConexao;

public class TelaDevoPedido extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String txt, pedido;

    public TelaDevoPedido() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisarCliente();
    }

    private void pesquisarCliente() {
        String sql = "select idcliente as ID,nome as Nome,fone as Telefone,Temcarro as PossuiCarro from tbcliente where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteúdo da caixa de pesquisa para o ?
            // Atenção ao "%" - continuação da String sql
            pst.setString(1, txtDevoCliente.getText() + "%");
            rs = pst.executeQuery();
            // A linha abaixo usa a biblioteca rs2xml.jar, para preencher a tabela
            tabelaClien.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void calcular() {
        if (txtTotal.getText().isEmpty() || txtTotalKm.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
        } else {
            String sql = "select * from tbcarro where idcar=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txt);
                rs = pst.executeQuery();
                rs.next();
                float valor = Float.parseFloat(rs.getString(7));
                float diasT = Float.parseFloat(txtTotalKm.getText());
                valor = diasT * valor;
                float v1 = Float.parseFloat(txtTotal.getText());
                valor += v1;
                txtTotal.setText(String.valueOf(valor));
                btnCalcular.setEnabled(false);
                btnRecibo.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void pesquisarPedido() {
        String sql = "select idpedido as ID_Pedido,valor as Valor,idcar as ID_Carro,encerrado as Encerrado from tbpedido where idcli like ?";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteúdo da caixa de pesquisa para o ?
            // Atenção ao "%" - continuação da String sql
            pst.setString(1, txtIdCliente.getText());
            rs = pst.executeQuery();
            // A linha abaixo usa a biblioteca rs2xml.jar, para preencher a tabela
            tabelaPedido.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setarCampoCliente() {
        String cliDisponivel;
        int setar = tabelaClien.getSelectedRow();
        cliDisponivel = tabelaClien.getModel().getValueAt(setar, 3).toString();
        if ("true".equals(cliDisponivel)) {
            txtIdCliente.setText(tabelaClien.getModel().getValueAt(setar, 0).toString());
            btnCalcular.setEnabled(true);
            btnRecibo.setEnabled(false);
        }
    }

    private void setarCampoPedido() {
        int setar = tabelaPedido.getSelectedRow();
        pedido = tabelaPedido.getModel().getValueAt(setar, 0).toString();
        txtTotal.setText(tabelaPedido.getModel().getValueAt(setar, 1).toString());
        txt = tabelaPedido.getModel().getValueAt(setar, 2).toString();
        btnCalcular.setEnabled(true);
        btnRecibo.setEnabled(false);
    }

    private void clienteLiv() {
        String sql = "update tbcliente set temcarro=? where idcliente=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "0");
            pst.setString(2, txtIdCliente.getText());
            int i = pst.executeUpdate();
            if (i > 0) {

            } else {
                JOptionPane.showMessageDialog(null, "Não deu certo");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void carroLiv() {
        String sql = "update tbcarro set disponivel=? where idcar=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "1");
            pst.setString(2, txt);
            int i = pst.executeUpdate();
            if (i > 0) {

            } else {
                JOptionPane.showMessageDialog(null, "Não deu certo");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void efetuarPagamento() {
        if (txtTotal.getText().isEmpty() || txtTotalKm.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
        } else {
            String sql = "update tbpedido set valor=?,encerrado=? where idpedido=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtTotal.getText());
                pst.setString(2, "1");
                pst.setString(3, pedido);
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    clienteLiv();
                    carroLiv();
                    JOptionPane.showMessageDialog(null, "Pagamento efetuado com sucesso");
                    txtIdCliente.setText(null);
                    txtTotal.setText(null);
                    txtTotalKm.setText(null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaClien = new javax.swing.JTable();
        txtDevoCliente = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtIdCliente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaPedido = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txtTotalKm = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        btnRecibo = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnCalcular = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("NitroX - Devolução");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        tabelaClien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "None", "Telefone", "PossuiCarro"
            }
        ));
        tabelaClien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaClienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaClien);

        txtDevoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDevoClienteKeyReleased(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/search.png"))); // NOI18N

        jLabel2.setText("* ID:");

        txtIdCliente.setEnabled(false);
        txtIdCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdClienteKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdClienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtDevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabelaPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID_Pedido", "Valor", "ID_Carro", "Encerrado"
            }
        ));
        tabelaPedido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaPedidoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaPedido);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("* Total de quilometros rodados pelo cliente:");

        jLabel4.setText("* Valor total a ser pago:");

        txtTotal.setEditable(false);
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        btnRecibo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/recib.png"))); // NOI18N
        btnRecibo.setToolTipText("Efetuar Pagamento");
        btnRecibo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRecibo.setEnabled(false);
        btnRecibo.setPreferredSize(new java.awt.Dimension(80, 80));
        btnRecibo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReciboActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText(" * Campos Obrigatórios");

        btnCalcular.setText("Calcular");
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 46, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTotalKm)
                            .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(btnCalcular)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtTotalKm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCalcular))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnRecibo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 50, Short.MAX_VALUE))
        );

        setBounds(0, 0, 571, 497);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void tabelaClienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaClienMouseClicked
        // Chamando metodo setarCampoCliente:
        setarCampoCliente();
        pesquisarPedido();
    }//GEN-LAST:event_tabelaClienMouseClicked

    private void txtDevoClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDevoClienteKeyReleased
        // Chamando metodo pesquisarCliente:
        pesquisarCliente();
    }//GEN-LAST:event_txtDevoClienteKeyReleased

    private void txtIdClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdClienteKeyReleased

    }//GEN-LAST:event_txtIdClienteKeyReleased

    private void txtIdClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdClienteKeyPressed

    }//GEN-LAST:event_txtIdClienteKeyPressed

    private void tabelaPedidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaPedidoMouseClicked
        // Chamando metodo setarCamposPedidos:
        setarCampoPedido();
    }//GEN-LAST:event_tabelaPedidoMouseClicked

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        // Chamando metodo Calcular:
        calcular();
    }//GEN-LAST:event_btnCalcularActionPerformed

    private void btnReciboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReciboActionPerformed
        // Chamando metodo efetuarPagamento:
        efetuarPagamento();
    }//GEN-LAST:event_btnReciboActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnRecibo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabelaClien;
    private javax.swing.JTable tabelaPedido;
    private javax.swing.JTextField txtDevoCliente;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalKm;
    // End of variables declaration//GEN-END:variables
}
