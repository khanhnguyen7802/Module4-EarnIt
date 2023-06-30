const saveChanges = document.getElementById("save_changes");
const studentUniversity = document.getElementById("university");
const studentStudy = document.getElementById("study");
const studentSkills = document.getElementById("skills");
const emailField = document.getElementById("email");
const passwordField = document.getElementById("new_password");
const confirmPassword = document.getElementById("comfirm_password"); // Corrected ID

// Initial data
let initialData = {
    email: emailField.value,
    password: passwordField.value,
    university: studentUniversity.value,
    study: studentStudy.value,
    skills: studentSkills.value
};

saveChanges.addEventListener("click", function() {
    let user = {};

    if (passwordField.value !== confirmPassword.value) {
        alert("Passwords do not match!");
        return;
    }

    user.email = emailField.value;
    user.password = passwordField.value;
    user.university = studentUniversity.value;
    user.study = studentStudy.value;
    user.skills = studentSkills.value;

    // Check if there are any changes
    if (JSON.stringify(user) === JSON.stringify(initialData)) {
        alert("No changes detected.");
        return;
    }

    if (student_tab.ariaSelected === "true") {
        fetch(window.location.origin + "/earnit/api/students", {
            method: "PUT",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(user)
        })
            .then(response => {
                if (!response.ok) throw new Error("HTTP Error! Status: " + response.status);
                return response.text();
            })
            .then(data => {
                // TODO: Replace this with redirect
                if (data === "SUCCESS") alert("Success!");
                if (data === "FAILURE") alert("Oof, failure...");
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
});
