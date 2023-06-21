const vacancy_list = document.getElementById("vacancy-list")
const student_list = document.getElementById("student-list")
const vacancy_template = document.getElementById("vacancy-template")
const student_template = document.getElementById("student-template")
const students_checkbox = document.getElementById("touch2")
const vacancies_checkbox = document.getElementById("touch1")
const vacancy_label = document.querySelector("label[for=\"touch1\"] span")
const students_label = document.querySelector("label[for=\"touch2\"] span")

let selectedJob = null
let selectedStudent = null;

$(window).on("load", function () {
    fetch(window.location.origin + "/earnit/api/employments/vacancies")
        .then(response => {
            if (!response.ok) throw new Error("HTTP Error: Status: " + response.status);
            return response.json();
        })
        .then(data => {
            console.log(data)
            if ("content" in document.createElement("template")) {
                for (let item of data) {
                    console.log(item);
                    const new_vacancy = vacancy_template.content.cloneNode(true);
                    let job_title = new_vacancy.querySelector(".job_title");
                    let company_name = new_vacancy.querySelector(".company_name");
                    let vacancy_button = new_vacancy.querySelector(".vacancy-button")
                    
                    job_title.innerHTML = item.job_title;
                    company_name.innerHTML = item.companyName;
                    vacancy_button.addEventListener("click", function() {
                        selectedJob = item;
                        students_checkbox.disabled = false
                        vacancies_checkbox.checked = false
                        vacancy_label.innerHTML = item.job_title
                    })
                    
                    vacancy_list.append(new_vacancy);
                }
            }
        });
    fetch(window.location.origin + "/earnit/api/students/all")
        .then(response => {
            if (!response.ok) throw new Error("HTTP Error! Status: " + response.status)
            return response.json()
        }).then(data => {
            console.log(data)
            if ("content" in document.createElement("template")) {
                for (let item of data) {
                    console.log(item)
                    const new_student = student_template.content.cloneNode(true)
                    let student_name = new_student.querySelector(".student-name")
                    let student_button = new_student.querySelector(".student-button")
                    student_name.innerHTML = item.name
                    student_button.addEventListener("click", function() {
                        selectedStudent = item
                        students_checkbox.checked = false
                        students_label.innerHTML = item.name
                    })
                    
                    student_list.append(new_student)
                }
            }
        });
    document.getElementById("link-button").addEventListener("click", function () {
        selectedJob.sid = selectedStudent.id
        fetch(window.location.origin + "/earnit/api/employments/link", {
            method: "POST",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(selectedJob)
        }).then(response => {
            if (!response.ok) throw new Error("HTTP Error! Status: " + response.status);
            return response.text()
        }).then(data => {
            if (data === "SUCCESS") alert("Update was successful!")
            if (data === "FAILURE") alert("Something went wrong")
        })
    })
})