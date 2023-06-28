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
const logo_field = document.getElementById("logo")
const email_field = document.getElementById("email-field")
const password_field = document.getElementById("password-field")
const confirm_password = document.getElementById("confirm-password")
const student_tab = document.getElementById("student-tab")
const company_tab = document.getElementById("company-tab")
const student_fields = [full_name, student_birthdate, student_university, student_study, student_skills, btw_number]
const company_fields = [company_name, location_field, field_field, contact_name, kvk_number, logo_field]
const general_fields = [email_field, password_field, confirm_password]
let logo_data = null;

submit_button.addEventListener("click", function () {
    let general_valid, company_valid, student_valid
    general_valid = company_valid = student_valid = true
    let user = {}
    general_fields.forEach(element => {
        if (!element.checkValidity()) {
            element.classList.add("invalid")
            if (element.validity.valueMissing) closest(element, ".err-msg").innerHTML = "This field is mandatory!"
            else if (element.validity.typeMismatch) closest(element, ".err-msg").innerHTML = "This is not a valid email address"
            else if (element.validity.tooShort) closest(element, ".err-msg").innerHTML = "Your input is too short"
            general_valid = false
        }
        else {
            element.classList.remove("invalid")
            closest(element, ".err-msg").innerHTML = ""
        }
    })
    if (password_field.value !== confirm_password.value) {
        password_field.classList.add("invalid")
        confirm_password.classList.add("invalid")
        closest(password_field, '.err-msg').innerHTML = "Passwords do not match!"
        return
    }
    user.email = email_field.value;
    user.password = password_field.value;
    if (student_tab.ariaSelected === "true") {
        student_fields.forEach(element => {
            if (!element.checkValidity()) {
                element.classList.add("invalid")
                if (element.validity.valueMissing) closest(element, ".err-msg").innerHTML = "This field is mandatory!"
                else if (element.validity.patternMismatch) closest(element, ".err-msg").innerHTML = "Please input a valid BTW-number"
                student_valid = false
            } else if (element === student_birthdate && calculateAge(student_birthdate.value) < 18) {
                element.classList.add("invalid")
                closest(element, ".err-msg").innerHTML = "You need to be at least 18 years to join Earn It"
                student_valid = false
            } else {
                element.classList.remove("invalid")
                closest(element, ".err-msg").innerHTML = ""
            }
        }) 
        
        if (!student_valid){
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
                //TODO make failure message more specific based on specific error.
                if (data === "SUCCESS") window.location.href = "./student/jobs"
                if (data === "FAILURE") closest(submit_button, '.err-msg').innerHTML = "Something went wrong... Try again in a few moments"
            })
        }
    }
    if (company_tab.ariaSelected === "true") {
        company_fields.forEach(element => {
            if (!element.checkValidity()) {
                element.classList.add("invalid")
                if (element.validity.valueMissing) closest(element, ".err-msg").innerHTML = "This field is mandatory!"
                else if (element.validity.patternMismatch) closest(element, ".err-msg").innerHTML = "Please input a valid KVK-number"
                company_valid = false
            } else {
                element.classList.remove("invalid")
                closest(element, ".err-msg").innerHTML = ""
            }
        });
        if (!company_valid) {
        } else {
            user.name = company_name.value;
            user.location = location_field.value;
            user.field = field_field.value === "" ? null : field_field.value
            user.contact = contact_name.value;
            user.kvk_num = kvk_number.value === "" ? null : kvk_number.value;
            user.logo = logo_data;
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
                if (data === "SUCCESS") window.location.href = "./company/employees"
                if (data === "FAILURE") closest(submit_button, '.err-msg').innerHTML = "Something went wrong... Try again in a few moments"
            })
        }
    }
    
})

function loadLogo(element) {
    const file = element.files[0];
    console.log(`file: ${file}`)
    const reader = new FileReader();
    reader.onloadend = function() {
        console.log(`reader.result onloadend: ${reader.result}`)
        logo_data = reader.result.replace("data:image/svg+xml;base64,", '');
    }
    if (file !== undefined) reader.readAsDataURL(file);
    else logo_data = null;
}

const closest = (to, selector) => {
    let currentElement = to
    let returnElement

    while (currentElement.parentNode && !returnElement) {
        currentElement = currentElement.parentNode
        returnElement = currentElement.querySelector(selector)
    }

    return returnElement
}

function calculateAge(date) {
    let dob = new Date(date);
    //calculate month difference from current date in time
    let month_diff = Date.now() - dob.getTime();

    //convert the calculated difference in date format
    let age_dt = new Date(month_diff);

    //extract year from date    
    let year = age_dt.getUTCFullYear();

    //now calculate the age of the user
    return Math.abs(year - 1970);
}