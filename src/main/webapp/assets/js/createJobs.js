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
            // console.log(json);
            // // Clear existing options
            // option.innerHTML = "";
            //
            // const defaultOption = document.createElement("option");
            // defaultOption.value = "";
            // defaultOption.textContent = "--- Company Name ---";
            // option.append(defaultOption);
            //
            // // Iterate over the companies and append options
            // json.forEach(company => {
            //     const newOption = companyTemplate.cloneNode(true);
            //     newOption.textContent = company.name;
            //     option.append(newOption);
            // });


            if ("content" in document.createElement("select")) {
                for (let item of json) {
                    console.log(item);
                    const new_company = companyTemplate.content.cloneNode(true);

                    let company_name = new_company.querySelector("#company_name");
                    company_name.textContent = item.name;

                    option.append(new_company);

                }
            }
        })
        .catch(error => {
            console.error(error);
        });
});

function logout() {
    fetch(window.location.origin + "/earnit/api/logout", {
        method: "POST"
    }).then(response => {
        if (response.ok) {
            window.location.href = window.location.origin + "/earnit/login"
        } else {
            console.log('Failed to logout');
        }
    })
        .catch(error => {
            // An error occurred during the fetch request
            console.log('Request error:', error);
        });
}