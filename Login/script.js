// =================================
// LOGIN PAGE
// =================================
document.addEventListener("DOMContentLoaded", function() {

    const loginForm = document.getElementById("loginForm");

    if (loginForm) {

        loginForm.addEventListener("submit", function(event) {

            event.preventDefault();

            const username =
                document.getElementById("username").value.trim();

            const password =
                document.getElementById("password").value;

            if (username !== "" && password.trim() !== "") {

                sessionStorage.setItem("loggedIn", "true");

                window.location.href = "index.html";

            } else {

                document.getElementById("loginError").textContent =
                    "Please enter username and password!";

            }

        });

    }


    // =================================
    // FARE CALCULATOR PAGE
    // =================================

    const fareForm = document.getElementById("fareForm");

    if (fareForm) {

        // Check login
        if (sessionStorage.getItem("loggedIn") !== "true") {

            window.location.replace("login.html");
            return;

        }

        // Waiting time show / hide
        const waitingOptions = document.querySelectorAll(
            'input[name="waiting"]'
        );

        const waitingField = document.getElementById("waitingField");

        waitingOptions.forEach(option => {

            option.addEventListener("change", function() {

                const waitingInput =
                    document.getElementById("waitingMinutes");

                if (this.value === "yes") {

                    waitingField.classList.remove("hidden");
                    waitingInput.required = true;

                } else {

                    waitingField.classList.add("hidden");
                    waitingInput.required = false;
                    waitingInput.value = "";

                }

            });

        });


        // Calculate fare
        fareForm.addEventListener("submit", function(event) {

            event.preventDefault();

            const carType =
                document.getElementById("carType").value;

            const distance = Number(
                document.getElementById("distance").value
            );

            const waiting = document.querySelector(
                'input[name="waiting"]:checked'
            ).value;

            const waitingInput =
                document.getElementById("waitingMinutes");

            const waitingMinutes = Number(waitingInput.value);

            const nightTrip =
                document.getElementById("nightTrip").checked;

            const feedback =
                document.getElementById("feedback").value.trim();


            // Validation
            if (!Number.isFinite(distance) || distance <= 0) {

                alert("Please enter a valid distance!");
                return;

            }

            if (waiting === "yes" &&
                (waitingInput.value.trim() === "" ||
                !Number.isInteger(waitingMinutes) ||
                waitingMinutes < 0)) {

                alert("Please enter valid waiting minutes!");
                return;

            }


            // Base fare
            const fares = {
                mini: 50,
                suv: 70,
                sedan: 90
            };

            const baseFare = fares[carType];

            if (baseFare === undefined) {

                alert("Please select a car type!");
                return;

            }


            // Extra distance charge
            const extraDistance = Math.max(0, distance - 2);

            const distanceCharge = extraDistance * 20;


            // Waiting charge
            let waitingCharge = 0;

            if (waiting === "yes" && waitingMinutes > 5) {

                waitingCharge = (waitingMinutes - 5) * 5;

            }


            // Subtotal
            const subtotal =
                baseFare + distanceCharge + waitingCharge;


            // Night charge 25%
            const nightCharge = nightTrip ? subtotal * 0.25 : 0;


            // Final fare
            const totalFare = subtotal + nightCharge;


            // Display fare breakdown
            document.getElementById("fareBreakdown").innerHTML = `
                <p>Base Fare: ₹${baseFare.toFixed(2)}</p>
                <p>Extra Distance: ${extraDistance.toFixed(2)} KM</p>
                <p>Distance Charge: ₹${distanceCharge.toFixed(2)}</p>
                <p>Waiting Charge: ₹${waitingCharge.toFixed(2)}</p>
                <p>Subtotal: ₹${subtotal.toFixed(2)}</p>
                <p>Night Charge (25%): ₹${nightCharge.toFixed(2)}</p>
            `;


            // Feedback
            document.getElementById("feedbackResult").textContent =
                "Feedback: " + (feedback || "No feedback");


            // Total amount
            document.getElementById("totalFare").textContent =
                "Total Fare: ₹" + totalFare.toFixed(2);


            // Show result
            document.getElementById("result").classList.remove("hidden");

        });


        // =================================
        // LOGOUT
        // =================================

        document.getElementById("logoutButton")
            .addEventListener("click", function() {

                sessionStorage.removeItem("loggedIn");

                window.location.href = "login.html";

            });

    }

});