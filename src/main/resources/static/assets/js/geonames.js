  let selectedCountry = '';
  let selectedCommunity = '';
  let selectedProvince = '';
  let selectedCity = '';

  // Función para obtener y mostrar la lista de países
  async function fetchCountries() {
      try {
          const response = await fetch('https://secure.geonames.org/countryInfoJSON?username=mohammedelyousfi&lang=es');
          const data = await response.json();
          const countries = data.geonames;
          const countrySelect = document.getElementById('country');

          countries.forEach(country => {
              const option = document.createElement('option');
              option.value = country.geonameId;
              option.text = country.countryName;
              option.setAttribute('data-name', country.countryName);
              countrySelect.appendChild(option);
          });

          if (selectedCountry) {
              countrySelect.value = selectedCountry;
          }
      } catch (error) {
          console.error('Error al obtener los países:', error);
      }
  }

  // Función para obtener y mostrar las comunidades del país seleccionado
  async function fetchCommunity() {
      try {
          const countrySelect = document.getElementById('country');
          selectedCountry = countrySelect.value;
          const countryName = countrySelect.options[countrySelect.selectedIndex].getAttribute('data-name');
          document.getElementById('countryName').value = countryName;

          const response = await fetch(`https://secure.geonames.org/childrenJSON?geonameId=${selectedCountry}&username=mohammedelyousfi&lang=es`);
          const data = await response.json();
          const communities = data.geonames;

          const communitySelect = document.getElementById('community');
          communitySelect.innerHTML = '<option value="">Seleccione una comunidad</option>'; // Limpiar opciones anteriores

          communities.forEach(community => {
              const option = document.createElement('option');
              option.value = community.geonameId;
              option.text = community.name;
              option.setAttribute('data-name', community.name);
              communitySelect.appendChild(option);
          });

          if (selectedCommunity) {
              communitySelect.value = selectedCommunity;
          }

          // Limpiar el selector de provincias y ciudades cuando se selecciona una nueva comunidad
          document.getElementById('province').innerHTML = '<option value="">Seleccione una provincia</option>';
          document.getElementById('city').innerHTML = '<option value="">Seleccione una ciudad</option>';
      } catch (error) {
          console.error('Error al obtener las comunidades:', error);
      }
  }

  // Función para obtener y mostrar las provincias de la comunidad seleccionada
  async function fetchProvince() {
      try {
          const communitySelect = document.getElementById('community');
          selectedCommunity = communitySelect.value;
          const communityName = communitySelect.options[communitySelect.selectedIndex].getAttribute('data-name');
          document.getElementById('communityName').value = communityName;

          const response = await fetch(`https://secure.geonames.org/childrenJSON?geonameId=${selectedCommunity}&username=mohammedelyousfi&lang=es`);
          const data = await response.json();
          const provinces = data.geonames;

          const provinceSelect = document.getElementById('province');
          provinceSelect.innerHTML = '<option value="">Seleccione una provincia</option>'; // Limpiar opciones anteriores

          provinces.forEach(province => {
              const option = document.createElement('option');
              option.value = province.geonameId;
              option.text = province.name;
              option.setAttribute('data-name', province.name);
              provinceSelect.appendChild(option);
          });

          if (selectedProvince) {
              provinceSelect.value = selectedProvince;
          }

          // Limpiar el selector de ciudades cuando se selecciona una nueva provincia
          document.getElementById('city').innerHTML = '<option value="">Seleccione una ciudad</option>';
      } catch (error) {
          console.error('Error al obtener las provincias:', error);
      }
  }

 // Función para obtener y mostrar las ciudades de la provincia seleccionada
    async function fetchCity() {
        try {
            const provinceSelect = document.getElementById('province');
            selectedProvince = provinceSelect.value;
            const provinceName = provinceSelect.options[provinceSelect.selectedIndex].getAttribute('data-name');
            document.getElementById('provinceName').value = provinceName;

            const response = await fetch(`https://secure.geonames.org/childrenJSON?geonameId=${selectedProvince}&username=mohammedelyousfi&lang=es`);
            const data = await response.json();
            const cities = data.geonames;

            const citySelect = document.getElementById('city');
            citySelect.innerHTML = '<option value="">Seleccione una ciudad</option>'; // Limpiar opciones anteriores

            cities.forEach(city => {
                const option = document.createElement('option');
                option.value = city.name; // Use city name instead of geonameId
                option.text = city.name;
                option.setAttribute('data-name', city.name);
                citySelect.appendChild(option);
            });

            if (selectedCity) {
                citySelect.value = selectedCity;
            }
             // Añadir evento para actualizar el campo oculto cuando se selecciona una ciudad
                    citySelect.addEventListener('change', () => {
                        const selectedCityName = citySelect.options[citySelect.selectedIndex].getAttribute('data-name');
                        document.getElementById('cityName').value = selectedCityName;
                    });
        } catch (error) {
            console.error('Error al obtener las ciudades:', error);
        }
    }

  // Inicializar la lista de países al cargar la página
  document.addEventListener('DOMContentLoaded', () => {
      fetchCountries();
  });
