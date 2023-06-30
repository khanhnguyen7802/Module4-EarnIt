const invoiceTemplate = document.getElementById("invoice-info");

fetch(window.location.origin + `/earnit/api/invoices/week?week=${weekNumber}&year=${yearNumber}`)
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
                const new_item = invoiceTemplate.content.cloneNode(true);

                let company_name = new_item.querySelector("#company-name");
                company_name.textContent = item["company_name"];
                let company_address = new_item.querySelector("#company-address")
                company_address.textContent = item["company_address"]
                let kvk_number = new_item.querySelector("#kvk-number")
                kvk_number.textContent = item["kvk_number"]
                let invoice_number = new_item.querySelector("#invoice-number")
                invoice_number.textContent = item["invoice_number"]
                let date_of_issue = new_item.querySelector("#date-of-issue")
                date_of_issue.textContent = new Date()

                let student_name = new_item.querySelector("#student-name")
                student_name.textContent = item["student_name"]
                let student_id = new_item.querySelector("#student-id")
                student_id.textContent = "#".concat(item["student_id"])
                let btw_number = new_item.querySelector("btw-number")
                btw_number.textContent = item["btw_number"]

                let week_number = new_item.querySelector("#week-number")
                week_number.textContent = weekNumber
                let job_name = new_item.querySelector("#job-title")
                job_name.textContent = item["job_title"]
                let total_salary = new_item.querySelector("#total-salary")
                total_salary.textContent = item["total_salary"]
                let tax_value = new_item.querySelector("#tax")
                tax_value.textContent = item["total_salary"] * (21 / 100)
                let total_amount = new_item.querySelector("#total-amount")
                total_amount.textContent = total_salary - tax_value

                // something .append(new_item)
            }
        }
    })
