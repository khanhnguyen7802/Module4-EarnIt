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
                    let company_logo = new_item.querySelector(".company_logo");
                    let job_name = new_item.querySelector(".job_title");
                    let viewDetails = new_item.querySelector(".view_details");
                    let submit_button = new_item.querySelector(".submit-button");
                    company_logo.src = (item["logo"] == null) ? company_logo.src : "data:image/svg+xml;base64," + item["logo"];
                    company_name.innerHTML = item["company_name"];
                    job_name.innerHTML = item["job_title"];
                    
                    viewDetails.addEventListener("click", function() {
                        let job_info = document.getElementById("job-info")
                        let salary = document.getElementById("salary")
                        let job_description = document.getElementById("job_description")
                        job_info.innerHTML = `${item["job_title"] == null ? "Not specified" : item["job_title"]} (${item["company_name"]})`
                        salary.innerHTML = `€${item['salary_per_hour']} per hour`
                        job_description.innerHTML = item["job_description"] == null ? "Not specified" : item["job_description"]
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
                            submission["hours"] = hours.value
                            submission["date"] = date.value;
                            submission["comment"] = comment.value;
                            alert(`About to send the following info to the API:\n${JSON.stringify(submission)}`)

                            fetchSubmission(submission).then(data => {
                                if (data === "SUCCESS") alert("Adding daily submission: Success!")
                                if (data === "FAILURE") alert("Oof, adding daily submission: Failure...")
                            })


                            //TODO: also POST flag to /flags
                            let flag ={}
                            flag["eid"] = item["eid"]
                            
                            let myDate = new Date(date.value);
                            console.log(myDate);
                            let year = myDate.getFullYear(); // number type
                            let week = myDate.getWeek();
                            flag["year"] = year
                            flag["week"] = week;
                            flag["status"] = "";

                            fetchFlag(flag);

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

async function fetchSubmission(submission) {
    const response = await fetch(window.location.origin + "/earnit/api/submissions", {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(submission)
    });

    if (!response.ok) {
        throw new Error("HTTP Error! Status: " + response.status);
    }

    return response.text()

}

function fetchFlag(flag) {
    const response = fetch(window.location.origin + "/earnit/api/flags", {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(flag)
    });

    if (!response.ok) {
        throw new Error("HTTP Error! Status: " + response.status);
    }

    return response.text()

}


Date.prototype.getWeek = function() {
    // find the year of the current date
    let oneJan = new Date(this.getFullYear(),0,1);

    // calculating number of days in given year before the given date
    let numberOfDays =  Math.floor((this - oneJan) / (24 * 60 * 60 * 1000));

    // adding 1 since to current date and returns value starting from 0
    let result = Math.ceil(( this.getDay() + 1 + numberOfDays) / 7);

    return result;
}

// Date.prototype.getWeek = function() {
//     let dowOffset = 0;
//     let date = new Date(this.getTime())
//
//     const newYear = new Date(date.getFullYear(), 0, 1);
//     let day = newYear.getDay() - dowOffset; //the day of week the year begins on
//     day = (day >= 0 ? day : day + 7);
//     const daynum = Math.floor((date.getTime() - newYear.getTime() -
//         (date.getTimezoneOffset() - newYear.getTimezoneOffset()) * 60000) / 86400000) + 1;
//     //if the year starts before the middle of a week
//     if (day < 4) {
//         const weeknum = Math.floor((daynum + day - 1) / 7) + 1;
//         if (weeknum > 52) {
//             const nYear = new Date(date.getFullYear() + 1, 0, 1);
//             let nday = nYear.getDay() - dowOffset;
//             nday = nday >= 0 ? nday : nday + 7;
//             /*if the next year starts before the middle of
//               the week, it is week #1 of that year*/
//             return nday < 4 ? 1 : 53;
//         }
//         return weeknum;
//     }
//     else {
//         return Math.floor((daynum + day - 1) / 7);
//     }
// }