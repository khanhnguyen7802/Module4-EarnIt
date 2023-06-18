const itemTemplate = document.getElementById("job-item");
const popupTemplate = document.getElementById("job-popup");
const ul = document.getElementById("list");

$(window).on("load", async function () {
    fetch(window.location.origin + "/earnit/api/companies")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            let idx = 0;
            console.log(json);
            if ("content" in document.createElement("template")) {
                for (let item of json) {
                    console.log(item);
                    const new_item = itemTemplate.content.cloneNode(true);
                    const new_popup = popupTemplate.content.cloneNode(true);

                    let company_name = new_item.querySelector(".company_name");
                    let job_name = new_item.querySelector(".job_title");
                    let viewDetails = new_item.querySelector("#view_details");
                    company_name.innerHTML = item.name;
                    job_name.innerHTML = item.job_title;
                    viewDetails.setAttribute("href", "#popup"+idx);


                    let popUp = new_popup.querySelector(".overlay");
                    popUp.setAttribute("id", "popup"+idx);
                    company_name = new_popup.querySelector(".company_name");
                    let job_description = new_popup.querySelector("#job_description")
                    company_name.textContent = item.name;
                    job_description.textContent = item.job_description;
                    ul.append(new_item);
                    ul.append(new_popup);
                    // document.getElementById("job_description").innerHTML = job_description;
                    console.log(job_description);
                    console.log(document.getElementById("job_description"));

                    idx++;

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