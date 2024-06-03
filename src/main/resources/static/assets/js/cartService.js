// Lógica del carrito
class Cart {
    constructor() {
        const storedCart = JSON.parse(localStorage.getItem('cart'));
        if (storedCart) {
            this.carItems = storedCart.carItems;
            this.quantityProduct = storedCart.quantityProduct;
        } else {
            this.carItems = [];
            this.quantityProduct = 0;
        }
    }

    static getInstance() {
        return Cart.instance || new Cart();
    }
}

class CarItem {
    constructor(shoe, quantity, price,size) {
        this.shoe = shoe;
        this.quantity = quantity;
        this.price = price;
        this.size=size;
    }

    getPriceQuantity() {
        return this.quantity * this.price;
    }
}

// Crear una instancia de Cart
const cart = Cart.getInstance();

function addToCart(shoe,size) {
  if (size.trim() === '') {
          Swal.fire({
              icon: 'warning',
              title: '¡Selecciona una talla!',
              text: 'Por favor, selecciona una talla antes de agregar al carrito.',
              confirmButtonText: 'Entendido'
          });
          return; // Salir de la función si no se ha seleccionado ninguna talla
      }

    // Verificar si el producto ya está en el carrito con la misma talla
       var existingItem = findCartItemByProductAndSize(shoe, size);
    if (existingItem !== null) {
     // Si el producto ya está en el carrito, incrementar la cantidad
     const currentStock = checkStock(shoe, size);
     console.log(currentStock)
     if(existingItem.quantity < currentStock){
        existingItem.quantity++;
         Swal.fire({
                        icon: 'success',
                        title: '¡Producto añadido!',
                        text: 'El producto ha sido añadido al carrito.',
                        confirmButtonText: 'Entendido'
                    });
      }else{
       Swal.fire({
                  icon: 'warning',
                  title: 'Stock insuficiente',
                  text: `No hay más unidades disponibles de la talla ${size}.`,
                  confirmButtonText: 'Entendido'
              });
      }
    } else {
        // Si el producto no está en el carrito, agregarlo como nuevo CarItem
   var newItem = new CarItem(shoe, 1, shoe.price, size);
        cart.carItems.push(newItem);
        // Actualizar la cantidad total de productos en el carrito
        cart.quantityProduct++;
         Swal.fire({
                    icon: 'success',
                    title: '¡Producto añadido!',
                    text: 'El producto ha sido añadido al carrito.',
                    confirmButtonText: 'Entendido'
                });
    }
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCartItems(); // Renderizar los elementos del carrito en la vista
}


// Función de utilidad para buscar un CarItem por producto dentro del carrito
function findCartItemByProductAndSize(shoe, size) {
    for (var i = 0; i < cart.carItems.length; i++) {
        var item = cart.carItems[i];
        // Verificar si el producto y la talla coinciden
        if (item.shoe.id === shoe.id && item.size === size) {
            return item;
        }
    }
    return null;
}

