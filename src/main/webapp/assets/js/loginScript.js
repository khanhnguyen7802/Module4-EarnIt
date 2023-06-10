const submit_button = document.getElementById("submit-login");
const err_msg = document.getElementById("err-msg");
const email_field = document.getElementById("email");
const password_field = document.getElementById("password");

submit_button.addEventListener("click", function () {
    if (email_field.value === "" || password_field.value === "") {
        err_msg.innerHTML = "Please input both a username and a password to login."
    } else {
        let user = {
            email: email_field.value,
            password: password_field.value
        };
        fetch(window.location.origin + "/earnit/api/login", {
            method: "POST",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(user)
        }).then(response => {
            if (!response.ok) throw new Error(`HTTP Error! Status: ${response.status}`);
            return response.text();
        }).then(data => {
            if (data === "INVALID") {
                // No redirect here
                err_msg.innerHTML = "The given credentials were invalid. Check spelling or create an account.";
            } else if (data === "STUDENT") {
                window.location.href = "./jobs/jobs.html";
                // Redirect to the student homepage
                err_msg.innerHTML = "Successfully logged in as a student";
            } else if (data === "COMPANY") {
                // Redirect to the company homepage
                err_msg.innerHTML = "Successfully logged in as a company";
            } else {
                err_msg.innerHTML = "Idk wtf you did but you messed some shit up.";
            }
        })
    }
});