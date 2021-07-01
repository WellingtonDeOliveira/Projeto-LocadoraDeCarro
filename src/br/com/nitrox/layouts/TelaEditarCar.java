package br.com.nitrox.layouts;

import java.sql.*;
import br.com.nitrox.dal.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaEditarCar extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    String temAr = "0", dispo = "0";
    
    public TelaEditarCar() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisarCarro();
    }
    
    private void pesquisarCarro(){
        String sql ="select * from tbcarro where marca like ?";
        try {
            pst = conexao.prepareStatement(sql); 
            // Passando o conteúdo da caixa de pesquisa para o ?
            // Atenção ao "%" - continuação da String sql
            pst.setString(1, txtPesMarca.getText() + "%");
            rs = pst.executeQuery();
            // A linha abaixo usa a biblioteca rs2xml.jar, para preencher a tabela
            tabelaCarro.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    // Metodo para setar os campos do formulario com o conteudo da tabela
    private void setarCampo(){
        int setar = tabelaCarro.getSelectedRow();
        txtEditCarId.setText(tabelaCarro.getModel().getValueAt(setar,0).toString());
        txtEditCarMarca.setText(tabelaCarro.getModel().getValueAt(setar,1).toString());
        txtEditCarModel.setText(tabelaCarro.getModel().getValueAt(setar,2).toString());
        txtEditCarAno.setText(tabelaCarro.getModel().getValueAt(setar,3).toString());
        txtEditCarCor.setText(tabelaCarro.getModel().getValueAt(setar,4).toString());
        txtEditCarVD.setText(tabelaCarro.getModel().getValueAt(setar,5).toString());
        txtEditCarVK.setText(tabelaCarro.getModel().getValueAt(setar,6).toString());
        String temAr = tabelaCarro.getModel().getValueAt(setar,7).toString();
        String dis = tabelaCarro.getModel().getValueAt(setar,8).toString();
        if(temAr.equals("true")){
            selTemAr.setSelected(true);
            this.temAr = "1";
        }else{
            selTemAr.setSelected(false);
            this.temAr = "0";
        }
        if(dis.equals("true")){
            selDis.setSelected(true);
            dispo = "1";
        }else{
            selDis.setSelected(false);
            dispo = "0";
        }
    }
    
    private void alterar(){
        String sql="update tbcarro set marca=?,modelo=?,ano=?,cor=?,valorD=?,valorK=?,temAr=?,disponivel=? where idcar=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEditCarMarca.getText());
            pst.setString(2, txtEditCarModel.getText());
            pst.setString(3, txtEditCarAno.getText());
            pst.setString(4, txtEditCarCor.getText());
            pst.setString(5, txtEditCarVD.getText().replace(",", "."));
            pst.setString(6, txtEditCarVK.getText().replace(",", "."));
            pst.setString(7, temAr);
            pst.setString(8, dispo);
            pst.setString(9, txtEditCarId.getText());
            // Validação dos campos obrigatórios 
            if ((txtEditCarMarca.getText().isEmpty()) || (txtEditCarModel.getText().isEmpty()) || (txtEditCarAno.getText().isEmpty())
                    || (txtEditCarCor.getText().isEmpty()) || (txtEditCarVD.getText().isEmpty()) || (txtEditCarVK.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // A linha abaixo vai atualizar a tabela com os dados fornecidos acima
                int adicionado = pst.executeUpdate();
                // A linha abaixo serve para entendimento da logica
                // System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do carro atualizados com sucesso");
                    txtEditCarMarca.setText(null);
                    txtEditCarModel.setText(null);
                    txtEditCarAno.setText(null);
                    txtEditCarCor.setText(null);
                    txtEditCarVD.setText(null);
                    txtEditCarVK.setText(null);
                    selTemAr.setSelected(false);
                    selDis.setSelected(false);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void remover(){
        int comfirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esse carro?", 
                "Atenção",JOptionPane.YES_NO_OPTION);
        if(comfirma == JOptionPane.YES_OPTION){
            String sql = "delete from tbcarro where idcar=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtEditCarId.getText());
                int remove = pst.executeUpdate();
                if(remove > 0){
                    JOptionPane.showMessageDialog(null, "Carro removido com sucesso");
                    txtEditCarMarca.setText(null);
                    txtEditCarModel.setText(null);
                    txtEditCarAno.setText(null);
                    txtEditCarCor.setText(null);
                    txtEditCarVD.setText(null);
                    txtEditCarVK.setText(null);
                    selTemAr.setSelected(false);
                    selDis.setSelected(false);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCarEdit = new javax.swing.JButton();
        btnCarDel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtEditCarMarca = new javax.swing.JTextField();
        txtEditCarModel = new javax.swing.JTextField();
        txtEditCarAno = new javax.swing.JTextField();
        txtEditCarCor = new javax.swing.JTextField();
        txtEditCarVK = new javax.swing.JTextField();
        txtEditCarVD = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtEditCarId = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaCarro = new javax.swing.JTable();
        txtPesMarca = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        selDis = new javax.swing.JRadioButton();
        selTemAr = new javax.swing.JRadioButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("NitroX - Editar Carro");

        btnCarEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/edit.png"))); // NOI18N
        btnCarEdit.setToolTipText("Editar");
        btnCarEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCarEdit.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCarEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarEditActionPerformed(evt);
            }
        });

        btnCarDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/delete.png"))); // NOI18N
        btnCarDel.setToolTipText("Excluir");
        btnCarDel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCarDel.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCarDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCarDelActionPerformed(evt);
            }
        });

        jLabel1.setText("* Marca:");

        jLabel2.setText("* Modelo:");

        jLabel3.setText("* Ano:");

        jLabel4.setText("* Cor:");

        jLabel5.setText("* Valor / Dia:");

        jLabel6.setText("* Valor / Km:");

        jLabel7.setText("Tem ar-condicionado:");

        txtEditCarMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEditCarMarcaActionPerformed(evt);
            }
        });

        txtEditCarVK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEditCarVKActionPerformed(evt);
            }
        });

        txtEditCarVD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEditCarVDActionPerformed(evt);
            }
        });

        jLabel8.setText("ID:");

        txtEditCarId.setEnabled(false);
        txtEditCarId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEditCarIdActionPerformed(evt);
            }
        });

        jLabel9.setText("Disponível:");

        jLabel10.setText("* Campos Obrigatórios");

        tabelaCarro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelaCarro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaCarroMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaCarro);

        txtPesMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesMarcaActionPerformed(evt);
            }
        });
        txtPesMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesMarcaKeyReleased(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/search.png"))); // NOI18N

        selDis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selDisActionPerformed(evt);
            }
        });

        selTemAr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selTemArActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(selTemAr)
                                    .addComponent(selDis))
                                .addGap(73, 73, 73)
                                .addComponent(btnCarEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCarDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel8))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtEditCarMarca)
                                                    .addComponent(txtEditCarAno, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                            .addComponent(jLabel2))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                            .addGap(35, 35, 35)
                                                            .addComponent(jLabel4)))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGap(29, 29, 29)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(txtEditCarCor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel6)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(txtEditCarVK, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addComponent(txtEditCarModel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                            .addComponent(txtEditCarId, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEditCarVD, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 64, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(txtPesMarca, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(txtPesMarca, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtEditCarId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtEditCarMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtEditCarModel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtEditCarAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtEditCarCor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(txtEditCarVK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEditCarVD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCarEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCarDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(selTemAr))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selDis)
                            .addComponent(jLabel9))))
                .addGap(31, 31, 31))
        );

        setBounds(0, 0, 571, 497);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCarDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarDelActionPerformed
        // chamando o metodo remover:
        remover();
    }//GEN-LAST:event_btnCarDelActionPerformed

    private void txtEditCarMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditCarMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEditCarMarcaActionPerformed

    private void txtEditCarVKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditCarVKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEditCarVKActionPerformed

    private void txtEditCarVDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditCarVDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEditCarVDActionPerformed

    private void txtEditCarIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditCarIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEditCarIdActionPerformed

    private void txtPesMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesMarcaActionPerformed

    private void txtPesMarcaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesMarcaKeyReleased
        // Chamando o metodo pesquisarCarro:
        pesquisarCarro();
    }//GEN-LAST:event_txtPesMarcaKeyReleased

    private void tabelaCarroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaCarroMouseClicked
        // Chamando o metodo setarCampo:
        setarCampo();
    }//GEN-LAST:event_tabelaCarroMouseClicked

    private void btnCarEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCarEditActionPerformed
        // Chamando o metodo alterar:
        alterar();
    }//GEN-LAST:event_btnCarEditActionPerformed

    private void selTemArActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selTemArActionPerformed
        // TODO add your handling code here:
        if(temAr == "1"){
            temAr = "0";
        }else{
            temAr = "1";
        }
    }//GEN-LAST:event_selTemArActionPerformed

    private void selDisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selDisActionPerformed
        // TODO add your handling code here:
        if(dispo == "1"){
            dispo = "0";
        }else{
            dispo = "1";
        }
    }//GEN-LAST:event_selDisActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCarDel;
    private javax.swing.JButton btnCarEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton selDis;
    private javax.swing.JRadioButton selTemAr;
    private javax.swing.JTable tabelaCarro;
    private javax.swing.JTextField txtEditCarAno;
    private javax.swing.JTextField txtEditCarCor;
    private javax.swing.JTextField txtEditCarId;
    private javax.swing.JTextField txtEditCarMarca;
    private javax.swing.JTextField txtEditCarModel;
    private javax.swing.JTextField txtEditCarVD;
    private javax.swing.JTextField txtEditCarVK;
    private javax.swing.JTextField txtPesMarca;
    // End of variables declaration//GEN-END:variables
}
