function addFavoriteProduct(shoe) {
    // Primero, verifica si el usuario está autenticado
    fetch('/auth/check')
        .then(response => response.json())
        .then(data => {
            if (data.authenticated) {
                // Si el usuario está autenticado, procede a añadir el producto a favoritos
                const favoriteShoeDTO = {
                    user: null,
                    shoe: shoe
                };
                fetch('/favoritos', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(favoriteShoeDTO)
                })
                .then(response => {
                    if (response.ok) {
                        Swal.fire({
                            icon: 'success',
                            title: 'Añadido a favoritos',
                            text: 'El producto ha sido añadido a tus favoritos.',
                            showConfirmButton: false,
                            timer: 1500
                        });
                    } else if (response.status === 409) {
                        Swal.fire({
                            icon: 'info',
                            title: 'Ya está en favoritos',
                            text: 'Este producto ya se encuentra en tus favoritos.',
                            showConfirmButton: false,
                            timer: 1500
                        });
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error en el servidor',
                            text: 'Hubo un problema al añadir el producto a tus favoritos. Por favor, inténtalo de nuevo más tarde.',
                            showConfirmButton: false,
                            timer: 1500
                        });
                    }
                })
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        title: 'Error en el servidor',
                        text: 'Hubo un problema al añadir el producto a tus favoritos. Por favor, inténtalo de nuevo más tarde.',
                        showConfirmButton: false,
                        timer: 1500
                    });
                });
            } else {
                // Si el usuario no está autenticado, redirige a la página de inicio de sesión
                window.location.href = '/iniciar-sesion';
            }
        })
        .catch(error => {
            Swal.fire({
                icon: 'error',
                title: 'Error en el servidor',
                text: 'Hubo un problema al verificar la autenticación. Por favor, inténtalo de nuevo más tarde.',
                showConfirmButton: false,
                timer: 1500
            });
        });
}


    function removeFavoriteProduct(favoriteId) {
        console.log('Removing product from favorites:', favoriteId);
         fetch('/favoritos/borrar', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(favoriteId)
        })
        .then(response => {
            if (response.ok) {
                console.log('Product removed from favorites successfully');
                location.reload(); // Reload the page to update the list of favorite products
            } else {
                console.error('Error removing product from favorites:', response.status);
            }
        })
        .catch(error => {
            console.error('Error removing product from favorites:', error);
        });
    }