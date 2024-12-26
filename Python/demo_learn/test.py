import turtle

# Thiết lập màn hình
screen = turtle.Screen()
screen.bgcolor("white")

# Hàm vẽ một cánh hoa
def draw_petal(t, radius, angle):
    for _ in range(2):
        t.circle(radius, angle)
        t.left(180 - angle)

# Hàm vẽ hoa hướng dương
def draw_sunflower(t, petals, radius, angle):
    t.color("pink", "pink")  # Màu cánh hoa
    t.begin_fill()
    for _ in range(petals):
        draw_petal(t, radius, angle)
        t.left(360 / petals)
    t.end_fill()

# Hàm vẽ nhụy hoa
def draw_center(t, radius):
    t.penup()
    t.goto(0, -radius)
    t.pendown()
    t.color("purple")
    t.begin_fill()
    t.circle(radius)
    t.end_fill()

# Hàm vẽ thân cây
def draw_stem(t, length):
    t.color("green")
    t.right(90)
    t.forward(length)

# Hàm vẽ lá cây
def draw_leaf(t, leaf_angle, leaf_length):
    t.color("green")
    t.begin_fill()
    t.left(leaf_angle)
    t.forward(leaf_length)
    t.left(45)
    t.forward(leaf_length)
    t.left(135)
    t.forward(leaf_length)
    t.left(45)
    t.forward(leaf_length)
    t.left(135)
    t.end_fill()

# Hàm vẽ chữ chúc mừng
def draw_text(text, x, y, font_size):
    pen = turtle.Turtle()
    pen.hideturtle()
    pen.penup()
    pen.goto(x, y)
    pen.pendown()
    pen.color("blue")
    pen.write(text, align="center", font=("Arial", font_size, "bold"))

# Khởi tạo turtle để vẽ hoa hướng dương
sunflower = turtle.Turtle()
sunflower.shape("turtle")
sunflower.speed(10)

# Vẽ cánh hoa hướng dương
draw_sunflower(sunflower, petals=20, radius=100, angle=60)

# Vẽ nhụy hoa ở giữa
draw_center(sunflower, radius=50)

# Di chuyển xuống để vẽ thân và lá
sunflower.penup()
sunflower.goto(0, -100)
sunflower.pendown()

# Vẽ thân cây
draw_stem(sunflower, 300)

# Vẽ lá hai bên thân cây
# Irregularly positioned leaf on the left
sunflower.penup()
sunflower.goto(-30, -180)  # Adjusted position for irregularity
sunflower.setheading(0)
sunflower.pendown()
draw_leaf(sunflower, 60, 50)  # Adjusted leaf angle

# Irregularly positioned leaf on the right
sunflower.penup()
sunflower.goto(40, -220)  # Adjusted position for irregularity
sunflower.setheading(180)
sunflower.pendown()
draw_leaf(sunflower, -50, 55)  # Adjusted leaf angle and length

# Thêm dòng chữ chúc mừng
draw_text("Tặng một bông hoa nhân ngày 20/10 cho người đẹp", 0, -310, 16)
draw_text("Chúc hạnh phúc vui vẻ xinh tươi", 0, -330, 16)

# Giữ cửa sổ để xem kết quả
turtle.done()
