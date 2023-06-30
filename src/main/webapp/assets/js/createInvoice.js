const invoiceTemplate = document.getElementById("invoice-info");
const sectionInvoice = document.getElementById("invoice")

let eid = sessionStorage.getItem("eid")
let week = sessionStorage.getItem("week")
let year = sessionStorage.getItem("year")

fetch(window.location.origin + `/earnit/api/invoices/week?eid=${eid}&week=${week}&year=${year}`)
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
            if ("content" in document.createElement("template")) {
                console.log(item)
                const new_item = invoiceTemplate.content.cloneNode(true);

                let company_name = new_item.querySelector("#company-name");
                company_name.textContent = item["company_name"];
                let company_address = new_item.querySelector("#company-location")
                // console.log(item["company_address"])
                company_address.textContent = item["company_address"]
                let kvk_number = new_item.querySelector("#kvk-number")
                kvk_number.textContent = "kvk number: ".concat(item["kvk_number"])
                let invoice_number = new_item.querySelector("#invoice-number")
                invoice_number.textContent = "invoice number: ".concat(item["iid"])
                let date_of_issue = new_item.querySelector("#date-of-issue")
                date_of_issue.textContent = new Date().toJSON().slice(0, 10);

                let student_name = new_item.querySelector("#student-name")
                student_name.textContent = item["student_name"]
                let btw_number = new_item.querySelector("#btw-number")
                btw_number.textContent = "btw number: ".concat(item["btw_number"])

                let week_number = new_item.querySelector("#week-number")
                week_number.textContent = week
                let job_name = new_item.querySelector("#job-title")
                job_name.textContent = "Position: ".concat(item["job_title"])
                let total_salary = new_item.querySelector("#total-salary")
                total_salary.textContent = item["total_salary"]
                total_salary.value = item["total_salary"]
                let tax_value = new_item.querySelector("#tax")
                tax_value.textContent = item["total_salary"] * (21 / 100)
                tax_value.value = item["total_salary"] * (21 / 100)
                let total_amount = new_item.querySelector("#total-amount")
                total_amount.textContent = total_salary.value - tax_value.value

                sectionInvoice.append(new_item)

            }
        }
    })
