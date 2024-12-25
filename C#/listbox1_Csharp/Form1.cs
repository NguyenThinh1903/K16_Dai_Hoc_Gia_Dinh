using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace listbox1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        private void Form1_Load(object sender, EventArgs e)
        {
            // Thêm danh sách tên sinh viên vào ListBox
            lstStudents.Items.Add("Nguyễn Văn A");
            lstStudents.Items.Add("Trần Thị B");
            lstStudents.Items.Add("Trần Thị C");
            // Thêm các sinh viên khác tương tự...
        }
        private string GetNgaySinh(string tenSinhVien)
        {
            // Thực hiện logic để lấy ngày sinh từ tên sinh viên (ví dụ: từ CSDL hoặc danh sách cố định)
            // Trả về ngày sinh tương ứng
            if (tenSinhVien == "Nguyễn Văn A")
            {
                return "1/1/1990";
            }
            else if (tenSinhVien == "Trần Thị B")
            {
                return "2/2/2000";
            }
            else if (tenSinhVien == "Trần Thị C")
            {
                return "3/3/2000";
            }
            // Xử lý các tên sinh viên khác tương tự...
            else
            {
                return "Không xác định";
            }
        }


        private void btnDisplay_Click(object sender, EventArgs e)
        {
            // Lấy tên sinh viên được chọn
            string selectedStudent = lstStudents.SelectedItem?.ToString();

            if (!string.IsNullOrEmpty(selectedStudent))
            {
                // Hiển thị thông tin sinh viên (ví dụ: thông qua MessageBox)
                string ngaySinh = GetNgaySinh(selectedStudent); // Hàm GetNgaySinh trả về ngày sinh tương ứng với tên sinh viên
                MessageBox.Show($"Thông tin sinh viên:\nTên: {selectedStudent}\nNgày sinh: {ngaySinh}");
            }
            else
            {
                MessageBox.Show("Vui lòng chọn một sinh viên từ danh sách.");
            }
        }

        private void btnExit_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
