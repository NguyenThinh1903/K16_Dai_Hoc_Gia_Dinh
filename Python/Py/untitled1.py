t = int(input("Nhập số giây: "))

hour = (t // 3600) % 24
minute = (t % 3600) % 60
second = (t%3600)%60

period = "AM"
if hour >= 12:
    period = "PM"
    if hour > 12:
        hour -= 12
if hour == 0:
    hour = 12  

print(f"{hour}:{minute:}:{second:} {period}")
