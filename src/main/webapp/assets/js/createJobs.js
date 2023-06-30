const companySelect = document.getElementById("companies");
const job_title = document.getElementById("job_title")
const salary = document.getElementById("salary")
const job_description = document.getElementById("job_description")
const submit_button = document.getElementById("submit-button")
const fields = [companySelect, job_title, salary, job_description]

$(window).on("load", function() {
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
                console.log(item)
                let option = item.name;
                let newOption = document.createElement("option");
                newOption.textContent = option;
                newOption.value = JSON.stringify(item)

                companySelect.appendChild(newOption);
            }

        })
        .catch(error => {
            console.error(error);
        });
});

submit_button.addEventListener("click", function () {
    let valid = true;
    fields.forEach(element => {
        if (!element.checkValidity()) {
            element.classList.add("invalid")
            if (element.validity.valueMissing) closest(element, ".err-msg").innerHTML = "This field is mandatory!"
            valid = false
        }
        else {
            element.classList.remove("invalid")
            closest(element, ".err-msg").innerHTML = ""
        }
    })
    let newJob = {
        cid: JSON.parse(companySelect.value)["cid"],
        job_description: sanitize(job_description.value),
        job_title: sanitize(job_title.value),
        salary_per_hour: sanitize(salary.value)
    }
    if (!valid) return
    fetch(window.location.origin + "/earnit/api/employments/new" , {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(newJob)
    }).then(response => {
        if (!response.ok) throw new Error("HTTP Error! Status: " + response.status)
        return response.text()
    }).then(data => {
        if (data === "SUCCESS") alert("Job successfully created!")
        if (data === "FAILURE") alert("An error has occured.")
    })
})

const closest = (to, selector) => {
    let currentElement = to
    let returnElement

    while (currentElement.parentNode && !returnElement) {
        currentElement = currentElement.parentNode
        returnElement = currentElement.querySelector(selector)
    }

    return returnElement
}