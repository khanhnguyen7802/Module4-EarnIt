$(window).on("load", function () {
    fetch(window.location.origin + "/earnit/api/employments/vacancies")
        .then(response => {
            if (!response.ok) throw new Error("HTTP Error: Status: " + response.status);
            return response.json();
        })
        .then(data => {
            console.log(data)
            const optionTemplate = document.getElementById('option_template');
            const selectElement = document.getElementById('vacancy-select');
            data.forEach(item => {
                console.log(data);
                const option = optionTemplate.content.cloneNode(true);
                option.querySelector('option').value = item.eid;
                option.querySelector('option').textContent = item.job_title + " - " + item.company_name;
                selectElement.appendChild(option);
            });
        })
})

//TODO: Add an event listener to the Submit button that send a POST request with body to submissions API