import os
import time
import multiprocessing as mp
from tqdm import tqdm
from functools import partial

def process_file(filepath, output_dir):
    """Process a single file and return its content with header"""
    try:
        with open(filepath, 'r', encoding='utf-8', errors='replace') as infile:
            content = f"// {filepath}\n" + infile.read() + "\n\n"
            return content
    except Exception as e:
        return f"// Error reading file: {filepath} - {str(e)}\n\n"

def export_code_to_txt(project_root, output_file_path, extensions=None, blacklist_dirs=None):
    """
    Exports all code files from a project directory to a single text file.
    Uses multiprocessing for improved performance.

    Args:
        project_root (str): Path to the root directory of the project.
        output_file_path (str): Path to the output text file.
        extensions (list, optional): List of file extensions to include.
        blacklist_dirs (set, optional): Set of directory names to skip.
    """
    if extensions is None:
        extensions = ['.cjs', '.js', '.py', '.java', '.html', '.css', '.php', '.c', '.cpp', '.go', '.rs', '.ts', '.jsx', '.tsx', '.vue']
    
    if blacklist_dirs is None:
        blacklist_dirs = {'node_modules', '.git', '__pycache__', 'venv', 'env', '.vscode', '.idea'}
    
    # Convert all extensions to lowercase with leading dots for consistency
    extensions = {ext.lower() if ext.startswith('.') else f'.{ext.lower()}' for ext in extensions}
    
    print(f"Scanning folder: {project_root}")
    
    # Single pass to find all matching files
    matching_files = []
    for root, dirs, files in os.walk(project_root):
        # Modify dirs in-place to skip blacklisted directories
        dirs[:] = [d for d in dirs if d not in blacklist_dirs]
        
        for file in files:
            ext = os.path.splitext(file)[1].lower()
            if ext in extensions:
                matching_files.append(os.path.join(root, file))
    
    total_files = len(matching_files)
    print(f"Found {total_files} files to process")
    
    # Use a pool of workers to process files in parallel
    num_cpus = max(1, mp.cpu_count() - 1)  # Leave one CPU free
    print(f"Using {num_cpus} CPU cores for processing")
    
    # Create a partial function with the output directory
    process_func = partial(process_file, output_dir=os.path.dirname(output_file_path))
    
    # Process files in chunks to reduce memory usage
    chunk_size = 100
    results = []
    
    with open(output_file_path, 'w', encoding='utf-8') as outfile:
        with tqdm(total=total_files, desc="Exporting files") as pbar:
            for i in range(0, total_files, chunk_size):
                chunk = matching_files[i:i+chunk_size]
                
                with mp.Pool(processes=num_cpus) as pool:
                    for result in pool.imap_unordered(process_func, chunk):
                        if result:
                            outfile.write(result)
                            pbar.update(1)

if __name__ == "__main__":
    # Simple menu for folder selection
    print("\nCODE EXPORTER (OPTIMIZED)")
    print("=======================")
    print("Select folder to scan:")
    print("1. Backend")
    print("2. Frontend")
    
    while True:
        choice = input("\nEnter your choice (1 or 2): ")
        if choice == "1":
            folder_name = "backend"
            break
        elif choice == "2":
            folder_name = "frontend"
            break
        else:
            print("Invalid choice. Please enter 1 or 2.")
    
    # Get the current directory (where the script is running)
    current_dir = os.path.dirname(os.path.abspath(__file__))
    parent_dir = os.path.dirname(current_dir)  # Go up one level if needed
    misc_dir = os.path.dirname(parent_dir)  # Go up another level to find the project root

    # Determine the target folder path based on the current directory structure
    if os.path.exists(os.path.join(misc_dir, folder_name)):
        # If frontend/backend is at the root level
        target_folder = os.path.join(misc_dir, folder_name)
    elif os.path.exists(os.path.join(parent_dir, folder_name)):
        # If frontend/backend is one level up
        target_folder = os.path.join(parent_dir, folder_name)
    elif os.path.exists(os.path.join(current_dir, folder_name)):
        # If frontend/backend is in the same directory as the script
        target_folder = os.path.join(current_dir, folder_name)
    else:
        print(f"Error: Could not locate the {folder_name} folder")
        exit(1)
    
    # Set up file extensions based on folder type
    if folder_name == "frontend":
        code_extensions = ['.js', '.ts', '.jsx', '.tsx', '.vue', '.html', '.css']
        output_txt_file = "exported_frontend_code.txt"
    else:  # backend
        code_extensions = ['.js', '.ts', '.py', '.java', '.php', '.go', '.rs', '.c', '.cpp']
        output_txt_file = "exported_backend_code.txt"
    
    # Additional directories to exclude based on project type
    if folder_name == "frontend":
        blacklist = {'node_modules', '.git', 'dist', 'build', 'coverage'}
    else:
        blacklist = {'node_modules', '.git', '__pycache__', 'venv', 'env', 'dist'}
    
    start_time = time.time()
    export_code_to_txt(target_folder, output_txt_file, code_extensions, blacklist)
    elapsed_time = time.time() - start_time
    
    print(f"Code exported to: {output_txt_file}")
    print(f"Export completed in {elapsed_time:.2f} seconds")