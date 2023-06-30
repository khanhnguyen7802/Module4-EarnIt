const submissionTemplate = document.getElementById("submission-item");
const flagTemplate = document.getElementById("flag-item");
const tabs = document.querySelectorAll(".nav-tabs");
const ul = document.getElementById("list-item");
const list_0 = document.getElementById("list-0")
const list_1 = document.getElementById("list-1")
const list_2 = document.getElementById("list-2")
const list_3 = document.getElementById("list-3")
const list_4 = document.getElementById("list-4")
const list_5 = document.getElementById("list-5")
const list_6 = document.getElementById("list-6")

const pending = document.getElementById("pending")
const rejected = document.getElementById("rejected")
const accepted = document.getElementById("accepted")

const submission_popup = document.getElementById("submission-popup")
const submission_close_button = document.getElementById("submission-popup-close")

const days = [list_6, list_0, list_1, list_2, list_3, list_4, list_5]

const weekNumberSelect = document.getElementById("week-dd"); //TODO: add a week dropdown list
const yearNumberSelect = document.getElementById("year-dd"); //TODO: add a year dropdown list

weekNumberSelect.addEventListener("change", function() {
    if (weekNumberSelect.value !== "" && yearNumberSelect.value !== "") fetchWeek(weekNumberSelect.value, yearNumberSelect.value)
})

yearNumberSelect.addEventListener("change", function() {
    if (weekNumberSelect.value !== "" && yearNumberSelect.value !== "") fetchWeek(weekNumberSelect.value, yearNumberSelect.value)
})

$(window).on("load", function () {
    fetchWeek(new Date().getWeek(), new Date().getWeekYear())
})

function fetchWeek(week, year) {
    days.map(removeAllChildNodes)
    removeAllChildNodes(pending)
    removeAllChildNodes(rejected)
    removeAllChildNodes(accepted)
    fetch(window.location.origin + `/earnit/api/submissions/student/week?week=${week}&year=${year}`)
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
                const new_item = submissionTemplate.content.cloneNode(true);
                let company_name = new_item.querySelector(".company_name");
                let job_title = new_item.querySelector(".job_title");
                let viewNotes = new_item.querySelector(".view-details")
                let hours_badge = new_item.querySelector(".hours")
                let company_logo = new_item.querySelector(".company_logo")
                company_name.innerHTML = item["company_name"];
                job_title.innerHTML = item["job_title"];
                hours_badge.innerHTML = `${item["hours"]} hours`
                company_logo.src = (item["logo"] == null) ? company_logo.src : "data:image/svg+xml;base64," + item["logo"];
                viewNotes.addEventListener("click", function() {
                    let submission_info = document.getElementById("submission-info")
                    let hours = document.getElementById("hours")
                    let notes = document.getElementById("notes")
                    submission_info.innerHTML = `${item["job_title"] == null ? "Not specified" : item["job_title"]} (${item["company_name"]})`
                    hours.innerHTML = item["hours"]
                    notes.innerHTML = item["comment"]
                    submission_popup.style.display = "flex";
                })
                days[new Date(item["date"]).getDay()].append(new_item);
            }
        })
    fetch(window.location.origin + `/earnit/api/flags/all?week=${week}&year=${year}`)
        .then(response => {
            if (!response.ok) throw new Error("HTTP Error! Status: " + response.status)
            return response.json()
        }).then(json => {
            console.log(json)
            for (let item of json) {
                console.log(item)
                const new_flag = flagTemplate.content.cloneNode(true)
                let company_name = new_flag.querySelector(".company_name");
                let job_title = new_flag.querySelector(".job_title");
                let total_hours = new_flag.querySelector(".total-hours")
                let company_logo = new_flag.querySelector(".company_logo")
                let buttons = new_flag.querySelector(".buttons")
                let accept = new_flag.querySelector(".accept")
                let reject = new_flag.querySelector(".reject")
                let status = new_flag.querySelector(".status")
                let download_button = new_flag.querySelector(".download-invoice")
                company_name.innerHTML = item["company_name"]
                job_title.innerHTML = item["job_title"];
                total_hours.innerHTML = (item["suggested_hours"] == null) ? `Total hours this week: ${item["total_hours"]}` : `Total hours this week: <s>${item["total_hours"]}</s> &rarr; ${item["suggested_hours"]}`
                company_logo.src = (item["logo"] == null) ? company_logo.src : "data:image/svg+xml;base64," + item["logo"];
                switch (item["status"]) {
                    case "appeal":
                        status.innerHTML = "Submission has been sent to admin for review"
                    case "pending":
                        pending.append(new_flag)
                        break;
                    case "accept":
                        accepted.append(new_flag)
                        
                        // TODO:
                        
                        download_button.style.display = "block"
                        download_button.addEventListener("click", function() {
                            sessionStorage.setItem("eid", item["eid"])
                            sessionStorage.setItem("week", week)
                            sessionStorage.setItem("year", year)
                            window.open("../student/invoice.html");
                            
                            })
                        
                        break;
                    case "reject":
                        buttons.style.display = "block"
                        accept.addEventListener("click", function() {
                            let flag = {}
                            flag.eid = item["eid"]
                            flag.week = week
                            flag.year = year
                            flag.status = "accept"
                            flag.suggested_hours = item["suggested_hours"]
                            fetch(window.location.origin + "/earnit/api/flags/update", {
                                method: "POST",
                                headers: {
                                    "Content-type": "application/json"
                                },
                                body: JSON.stringify(flag)
                            }).then(response => {
                                if(!response.ok) throw new Error("HTTP Error! Status: " + response.status)
                                return response.text()
                            }).then(data => {
                                if (data === "SUCCESS") alert("Week was successfully accepted!")
                                if (data === "FAILURE") alert("Something went wrong with accepting the weekly submission...")
                            })


                            // if the student accepts the newly suggested hours, then save into the invoice table
                            let invoice = {}
                            let currentDate = new Date().toJSON().slice(0, 10)
                            invoice.eid = item["eid"]
                            invoice.week = week
                            invoice.year = year
                            invoice.total_salary = item["salary_per_hour"] * item["suggested_hours"]
                            invoice.date_of_issue = currentDate

                            fetch(window.location.origin + "/earnit/api/invoices/add", {
                                method: "POST",
                                headers: {
                                    "Content-type": "application/json"
                                },
                                body: JSON.stringify(invoice)
                            }).then(response => {
                                if(!response.ok) throw new Error("HTTP Error! Status: " + response.status)
                                return response.text()
                            }).then(data => {
                                if (data === "SUCCESS") location.reload()
                                if (data === "FAILURE") alert("Something went wrong with adding the invoice...")
                            })

                        })
                        reject.addEventListener("click", function() {
                            let flag = {}
                            flag.eid = item["eid"]
                            flag.week = week
                            flag.year = year
                            flag.status = "appeal"
                            flag.suggested_hours = item["suggested_hours"]
                            fetch(window.location.origin + "/earnit/api/flags/update", {
                                method: "POST",
                                headers: {
                                    "Content-type": "application/json"
                                },
                                body: JSON.stringify(flag)
                            }).then(response => {
                                if(!response.ok) throw new Error("HTTP Error! Status: " + response.status)
                                return response.text()
                            }).then(data => {
                                if (data === "SUCCESS") location.reload()
                                if (data === "FAILURE") alert("Something went wrong with accepting the weekly submission...")
                            })
                        })
                        rejected.append(new_flag)
                        break;
                }
            }
    })
}

