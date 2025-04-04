// middlewares/fileUpload.js
const multer = require('multer');
const path = require('path');
const fs = require('fs').promises;
const AppError = require('../utils/AppError');

const uploadDir = path.join(__dirname, '../uploads');

const ensureUploadDir = async () => {
  try {
    await fs.mkdir(uploadDir, { recursive: true });
    console.log('Upload directory ensured.');
  } catch (err) {
    console.error('Error ensuring upload directory:', err);
    process.exit(1); // Exit on failure to create directory
  }
};

const setupMulter = async () => {
  try { // Add try...catch around the ENTIRE setup
    await ensureUploadDir();

    const storage = multer.diskStorage({
      destination: (req, file, cb) => cb(null, uploadDir),
      filename: (req, file, cb) => {
        const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
        const ext = path.extname(file.originalname);
        cb(null, uniqueSuffix + ext);
      }
    });

    const fileFilter = (req, file, cb) => {
      const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
      if (!allowedTypes.includes(file.mimetype)) {
        return cb(new AppError('Invalid file type. Only JPEG, PNG, and GIF are allowed.', 400), false);
      }
      cb(null, true);
    };

    const upload = multer({
      storage: storage,
      limits: { fileSize: 5 * 1024 * 1024 },
      fileFilter: fileFilter
    });

    const logFileUploadRequest = (req, res, next) => {
      console.log('\n--- File Upload Request ---');
      console.log('Content-Type:', req.headers['content-type']);
      console.log('Has Files (req.files):', !!req.files);
      console.log('Has File (req.file):', !!req.file);
      console.log('Body before multer:', req.body);
      next();
    };

    const deleteOldFile = async (filePath) => {
        try {
            if(filePath){
                await fs.unlink(path.join(__dirname, '..', filePath));
            }
        } catch (error) {
            if(error.code !== 'ENOENT'){
                console.error(`Error deleting file ${filePath}`, error)
            }
        }
    };

    return { upload, deleteOldFile, logFileUploadRequest };

  } catch (error) {
    console.error("Error during multer setup:", error); // Log the error
    throw error; // Re-throw the error to be caught by app.js
  }
};

module.exports = setupMulter();