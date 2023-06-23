const itemTemplate = document.getElementById("job-item");
const popupTemplate = document.getElementById("job-popup");
const ul = document.getElementById("list");
const job_popup = document.getElementById("job-popup")
const submit_popup = document.getElementById("submit-popup")
const job_close_button = document.getElementById("job-popup-close")
const submit_close_button = document.getElementById("submit-popup-close")

$(window).on("load", function () {
    fetch(window.location.origin + "/earnit/api/employments")
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

                    let company_name = new_item.querySelector(".company_name");
                    let job_name = new_item.querySelector(".job_title");
                    let viewDetails = new_item.querySelector(".view_details");
                    let submit_button = new_item.querySelector(".submit-button")
                    company_name.innerHTML = item["company_name"];
                    job_name.innerHTML = item["job_title"];
                    
                    viewDetails.addEventListener("click", function() {
                        let job_info = document.getElementById("job-info")
                        let salary = document.getElementById("salary")
                        let job_description = document.getElementById("job_description")
                        job_info.innerHTML = `${item["job_title"]} (${item["company_name"]})`
                        salary.innerHTML = `€${item['salary_per_hour']} per hour`
                        job_description.innerHTML = item["job_description"]
                        job_popup.style.display = "flex";
                    });
                    submit_button.addEventListener("click", function() {
                        let job_info = document.getElementById("submit-info")
                        let final_submit = document.getElementById("send-submission")
                        job_info.innerHTML = `${item["job_title"]} (${item["company_name"]})`
                        final_submit.addEventListener("click", function () {
                            let submission = {}
                            submission["eid"] = item["eid"]

                            let hours = document.getElementById("hours")
                            let date = document.getElementById("date")
                            let comment = document.getElementById("comment")
                            submission["hours_worked"] = hours.value
                            submission["worked_date"] = date.value;
                            submission["comment"] = comment.value;
                            alert(`About to send the following info to the API:\n${JSON.stringify(submission)}`)
                            fetch(window.location.origin + "/earnit/api/submissions/day", {
                                method: "POST",
                                headers: {
                                    "Content-type": "application/json"
                                },
                                body: JSON.stringify(submission)
                            }).then(response => {
                                if (!response.ok) throw new Error("HTTP Error! Status: " + response.status);
                                return response.text()
                            }).then(data => {
                                //TODO replace this with redirect
                                if (data === "SUCCESS") alert("Success!")
                                if (data === "FAILURE") alert("Oof, failure...")
                            })
                        })
                        submit_popup.style.display = "flex"
                    });
                    
                    ul.append(new_item);
                }
            }
        })
});

job_close_button.addEventListener("click", closeJobPopup)

function closeJobPopup() {
    job_popup.classList.add('animate-out');

    job_popup.addEventListener('animationend', function() {
        job_popup.style.display = "none";
        job_popup.classList.remove('animate-out');
    }, { once: true });
}

document.addEventListener("click", function(event) {
    if (event.target.matches("#job-popup")) closeJobPopup();
    else if (event.target.matches("#submit-popup")) closeSubmitPopup();
})

submit_close_button.addEventListener("click", closeSubmitPopup)

function closeSubmitPopup() {
    submit_popup.classList.add('animate-out');

    submit_popup.addEventListener('animationend', function() {
        submit_popup.style.display = "none";
        submit_popup.classList.remove('animate-out');
    }, { once: true });
}