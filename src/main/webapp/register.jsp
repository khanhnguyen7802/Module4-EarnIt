<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Register</title>
    <script>
        function validate()
        {
            var name = document.form.name.value;
            var email = document.form.email.value;
            var password = document.form.password.value;
            var conpassword= document.form.conpassword.value;

            if (name==null || name==="")
            {
                alert("Name can't be blank");
                return false;
            }
            else if (email==null || email==="")
            {
                alert("Email can't be blank");
                return false;
            }
            else if (password.length<6)
            {
                alert("Password must be at least 6 characters long.");
                return false;
            }
            else if (password!==conpassword)
            {
                alert("Confirm Password should match with the Password");
                return false;
            }
        }

        const form = document.querySelector("form");
        const log = document.querySelector("#log");

        form.addEventListener(
            "submit",
            (event) => {
                const data = new FormData(form);
                let output = "";
                for (const entry of data) {
                    output = `${output}${entry[0]}=${entry[1]}\r`;
                }
                log.innerText = output;
                event.preventDefault();
            },
            false
        );
    </script>
</head>
<body>
<h2>Register</h2>
<form name="form" onsubmit="return validate()">
    <div>
        <input type="radio" id="student" name="contact" value="student" />
        <label for="student">Student</label>
        <input type="radio" id="company" name="contact" value="company" />
        <label for="company">Company</label>
    </div>
    <table>
        <tr>
            <td>Name</td>
            <td><input type="text" name="name" /></td>
        </tr>
        <tr>
            <td>Email</td>
            <td><input type="text" name="email" /></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" name="password" /></td>
        </tr>
        <tr>
            <td>Confirm Password</td>
            <td><input type="password" name="conpassword" /></td>
        </tr>
        <tr>
            <td><%=(request.getAttribute("errMessage") == null) ? ""
                    : request.getAttribute("errMessage")%></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Register"><input type="reset" value="Reset"></input></td>
        </tr>
    </table>
</form>
</body>
</html>
