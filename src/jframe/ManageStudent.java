/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages Students in the library system.
 */
public class ManageStudent extends javax.swing.JFrame {

    private String studentName, course, branch;
    private int studentID;
    private DefaultTableModel model;

    public ManageStudent() {
        initComponents();
        setStudentDetailsTable();
    }

    public class Student {
        private int studentID;
        private String studentName;
        private String course;
        private String branch;

        // Constructor
        public Student(int studentID, String studentName, String course, String branch) {
            this.studentID = studentID;
            this.studentName = studentName;
            this.course = course;
            this.branch = branch;
        }

        // Getter methods
        public int getStudentID() {
            return studentID;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getCourse() {
            return course;
        }

        public String getBranch() {
            return branch;
        }

        // Setter methods
        public void setStudentID(int studentID) {
            this.studentID = studentID;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public void setBranch(String branch) {
            this.branch = branch;
        }
    }

    // Set student details into table
    public void setStudentDetailsTable() {
    List<Student> students = new LinkedList<>();

    try (Connection con = DBconnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery("SELECT * FROM student_details")) {

        while (rs.next()) {
            int studentID = rs.getInt("student_id");
            String studentName = rs.getString("name");
            String course = rs.getString("course");
            String branch = rs.getString("branch");

            students.add(new Student(studentID, studentName, course, branch));
        }

        // Clear existing rows and update the table model
        model = (DefaultTableModel) tbl_StudentDetails.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Student student : students) {
            model.addRow(new Object[]{student.getStudentID(), student.getStudentName(), student.getCourse(), student.getBranch()});
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // Add student to student_details table with validations
    public boolean addStudent() {
        boolean isAdded = false;

        // Validation: ID should be a number
        try {
            studentID = Integer.parseInt(txt_studentID.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Student ID must be a number.");
            return false;
        }

        studentName = txt_StudentName.getText();
        course = cmb_course.getSelectedItem().toString();
        branch = cmb_branch.getSelectedItem().toString();

        // Validation: Student name must be text and not empty
        if (studentName.isEmpty() || !studentName.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, "Student name must be text and not empty.");
            return false;
        }

        // Validation: Check if Student ID already exists
        try {
            Connection con = DBconnection.getConnection();
            String checkQuery = "SELECT * FROM student_details WHERE student_id=?";
            PreparedStatement pst = con.prepareStatement(checkQuery);
            pst.setInt(1, studentID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Student ID already exists.");
                return false;
            }

            // Insert new student
            String insertQuery = "INSERT INTO student_details (student_id, name, course, branch) VALUES (?, ?, ?, ?)";
            PreparedStatement pstInsert = con.prepareStatement(insertQuery);
            pstInsert.setInt(1, studentID);
            pstInsert.setString(2, studentName);
            pstInsert.setString(3, course);
            pstInsert.setString(4, branch);

            int rowsAffected = pstInsert.executeUpdate();
            if (rowsAffected > 0) {
                isAdded = true;
                JOptionPane.showMessageDialog(this, "Student added successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAdded;
    }

    // Update student details
    public boolean updateStudent() {
        boolean isUpdated = false;

        // Validation: ID should be a number
        try {
            studentID = Integer.parseInt(txt_studentID.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Student ID must be a number.");
            return false;
        }

        studentName = txt_StudentName.getText();
        course = cmb_course.getSelectedItem().toString();
        branch = cmb_branch.getSelectedItem().toString();

        // Validation: Student name must be text and not empty
        if (studentName.isEmpty() || !studentName.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, "Student name must be text and not empty.");
            return false;
        }

        // Update student details
        try {
            Connection con = DBconnection.getConnection();
            String updateQuery = "UPDATE student_details SET name=?, course=?, branch=? WHERE student_id=?";
            PreparedStatement pst = con.prepareStatement(updateQuery);
            pst.setString(1, studentName);
            pst.setString(2, course);
            pst.setString(3, branch);
            pst.setInt(4, studentID);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                isUpdated = true;
                JOptionPane.showMessageDialog(this, "Student details updated successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
    }

    // Delete student from student_details table
    public boolean deleteStudent() {
        boolean isDeleted = false;

        // Validation: ID should be a number
        try {
            studentID = Integer.parseInt(txt_studentID.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Student ID must be a number.");
            return false;
        }

        // Delete student details
        try {
            Connection con = DBconnection.getConnection();
            String deleteQuery = "DELETE FROM student_details WHERE student_id=?";
            PreparedStatement pst = con.prepareStatement(deleteQuery);
            pst.setInt(1, studentID);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                isDeleted = true;
                JOptionPane.showMessageDialog(this, "Student deleted successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    // Clear form fields
    public void clearForm() {
        txt_studentID.setText("");
        txt_StudentName.setText("");
        cmb_course.setSelectedIndex(0);
        cmb_branch.setSelectedIndex(0);
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_studentID = new app.bolivia.swing.JCTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_StudentName = new app.bolivia.swing.JCTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle3 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle4 = new rojerusan.RSMaterialButtonCircle();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmb_course = new javax.swing.JComboBox<>();
        cmb_branch = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_StudentDetails = new rojeru_san.complementos.RSTableMetro();
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
        jLabel2.setText("Enter Student ID");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, -1, -1));

        txt_studentID.setBackground(new java.awt.Color(255, 204, 0));
        txt_studentID.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_studentID.setPlaceholder("Enter Student ID");
        jPanel3.add(txt_studentID, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, 50));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Moleskine_26px.png"))); // NOI18N
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, 50));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Enter Student Name");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 270, -1, -1));

        txt_StudentName.setBackground(new java.awt.Color(255, 204, 0));
        txt_StudentName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        txt_StudentName.setPlaceholder("Enter Student Name");
        jPanel3.add(txt_StudentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 310, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, -1, 50));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Course");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 380, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Unit_26px.png"))); // NOI18N
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, -1, 50));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Branch");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 480, -1, -1));

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

        cmb_course.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CSE", "DSE", "HNDSE", "DBIS" }));
        jPanel3.add(cmb_course, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 420, 210, -1));

        cmb_branch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kandy", "Colombo", "Galle", "Kurunagela", " " }));
        jPanel3.add(cmb_branch, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 530, 230, -1));

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

