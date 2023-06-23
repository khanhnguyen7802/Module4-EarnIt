function logout() {
    fetch(window.location.origin + "/earnit/api/logout", {
        method: "POST"
    }).then(response => {
        if (response.ok) {
            window.location.href = window.location.origin + "/earnit/login"
        } else {
            console.log('Failed to logout');
        }
    })
        .catch(error => {
            // An error occurred during the fetch request
            console.log('Request error:', error);
        });
}