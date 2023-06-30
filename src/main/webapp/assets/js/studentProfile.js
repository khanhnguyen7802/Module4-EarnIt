const form = document.getElementById("form");
const submit = document.getElementById("save_button");

$(window).on("load", function () {
    fetch(window.location.origin + "/earnit/api/students/profile")
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Request failed with status ' + response.status);
            }
        })
        .then(json => {
            console.log(json);
            form.email.value = json.email;
            form.name.value = json.name;
            form.university.value = json.university;
            form.study.value = json.study;
            form.skills.value = json.skills;
            form.btw_num.value = json.btw_num;
        })
});

// Initial data
let initialData = {
    email: form.email.value,
    name: form.name.value,
    university: form.university.value,
    study: form.study.value,
    skills: form.skills.value,
    btw_num: form.btw_num
};

submit.addEventListener("click", function() {
    let student = {};

    if (form.password.value !== form.confirm_pass.value) {
        alert("Passwords do not match!");
        return;
    }

    student.email = sanitize(form.email.value);
    student.name = sanitize(form.name.value);
    student.university = sanitize(form.university.value);
    student.study = sanitize(form.study.value);
    student.skills = form.skills.value === "" ? null : sanitize(form.skills.value);
    student.password = sanitize(form.password.value);
    student.btw_num = sanitize(form.btw_num.value);

    // Check if there are any changes
    if (JSON.stringify(student) === JSON.stringify(initialData)) {
        alert("No changes detected!");
        return;
    }

    fetch(window.location.origin + "/earnit/api/students/update", {
        method: "POST",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(student)
    })
        .then(response => {
            if (!response.ok) throw new Error("HTTP Error! Status: " + response.status);
            return response.text();
        })
        .then(data => {
            if (data === "SUCCESS"){
                window.location.href = "./student_profile";
            } else {
                alert("Please try again");
            }
        })
        .catch(error => {
            console.error(error);
        });
});