// Función para renderizar los elementos del carrito
function renderCartItems() {
    updateCartQuantityFromLocalStorage();
    const cartTableBody = document.querySelector('#cartTable tbody');
    // Verificar si cartTableBody es null
    if (cartTableBody) {
        // Limpiar el cuerpo de la tabla antes de volver a agregar los elementos
        cartTableBody.innerHTML = '';
        // Iterar sobre los elementos del carrito y agregarlos al cuerpo de la tabla
        cart.carItems.forEach((item, index) => {
            item = new CarItem(item.shoe, item.quantity, item.price);
            // Crear una nueva fila de la tabla
            const cartTableRow = document.createElement('tr');
            cartTableRow.innerHTML = `
                            <td class="col-12 col-md-2"><img class="img-fluid" src="${item.shoe.imagePrimary}" alt="Product Image"></td>
                            <td class="col-12 col-md-2">${item.shoe.name}</td>
                            <td class="col-12 col-md-2">
                                <button onclick="decrementQuantity(${index})">-</button>
                                <span  id="quantity_${item.shoe.id}" th:field="*{items.quantity}" >${item.quantity}</span>
                                <button onclick="incrementQuantity(${index})">+</button>
                            </td>
                            <td class="col-12 col-md-2" >${item.price.toLocaleString('es-ES', { minimumFractionDigits: 2 })}&euro;</td>
                            <td class="col-12 col-md-2" th:field="*{items.totalPrice}">${item.getPriceQuantity().toLocaleString('es-ES', { minimumFractionDigits: 2 })}&euro;</td>
                            <td class="col-12 col-md-2"><a class="remove-link" onclick="removeItemFromCart(${index})">
                                                    <img src="/assets/img/icons/borrar.png" class="img-responsive"
                                                         style="width: 30px; height: 30px;"/>
                                                    </a>
                                                    </td>
                        `;
                        cartTableBody.appendChild(cartTableRow);
        });
        updateTotalCart();
    } else {
     console.log('#cartTable tbody no encontrado');
    }
}
// Función para eliminar un elemento del carrito
function removeItemFromCart(index) {
    cart.carItems.splice(index, 1); // Eliminar el elemento del carrito
    cart.quantityProduct--;
    updateTotalCart();
    updateCartQuantityFromLocalStorage();
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCartItems(); // Renderizar los elementos del carrito en la vista
}
// Función para incrementar la cantidad de un producto en el carrito
function incrementQuantity(index) {
    const item = cart.carItems[index];
    const currentStock = checkStock(item.shoe, item.size); // Usar la función checkStock para obtener el stock actual
    if (item.quantity < currentStock) {
        item.quantity++;
        updateTotalCart();
        updateCartQuantityFromLocalStorage();
        localStorage.setItem('cart', JSON.stringify(cart));
        renderCartItems(); // Renderizar los elementos del carrito en la vista
    } else {
        Swal.fire({
            icon: 'warning',
            title: 'Stock insuficiente',
            text: `No hay más unidades disponibles de la talla ${item.size}.`,
            confirmButtonText: 'Entendido'
        });
    }
}
function checkStock(shoe, size) {
    const sizeFormatted = `SIZE_${size}`; // Formatear la talla
    const shoeStocks = shoe.shoeStocks; // Obtener los stocks del zapato
    // Buscar el stock de la talla correspondiente
    const stockInfo = shoeStocks.find(stock => stock.size.shoeSize === sizeFormatted);
    return stockInfo ? stockInfo.stock : 0; // Retornar el stock si se encuentra, de lo contrario 0
}

// Función para decrementar la cantidad de un producto en el carrito
function decrementQuantity(index) {
    cart.carItems[index].quantity--;
    if (cart.carItems[index].quantity <= 0) {
        cart.carItems.splice(index, 1); // Eliminar el elemento si la cantidad es 0 o menos
        cart.quantityProduct--;
    }
    updateTotalCart();
    updateCartQuantityFromLocalStorage();
    localStorage.setItem('cart', JSON.stringify(cart));
    renderCartItems(); // Renderizar los elementos del carrito en la vista
}

// Función para calcular el total del carrito
function calculateTotalCart() {
    // Recuperar el carrito del localStorage
    const storedCart = JSON.parse(localStorage.getItem('cart'));
    let total = 0;

    // Verificar si hay un carrito almacenado
    if (storedCart && storedCart.carItems) {
        // Iterar sobre los elementos del carrito
        for (const item of storedCart.carItems) {
            // Multiplicar el precio por la cantidad y sumar al total
            total += item.price * item.quantity;
        }
    }
   total = parseFloat(total.toFixed(2));
    // Formatear el total como moneda
    const formattedTotal = total.toLocaleString('es-ES', { style: 'currency', currency: 'EUR', minimumFractionDigits: 2 });
    // Retornar el total formateado del carrito
    return formattedTotal;
}


