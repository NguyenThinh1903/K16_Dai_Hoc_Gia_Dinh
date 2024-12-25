using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace bt1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void btSub_Click(object sender, EventArgs e)
        {
            int a, b;
            if (!int.TryParse(this.tbA.Text, out a) || !int.TryParse(this.tbB.Text, out b))
            {
                MessageBox.Show("Vui lòng nhập số hợp lệ.");
                return;
            }

            // UCLN
            int ucln = UCLN(a, b);
            this.tbUCLN.Text = ucln.ToString();

            // BCNN
            int bcnn = BCNN(a, b);
            this.tbBCNN.Text = bcnn.ToString();
        }

        private int UCLN(int a, int b)
        {
            while (a != b)
            {
                if (a > b)
                {
                    a = a - b;
                }
                else
                {
                    b = b - a;
                }
            }
            return a;
        }

        private int BCNN(int a, int b)
        {
            int bcnn = (a > b) ? a : b;
            while (true)
            {
                if (bcnn % a == 0 && bcnn % b == 0)
                {
                    break;
                }
                bcnn++;
            }
            return bcnn;
        }

        private void btClear_Click(object sender, EventArgs e)
        {
            this.tbA.Text = "";
            this.tbB.Text = "";
            this.tbUCLN.Text = "";
            this.tbBCNN.Text = "";
        }

        private void btExit_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
