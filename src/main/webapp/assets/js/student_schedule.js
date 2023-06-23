const itemTemplate = document.getElementById("vacancy-template");
const overviewTemplate = document.getElementById("overview-item");
const tabs = document.querySelectorAll(".nav-tabs");
const ul = document.getElementById("list-item");

let currentDate = new Date().toJSON().slice(0, 10);

$(window).on("load", async function () {
    fetch(window.location.origin + "/earnit/api/submissions/week?date=" + currentDate)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            console.log(json);
        })
})

function getDate(dayIndex) {
    let today = new Date();
    let currentDay = today.getDay(); // Get the current day index (0-6)
    let distance = dayIndex - currentDay; // Calculate the difference between the desired day and the current day
    let targetDate = new Date(today.setDate(today.getDate() + distance)); // Set the target date by adding the difference to the current date
    return targetDate.toLocaleDateString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit' }); // Return the formatted date
}

function fetchDate(date) {
    fetch(window.location.origin + "/earnit/api/submissions/day?date=" + date)
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
                ul.clear();
                for (let item of data) {
                    console.log(item);
                    const new_item = itemTemplate.content.cloneNode(true);
                    let company_name = new_item.querySelector(".company_name");
                    let job_title = new_item.querySelector(".job_title");
                    //let viewNotes
                    company_name.innerHTML = item.companyName;
                    job_title.innerHTML = item.jobTitle;
                    //viewNote.setAttribute()
                    ul.append(new_item);
                    //TODO: implement viewNotes popup
                }
            }
        })
}

function fetchWeek(date) {
    fetch(window.location.origin + "/earnit/api/submissions/week?date=" + date)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            console.log(json);
            //TODO: Create an API that calculate the total hours in a week
        })
}

tabs.forEach(tab => {
    tab.addEventListener('click', function () {
        const tabId = tab.id;
        switch (tabId) {
            case 'nav-1':
                fetchDate(getDate(1));
                break;
            case 'nav-2':
                fetchDate(getDate(2));
                break;
            case 'nav-3':
                fetchDate(getDate(3));
                break;
            case 'nav-4':
                fetchDate(getDate(4));
                break;
            case 'nav-5':
                fetchDate(getDate(5));
                break;
            case 'nav-6':
                fetchWeek(currentDate);
                break;
            default:
                break;
        }
    });
});