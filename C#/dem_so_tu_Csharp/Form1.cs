using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace dem_so_tu_Csharp
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void btnCountWords_Click(object sender, EventArgs e)
        {
            // Lấy nội dung từ textBoxInput
            string inputText = textBoxInput.Text.ToLower(); // Chuyển thành chữ thường để không phân biệt hoa thường

            // Tách các từ
            string[] words = inputText.Split(new char[] { ' ', '\t', '\n', '\r' }, StringSplitOptions.RemoveEmptyEntries);

            // Đếm số lần xuất hiện của từ
            Dictionary<string, int> wordCounts = new Dictionary<string, int>();
            foreach (string word in words)
            {
                if (wordCounts.ContainsKey(word))
                {
                    wordCounts[word]++;
                }
                else
                {
                    wordCounts[word] = 1;
                }
            }

            // Hiển thị kết quả trong listBoxResult
            listBoxResult.Items.Clear();
            foreach (var pair in wordCounts)
            {
                listBoxResult.Items.Add($"{pair.Key}: {pair.Value} lần");
            }
        }

        private void btnExit_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
