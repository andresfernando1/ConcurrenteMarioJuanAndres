/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientesservidores;

import com.sun.management.jmx.Trace;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Insets;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;

/**
 *
 * @author User
 */
public class Index extends javax.swing.JFrame implements Runnable {

    Agent agent;

    SocketController socket;
    Thread hilo;
    LinkedList<User> users;
    String myuser;
    int contador_users;
    LinkedList<UserView> listaUserView;
    User msgViewtemp;

    UserView userViewActico;

    public Index() {
        listaUserView = new LinkedList<>();
        initComponents();
        users = new LinkedList<User>();

        setIconImage(new ImageIcon(getClass().getResource("../Images/icono.png")).getImage());

        setSize(663, 575);
        setResizable(false);
        contador_users = 0;
        agregarValores("$todos");
        this.users.add(new User("", new Texto()));
        cerrar();

        this.jScrollPane1.setComponentZOrder(this.jScrollPane1.getVerticalScrollBar(), 0);
        this.jScrollPane1.setComponentZOrder(this.jScrollPane1.getViewport(), 1);
        this.jScrollPane1.getVerticalScrollBar().setOpaque(false);
        this.jScrollPane1.setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                JScrollPane scrollPane = (JScrollPane) parent;

                Rectangle availR = scrollPane.getBounds();
                availR.x = availR.y = 0;

                Insets parentInsets = parent.getInsets();
                availR.x = parentInsets.left;
                availR.y = parentInsets.top;
                availR.width -= parentInsets.left + parentInsets.right;
                availR.height -= parentInsets.top + parentInsets.bottom;

                Rectangle vsbR = new Rectangle();
                vsbR.width = 12;
                vsbR.height = availR.height;
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;

                if (viewport != null) {
                    viewport.setBounds(availR);
                }
                if (vsb != null) {
                    vsb.setVisible(true);
                    vsb.setBounds(vsbR);
                }
            }
        });
        jScrollPane1.getVerticalScrollBar().setUI(new MyScrollBarUI());

        try {
            agent = new Agent(30000);
            agent.setIndex(this);
        } catch (Exception e) {

        }
        if (agent != null) {
            agent.start();
        }

    }

    //Agrega jpanel con la informacion del ususrio conectado
    public void agregarValores(String user) {

        User temp = new User(user, new Texto());
        this.users.add(temp);
        temp.setActivo(true);

        UserView view = new UserView();
        view.setSize(260, 65);
        view.setLocation(0, contador_users);

        if (!user.equals("$todos")) {
            view.setUser(user);
        } else {
            view.setUser("");
            view.setunserText("Todos");
        }
        view.setIndex(this);
        this.contador_users += 65;
        this.jPanel4.add(view);

        this.jPanel4.revalidate();
        this.jPanel4.repaint();

        this.listaUserView.add(view);
    }

    public void toogleListUserView(UserView view) {

        for (UserView item : listaUserView) {
            item.Desactivar();
        }
        view.Activar();
        userViewActico = view;
    }

    public void agregarMensaje(String mensaje, User user) {

        for (UserView item : listaUserView) {
            if (item.getUser().trim().equals(user.getUsername().trim())) {
                item.agregarMensaje(mensaje);
            }
        }

    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getMyuser() {
        return myuser;
    }

   

//    
//    public void addUserList(String cadena) {
//        for (User item : this.users) {
//            item.setActivo(false);
//        }
//
//        String vector[] = cadena.split(";");
//        boolean bandera = false;
//        
//        
//        for (int i = 0; i < vector.length; i++) {
//            bandera = false;
//            String id_temp[] = vector[i].split(" ");
//            if (!myuser.toUpperCase().equals(id_temp[1].toUpperCase())) {
//                for (User item : this.users) {
//
//                    if (item.getId() == Integer.parseInt(id_temp[0])) {
//                        item.setActivo(true);
//                        bandera = true;
//                    }
//                }
//                if (!bandera) {
//                    User user = new User(Integer.parseInt(id_temp[0]), id_temp[1], new Texto());
//                    this.users.add(user);
//                    user.setActivo(true);
//                    agregarValores(id_temp[1]);
//                }
//            }
//        }
//        for (User item : this.users) {
//            if (!item.isActivo()) {
//                for (UserView aux : listaUserView) {
//                    if (aux.getUser().equals(item.getUsername())) {
//                        if (!aux.getUser().equals("")) {
//                            aux.desconectar();
//                        }
//                    }
//                }
//            }
//        }
//
//    }
    //Cambioa el jpanel de cada chat 
    public void cambiarChat(String username) {

        for (User item : users) {
            if (item.getUsername().toUpperCase().equals(username.toUpperCase())) {

                item.jpanel.setSize(this.mainPanel.getWidth(), this.mainPanel.getHeight());
                item.jpanel.setVisible(true);
                item.jpanel.setLocation(0, 0);

                this.mainPanel.removeAll();
                this.mainPanel.add(item.jpanel);
                this.mainPanel.revalidate();
                this.mainPanel.repaint();
                this.Actual.setText(username);
            }
        }
    }

    public void send(String ip) {

        String salida = this.entrydata.getText();

        if (!salida.equals("")) {

            //temporal-----------------------------------
            if (userViewActico != null) {

                for (User item : users) {
                    if (item.getUsername().equals(this.userViewActico.getUser().trim())) {

                        if (!userViewActico.getUser().equals("")) {
                            this.agent.send(ip, salida);

                        } else {
                            this.send(salida);

                        }
                        msgViewtemp = item;
                        MensajeView aux = msgViewtemp.jpanel.AgregarSaliente(salida);
                        aux.setIndex(this);
                        msgViewtemp.addMensaje(aux);
                        this.entrydata.setText("");

                    }
                }

            }
        }
    }

    public void confirmarSalida() {

        int valor = JOptionPane.showConfirmDialog(this, "Esta seguro de salir", "Adventencia", JOptionPane.YES_NO_OPTION);

        if (valor == JOptionPane.YES_OPTION) {
            //
            System.exit(0);
        } else {

        }
    }

    public void cerrar() {
        try {
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    confirmarSalida();
                }
            });
            this.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        inputHost = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        panelEntry = new javax.swing.JPanel();
        entrydata = new javax.swing.JTextField();
        btnsend = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        Actual = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jPanel6.setBackground(new java.awt.Color(37, 53, 91));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        inputHost.setBackground(new java.awt.Color(37, 53, 91));
        inputHost.setForeground(new java.awt.Color(250, 250, 250));
        inputHost.setText("192.168.0.107");
        inputHost.setBorder(null);
        inputHost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputHostActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8_Search_32px_2.png"))); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputHost, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputHost, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        jPanel1.add(jPanel6);
        jPanel6.setBounds(0, 0, 280, 60);

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setAlignmentX(0.0F);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(273, 490));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel4);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(0, 67, 272, 480);

        jPanel2.setBackground(new java.awt.Color(97, 152, 190));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setLayout(null);

        entrydata.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        entrydata.setText("Escribe un mensaje");
        entrydata.setBorder(null);
        entrydata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entrydataActionPerformed(evt);
            }
        });
        entrydata.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                entrydataKeyPressed(evt);
            }
        });

        btnsend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/send.png"))); // NOI18N
        btnsend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnsendMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout panelEntryLayout = new javax.swing.GroupLayout(panelEntry);
        panelEntry.setLayout(panelEntryLayout);
        panelEntryLayout.setHorizontalGroup(
            panelEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEntryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(entrydata, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnsend, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
        );
        panelEntryLayout.setVerticalGroup(
            panelEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEntryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnsend, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(entrydata, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(panelEntry);
        panelEntry.setBounds(0, 496, 390, 60);

        jPanel5.setBackground(new java.awt.Color(37, 53, 91));

        Actual.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Actual.setForeground(new java.awt.Color(242, 242, 242));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Actual, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(201, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Actual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5);
        jPanel5.setBounds(0, 0, 390, 60);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/if_Chat_chat_bubble_comment_comments_1886540_1.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("Bienvenido a chat client");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Desarollado por:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Andrés Fernando Restrepo");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Carlos Mario Jaramillo");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Juan Martin Zuluaga");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(83, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(51, 51, 51))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(244, Short.MAX_VALUE))
        );

        jPanel2.add(mainPanel);
        mainPanel.setBounds(0, 60, 390, 437);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(272, 0, 390, 554);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void entrydataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entrydataActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_entrydataActionPerformed

    private void btnsendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnsendMouseClicked

        agent.sendAll(this.entrydata.getText());
        send(userViewActico.user);

    }//GEN-LAST:event_btnsendMouseClicked

    private void entrydataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entrydataKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            send(userViewActico.user);
        }
    }//GEN-LAST:event_entrydataKeyPressed

    private void inputHostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputHostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputHostActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
       
         agent.connect(this.inputHost.getText());
    }//GEN-LAST:event_jLabel6MouseClicked

    public void mensajeNuevo(String id, String mensaje) {

        User temp = buscarUser(id);
        
        if(temp!=null){
            MensajeView aux = temp.jpanel.AgregarEntrante(mensaje, id);
            aux.setIndex(this);
            temp.addMensaje(aux);
            agregarMensaje(mensaje, temp);
        }else{
            System.out.println("esta mal");
        }
    }

    public User buscarUser(String id) {

        for (User item : users) {
            System.out.println(item.getUsername()+ "......" + id);
            if (item.getUsername().trim().equals(id.trim())) {
                System.out.println("entro");
                return item;
            }
        }
        return null;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Index().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Actual;
    private javax.swing.JLabel btnsend;
    private javax.swing.JTextField entrydata;
    private javax.swing.JTextField inputHost;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel panelEntry;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {

    }
}
