# main.py
import sys
from game_launcher import GameLauncher
from view.menu import StartMenu

def main():
    menu = StartMenu()
    launcher = GameLauncher(menu)
    launcher.run()
    sys.exit(0)

if __name__ == "__main__":
    main()