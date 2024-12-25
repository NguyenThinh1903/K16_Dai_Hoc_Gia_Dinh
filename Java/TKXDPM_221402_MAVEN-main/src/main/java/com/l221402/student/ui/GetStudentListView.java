package com.l221402.student.ui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.l221402.student.usecase.GetStudentListOutputDTO;

public class GetStudentListView {

    private List<GetStudentListViewModel> students = null;

    public  void createAndShowGUI(List<GetStudentListOutputDTO> students) {
        JFrame frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);

        // Create title label
        JLabel titleLabel = new JLabel("DANH SÁCH SINH VIÊN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding around the title

        // Column headers for the JTable
        String[] columns = {
                "STT", "Họ tên", "Địa chỉ", "Ngày sinh", "Điểm trung bình", "Học lực", "Ngành"
        };

        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Sample list of students
        //List<Student> students = getStudentList();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Add student data to the table
        for (int i = 0; i < students.size(); i++) {
            GetStudentListOutputDTO student = students.get(i);
            Object[] row = {
                    i + 1,
                    student.getHoTen(),
                    student.getDiaChi(),
                    sdf.format(student.getNgaySinh()),
                    student.getDiemTB(),
                    student.getHocLuc(),
                    student.getNganh()
            };
            tableModel.addRow(row);
        }

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Set up layout for the frame
        frame.setLayout(new BorderLayout());
        frame.add(titleLabel, BorderLayout.NORTH); // Add title label at the top
        frame.add(scrollPane, BorderLayout.CENTER); // Add the table in the center

        // Make the frame visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public  void createAndShowGUI2() {
        JFrame frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);

        // Create title label
        JLabel titleLabel = new JLabel("DANH SÁCH SINH VIÊN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding around the title

        // Column headers for the JTable
        String[] columns = {
                "STT", "Họ tên", "Địa chỉ", "Ngày sinh", "Điểm trung bình", "Học lực", "Ngành"
        };

        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Sample list of students
        //List<Student> students = getStudentList();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // // Add student data to the table
        // for (int i = 0; i < students.size(); i++) {
        //     GetStudentListOutputDTO student = students.get(i);
        //     Object[] row = {
        //             i + 1,
        //             student.getHoTen(),
        //             student.getDiaChi(),
        //             sdf.format(student.getNgaySinh()),
        //             student.getDiemTB(),
        //             student.getHocLuc(),
        //             student.getNganh()
        //     };
        //     tableModel.addRow(row);
        // }

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Set up layout for the frame
        frame.setLayout(new BorderLayout());
        frame.add(titleLabel, BorderLayout.NORTH); // Add title label at the top
        frame.add(scrollPane, BorderLayout.CENTER); // Add the table in the center

        // Make the frame visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public  void createAndShowGUI3(List<GetStudentListViewModel> students) {
        this.students = students;
        JFrame frame = new JFrame("Student Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);

        // Create title label
        JLabel titleLabel = new JLabel("DANH SÁCH SINH VIÊN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding around the title

        // Column headers for the JTable
        String[] columns = {
                "STT", "Họ tên", "Địa chỉ", "Ngày sinh", "Điểm trung bình", "Học lực", "Ngành"
        };

        


        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        //custom
        table.setDefaultRenderer(Object.class, new StudentCellRenderer());

        // Sample list of students
        //List<Student> students = getStudentList();

        // Add student data to the table
        for (int i = 0; i < students.size(); i++) {
            GetStudentListViewModel student = students.get(i);
            Object[] row = {
                    i + 1,
                    student.hoTen,
                    student.diaChi,
                    student.ngaySinh,
                    student.diemTB,
                    student.hocLuc,
                    student.nganh
            };
            tableModel.addRow(row);
        }

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Set up layout for the frame
        frame.setLayout(new BorderLayout());
        frame.add(titleLabel, BorderLayout.NORTH); // Add title label at the top
        frame.add(scrollPane, BorderLayout.CENTER); // Add the table in the center

        // Make the frame visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        GetStudentListView getStudentListForm = new GetStudentListView();
        getStudentListForm.createAndShowGUI2();
        
    }

    class  StudentCellRenderer extends DefaultTableCellRenderer{
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
                   
                    
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    GetStudentListViewModel vm = students.get(row);
        
                    if(column == 5){
                        c.setForeground(vm.textColor);
                         if(vm.bold){
                             c.setFont(c.getFont().deriveFont(Font.BOLD));
                         }
        
                         if (vm.bold && vm.italic) {
                             c.setFont(c.getFont().deriveFont(Font.BOLD | Font.ITALIC));
                         }
                     
                    }else{
                        c.setForeground(Color.BLACK);
                    }
                     return c;
                }
        }

}