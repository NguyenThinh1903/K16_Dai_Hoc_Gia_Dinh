import 'package:flutter/material.dart';
import 'models/Product.dart';

class ProductEdit extends StatefulWidget {
  final Product product;
  final void Function(Product) onSave;

  const ProductEdit({Key? key, required this.product, required this.onSave})
      : super(key: key);

  @override
  Trangthai_ProductEdit createState() => Trangthai_ProductEdit();
}

class Trangthai_ProductEdit extends State<ProductEdit> {
  final _formKey = GlobalKey<FormState>();
  late TextEditingController _titleController;
  late TextEditingController _descriptionController;
  late TextEditingController _categoryController;
  late TextEditingController _imageController;
  late TextEditingController _priceController;
  late TextEditingController _sizeController;

  @override
  void initState() {
    super.initState();
    _titleController = TextEditingController(text: widget.product.title);
    _descriptionController =
        TextEditingController(text: widget.product.description);
    _categoryController = TextEditingController(text: widget.product.category);
    _imageController = TextEditingController(text: widget.product.image);
    _priceController =
        TextEditingController(text: widget.product.price?.toString());
    _sizeController =
        TextEditingController(text: (widget.product.size ?? 0).toString());
  }

  @override
  void dispose() {
    _titleController.dispose();
    _descriptionController.dispose();
    _categoryController.dispose();
    _imageController.dispose();
    _priceController.dispose();
    _sizeController.dispose();
    super.dispose();
  }

  void _saveProduct() {
    if (_formKey.currentState!.validate()) {
      widget.onSave(
        Product(
          id: widget.product.id,
          title: _titleController.text,
          description: _descriptionController.text,
          category: _categoryController.text,
          image: _imageController.text,
          price: double.parse(_priceController.text),
          size: 0,
        ),
      );
      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: const Text("Hiệu chỉnh sản phẩm"),
      content: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                controller: _titleController,
                decoration: const InputDecoration(labelText: "Tên sản phẩm"),
                validator: (value) =>
                    value!.isEmpty ? "Không được để trống" : null,
              ),
              TextFormField(
                controller: _descriptionController,
                decoration: const InputDecoration(labelText: "Mô tả"),
              ),
              TextFormField(
                controller: _categoryController,
                decoration: const InputDecoration(labelText: "Danh mục"),
              ),
              TextFormField(
                controller: _imageController,
                decoration: const InputDecoration(labelText: "URL Hình ảnh"),
              ),
              TextFormField(
                controller: _priceController,
                decoration: const InputDecoration(labelText: "Giá"),
                keyboardType: TextInputType.number,
              ),
              TextFormField(
                controller: _sizeController,
                decoration: const InputDecoration(labelText: "Kích thước"),
              ),
            ],
          ),
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.pop(context),
          child: const Text("Hủy"),
        ),
        ElevatedButton(
          onPressed: _saveProduct,
          child: const Text("Lưu"),
        ),
      ],
    );
  }
}
