namespace Combobox_Csharp
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.cboColors = new System.Windows.Forms.ComboBox();
            this.cboFontSizes = new System.Windows.Forms.ComboBox();
            this.btnPrint = new System.Windows.Forms.Button();
            this.lblText = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // cboColors
            // 
            this.cboColors.FormattingEnabled = true;
            this.cboColors.Items.AddRange(new object[] {
            "Đỏ",
            "Xanh "});
            this.cboColors.Location = new System.Drawing.Point(136, 86);
            this.cboColors.Name = "cboColors";
            this.cboColors.Size = new System.Drawing.Size(121, 21);
            this.cboColors.TabIndex = 0;
            // 
            // cboFontSizes
            // 
            this.cboFontSizes.FormattingEnabled = true;
            this.cboFontSizes.Items.AddRange(new object[] {
            "12",
            "14"});
            this.cboFontSizes.Location = new System.Drawing.Point(411, 86);
            this.cboFontSizes.Name = "cboFontSizes";
            this.cboFontSizes.Size = new System.Drawing.Size(125, 21);
            this.cboFontSizes.TabIndex = 1;
            // 
            // btnPrint
            // 
            this.btnPrint.Location = new System.Drawing.Point(283, 172);
            this.btnPrint.Name = "btnPrint";
            this.btnPrint.Size = new System.Drawing.Size(75, 23);
            this.btnPrint.TabIndex = 2;
            this.btnPrint.Text = "Thực Hiện ";
            this.btnPrint.UseVisualStyleBackColor = true;
            this.btnPrint.Click += new System.EventHandler(this.btnPrint_Click);
            // 
            // lblText
            // 
            this.lblText.AutoSize = true;
            this.lblText.Location = new System.Drawing.Point(243, 267);
            this.lblText.Name = "lblText";
            this.lblText.Size = new System.Drawing.Size(35, 17);
            this.lblText.TabIndex = 3;
            this.lblText.Text = "label1";
            this.lblText.UseCompatibleTextRendering = true;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.lblText);
            this.Controls.Add(this.btnPrint);
            this.Controls.Add(this.cboFontSizes);
            this.Controls.Add(this.cboColors);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load_1);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox cboColors;
        private System.Windows.Forms.ComboBox cboFontSizes;
        private System.Windows.Forms.Button btnPrint;
        private System.Windows.Forms.Label lblText;
    }
}