submission_close_button.addEventListener("click", closeSubmissionPopup)

function closeSubmissionPopup() {
    submission_popup.classList.add('animate-out');

    submission_popup.addEventListener('animationend', function() {
        submission_popup.style.display = "none";
        submission_popup.classList.remove('animate-out');
    }, { once: true });
}

document.addEventListener("click", function(event) {
    if (event.target.matches("#submission-popup")) closeSubmissionPopup();
})

// function getDate(dayIndex) {
//     let today = new Date();
//     let currentDay = today.getDay(); // Get the current day index (0-6) with 0 for Sunday
//     let distance = dayIndex - currentDay; // Calculate the difference between the desired day and the current day
//     let targetDate = new Date(today.setDate(today.getDate() + distance)); // Set the target date by adding the difference to the current date
//     return targetDate.toLocaleDateString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit' }); // Return the formatted date
// }
//
// function fetchDate(date) {
//     fetch(window.location.origin + "/earnit/api/submissions/day?date=" + date)
//         .then(response => {
//             if (response.ok) {
//                 return response.json();
//             } else {
//                 throw new Error('Request failed with status ' + response.status);
//             }
//         })
//         .then(json => {
//             console.log(json);
//             if ("content" in document.createElement("template")) {
//                 ul.clear();
//                 for (let item of json) {
//                     console.log(item);
//                     const new_item = itemTemplate.content.cloneNode(true);
//                     let company_name = new_item.querySelector(".company_name");
//                     let job_title = new_item.querySelector(".job_title");
//                     //let viewNotes
//                     company_name.innerHTML = item["companyName"];
//                     job_title.innerHTML = item["jobTitle"];
//                     //viewNote.setAttribute()
//                     ul.append(new_item);
//                     //TODO: implement viewNotes popup
//                 }
//             }
//         })
// }




Date.prototype.getWeek = function() {
    let date = new Date(this.getTime());
    date.setHours(0, 0, 0, 0);
    // Thursday in current week decides the year.
    date.setDate(date.getDate() + 3 - (date.getDay() + 6) % 7);
    // January 4 is always in week 1.
    var week1 = new Date(date.getFullYear(), 0, 4);
    // Adjust to Thursday in week 1 and count number of weeks from date to week1.
    return 1 + Math.round(((date.getTime() - week1.getTime()) / 86400000
        - 3 + (week1.getDay() + 6) % 7) / 7);
}

Date.prototype.getWeekYear = function() {
    var date = new Date(this.getTime());
    date.setDate(date.getDate() + 3 - (date.getDay() + 6) % 7);
    return date.getFullYear();
}

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}