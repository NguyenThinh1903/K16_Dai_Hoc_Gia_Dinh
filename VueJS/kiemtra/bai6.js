document.addEventListener('DOMContentLoaded', () => {
    const productTable = document.getElementById('productTable');
    const totalElement = document.getElementById('total');
    const priceFilter = document.getElementById('priceFilter');

    const fetchProducts = () => {
        return new Promise((resolve) => {
            setTimeout(() => {
                const products = [
                    { name: 'iPhone 9', price: 700 },
                    { name: 'Samsung', price: 400 },
                    { name: 'Nokia', price: 100 },
                    { name: 'Sony Xperia', price: 450 },
                    { name: 'Motorola', price: 180 },
                    { name: 'Oppo', price: 600 },
                    { name: 'bPhone', price: 90 }
                ];
                resolve(products);
            }, 1000);
        });
    };

    const filterProducts = (products, maxPrice) => {
        return products.filter(product => product.price <= maxPrice);
    };

    const renderProducts = async () => {
        try {
            const products = await fetchProducts();
            productTable.innerHTML = "";

            const maxPrice = getMaxPrice(priceFilter.value);
            const filteredProducts = filterProducts(products, maxPrice);

            filteredProducts.forEach(product => {
                let row = productTable.insertRow();
                let checkBoxCell = row.insertCell(0);
                let checkBox = document.createElement('input');
                checkBox.type = 'checkbox';
                checkBoxCell.appendChild(checkBox);

                let nameCell = row.insertCell(1);
                nameCell.textContent = product.name;

                let priceCell = row.insertCell(2);
                priceCell.textContent = product.price;

                let quantityCell = row.insertCell(3);
                let quantityInput = document.createElement('input');
                quantityInput.type = 'number';
                quantityInput.min = 0;
                quantityInput.value = 0;
                quantityInput.disabled = true;
                quantityCell.appendChild(quantityInput);

                let totalCell = row.insertCell(4);
                totalCell.textContent = '0';

                checkBox.addEventListener('change', () => {
                    if (checkBox.checked) {
                        quantityInput.disabled = false;
                        quantityInput.focus();
                    } else {
                        quantityInput.disabled = true;
                        quantityInput.value = 0;
                        totalCell.textContent = '0';
                        updateTotal();
                    }
                });

                quantityInput.addEventListener('input', () => {
                    updateRowTotal(row, product.price);
                    updateTotal();
                });
            });

            updateTotal();
        } catch (error) {
            console.error('Error fetching products:', error);
            productTable.innerHTML = "<tr><td colspan='5'>Đã có lỗi xảy ra khi tải dữ liệu sản phẩm.</td></tr>";
        }
    };

    const updateRowTotal = (row, price) => {
        let quantityInput = row.cells[3].querySelector('input');
        let totalCell = row.cells[4];
        let quantity = parseInt(quantityInput.value) || 0;
        let totalPrice = quantity * price;
        totalCell.textContent = `${totalPrice}`;
    };

    const updateTotal = () => {
        let total = 0;
        for (let i = 0; i < productTable.rows.length; i++) {
            let row = productTable.rows[i];
            let rowTotal = parseInt(row.cells[4].textContent) || 0;
            total += rowTotal;
        }
        totalElement.textContent = total;
    };

    const getMaxPrice = (filterValue) => {
        switch (filterValue) {
            case 'under200':
                return 200;
            case '200to500':
                return 500;
            case 'above500':
                return Infinity;
            default:
                return Infinity; // Tất cả
        }
    };

    priceFilter.addEventListener('change', () => {
        renderProducts();
    });

    renderProducts();
});
