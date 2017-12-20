/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientesservidores;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javafx.scene.paint.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import sun.awt.image.PixelConverter;

/**
 *
 * @author User
 */
public class Texto extends javax.swing.JPanel {

    int contador;
    GridLayout grid;

    public Texto() {
        initComponents();
        JLabel  back=new JLabel();
//        back.setLocation(0,0);
//         Image imagen=new ImageIcon(getClass().getResource("../Images/wp.jpg")).getImage();
//         back.setSize(500,500);
//        back.setIcon(new ImageIcon(imagen));
//        this.add(back);
        contador = 10;
        this.setOpaque(false);
        
       // this.jPanel1.setBackground(new java.awt.Color(255, 255, 255, 0));
//        this.jScrollPane2.setBackground(new java.awt.Color(255, 255, 255, 0));
//        
         this.jScrollPane2.setComponentZOrder(this.jScrollPane2.getVerticalScrollBar(), 0);
    this.jScrollPane2.setComponentZOrder(this.jScrollPane2.getViewport(), 1);
    this.jScrollPane2.getVerticalScrollBar().setOpaque(false);
    this.jScrollPane2.setLayout(new ScrollPaneLayout() {
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
        jScrollPane2.getVerticalScrollBar().setUI(new MyScrollBarUI());
        
    }

    public MensajeView AgregarEntrante(String texto,String id) {

        int tama = (texto.length() / 35) + 1;

        MensajeView temp = new MensajeView();
        temp.setId(id);
        temp.setSize(200, tama * 22);
        temp.setLocation(10, contador);
        // temp.setBackground(java.awt.Color.red);
        // JEditorPane ed1= new JEditorPane();
        contador += tama * 22 + 10;
        if(contador>jPanel1.getHeight()){
              jPanel1.setPreferredSize(new Dimension(jPanel1.getWidth(), contador));
          
             this.jPanel1.revalidate();
            this.jLabel1.repaint();
        }
        // temp.add(ed1);
        //   ed1.setLocation(0, 0);
        /////  ed1.setSize(temp.getWidth(),temp.getHeight());
        temp.setBackground(java.awt.Color.white);
        temp.setMensaje(texto);
        temp.setSizeText(190, tama * 22);
        temp.revalidate();
        temp.repaint();
        // temp.setOpaque(true);

        //   temp.setBackground(java.awt.Color.red);
        this.jPanel1.add(temp, 0);

        this.jPanel1.revalidate();
        this.jPanel1.repaint();
        return temp;
    }

    public MensajeView AgregarSaliente(String texto) {

        int tama = (texto.length() / 35) + 1;

        MensajeView temp = new MensajeView();
        temp.setSize(215, tama * 22);
        temp.setLocation(150, contador);
      

//          JEditorPane ed1= new JEditorPane();
        contador += tama * 22 + 10;
         if(contador>jPanel1.getHeight()){
            jPanel1.setPreferredSize(new Dimension(jPanel1.getWidth(), contador));
          
            
             this.jPanel1.revalidate();
            this.jLabel1.repaint();
           
//         jScrollPane2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
//        public void adjustmentValueChanged(AdjustmentEvent e) {  
//            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
//        }
//    });
        }
//         temp.add(ed1);
//         ed1.setLocation(0, 0);
//         ed1.setSize(temp.getWidth(),temp.getHeight());
//         ed1.setText(texto);
        temp.setMensaje(texto);

        temp.revalidate();
        temp.repaint();
        // temp.setOpaque(true);

        //   temp.setBackground(java.awt.Color.red);
        this.jPanel1.add(temp, 0);

        this.jPanel1.revalidate();
        this.jPanel1.repaint();
     return temp;
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();

        jLabel1.setText("jLabel1");

        setBackground(new java.awt.Color(210, 200, 250));
        setPreferredSize(new java.awt.Dimension(402, 500));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(400, 440));

        jPanel1.setBackground(new java.awt.Color(168, 182, 219));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 437));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        JLabel lbl = new JLabel("carlos mario");

        this.revalidate();
        this.repaint();
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
