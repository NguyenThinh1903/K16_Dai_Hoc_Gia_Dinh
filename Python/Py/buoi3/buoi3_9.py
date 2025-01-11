import math

def generate_expression(x, n):
  expression = f"S({x}, {n}) = "
  for i in range(n + 1):
    power = 2 * i + 1
    if i == 0:
      numerator = f"{x}"
      term = numerator 
    else:
      numerator = f"({x}^{power})"
      denominator = f"{power}!"
      term = f"{numerator}/{denominator}"

    if i == 0:
      expression += term
    else:
      expression += " + " + term
  return expression

def calculate_S(x, n):
  S = 0
  for i in range(n + 1):
    mu = 2 * i + 1
    giai_thua = math.factorial(mu)
    gia_tri = x**mu / giai_thua
    S += gia_tri
  return S

x = int(input("Nhập giá trị của x: "))
n = int(input("Nhập giá trị của n: "))
expression = generate_expression(x, n)
result = calculate_S(x, n)

print(expression)
print(f"Kết quả của S({x},{n}) là: {result}")