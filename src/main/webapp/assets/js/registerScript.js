const submit_button = document.getElementById("submit-button")
const full_name = document.getElementById("student-name")
const student_birthdate = document.getElementById("student-birthdate")
const student_university = document.getElementById("student-university")
const student_study = document.getElementById("student-study")
const student_skills = document.getElementById("student-skills")
const btw_number = document.getElementById("btw-number")
const company_name = document.getElementById("company-name")
const location_field = document.getElementById("location-field")
const field_field = document.getElementById("field-field")
const contact_name = document.getElementById("contact-name")
const kvk_number = document.getElementById("kvk-number")
const email_field = document.getElementById("email-field")
const password_field = document.getElementById("password-field")
const confirm_password = document.getElementById("confirm-password")
const student_tab = document.getElementById("student-tab")
const company_tab = document.getElementById("company-tab")
const mandatory_student_fields = [full_name, student_birthdate, student_study]
const mandatory_company_fields = [company_name, location_field, contact_name]
const mandatory_general_fields = [email_field, password_field, confirm_password]
submit_button.addEventListener("click", function () {
    let user = {}
    if (!mandatory_general_fields.every(element => element.value !== null && element.value !== "")) {
        alert("Not all mandatory fields are filled in!");
        return
    }
    if (password_field.value !== confirm_password.value) {
        alert("Passwords do not match!")
        return
    }
    user.email = email_field.value;
    user.password = password_field.value;
    if (student_tab.ariaSelected === "true") {
        if (!mandatory_student_fields.every(element => element.value !== null && element.value !== "")) {
            alert("Not all mandatory Student fields are filled in!")
            return
        } else {
            user.name = full_name.value;
            user.birth = student_birthdate.value;
            user.university = student_university.value === "" ? null: student_university.value;
            user.study = student_study.value;
            user.skills = student_skills.value === "" ? null : student_skills.value;
            user.btw_num = btw_number.value === "" ? null : btw_number.value;
            fetch(window.location.origin + "/earnit/api/register/student", {
                method: "POST",
                headers: {
                    "Content-type": "application/json"
                },
                body: JSON.stringify(user)
            }).then(response => {
                if (!response.ok) throw new Error("HTTP Error! Status: " + response.status);
                return response.text()
            }).then(data => {
                //TODO replace this with redirect
                if (data === "SUCCESS") alert("Success!")
                if (data === "FAILURE") alert("Oof, failure...")
            })
        }
    }
    if (company_tab.ariaSelected === "true") {
        if (!mandatory_company_fields.every(element => element.value !== null && element.value !== "")) {
            alert("Not all mandatory Company fields are filled in!")
        } else {
            user.name = company_name.value;
            user.location = location_field.value;
            user.field = field_field.value === "" ? null : field_field.value
            user.contact = contact_name.value;
            user.kvk_num = kvk_number.value === "" ? null : kvk_number.value;
            fetch(window.location.origin + "/earnit/api/register/company", {
                method: "POST",
                headers: {
                    "Content-type": "application/json"
                },
                body: JSON.stringify(user)
            }).then(response => {
                if (!response.ok) throw new Error("HTTP Error! Status: " + response.status)
                return response.text()
            }).then(data => {
                //TODO replace this with redirect
                if (data === "SUCCESS") alert("Company successfully registered!")
                if (data === "FAILURE") alert("Oopsie poopsie, something went wrong")
            })
        }
    }
    
})