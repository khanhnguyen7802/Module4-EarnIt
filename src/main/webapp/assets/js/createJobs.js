const companyTemplate = document.getElementById("company");
const option = document.getElementById("company_name");

$(window).on("load", async function() {
    fetch(window.location.origin + "earnit/api/companies")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            console.log(json);
            // Clear existing options
            option.innerHTML = "";

            const defaultOption = document.createElement("option");
            defaultOption.value = "";
            defaultOption.textContent = "--- Company Name ---";
            option.appendChild(defaultOption);

            // Iterate over the companies and append options
            json.forEach(company => {
                const newOption = companyTemplate.cloneNode(true);
                newOption.textContent = company.name;
                option.appendChild(newOption);
            });
        })
        .catch(error => {
            console.error(error);
        });
});
