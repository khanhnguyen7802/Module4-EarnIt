const overviewTemplate = document.getElementById("employment-item");
const tbody = document.getElementById("table-body");

$(window).on("load", async function () {
    fetch(window.location.origin + "/earnit/api/employments")
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
                    const new_employment = overviewTemplate.content.cloneNode(true);

                    let studentName = new_employment.querySelector("#student-name");
                    let companyName = new_employment.querySelector("#company-name");
                    let jobTitle = new_employment.querySelector("#job-title");

                    studentName.textContent = item["student_name"];
                    companyName.textContent = item["company_name"];
                    jobTitle.textContent = item["job_title"];

                    tbody.append(new_employment);

                }
            }
        })
});
