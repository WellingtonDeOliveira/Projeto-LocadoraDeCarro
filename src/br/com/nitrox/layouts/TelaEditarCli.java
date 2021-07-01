package br.com.nitrox.layouts;

import java.sql.*;
import br.com.nitrox.dal.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaEditarCli extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String texto = "0";
    
    public TelaEditarCli() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisarCliente();
    }
    public String verificarCpf(String cpf){
        String s[] = new String[9], confere = "";
        int n[] = new int[9], verificador1 = 0, verificador2, con = 0;
        
        for(int i = 0; i<9; i++){
            s[i] = cpf.substring(i, i+1); n[i] = Integer.parseInt(s[i]);
        }
        for(int i = 10; i>=2; i--){
            verificador1 += (n[con] * i);
            con++;
        }
        con = 0;
        if((verificador1 % 11) < 2){
            verificador1 = 0;
        }else{
            verificador1 = 11 - (verificador1 % 11);
        }
        verificador2 = verificador1 * 2;
        for(int i = 11; i>=3; i--){
            verificador2 += (n[con] * i);
            con++;
        }
        if((verificador2 % 11) < 2){
            verificador2 = 0;
        }else{
            verificador2 = 11 - (verificador2 % 11);
        }
       confere = (s[0]+ s[1] + s[2] + s[3] + s[4] + s[5] + s[6] + s[7] + s[8] + verificador1 + "" + verificador2);
        if(!confere.equals(cpf)){
            JOptionPane.showMessageDialog(null, "CPF INVALIDO e CORRIGIDO: " + confere);
        }
        return confere;
    }

    private void adicionar() {
        // Fazendo teste se o login já existe no banco de dados
        String sql = "insert into tbcliente(cpf,nome,fone,idade,sexo,temcarro) values(?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, verificarCpf(txtCliCpf.getText()));
            pst.setString(2, txtCliNome.getText());
            pst.setString(3, txtEditCliFone.getText());
            pst.setString(4, txtEditCliIdade.getText());
            pst.setString(5, camEditSexo.getSelectedItem().toString());
            pst.setString(6, texto);
            // Validação dos campos obrigatórios 
            if ((txtCliCpf.getText().isEmpty()) || (txtCliNome.getText().isEmpty()) || (txtEditCliFone.getText().isEmpty())
                    || (txtEditCliIdade.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // A linha abaixo vai atualizar a tabela com os dados fornecidos acima
                int adicionado = pst.executeUpdate();
                // A linha abaixo serve para entendimento da logica
                // System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso");
                    txtCliCpf.setText(null);
                    txtCliNome.setText(null);
                    txtEditCliFone.setText(null);
                    txtCliPesquisar.setText(null);
                    txtEditCliIdade.setText(null);
                    selTeCar.setSelected(false);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void pesquisarCliente(){
        String sql ="select * from tbcliente where nome like ?";
        try {
            pst = conexao.prepareStatement(sql); 
            // Passando o conteúdo da caixa de pesquisa para o ?
            // Atenção ao "%" - continuação da String sql
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            // A linha abaixo usa a biblioteca rs2xml.jar, para preencher a tabela
            tabelaDados.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    // Metodo para setar os campos do formulario com o conteudo da tabela
    private void setarCampo(){
        int setar = tabelaDados.getSelectedRow();
        txtCliId.setText(tabelaDados.getModel().getValueAt(setar,0).toString());
        txtCliCpf.setText(tabelaDados.getModel().getValueAt(setar,1).toString());
        txtCliNome.setText(tabelaDados.getModel().getValueAt(setar,2).toString());
        txtEditCliFone.setText(tabelaDados.getModel().getValueAt(setar,3).toString());
        txtEditCliIdade.setText(tabelaDados.getModel().getValueAt(setar,4).toString());
        camEditSexo.setSelectedItem(tabelaDados.getModel().getValueAt(setar,5).toString());
        // A linha abaixo desabilida o botao adicionar
        btnCliAdic.setEnabled(false);
    }
    
    private void alterar(){
        String sql="update tbcliente set cpf=?,nome=?,fone=?,idade=?,sexo=? where idcliente=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, verificarCpf(txtCliCpf.getText()));
            pst.setString(2, txtCliNome.getText());
            pst.setString(3, txtEditCliFone.getText());
            pst.setString(4, txtEditCliIdade.getText());
            pst.setString(5, camEditSexo.getSelectedItem().toString());
            pst.setString(6, txtCliId.getText());
            // Validação dos campos obrigatórios 
            if ((txtCliCpf.getText().isEmpty()) || (txtCliNome.getText().isEmpty()) || (txtEditCliFone.getText().isEmpty())
                    || (txtEditCliIdade.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // A linha abaixo vai atualizar a tabela com os dados fornecidos acima
                int adicionado = pst.executeUpdate();
                // A linha abaixo serve para entendimento da logica
                // System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente atualizados com sucesso");
                    txtCliCpf.setText(null);
                    txtCliNome.setText(null);
                    txtEditCliFone.setText(null);
                    txtCliPesquisar.setText(null);
                    txtEditCliIdade.setText(null);
                    selTeCar.setSelected(false);
                    btnCliAdic.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void remover(){
        int comfirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esse cliente?", 
                "Atenção",JOptionPane.YES_NO_OPTION);
        if(comfirma == JOptionPane.YES_OPTION){
            String sql = "delete from tbcliente where idcliente=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCliId.getText());
                int remove = pst.executeUpdate();
                if(remove > 0){
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso");
                    txtCliId.setText(null);
                    txtCliCpf.setText(null);
                    txtCliNome.setText(null);
                    txtEditCliFone.setText(null);
                    txtCliPesquisar.setText(null);
                    txtEditCliIdade.setText(null);
                    selTeCar.setSelected(false);
                    btnCliAdic.setEnabled(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCliAdic = new javax.swing.JButton();
        btnCliEdit = new javax.swing.JButton();
        btnCliDel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCliNome = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtEditCliIdade = new javax.swing.JTextField();
        txtCliCpf = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        camEditSexo = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtEditCliFone = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        selTeCar = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        txtCliPesquisar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaDados = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("NitroX - Cadastro e editação de clientes");
        setPreferredSize(new java.awt.Dimension(600, 497));

        btnCliAdic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/create.png"))); // NOI18N
        btnCliAdic.setToolTipText("Consultar");
        btnCliAdic.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliAdic.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliAdic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAdicActionPerformed(evt);
            }
        });

        btnCliEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/edit.png"))); // NOI18N
        btnCliEdit.setToolTipText("Editar");
        btnCliEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliEdit.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliEditActionPerformed(evt);
            }
        });

        btnCliDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/delete.png"))); // NOI18N
        btnCliDel.setToolTipText("Excluir");
        btnCliDel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliDel.setPreferredSize(new java.awt.Dimension(80, 80));
        btnCliDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliDelActionPerformed(evt);
            }
        });

        jLabel1.setText("* CPF:");

        jLabel2.setText("* Nome:");

        txtCliNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliNomeActionPerformed(evt);
            }
        });

        jLabel3.setText("* Idade:");

        txtEditCliIdade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEditCliIdadeActionPerformed(evt);
            }
        });

        try {
            txtCliCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCliCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliCpfActionPerformed(evt);
            }
        });

        jLabel6.setText("* Sexo:");

        camEditSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "F" }));

        jLabel7.setText("* Celular:");

        txtEditCliFone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEditCliFoneActionPerformed(evt);
            }
        });

        jLabel5.setText("Cliente se encontra com algum carro:");

        selTeCar.setEnabled(false);
        selTeCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selTeCarActionPerformed(evt);
            }
        });

        jLabel8.setText("* Campos Obrigatórios");

        txtCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliPesquisarActionPerformed(evt);
            }
        });
        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/search.png"))); // NOI18N

        tabelaDados.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelaDados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaDadosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaDados);

        jLabel9.setText("ID:");

        txtCliId.setEnabled(false);
        txtCliId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliIdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(btnCliEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(camEditSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel7))
                                            .addComponent(txtCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtEditCliIdade, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(txtEditCliFone, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(14, 14, 14)
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(selTeCar))
                                        .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCliAdic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(164, 164, 164)
                                .addComponent(btnCliDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(67, 115, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtEditCliIdade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(camEditSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtEditCliFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addComponent(selTeCar))
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCliEdit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCliAdic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCliDel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );

        setBounds(0, 0, 571, 497);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCliAdicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAdicActionPerformed
        // Chamando o metodo adicionar:
        adicionar();
    }//GEN-LAST:event_btnCliAdicActionPerformed

    private void btnCliDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliDelActionPerformed
        // Chamando o metodo remover:
        remover();
    }//GEN-LAST:event_btnCliDelActionPerformed

    private void btnCliEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliEditActionPerformed
        // Chamando metodo Alterar:
        alterar();
    }//GEN-LAST:event_btnCliEditActionPerformed

    private void txtCliNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliNomeActionPerformed

    private void txtEditCliIdadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditCliIdadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEditCliIdadeActionPerformed

    private void txtCliCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliCpfActionPerformed

    private void txtEditCliFoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditCliFoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEditCliFoneActionPerformed

    private void selTeCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selTeCarActionPerformed
        // Ao Clicar
        if(texto == "0"){
            texto = "1";
        }else{
            texto = "0";
        }
    }//GEN-LAST:event_selTeCarActionPerformed

    private void txtCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliPesquisarActionPerformed
       
    }//GEN-LAST:event_txtCliPesquisarActionPerformed

    private void tabelaDadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaDadosMouseClicked
        // Chamando metodo de setar campos
        setarCampo();
    }//GEN-LAST:event_tabelaDadosMouseClicked

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // Pesquisar cliente enquanto estiver digitando
        pesquisarCliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void txtCliIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliIdActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCliAdic;
    private javax.swing.JButton btnCliDel;
    private javax.swing.JButton btnCliEdit;
    private javax.swing.JComboBox<String> camEditSexo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox selTeCar;
    private javax.swing.JTable tabelaDados;
    private javax.swing.JFormattedTextField txtCliCpf;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtEditCliFone;
    private javax.swing.JTextField txtEditCliIdade;
    // End of variables declaration//GEN-END:variables
}
