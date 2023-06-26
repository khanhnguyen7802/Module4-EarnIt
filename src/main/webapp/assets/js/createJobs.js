const companySelect = document.getElementById("companies");

$(window).on("load", async function() {
    fetch(window.location.origin + "/earnit/api/companies/all")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            console.log(json);

            for (let item of json) {
                let option = item.name;
                let newOption = document.createElement("option");
                newOption.textContent = option;

                companySelect.appendChild(newOption);
            }

        })
        .catch(error => {
            console.error(error);
        });
});
