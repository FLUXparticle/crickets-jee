async function login() {
    const form = document.getElementById('loginForm');
    const formData = new FormData(form);
    const json = JSON.stringify(Object.fromEntries(formData.entries()));
    const errorMessage = document.getElementById('error-message');

    const response = await fetch('/rest/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: json,
        credentials: 'include' // WICHTIG: Cookie wird mitgesendet und gespeichert
    });

    if (response.ok) {
        // Login erfolgreich, umleiten zur Hauptanwendung
        window.location.href = '/app/';
    } else {
        // Fehlermeldung anzeigen
        const result = await response.json();
        errorMessage.textContent = result.error;
    }
}

document.getElementById("loginForm").onsubmit = (event) => {
    event.preventDefault();
    login();
};