        tbl_StudentDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Student ID", " Name", "Course", "Branch"
            }
        ));
        tbl_StudentDetails.setColorBackgoundHead(new java.awt.Color(255, 204, 0));
        tbl_StudentDetails.setColorBordeFilas(new java.awt.Color(102, 102, 102));
        tbl_StudentDetails.setColorBordeHead(new java.awt.Color(102, 102, 102));
        tbl_StudentDetails.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tbl_StudentDetails.setColorFilasForeground1(new java.awt.Color(102, 102, 102));
        tbl_StudentDetails.setColorFilasForeground2(new java.awt.Color(255, 204, 0));
        tbl_StudentDetails.setColorSelBackgound(new java.awt.Color(255, 204, 0));
        tbl_StudentDetails.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_StudentDetails.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tbl_StudentDetails.setMinimumSize(new java.awt.Dimension(60, 20));
        tbl_StudentDetails.setRowHeight(40);
        tbl_StudentDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_StudentDetailsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_StudentDetails);
        if (tbl_StudentDetails.getColumnModel().getColumnCount() > 0) {
            tbl_StudentDetails.getColumnModel().getColumn(0).setResizable(false);
            tbl_StudentDetails.getColumnModel().getColumn(1).setResizable(false);
            tbl_StudentDetails.getColumnModel().getColumn(2).setResizable(false);
            tbl_StudentDetails.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, 160));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/adminIcons/icons8_Books_26px.png"))); // NOI18N
        jLabel8.setText("Manage Students");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 270, 40));

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

    private void tbl_StudentDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_StudentDetailsMouseClicked
      int selectedRow = tbl_StudentDetails.getSelectedRow();
        TableModel model = tbl_StudentDetails.getModel();
        txt_studentID.setText(model.getValueAt(selectedRow, 0).toString());
        txt_StudentName.setText(model.getValueAt(selectedRow, 1).toString());
        cmb_course.setSelectedItem(model.getValueAt(selectedRow, 2).toString());
        cmb_branch.setSelectedItem(model.getValueAt(selectedRow, 3).toString());
    }//GEN-LAST:event_tbl_StudentDetailsMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
         HomePage home = new HomePage();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
         if (addStudent()) {
            setStudentDetailsTable();
            clearForm();
        }
        
        
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        if (updateStudent()) {
            setStudentDetailsTable();
            clearForm();
        }
        
    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void rSMaterialButtonCircle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle4ActionPerformed
          if (deleteStudent()) {
            setStudentDetailsTable();
            clearForm();
        }
    }//GEN-LAST:event_rSMaterialButtonCircle4ActionPerformed

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
       System.exit(0);
    }//GEN-LAST:event_jLabel6MouseClicked

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
            java.util.logging.Logger.getLogger(ManageStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageStudent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageStudent().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmb_branch;
    private javax.swing.JComboBox<String> cmb_course;
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
    private rojeru_san.complementos.RSTableMetro tbl_StudentDetails;
    private app.bolivia.swing.JCTextField txt_StudentName;
    private app.bolivia.swing.JCTextField txt_studentID;
    // End of variables declaration//GEN-END:variables
}
