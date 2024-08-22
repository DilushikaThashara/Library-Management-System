/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dilus
 */
public class ManageBooks extends javax.swing.JFrame {

    /**
     * Creates new form ManageBooks
     */
    
     String book_name, author;
    int book_id, quantity;
    DefaultTableModel model;
    
    // LinkedList to hold book details temporarily
    LinkedList<Book> bookList = new LinkedList<>();

    public ManageBooks() {
        initComponents();
        setBookDetailsTable();
    }

    // Inner class to represent a Book
    public class Book {
        int bookID;
        String bookName;
        String author;
        int quantity;

        Book(int bookID, String bookName, String author, int quantity) {
            this.bookID = bookID;
            this.bookName = bookName;
            this.author = author;
            this.quantity = quantity;
        }
    }

    // Set book details into table
    public void setBookDetailsTable() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/library management", "root", "");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM book_details");
            
            // Clear the previous table data
            clearTable();

            while (rs.next()) {
                int bookID = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");

                // Add book to LinkedList
                bookList.add(new Book(bookID, bookName, author, quantity));

                // Update table model
                Object[] obj = {bookID, bookName, author, quantity};
                model = (DefaultTableModel) tb_bookDetails.getModel();
                model.addRow(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add book to book_details table
    public boolean addBook() {
        boolean isAdded = false;    
        
        // Validate input
        if (validateBookInputs()) {
            book_id = Integer.parseInt(txt_bookID.getText());
            book_name = txt_bookName.getText();
            author = txt_authorName.getText();
            quantity = Integer.parseInt(txt_quantity.getText());

            try {
                Connection con = DBconnection.getConnection();
                String sql = "INSERT INTO book_details VALUES (?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setInt(1, book_id);
                pst.setString(2, book_name);
                pst.setString(3, author);
                pst.setInt(4, quantity);

                int rowCount = pst.executeUpdate();
                if (rowCount > 0) {
                    isAdded = true;
                    bookList.add(new Book(book_id, book_name, author, quantity)); // Add to LinkedList
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isAdded;
    }

    // Update book in book_details table
    public boolean updateBook() {
        boolean isUpdated = false;    

        // Validate input
        if (validateBookInputs()) {
            book_id = Integer.parseInt(txt_bookID.getText());
            book_name = txt_bookName.getText();
            author = txt_authorName.getText();
            quantity = Integer.parseInt(txt_quantity.getText());

            try {
                Connection con = DBconnection.getConnection();
                String sql = "UPDATE book_details SET book_name = ?, author = ?, quantity = ? WHERE book_id = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, book_name);
                pst.setString(2, author);
                pst.setInt(3, quantity);
                pst.setInt(4, book_id);

                int rowCount = pst.executeUpdate();
                if (rowCount > 0) {
                    isUpdated = true;

                    // Update LinkedList
                    for (Book book : bookList) {
                        if (book.bookID == book_id) {
                            book.bookName = book_name;
                            book.author = author;
                            book.quantity = quantity;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isUpdated;
    }

    // Delete book from book_details table
    public boolean deleteBook() {
        boolean isDeleted = false;    
        book_id = Integer.parseInt(txt_bookID.getText());

        try {
            Connection con = DBconnection.getConnection();
            String sql = "DELETE FROM book_details WHERE book_id = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, book_id);

            int rowCount = pst.executeUpdate();
            if (rowCount > 0) {
                isDeleted = true;

                // Remove from LinkedList
                bookList.removeIf(book -> book.bookID == book_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    // Method to clear table
    public void clearTable() {
        DefaultTableModel model = (DefaultTableModel) tb_bookDetails.getModel();
        model.setRowCount(0);
    }

    // Validate book inputs
    private boolean validateBookInputs() {
        if (txt_bookID.getText().trim().isEmpty() || txt_bookName.getText().trim().isEmpty() ||
            txt_authorName.getText().trim().isEmpty() || txt_quantity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(txt_bookID.getText()); // Validate book_id
            Integer.parseInt(txt_quantity.getText()); // Validate quantity
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID and Quantity must be integers.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
            
            
  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_bookID = new app.bolivia.swing.JCTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_bookName = new app.bolivia.swing.JCTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_authorName = new app.bolivia.swing.JCTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_quantity = new app.bolivia.swing.JCTextField();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle3 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle4 = new rojerusan.RSMaterialButtonCircle();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_bookDetails = new rojeru_san.complementos.RSTableMetro();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 204, 0));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Enter Book ID");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, -1, -1));

        txt_bookID.setBackground(new java.awt.Color(255, 204, 0));
        txt_bookID.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_bookID.setPlaceholder("Enter Book ID");
        jPanel3.add(txt_bookID, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, 50));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Moleskine_26px.png"))); // NOI18N
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, 50));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Enter Book Name");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 270, -1, -1));

        txt_bookName.setBackground(new java.awt.Color(255, 204, 0));
        txt_bookName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_bookName.setPlaceholder("Enter Book Name");
        jPanel3.add(txt_bookName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 310, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, -1, 50));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Author name");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, -1, -1));

        txt_authorName.setBackground(new java.awt.Color(255, 204, 0));
        txt_authorName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_authorName.setPlaceholder("Author Name");
        jPanel3.add(txt_authorName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 420, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Unit_26px.png"))); // NOI18N
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, -1, 50));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Quantity");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 480, -1, -1));

        txt_quantity.setBackground(new java.awt.Color(255, 204, 0));
        txt_quantity.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_quantity.setPlaceholder("Quantity");
        jPanel3.add(txt_quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 520, -1, -1));

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(255, 51, 0));
        rSMaterialButtonCircle2.setText("ADD");
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });
        jPanel3.add(rSMaterialButtonCircle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 630, 100, 50));

        rSMaterialButtonCircle3.setBackground(new java.awt.Color(255, 51, 0));
        rSMaterialButtonCircle3.setText("UPDATE");
        rSMaterialButtonCircle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle3ActionPerformed(evt);
            }
        });
        jPanel3.add(rSMaterialButtonCircle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 630, 110, 50));

        rSMaterialButtonCircle4.setBackground(new java.awt.Color(255, 51, 0));
        rSMaterialButtonCircle4.setText("DELETE");
        rSMaterialButtonCircle4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle4ActionPerformed(evt);
            }
        });
        jPanel3.add(rSMaterialButtonCircle4, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 630, 110, 50));

        jPanel2.setBackground(new java.awt.Color(255, 0, 51));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Rewind_48px.png"))); // NOI18N
        jLabel1.setText("Back");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 830));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 51, 0));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("x");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 0, 70, -1));

        tb_bookDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Book ID", "Book Name", "Author", "Quantity"
            }
        ));
        tb_bookDetails.setColorBackgoundHead(new java.awt.Color(255, 204, 0));
        tb_bookDetails.setColorBordeFilas(new java.awt.Color(102, 102, 102));
        tb_bookDetails.setColorBordeHead(new java.awt.Color(102, 102, 102));
        tb_bookDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tb_bookDetails.setColorFilasForeground1(new java.awt.Color(102, 102, 102));
        tb_bookDetails.setColorFilasForeground2(new java.awt.Color(255, 204, 0));
        tb_bookDetails.setColorSelBackgound(new java.awt.Color(255, 204, 0));
        tb_bookDetails.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tb_bookDetails.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tb_bookDetails.setMinimumSize(new java.awt.Dimension(60, 20));
        tb_bookDetails.setRowHeight(40);
        tb_bookDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_bookDetailsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_bookDetails);
        if (tb_bookDetails.getColumnModel().getColumnCount() > 0) {
            tb_bookDetails.getColumnModel().getColumn(0).setResizable(false);
            tb_bookDetails.getColumnModel().getColumn(1).setResizable(false);
            tb_bookDetails.getColumnModel().getColumn(2).setResizable(false);
            tb_bookDetails.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, 160));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/adminIcons/icons8_Books_26px.png"))); // NOI18N
        jLabel8.setText("Manage Books");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 210, 40));

        jPanel6.setBackground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 270, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 270, 5));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 660, 830));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tb_bookDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_bookDetailsMouseClicked
        int rowNo = tb_bookDetails.getSelectedRow();
        TableModel model = tb_bookDetails.getModel();
        
        txt_bookID.setText(model.getValueAt(rowNo, 0).toString());
        txt_bookName.setText(model.getValueAt(rowNo, 1).toString());
        txt_authorName.setText(model.getValueAt(rowNo, 2).toString());
        txt_quantity.setText(model.getValueAt(rowNo, 3).toString());
        
    }//GEN-LAST:event_tb_bookDetailsMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        HomePage home = new HomePage();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        if (addBook()) {
            JOptionPane.showMessageDialog(this, "Book Added");
            clearTable();
            setBookDetailsTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Add Book");
        }
        
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        if (updateBook()) {
            JOptionPane.showMessageDialog(this, "Book Updated");
            clearTable();
            setBookDetailsTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Update Book");
        }
    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void rSMaterialButtonCircle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle4ActionPerformed
        if (deleteBook()) {
            JOptionPane.showMessageDialog(this, "Book Deleted");
            clearTable();
            setBookDetailsTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to Delete Book");
        }
        
        
    }//GEN-LAST:event_rSMaterialButtonCircle4ActionPerformed

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
         System.exit(0);
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel6MouseClicked
private boolean validateInputs() {
        try {
            int id = Integer.parseInt(txt_bookID.getText());
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "Book ID must be a positive integer.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID must be a number.");
            return false;
        }

        if (txt_bookName.getText().isEmpty() || txt_authorName.getText().isEmpty() || txt_quantity.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return false;
        }

        try {
            int qty = Integer.parseInt(txt_quantity.getText());
            if (qty < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.");
            return false;
        }
        
        return true;
    }

    private boolean checkDuplicateBookId(int bookId) {
        boolean exists = false;
        
        try {
            Connection con = DBconnection.getConnection();
            String sql = "SELECT * FROM book_details WHERE book_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, bookId);
            ResultSet rs = pst.executeQuery();
            exists = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return exists;
    }

    private void clearForm() {
        txt_bookID.setText("");
        txt_bookName.setText("");
        txt_authorName.setText("");
        txt_quantity.setText("");
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
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageBooks().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle4;
    private rojeru_san.complementos.RSTableMetro tb_bookDetails;
    private app.bolivia.swing.JCTextField txt_authorName;
    private app.bolivia.swing.JCTextField txt_bookID;
    private app.bolivia.swing.JCTextField txt_bookName;
    private app.bolivia.swing.JCTextField txt_quantity;
    // End of variables declaration//GEN-END:variables
}
