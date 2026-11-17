function copyPassword() {

    const password = document.getElementById("password");

    password.select();

    navigator.clipboard.writeText(password.value)
        .then(() => {
            alert("Password copiata negli appunti!");
        })
        .catch(() => {
            alert("Impossibile copiare la password.");
        });
}
