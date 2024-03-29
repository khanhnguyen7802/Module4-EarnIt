const form = document.getElementById("form");
const submit = document.getElementById("save_button");

$(window).on("load", function () {
    fetch(window.location.origin + "/earnit/api/companies/profile")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            console.log(json);
            form.email.value = json.email;
            form.name.value = json.name;
            form.field.value = json.field;
            form.contact.value = json.contact;
            form.location.value = json.location;
            form.kvk_num.value = json.kvk_num;
        })
});

// Initial data
let initialData = {
    email: form.email.value,
    name: form.name.value,
    field: form.field.value,
    contact: form.contact.value,
    location: form.location.value,
    kvk_num: form.kvk_num.value
};

submit.addEventListener("click", function() {
    let company = {};

    if (form.password.value !== form.confirm_pass.value) {
        alert("Passwords do not match!");
        return;
    }

    company.email = sanitize(form.email.value);
    company.name = sanitize(form.name.value);
    company.field = form.field.value === "" ? null : sanitize(form.field.value);
    company.contact = form.contact.value === "" ? null : sanitize(form.contact.value);
    company.location = form.location.value === "" ? null : sanitize(form.location.value);
    company.kvk_num = form.kvk_num.value === "" ? null : sanitize(form.kvk_num.value);
    // company.password = form.password.value === "" ? initialData.password.value : sanitize(form.password.value);

    // Check if there are any changes
    if (JSON.stringify(company) === JSON.stringify(initialData)) {
        alert("No changes detected!");
        return;
    }

    fetch(window.location.origin + "/earnit/api/companies/update", {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(company)
    })
        .then(response => {
            if (!response.ok) throw new Error("HTTP Error! Status: " + response.status);
            return response.text();
        })
        .then(data => {
            if (data === "SUCCESS"){
                window.location.href = "./company_profile";
            } else {
                alert("Please try again");
            }
        })
        .catch(error => {
            console.error(error);
        });
});
