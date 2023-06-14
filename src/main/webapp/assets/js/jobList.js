const template = document.getElementById("job-item");
const ul = document.getElementById("list");

$(window).on("load", function () {
    fetch(window.location.origin + "/earnit/api/companies")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            console.log(json);
            if ("content" in document.createElement("template")) {
                for (let item of json) {
                    console.log(item);
                    const new_item = template.content.cloneNode(true);
                    let company_name = new_item.querySelector(".company_name");
                    let job_name = new_item.querySelector(".job_title");
                    company_name.textContent = item.name;
                    job_name.textContent = item.title;
                    ul.appendChild(new_item);
                }
            }
        })
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

function submit() {
    window.location.href = window.location.origin + "/earnit/student/submission"
}