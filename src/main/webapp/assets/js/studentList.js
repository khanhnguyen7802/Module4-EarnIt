const ul = document.getElementById("list");
const template = document.getElementById("student-item")


$(window).on("load", function() {
    fetch(window.location.origin + "/earnit/api/students")
        .then(response => {
            if (!response.ok) throw new Error("HTTP Error! Status: " + response.status);
            return response.json();
        })
        .then(json => {
            console.log(json);
            if ("content" in document.createElement("template")) {
                for (let item of json) {
                    const new_student = template.content.cloneNode(true);
                    let student_name = new_student.querySelector(".student_name")
                    student_name.textContent = item.name;
                    ul.appendChild(new_student);
                }
            }
        });
})
