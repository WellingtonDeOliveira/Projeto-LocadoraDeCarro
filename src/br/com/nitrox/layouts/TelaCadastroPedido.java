package br.com.nitrox.layouts;

import java.sql.*;
import br.com.nitrox.dal.ModuloConexao;
import java.text.DateFormat;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaCadastroPedido extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaCadastroPedido() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisarCliente();
        pesquisarCarro();
    }
    
    private void clienteOCu(){
        String sql="update tbcliente set temcarro=? where idcliente=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "1");
            pst.setString(2, txtIdCli.getText());
            int i = pst.executeUpdate();
            if(i>0){
                
            }else{
                JOptionPane.showMessageDialog(null, "Não deu certo");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    private void carroOCu(){
        String sql="update tbcarro set disponivel=? where idcar=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "0");
            pst.setString(2, txtIdCar.getText());
             int i = pst.executeUpdate();
            if(i>0){
                
            }else{
                JOptionPane.showMessageDialog(null, "Não deu certo");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public int valorDia(String data) {
        String s[] = new String[2], conc;
        int n;
        for (int i = 0; i < 2; i++) {
            s[i] = data.substring(i, i + 1);
        }
        conc = s[0] + s[1];
        n = Integer.parseInt(conc);
        return n;
    }

    public int valorMes(String data) {
        String s[] = new String[2], conc;
        int n;
        for (int i = 0; i < 2; i++) {
            s[i] = data.substring(i + 3, i + 4);
        }
        conc = s[0] + s[1];
        n = Integer.parseInt(conc);
        return n;
    }

    private void valor() {
        if (txtDataPedido.getText().isEmpty() || txtDataEntrega.getText().equals("  /  /    ")) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos Obrigatórios");
        }else{
            float val = Float.parseFloat(txtValor.getText());
            DecimalFormat formatter = new DecimalFormat("#.00");
            int diaP, mesP, diaE, mesE, dia;
            diaP = valorDia(txtDataPedido.getText());
            mesP = valorMes(txtDataPedido.getText());
            diaE = valorDia(txtDataEntrega.getText());
            mesE = valorMes(txtDataEntrega.getText());
            if (mesP < mesE) {
                int c = mesE - mesP;
                dia = 30 - diaP;
                for (int i = 2; i < c; i++) {
                    dia += 30;
                }
                dia += diaE;
            } else {
                dia = diaE - diaP;
            }
            val = (float) val * dia;
            String valo = String.valueOf(formatter.format(val));
            txtValor.setText(valo);
            btnGerarPedido.setEnabled(true);
            btnCalculo.setEnabled(false);
        }
    }

    private void pesquisarCliente() {
        String sql = "select idcliente as ID,nome as Nome,fone as Telefone,Temcarro as PossuiCarro from tbcliente where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            // Passando o conteúdo da caixa de pesquisa para o ?
            // Atenção ao "%" - continuação da String sql
            pst.setString(1, txtPesNomeCli.getText() + "%");
            rs = pst.executeQuery();
            // A linha abaixo usa a biblioteca rs2xml.jar, para preencher a tabela
            txtTabelaCliente.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setarCampoCliente() {
        String cliDisponivel;
        int setar = txtTabelaCliente.getSelectedRow();
        cliDisponivel = txtTabelaCliente.getModel().getValueAt(setar, 3).toString();
        if ("false".equals(cliDisponivel)) {
            txtIdCli.setText(txtTabelaCliente.getModel().getValueAt(setar, 0).toString());
        }
    }

    private void pesquisarCarro() {
        String sqlu = "select idcar as ID,marca,modelo,ano,cor,valorD as ValorDia,valorK as ValorKm, disponivel from tbcarro where marca like ?";
        try {
            pst = conexao.prepareStatement(sqlu);
            // Passando o conteúdo da caixa de pesquisa para o ?
            // Atenção ao "%" - continuação da String sql
            pst.setString(1, txtMarcaCar.getText() + "%");
            rs = pst.executeQuery();
            // A linha abaixo usa a biblioteca rs2xml.jar, para preencher a tabela
            txtTabelaCarro.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setarCampoCarro() {
        String dis;
        int setar = txtTabelaCarro.getSelectedRow();
        dis = txtTabelaCarro.getModel().getValueAt(setar, 7).toString();
        if ("true".equals(dis)) {
            txtIdCar.setText(txtTabelaCarro.getModel().getValueAt(setar, 0).toString());
            txtValor.setText(txtTabelaCarro.getModel().getValueAt(setar, 5).toString());
            java.util.Date date = new java.util.Date();
            DateFormat form = DateFormat.getDateInstance(DateFormat.SHORT);
            txtDataPedido.setText(form.format(date));
            btnGerarPedido.setEnabled(false);
            btnCalculo.setEnabled(true);
        }
    }
    private void adicionar() {
        // Fazendo teste se o login já existe no banco de dados
        String sql = "insert into tbpedido(dataE,valor,encerrado,idcli,idcar) values(?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtDataEntrega.getText());
            pst.setString(2, txtValor.getText().replace(",", "."));
            pst.setString(3, "0");
            pst.setString(4, txtIdCli.getText());
            pst.setString(5, txtIdCar.getText());
            // Validação dos campos obrigatórios 
            if (txtIdCli.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {
                // A linha abaixo vai atualizar a tabela com os dados fornecidos acima
                int adicionado = pst.executeUpdate();
                // A linha abaixo serve para entendimento da logica
                // System.out.println(adicionado);
                if (adicionado > 0) {
                    clienteOCu();
                    carroOCu();
                    JOptionPane.showMessageDialog(null, "Aluguel efetuado com sucesso");
                    txtDataEntrega.setText(null);
                    txtDataPedido.setText(null);
                    txtIdCar.setText(null);
                    txtIdCli.setText(null);
                    txtMarcaCar.setText(null);
                    txtPesNomeCli.setText(null);
                    txtValor.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtIdCli = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtTabelaCliente = new javax.swing.JTable();
        txtPesNomeCli = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdCar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtTabelaCarro = new javax.swing.JTable();
        txtMarcaCar = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDataEntrega = new javax.swing.JFormattedTextField();
        btnGerarPedido = new javax.swing.JButton();
        txtDataPedido = new javax.swing.JTextField();
        txtValor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnCalculo = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("NitroX - Fazer Pedido");
        setPreferredSize(new java.awt.Dimension(600, 497));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));
        jPanel1.setToolTipText("");
        jPanel1.setName(""); // NOI18N

        jLabel2.setText("* ID do Cliente:");

        txtIdCli.setEnabled(false);
        txtIdCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdCliActionPerformed(evt);
            }
        });

        txtTabelaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Telefone", "PossuiCarro"
            }
        ));
        txtTabelaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTabelaClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(txtTabelaCliente);

        txtPesNomeCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesNomeCliActionPerformed(evt);
            }
        });
        txtPesNomeCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesNomeCliKeyReleased(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/search.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtPesNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesNomeCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Carro"));

        jLabel1.setText("* ID do Carro:");

        txtIdCar.setEnabled(false);
        txtIdCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdCarActionPerformed(evt);
            }
        });

        txtTabelaCarro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Marca", "Modelo", "Ano", "Cor", "Valor/Dia", "Valor/Km", "Disponivel"
            }
        ));
        txtTabelaCarro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTabelaCarroMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(txtTabelaCarro);

        txtMarcaCar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMarcaCarKeyReleased(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/search.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Pagamento é efetuado apenas na devolução do carro");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("* Campos Obrigatórios");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtMarcaCar, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdCar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMarcaCar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtIdCar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Fazer Aluguel"));
        jPanel3.setPreferredSize(new java.awt.Dimension(353, 110));

        jLabel3.setText("* Data do Pedido:");

        jLabel5.setText("* Valor:");

        jLabel4.setText("* Data de Entrega:");

        try {
            txtDataEntrega.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataEntrega.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N

        btnGerarPedido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/nitrox/icons/create.png"))); // NOI18N
        btnGerarPedido.setToolTipText("Gerar Aluguel");
        btnGerarPedido.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGerarPedido.setEnabled(false);
        btnGerarPedido.setPreferredSize(new java.awt.Dimension(75, 75));
        btnGerarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarPedidoActionPerformed(evt);
            }
        });

        txtDataPedido.setEditable(false);
        txtDataPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataPedidoActionPerformed(evt);
            }
        });

        txtValor.setEditable(false);
        txtValor.setText("Valor");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("+  Valor/Km x Km rodaddos");

        btnCalculo.setText("Calcular");
        btnCalculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDataPedido)
                    .addComponent(txtValor, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDataEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(btnCalculo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(btnGerarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(btnGerarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(txtDataPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalculo))
                .addGap(8, 8, 8)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        setBounds(0, 0, 571, 497);
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdCarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdCarActionPerformed

    private void txtPesNomeCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesNomeCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesNomeCliActionPerformed

    private void txtIdCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdCliActionPerformed

    private void txtDataPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataPedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataPedidoActionPerformed

    private void txtPesNomeCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesNomeCliKeyReleased
        // Chamando metodo pesquisarCliente:
        pesquisarCliente();
    }//GEN-LAST:event_txtPesNomeCliKeyReleased

    private void txtMarcaCarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarcaCarKeyReleased
        // Chamando metodo pesquisarCarro:
        pesquisarCarro();
    }//GEN-LAST:event_txtMarcaCarKeyReleased

    private void txtTabelaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTabelaClienteMouseClicked
        // Chamando metodo setarCampoCliente:
        setarCampoCliente();
    }//GEN-LAST:event_txtTabelaClienteMouseClicked

    private void txtTabelaCarroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTabelaCarroMouseClicked
        // Chamando metodo setarCampoCarro:
        setarCampoCarro();
    }//GEN-LAST:event_txtTabelaCarroMouseClicked

    private void btnCalculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculoActionPerformed
        // Chamando metodo valor:
        valor();
    }//GEN-LAST:event_btnCalculoActionPerformed

    private void btnGerarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarPedidoActionPerformed
        // Chamando metodo adicionar:
        adicionar();
    }//GEN-LAST:event_btnGerarPedidoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalculo;
    private javax.swing.JButton btnGerarPedido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JFormattedTextField txtDataEntrega;
    private javax.swing.JTextField txtDataPedido;
    private javax.swing.JTextField txtIdCar;
    private javax.swing.JTextField txtIdCli;
    private javax.swing.JTextField txtMarcaCar;
    private javax.swing.JTextField txtPesNomeCli;
    private javax.swing.JTable txtTabelaCarro;
    private javax.swing.JTable txtTabelaCliente;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
