package br.com.nitrox.layouts;

import java.sql.*;
import br.com.nitrox.dal.ModuloConexao;
import java.security.MessageDigest;
import javax.swing.JOptionPane;

public class TelaEditarUser extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaEditarUser
     */
    public TelaEditarUser() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    private void adicionar() {
        // Fazendo teste se o login já existe no banco de dados
        String sql = "insert into tbusuario(iduser,usuario,login,senha,perfil) values(?,?,?,?,?)";
        String senha = new String(txtUserEditSen.getPassword());
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));
            
            StringBuilder sb = new StringBuilder();
            
            for(byte b : messageDigest){
                sb.append(String.format("%02X", 0xFF & b));
            }
            
            String senhaHash = sb.toString();
            
            pst = conexao.prepareStatement(sql);
            pst.setString(1,txtUserIdEdit.getText());
            pst.setString(2, txtUserEditNome.getText());
            pst.setString(3, txtUserEditLog.getText());
            pst.setString(4, senhaHash);
            pst.setString(5, jcUserEditCamp.getSelectedItem().toString());
            // Validação dos campos obrigatórios 
            if ((txtUserEditNome.getText().isEmpty()) || (txtUserEditLog.getText().isEmpty()) 
                    || (txtUserEditSen.getText().isEmpty()) || (txtUserIdEdit.getText().isEmpty()) ) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // A linha abaixo vai atualizar a tabela com os dados fornecidos acima
                int adicionado = pst.executeUpdate();
                // A linha abaixo serve para entendimento da logica
                // System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso");
                    txtUserIdEdit.setText(null);
                    txtUserEditNome.setText(null);
                    txtUserEditLog.setText(null);
                    txtUserEditSen.setText(null);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void consultar() {
        String sql = "select * from tbusuario where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserIdEdit.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                txtUserEditNome.setText(rs.getString(2));
                txtUserEditLog.setText(rs.getString(3));
                jcUserEditCamp.setSelectedItem(rs.getString(5));
            } else {
                JOptionPane.showMessageDialog(null, "Usuário não cadastrado");
                // As linhas abaixo limpa os campos
                txtUserEditNome.setText(null);
                txtUserEditLog.setText(null);
                txtUserEditSen.setText(null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void alterar(){
        String sql="update tbusuario set usuario=?,login=?,senha=?,perfil=? where iduser=?";
        String senha = new String(txtUserEditSen.getPassword());
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));
            
            StringBuilder sb = new StringBuilder();
            
            for(byte b : messageDigest){
                sb.append(String.format("%02X", 0xFF & b));
            }
            
            String senhaHash = sb.toString();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUserEditNome.getText());
            pst.setString(2, txtUserEditLog.getText());
            pst.setString(3, senhaHash);
            pst.setString(4, jcUserEditCamp.getSelectedItem().toString());
            pst.setString(5, txtUserIdEdit.getText());
            // Validação dos campos obrigatórios 
            if ((txtUserEditNome.getText().isEmpty()) || (txtUserEditLog.getText().isEmpty()) 
                    || (txtUserEditSen.getText().isEmpty()) || (txtUserIdEdit.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // A linha abaixo vai atualizar a tabela com os dados fornecidos acima
                int adicionado = pst.executeUpdate();
                // A linha abaixo serve para entendimento da logica
                // System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do usuário alterados com sucesso");
                    txtUserIdEdit.setText(null);
                    txtUserEditNome.setText(null);
                    txtUserEditLog.setText(null);
                    txtUserEditSen.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void remover(){
        int comfirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esse usuário?", 
                "Atenção",JOptionPane.YES_NO_OPTION);
        if(comfirma == JOptionPane.YES_OPTION){
            String sql = "delete from tbusuario where iduser=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtUserIdEdit.getText());
                int remove = pst.executeUpdate();
                if(remove > 0){
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso");
                    txtUserIdEdit.setText(null);
                    txtUserEditNome.setText(null);
                    txtUserEditLog.setText(null);
                    txtUserEditSen.setText(null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
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

        txtUserEditSen = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jcUserEditCamp = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtUserEditLog = new javax.swing.JTextField();
        txtUserEditNome = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtUserIdEdit = new javax.swing.JTextField();
        btnUserProc = new javax.swing.JButton();
        btnUserEdit = new javax.swing.JButton();
        btnUserDel = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("NitroX - Cadastro e Edição de usuários");
        setPreferredSize(new java.awt.Dimension(600, 497));

        jLabel1.setText("* Nome:");

        jLabel2.setText("* Login:");

        jLabel3.setText("* Senha:");

        jcUserEditCamp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));
        jcUserEditCamp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcUserEditCampActionPerformed(evt);
            }
        });

        jLabel4.setText("* Perfil:");

        jLabel5.setText("* ID:");

        btnUserProc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/reade.png"))); // NOI18N
        btnUserProc.setToolTipText("Consultar");
        btnUserProc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserProc.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUserProc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserProcActionPerformed(evt);
            }
        });

        btnUserEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/edit.png"))); // NOI18N
        btnUserEdit.setToolTipText("Editar");
        btnUserEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserEdit.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUserEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserEditActionPerformed(evt);
            }
        });

        btnUserDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/delete.png"))); // NOI18N
        btnUserDel.setToolTipText("Excluir");
        btnUserDel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserDel.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUserDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserDelActionPerformed(evt);
            }
        });

        jLabel6.setText("* Campos obrigatórios");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/create.png"))); // NOI18N
        jButton1.setToolTipText("Cadastrar");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setPreferredSize(new java.awt.Dimension(80, 80));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcUserEditCamp, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtUserIdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txtUserEditLog, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtUserEditSen, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtUserEditNome, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUserProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUserEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUserDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(91, 91, 91))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtUserIdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUserEditNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtUserEditLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(txtUserEditSen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jcUserEditCamp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnUserProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUserDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUserEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(90, 90, 90))
        );

        setBounds(0, 0, 571, 497);
    }// </editor-fold>//GEN-END:initComponents

    private void jcUserEditCampActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcUserEditCampActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcUserEditCampActionPerformed

    private void btnUserProcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserProcActionPerformed
        // Chamando o metodo consultar:
        consultar();
    }//GEN-LAST:event_btnUserProcActionPerformed

    private void btnUserEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserEditActionPerformed
        // Chamando o metodo alterar:
        alterar();
    }//GEN-LAST:event_btnUserEditActionPerformed

    private void btnUserDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserDelActionPerformed
        // Chamando o metodo remover:
        remover();
    }//GEN-LAST:event_btnUserDelActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Chamando o metodo adicionar;
        adicionar();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUserDel;
    private javax.swing.JButton btnUserEdit;
    private javax.swing.JButton btnUserProc;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JComboBox<String> jcUserEditCamp;
    private javax.swing.JTextField txtUserEditLog;
    private javax.swing.JTextField txtUserEditNome;
    private javax.swing.JPasswordField txtUserEditSen;
    private javax.swing.JTextField txtUserIdEdit;
    // End of variables declaration//GEN-END:variables

}
