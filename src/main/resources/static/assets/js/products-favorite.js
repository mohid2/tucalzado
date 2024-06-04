  function addFavoriteProduct(shoe) {

        const favoriteProduct = {
            user: null,
            shoe: shoe
        };
        fetch('/favoritos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(favoriteProduct)
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
    }