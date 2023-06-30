const employment_item = document.getElementById("employment-item");
const important_items = document.getElementById("important")
const other_items = document.getElementById("other")
const resolve_popup = document.getElementById("resolve-popup")
const resolve_close_button = document.getElementById("resolve-popup-close")

$(window).on("load", function () {
    fetch(window.location.origin + "/earnit/api/employments/all")
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
                    console.log(item)
                    const new_item = employment_item.content.cloneNode(true);
                    
                    let company_name = new_item.querySelector(".student_name");
                    let company_logo = new_item.querySelector(".company_logo");
                    let job_info = new_item.querySelector(".job_info");
                    let resolve_buttons = new_item.querySelector(".resolve-buttons");
                    let appeal_amount = new_item.querySelector(".appeal-amount")
                    let resolve = new_item.querySelector(".resolve")
                    
                    company_logo.src = (item["logo"] == null) ? company_logo.src : "data:image/svg+xml;base64," + item["logo"];
                    company_name.innerHTML = item["student_name"];
                    job_info.innerHTML = `${item["job_title"] == null ? "Not specified" : item["job_title"]} (${item["company_name"]})`;
                    
                    if (item["appeal_amount"] !== 0) {
                        appeal_amount.innerHTML = item["appeal_amount"]
                        resolve.addEventListener("click", function() {
                            let openAppeals = []
                            fetch(window.location.origin + "/earnit/api/flags/appeals", {
                                method: "POST",
                                headers: {
                                    "Content-type": "application/json"
                                },
                                body: JSON.stringify(item)
                            }).then(response => {
                                if (!response.ok) throw new Error("HTTP Error! Status: " + response.status)
                                return response.json()
                            }).then(data => {
                                console.log(data)
                                for (let flag of data) {
                                    openAppeals.push(flag)
                                }
                                let resolve_info = document.getElementById("resolve-info")
                                let original_hours = document.getElementById("original-hours")
                                let suggested_hours = document.getElementById("suggested-hours")
                                // let comment = document.getElementById("comment")
                                let final_hours = document.getElementById("final-hours")
                                let send_resolve = document.getElementById("send-resolve")
                                function loadNewAppeal() {
                                    let currAppeal= openAppeals.shift()
                                    if (currAppeal !== undefined) {
                                        console.log(currAppeal)
                                        resolve_info.innerHTML = `${item["student_name"]} &LeftRightArrow; ${item["job_title"]} (${item["company_name"]})`
                                        original_hours.innerHTML = currAppeal["total_hours"]
                                        suggested_hours.innerHTML = currAppeal["suggested_hours"]
                                        send_resolve.onclick = function() {
                                            let final_flag = JSON.parse(JSON.stringify(currAppeal))
                                            final_flag["suggested_hours"] = final_hours.value
                                            final_flag["status"] = "accept"
                                            fetch(window.location.origin + "/earnit/api/flags/update", {
                                                method: "POST",
                                                headers: {
                                                    "Content-type": "application/json"
                                                },
                                                body: JSON.stringify(final_flag)
                                            }).then(response => {
                                                if (!response.ok) throw new Error("HTTP Error! Status: " + response.status)
                                                return response.text()
                                            }).then(data => {
                                                if (data === "SUCCESS") {
                                                    alert("Appeal has successfully been resolved!")
                                                    loadNewAppeal()
                                                }
                                                if (data === "FAILURE") alert("There was an error while trying to resolve this appeal")
                                            })


                                            // admin is the final decision, then add to the invoice table
                                            let invoice = {}
                                            let currentDate = new Date().toJSON().slice(0, 10)
                                            invoice.eid = item["eid"]
                                            invoice.week = item["week"]
                                            invoice.year = item["year"]
                                            invoice.total_salary = item["salary_per_hour"] * final_hours
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
                                                if (data === "SUCCESS") alert("Invoice was successfully added!")
                                                if (data === "FAILURE") alert("Something went wrong with adding the invoice...")
                                            })

                                        }
                                        resolve_popup.style.display = "block"
                                    } else {
                                        closeResolvePopup()
                                    }
                                }
                                loadNewAppeal()
                            })
                            
                        })
                        resolve_buttons.style.display = "block";
                        important_items.append(new_item)
                    } else {
                        other_items.append(new_item)
                    }
                }
            }
        })
});

document.addEventListener("click", function(event) {
    if (event.target.matches("#resolve-popup")) closeResolvePopup();
})

resolve_close_button.addEventListener("click", closeResolvePopup)

function closeResolvePopup() {

    resolve_popup.classList.add('animate-out');

    resolve_popup.addEventListener('animationend', function() {
        resolve_popup.style.display = "none";
        resolve_popup.classList.remove('animate-out');
    }, { once: true });
    
    location.reload()
}