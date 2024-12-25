using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Combobox_Csharp
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        private void Form1_Load(object sender, EventArgs e)
        {
            // Thêm danh sách màu vào ComboBox
            cboColors.Items.Add("Đỏ");
            cboColors.Items.Add("Xanh");
            // Thêm các màu khác tương tự...

            // Thêm danh sách cỡ chữ vào ComboBox
            cboFontSizes.Items.Add("12");
            cboFontSizes.Items.Add("14");
            // Thêm các cỡ chữ khác tương tự...
        }
        private void btnPrint_Click(object sender, EventArgs e)
        {
            // Lấy màu và cỡ chữ được chọn
            string selectedColor = cboColors.SelectedItem?.ToString();
            string selectedFontSize = cboFontSizes.SelectedItem?.ToString();

            if (!string.IsNullOrEmpty(selectedColor) && !string.IsNullOrEmpty(selectedFontSize))
            {
                // Đặt màu và cỡ chữ cho Label
                lblText.ForeColor = Color.FromName(selectedColor);
                lblText.Font = new Font(lblText.Font.FontFamily, float.Parse(selectedFontSize));

                // Hiển thị dòng chữ
                lblText.Text = "Hoàng Sa, Trường Sa";
            }
            else
            {
                MessageBox.Show("Vui lòng chọn màu và cỡ chữ.");
            }
        }

        private void Form1_Load_1(object sender, EventArgs e)
        {

        }
    }
}

