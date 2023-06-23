const companySelect = document.getElementById("company");

$(window).on("load", async function() {
    fetch(window.location.origin + "earnit/api/employments")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            console.log(json);
            if ('content' in document.getElementById('company')) {
                for (let item of json) {
                    const option = document.getElementById('company_name');

                    option.value = item.id; // Replace with the actual property containing the company ID
                    option.textContent = item.company_name; // Replace with the actual property containing the company name

                    companySelect.appendChild(option);
                }
            }
        })
        .catch(error => {
            console.error(error);
        });
});
