const student = document.getElementById("student");
const company = document.getElementById("company");

//fetch data for student
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
                    const new_item = student.content.cloneNode(true);

                    let student_name = new_item.querySelector(".student_name");
                    let studentID = new_item.querySelector(".studentID");
                    let btwnumber = new_item.querySelector(".btwnumber");

                    student_name.innerHTML = item.name;
                    studentID.innerHTML = item.sid;
                    btwnumber.innerHTML = item.btw_number;

                }
            }
        })
});

//fetch data for company
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
            console.log(json);
            if ("content" in document.createElement("template")) {
                for (let item of json) {
                    console.log(item);
                    const new_item = itemTemplate.content.cloneNode(true);

                    let company_name = new_item.querySelector(".company_name");
                    let location = new_item.querySelector(".location");
                    let kvk_number = new_item.querySelector(".kvk_number");

                    company_name.innerHTML = item["company_name"];
                    location.innerHTML = item["location"];
                    kvk_number.innerHTML = item["kvk_number"];

                }
            }
        })
});

$(window).on("load", async function () {
    fetch(window.location.origin + "/earnit/api/invoices")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(data => {
            console.log(data);
            // Update invoice information
            document.getElementById("invoice_number").textContent = "Factuurnummer: " + data.iid;
            document.getElementById("date").textContent = "Factuurdatum: " + data.date;


            // Update week details
            document.querySelector(".week div:last-child").textContent = data.week;

            // Update salary and total money
            document.getElementById("salary").textContent = data.salary_per_hour;
            document.getElementById("total_salary").textContent = data.total_salary;

            // Update tax amount
            const taxValue = data.total_salary * 0.21; // Assuming tax rate is 21%
            document.querySelector(".tax div:last-child").textContent = taxValue;

            // Update total amount
            const totalAmount = data.total_salary + taxValue;
            document.getElementById("total amount").textContent = totalAmount;
        })
        .catch(error => {
            console.log("An error occurred:", error);
        });
});