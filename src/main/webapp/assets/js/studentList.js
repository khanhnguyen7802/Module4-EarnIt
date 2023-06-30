const studentTemplate = document.getElementById("student-item");
const ul = document.getElementById("list");

$(window).on("load", async function () {
    fetch(window.location.origin + "/earnit/api/students")
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
                    const new_item = studentTemplate.content.cloneNode(true);

                    let student_name = new_item.querySelector(".student_name");
                    let job_name = new_item.querySelector(".job_title");
                    student_name.innerHTML = item["name"];
                    job_name.innerHTML = item["job_title"];
                    console.log(job_name)

                    ul.append(new_item);

                }
            }
        })
});

function getJobTitle() {
    fetch(window.location.origin + "/earnit/api/companies/${companyEmail}")
        .then(response => {
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