// Función para actualizar el total del carrito en la interfaz de usuario
function updateTotalCart() {
    // Obtener el elemento del total del carrito
    const totalPriceElement = document.getElementById('totalPrice');
    // Verificar si totalPriceElement es null
    if (totalPriceElement) {
        // Calcular el total del carrito
        const totalCart = calculateTotalCart().toLocaleString('es-ES', { style: 'currency', currency: 'EUR', minimumFractionDigits: 2 });
        console.log("total", totalCart);
        // Actualizar el total del carrito en la interfaz de usuario
        totalPriceElement.textContent = totalCart;
    } else {
        console.log('#totalPrice no encontrado');
    }
}

// Llamar a la función updateTotalCart cuando se haya cargado el DOM
document.addEventListener('DOMContentLoaded', updateTotalCart);


// Función para obtener la cantidad del carrito desde localStorage y actualizar el elemento HTML
function updateCartQuantityFromLocalStorage() {
    // Obtener los datos del carrito del localStorage
    const cartDataString = localStorage.getItem('cart');

    // Verificar si hay datos en el localStorage
    if (cartDataString) {
        // Parsear la cadena JSON para obtener el objeto del carrito
        const cartData = JSON.parse(cartDataString);

        // Acceder a la cantidad del carrito
        const cartQuantity = cartData.quantityProduct;

        // Actualizar el contenido del elemento HTML con la cantidad del carrito
        document.getElementById('cart-count').textContent = cartQuantity;
    } else {
        // Si no hay datos en el localStorage, establecer la cantidad en 0
        document.getElementById('cart-count').textContent = '0';
    }

}

function sendCartDataToBackend(address) {
    const cartData = JSON.parse(localStorage.getItem('cart'));
    // Verificar si el carrito está vacío
    if (!cartData || !cartData.carItems || cartData.carItems.length === 0) {
        Swal.fire({
            icon: 'error',
            title: 'Carrito vacío',
            text: 'No hay elementos en el carrito',
            showConfirmButton: true
        });
        return; // Salir de la función si el carrito está vacío
    }

    // Verificar si el usuario tiene dirección
    if (!address || !address.street || !address.city || !address.zipCode ){
        Swal.fire({
            icon: 'error',
            title: 'Sin dirección',
            text: 'No tienes una dirección de envío.',
            confirmButtonText: 'Añadir dirección',
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = '/perfil'; // Redirigir a la página para añadir dirección
            }
        });
        return; // Salir de la función si el usuario no tiene dirección
    }

    const purchaseData = {
        user: cartData.user,
        items: cartData.carItems.map(item => ({
            shoe: item.shoe,
            quantity: item.quantity,
            size: item.size, // Agregar la talla aquí
            totalPrice: item.quantity * item.price  // Calcular el precio total aquí
        })),
        totalPurchase: parseFloat(calculateTotalCart().toFixed(2)),
        date: new Date().toISOString()
    };
    // Crear una solicitud HTTP POST
    fetch('/mis-compras', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(purchaseData)
    })
    .then(response => {
        if (response.ok) {
            console.log('Purchase successful');
            // Limpiar el carrito después de que la compra sea exitosa
            localStorage.removeItem('cart');
            Swal.fire({
                icon: 'success',
                title: '¡Compra realizada con éxito!',
                showConfirmButton: false,
                timer: 2000
            }).then(() => {
                // Redirigir a la página principal
                window.location.href = '/tienda';
            });
        } else {
             Swal.fire({
                      icon: 'error',
                      title: 'Error ' + response.status,
                      text: 'Ha ocurrido un error en el servidor.',
                      showConfirmButton: true
                       });
                       return;
        }
    })
    .catch(error => {
        console.error('Error sending cart data to backend:', error);
    });
}
// Llamar a la función para actualizar la cantidad del carrito al cargar la página
updateCartQuantityFromLocalStorage();
// Llamar a renderCartItems al cargar la página para mostrar los elementos del carrito
renderCartItems();


