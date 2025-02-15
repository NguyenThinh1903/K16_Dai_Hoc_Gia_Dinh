import re  # Import thư viện regular expression (nếu cần xử lý dấu câu)

def is_palindrome(s):
    """Kiểm tra chuỗi đối xứng (bỏ qua dấu câu, dấu cách, chữ hoa/thường)."""
    processed_string = re.sub(r'[^a-zA-Z0-9]', '', s).lower()
    return processed_string == processed_string[::-1]


while True:
    text = input("Nhập một chuỗi: ")
    if is_palindrome(text):
        print("Chuỗi này là chuỗi đối xứng.")
    else:
        print("Chuỗi này không phải là chuỗi đối xứng.")

    choice = input("Bạn có muốn tiếp tục không? (c/k): ").lower()
    if choice == 'k':
        print("Cảm ơn bạn đã sử dụng chương trình!")
        break  # Thoát khỏi vòng lặp while
    elif choice != 'c':
        print("Lựa chọn không hợp lệ. Chương trình sẽ tiếp tục.")
        # Không cần continue ở đây, vì nó sẽ tự động quay lại đầu vòng lặp while