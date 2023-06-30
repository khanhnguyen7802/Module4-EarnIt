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
            const data = json[0];
            form.email.value = data.email;
            form.name.value = data.name;
            form.university.value = data.university;
            form.study.value = data.study;
            form.skills.value = data.skills;
            form.btw_num.value = data.btw_num;
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

    student.email = form.email.value;
    student.name = form.name.value;
    student.university = form.university.value;
    student.study = form.study.value;
    student.skills = form.skills.value;
    student.password = form.password.value;
    student.btw_num = form.btw_num.value;

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
