class HinhChuNhat:
    cd = 1.0
    cr = 1.0
    def dt (self):
        return self.cd*self.cr
    def cv(self):
        return (self.cd+self.cr)*2

#main
hcn = HinhChuNhat()
print("Dien tich hinh chu nhat: ", hcn.dt())
