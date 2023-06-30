const flag_popup = document.getElementById("flag-popup")
const flag_template = document.getElementById("flag-item")
const flag_close_button = document.getElementById("flag-popup-close")

const offer_popup = document.getElementById("offer-popup")
const offer_close_button = document.getElementById("offer-popup-close")

const pending = document.getElementById("pending")
const rejected = document.getElementById("rejected")
const accepted = document.getElementById("accepted")
const in_progress = document.getElementById("in-progress")

const weekNumberSelect = document.getElementById("week-dd");
const yearNumberSelect = document.getElementById("year-dd");

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
    removeAllChildNodes(pending)
    removeAllChildNodes(in_progress)
    removeAllChildNodes(rejected)
    removeAllChildNodes(accepted)
    fetch(window.location.origin + `/earnit/api/flags/all?week=${week}&year=${year}`)
        .then(response => {
            if (!response.ok) throw new Error("HTTP Error! Status: "+ response.status)
            return response.json()
        }).then(json => {
            console.log(json)
            for (let item of json) {
                console.log(item)
                const new_flag = flag_template.content.cloneNode(true)
                let company_name = new_flag.querySelector(".company_name")
                let job_title = new_flag.querySelector(".job_title")
                let company_logo = new_flag.querySelector(".company_logo")
                let view_details = new_flag.querySelector(".view-details")
                let buttons = new_flag.querySelector(".buttons")
                let accept = new_flag.querySelector(".accept")
                let reject = new_flag.querySelector(".reject")
                let status = new_flag.querySelector(".status")
                company_name.innerHTML = item["company_name"]
                job_title.innerHTML = item["job_title"]
                company_logo.src = (item["logo"] == null) ? company_logo.src : "data:image/svg+xml;base64," + item["logo"];
                view_details.addEventListener("click", function() {
                    let flag_info = document.getElementById("flag-info")
                    //TODO decide whether or not to do a seperate fetch or get the data at the beginning (probably better to do a separate fetch)
                    flag_info.innerHTML = `${item["job_title"] == null ? "Not specified" : item["job_title"]} (${item["company_name"]})`
                    flag_popup.style.display = "flex"
                })
                
                switch (item["status"]) {
                    case "appeal":
                    case "pending":
                        if (item["week"] === new Date().getWeek() && item["year"] === new Date().getWeekYear()) {
                            status.innerHTML = "Currently in progress"
                            in_progress.append(new_flag)
                        } else {
                            buttons.style.display = "block"
                            accept.addEventListener("click", function() {
                                let flag = {}
                                flag.eid = item["eid"]
                                flag.week = week
                                flag.year = year
                                flag.status = "accept"
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
                            })
                            reject.addEventListener("click", function() {
                                // a small pop up form to fill in the suggested hours
                                let offer_info = document.getElementById("offer-info")
                                let offer_submit = document.getElementById("send-offer")
                                let hours = document.getElementById("hours")
                                offer_info.innerHTML = `${item["job_title"] == null ? "Not specified" : item["job_title"]} (${item["company_name"]})`
                                offer_submit.addEventListener("click", function () {
                                    let flag = {}
                                    flag.eid = item["eid"]
                                    flag.week = week
                                    flag.year = year
                                    flag.status = "reject"
                                    flag.suggested_hours = hours.value;
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
                                        if (data === "SUCCESS") alert("Week was successfully rejected!")
                                        if (data === "FAILURE") alert("Something went wrong with rejecting the weekly submission...")
                                    })
                                })
                                offer_popup.style.display = "flex"
                            })
                            pending.append(new_flag)
                        }
                        break;
                        
                    case "accept":
                        accepted.append(new_flag)
                        break;
                        
                    case "reject":
                        rejected.append(new_flag)
                        break;
                    
                }
            }
    })
}

flag_close_button.addEventListener("click", closeFlagPopup)

function closeFlagPopup() {
    flag_popup.classList.add('animate-out');

    flag_popup.addEventListener('animationend', function() {
        flag_popup.style.display = "none";
        flag_popup.classList.remove('animate-out');
    }, { once: true });
}

document.addEventListener("click", function(event) {
    if (event.target.matches("#flag-popup")) closeFlagPopup();
})

offer_close_button.addEventListener("click", closeOfferPopup)

function closeOfferPopup() {
    offer_popup.classList.add('animate-out');

    offer_popup.addEventListener('animationend', function() {
        offer_popup.style.display = "none";
        offer_popup.classList.remove('animate-out');
    }, { once: true });
}

document.addEventListener("click", function(event) {
    if (event.target.matches("#offer-popup")) closeOfferPopup();
})

function removeAllChildNodes(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

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