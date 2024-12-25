using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace checkbox_Csharp
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (this.CheckName())
            {
                this.Clear();
                List<String> result = this.GetResult();
                foreach (String s in result)
                {
                    listBox1.Items.Add(s);
                    textBox3.Text = textBox3.Text + s + "\n";

                }
                this.WriteFile("luuthongtin.txt", textBox3.Text);
            }
        }

        private void Clear()
        {
            listBox1.Items.Clear();
            textBox3.Clear();
        }

        private List<String> GetResult()
        {
            List<String> result = new List<String>();
            result.Add("Họ và tên: " + textBox1.Text);
            result.Add("Lớp: " + textBox2.Text);

            if (radioButton1.Checked)
            {
                result.Add("Giới tính: Nam");
            }
            else if (radioButton2.Checked)
            {
                result.Add("Giới tính: Nữ");
            }

            if (checkBox1.Checked)
            {
                result.Add("Lập trình trực quan");
            }
            if (checkBox2.Checked)
            {
                result.Add("Lập trình web");
            }
            if (checkBox3.Checked)
            {
                result.Add("Lập trình quản lý");
            }
            if (checkBox4.Checked)
            {
                result.Add("An ninh mạng");
            }
            

            return result;
        }

        private void WriteFile(string path, string text)
        {
            using (StreamWriter writer = new StreamWriter(path))
            {
                writer.WriteLine(text);
            }
        }

        private void MessageError(string msg)
        {
            MessageBox.Show(msg, "THÔNG BÁO", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        private bool CheckName()
        {
            if (string.IsNullOrEmpty(textBox1.Text))
            {
                MessageError("Vui lòng nhập Họ và Tên");
                textBox1.Focus();
                return false;
            }
            if (string.IsNullOrEmpty(textBox2.Text))
            {
                MessageError("Vui lòng nhập Lớp");
                textBox2.Focus();
                return false;
            }
            return true;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}
